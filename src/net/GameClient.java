package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import utils.Printer;
import engine.board.GameBoard;
import engine.gamestate.GameState;

public class GameClient extends Thread
{
	private static int BUFFER_SIZE = 1024;
	private static final int SERVER_LISTENING_PORT = 1234;
	private InetAddress serverIp;
	private DatagramSocket socket;
	private GameBoard game;
	private String clientName;
	
	public GameClient(String name, GameBoard game, String ipAddress)
	{
		this.game = game;
		this.clientName = name;
		try
		{
			this.socket = new DatagramSocket();
			this.serverIp = InetAddress.getByName(ipAddress);
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run()
	{
		while(true)
		{
//			byte[] data = new byte[BUFFER_SIZE];
//			DatagramPacket packet = new DatagramPacket(data,  data.length);
//			try
//			{
//				socket.receive(packet);
//				Printer.debugMessage(this.getClass(), String.format("received %s bytes", packet.getLength()));
//			} catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//	
//			// Deserialize the object.
//			GameState received = GameState.deserialize(packet.getData(), packet.getOffset(), packet.getLength()); // Does not work.
//			this.game.gameState = received;
			sendData(GameState.serialize(this.game.gameState));
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendData(byte[] data)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, serverIp, SERVER_LISTENING_PORT);
		
		try
		{
			socket.send(packet);
			Printer.debugMessage(this.getClass(), String.format("sent %d bytes to the server", data.length));;

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
