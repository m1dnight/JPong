package net.server;

import engine.board.GameBoard;
import engine.gamestate.GameState;
import net.packets.HelloServer;
import net.packets.ReplyStatus;
import net.packets.Side;
import net.server.packets.HelloClient;
import net.server.packets.PacketWrapper;
import org.joda.time.DateTime;
import utils.Printer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class GameServer extends Thread
{
    private static       int BUFFER_SIZE           = 1024; //64k buffer
    private static final int SERVER_LISTENING_PORT = 1234;
    private DatagramSocket                socket;
    private GameBoard                     game;
    private HashMap<PlayerId, PlayerData> players;
    private HashMap<PlayerId, PlayerData> waitingPlayers;

    public GameServer()
    {
        players = new HashMap<>();
        waitingPlayers = new HashMap<>();
        try
        {
            this.socket = new DatagramSocket(SERVER_LISTENING_PORT);
        } catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while (true)
        {
            byte[] data = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try
            {
                socket.receive(packet);
                Printer.debugMessage(this.getClass(), String.format("received %s bytes", packet.getLength()));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            PacketWrapper wrapped = (PacketWrapper) deserialize(packet.getData(), packet.getOffset(), packet.getLength());
            // Deserialize the object.

            // Dispatch gamestate to proper player.
            if (wrapped.getData() instanceof GameState)
            {
                GameState gameState = (GameState) wrapped.getData();
                PlayerId sender = new PlayerId(packet.getAddress(), packet.getPort());
                if (!players.containsKey(sender))
                {
                    Printer.debugMessage(this.getClass(), "Received GameState from unknown player!");
                } else if (waitingPlayers.containsKey(sender))
                {
                    Printer.debugMessage(this.getClass(), "Received GameState from waiting player!");
                } else
                {
                    // Get the oponent.
                    PlayerData opponentData = players.get(sender);
                    dispatchGameState(gameState, opponentData.getOponent(), wrapped.getMilis());
                }
            }
            if (wrapped.getData() instanceof HelloServer)
                registerPlayer(packet, wrapped.getData());
        }
    }

    //-------------------------------------------------------------------------/
    //---- SEND METHODS -------------------------------------------------------/
    //-------------------------------------------------------------------------/
    private void dispatchGameState(GameState gameState, PlayerData recipient, long lagToServer)
    {
        PacketWrapper wrapper = new PacketWrapper(gameState, lagToServer);
        sendData(serialize(wrapper), recipient.getPlayerAddress(), recipient.getPlayerPort());
    }

    /**
     * Replies to a client with its connected status after the client sent the
     * <p/>
     * first packet.
     *
     * @param helloClient The object containing status information.
     * @param senderIp    Ip of the client.
     * @param senderPort  Port of the client.
     */
    private void sendStatusReply(HelloClient helloClient, InetAddress senderIp, int senderPort)
    {
        PacketWrapper wrapper = new PacketWrapper(helloClient, new DateTime().getMillis());
        sendData(serialize(wrapper), senderIp, senderPort);
    }

    /**
     * Sends an array of bytes to the given client.
     *
     * @param data
     */
    private void sendData(byte[] data, InetAddress recipientAddress, int recipientPort)
    {
        DatagramPacket packet = new DatagramPacket(data, data.length, recipientAddress, recipientPort);
        try
        {
            socket.send(packet);
            Printer.debugMessage(this.getClass(), String.format("sent %d bytes to %s:%s\n", data.length, recipientAddress, recipientPort));
            ;
        } catch (IOException e)
        {

            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------/
    //---- SERVER HANDLERS ----------------------------------------------------/
    //-------------------------------------------------------------------------/
    private void registerPlayer(DatagramPacket packet, Object received)
    {
        HelloServer hs = (HelloServer) received;

        InetAddress senderIp = packet.getAddress();
        int senderPort = packet.getPort();

        // Construct data for the server.
        PlayerId newPlayerId = new PlayerId(senderIp, senderPort);
        PlayerData newPlayerData = new PlayerData(hs.getUsername(), senderIp, hs.getSide(), senderPort);

        // Message to reply to the client.
        HelloClient response = new HelloClient(hs.getUsername(), hs.getSide(), ReplyStatus.WAIT_FOR_OPPONENT);

        // If we have players waiting for a game, join this player.
        if (waitingPlayers.size() > 0)
        {
            // Get a random player from the list.
            Object[] keys = waitingPlayers.keySet().toArray();
            PlayerId randomId = (PlayerId) keys[(int) (Math.random() * keys.length)];
            PlayerData existingPlayer = waitingPlayers.get(randomId);

            // Update existing player data.
            existingPlayer.setOponent(newPlayerData);

            // Update new player
            if (existingPlayer.getSide() == Side.LEFT)
                newPlayerData.setSide(Side.RIGHT);
            else
                newPlayerData.setSide(Side.LEFT);

            newPlayerData.setOponent(existingPlayer);

            // Remove from waiting list.
            waitingPlayers.remove(randomId);
            players.put(randomId, existingPlayer);
            players.put(newPlayerId, newPlayerData);

            Printer.debugMessage(this.getClass(), String.format("Paired %s @ %s:%s with %s @ %s:%s\n",
                    newPlayerData.getNickname(), senderIp, senderPort,
                    existingPlayer.getNickname(), randomId.getPlayerAddress(), randomId.getPlayerPort()));

            // Update the response.
            response.setReplyStatus(ReplyStatus.PAIRED_WITH_PLAYER);
            response.setSide(newPlayerData.getSide());

            // Notify original player the game begins.
            HelloClient response2 = new HelloClient(existingPlayer.getNickname(), existingPlayer.getSide(), ReplyStatus.PAIRED_WITH_PLAYER);
            sendStatusReply(response2, randomId.getPlayerAddress(), randomId.getPlayerPort());
        } else if (waitingPlayers.containsKey(senderIp) || players.containsKey(senderIp))
        {
            Printer.debugMessage(this.getClass(), String.format("Tried to register existing player: %s\n", senderIp));
            response.setReplyStatus(ReplyStatus.ALREADY_CONNECTED);
        } else
        {
            // Players is unknown and has to wait. Add to waiting list.
            waitingPlayers.put(newPlayerId, newPlayerData);
            Printer.debugMessage(this.getClass(), String.format("Registered new player: %s @ %s:%s\n", hs.getUsername(), senderIp, senderPort));
            response.setReplyStatus(ReplyStatus.WAIT_FOR_OPPONENT);
        }

        // Reply with a connected message.
        sendStatusReply(response, senderIp, senderPort);
    }

    //-------------------------------------------------------------------------/
    //---- SERIALIZATION ------------------------------------------------------/
    //-------------------------------------------------------------------------/
    public static byte[] serialize(Object o)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
            // get the byte array of the object
            byte[] obj = baos.toByteArray();
            baos.close();
            return obj;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static Object deserialize(byte[] data, int offset, int length)
    {
        try
        {
            ObjectInputStream iStream = new ObjectInputStream(
                    new ByteArrayInputStream(data, offset, length));
            Object obj = (Object) iStream.readObject();
            iStream.close();
            return obj;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    //-------------------------------------------------------------------------/
    //---- HELPERS  -----------------------------------------------------------/
    //-------------------------------------------------------------------------/
}
