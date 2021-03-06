package net.client;

import engine.ball.Ball;
import engine.board.GameBoard;
import engine.gamestate.GameState;
import engine.paddle.Paddle;
import net.packets.HelloServer;
import net.packets.ReplyStatus;
import net.packets.Side;
import net.server.packets.HelloClient;
import net.server.packets.PacketWrapper;
import org.joda.time.DateTime;
import utils.Printer;

import java.io.*;
import java.net.*;

public class GameClient extends Thread
{
    private static       int BUFFER_SIZE           = 1024;
    private static final int SERVER_LISTENING_PORT = 1234;
    private InetAddress    serverIp;
    private DatagramSocket socket;
    private GameBoard      game;
    private String         clientName;

    public GameClient(String name, GameBoard game, String ipAddress)
    {
        this.game = game;
        this.clientName = name;
        try
        {
            this.socket = new DatagramSocket();
            this.serverIp = InetAddress.getByName(ipAddress);
        } catch (SocketException | UnknownHostException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run()
    {
        /*
        Receive packets from the server in a loop.
        Initially we want a HelloClient packet that will
        tell us if we are paired with a player.
         */
        while (true)
        {
            byte[] data = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try
            {
                socket.receive(packet);

                Printer.debugMessage(this.getClass(),
                        String.format("received %s bytes", packet.getLength()));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            // Deserialize
            PacketWrapper received = (PacketWrapper) deserialize(packet.getData(), packet.getOffset(),
                    packet.getLength());

            // Dispatch over the type and act accordingly.
            if (received.getData() instanceof GameState)
            {
                // Merge the game states.
                GameState receivedState = (GameState) received.getData();
                GameState current = this.game.getGameState();

                // Merge the states into the receivedState, which will become our new state.
                // Merge scores.
                // The score is the maximum of both scores.
                receivedState.setScore_1(Math.max(current.getScore_1(), receivedState.getScore_1()));
                receivedState.setScore_2(Math.max(current.getScore_2(), receivedState.getScore_2()));
                receivedState.setPing(new DateTime().getMillis() - received.getMilis());

                // Update the current ball location.
                Ball currentBall = current.getBall();
                if (this.game.getPlayerSide() == Side.LEFT)
                {
                    Paddle localPlayer = this.game.getGameState().getPlayer1();
                    receivedState.setPlayer1(localPlayer);

                    // If we are the left player we move our ball to the position
                    // of the right player to avoid sync issues.
                    receivedState.setBall(currentBall);
                } else
                {
                    Paddle localPlayer = this.game.getGameState().getPlayer2();
                    receivedState.setPlayer2(localPlayer);
                }
                this.game.setGameState(receivedState);
            }
            if (received.getData() instanceof HelloClient)
            {
                HelloClient response = (HelloClient) received.getData();
                this.game.setPlayerSide(response.getSide());
                if (response.getReplyStatus() == ReplyStatus.PAIRED_WITH_PLAYER)
                {
                    this.game.StartGame();
                }
            }
        }
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
    //---- SEND METHODS -------------------------------------------------------/
    //-------------------------------------------------------------------------/

    /**
     * Sends the current gamestate to the server.
     */
    public void sendGameState()
    {
        PacketWrapper wrapper = new PacketWrapper(this.game.getGameState(), new DateTime().getMillis());
        sendData(serialize(wrapper));
    }

    /**
     * Registers the client with the server such that it can be connected to by another player.
     */
    public void registerWithServer()
    {
        HelloServer obj = new HelloServer(this.clientName, Side.LEFT);
        PacketWrapper wrapper = new PacketWrapper(obj, new DateTime().getMillis());
        sendData(serialize(wrapper));
    }

    /**
     * Sends an array of bytes to the server.
     *
     * @param data
     */
    public void sendData(byte[] data)
    {
        DatagramPacket packet = new DatagramPacket(data, data.length, serverIp, SERVER_LISTENING_PORT);
        try
        {
            socket.send(packet);
            Printer.debugMessage(this.getClass(), String.format("sent %d bytes to the server", data.length));
            ;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
