package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import utils.Printer;
import engine.board.GameBoard;

public class GameServer extends Thread
{	private static int BUFFER_SIZE = 64000; //64k buffer
	private static final int SERVER_LISTENING_PORT = 1234;
	private DatagramSocket socket;
	private GameBoard game;
	
	public GameServer(GameBoard game)
	{
		this.game = game;
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
		while(true)
		{
			byte[] data = new byte[BUFFER_SIZE];
			DatagramPacket packet = new DatagramPacket(data,  data.length);
			try
			{
				socket.receive(packet);
				Printer.debugMessage(this.getClass(), String.format("received %s bytes", packet.getLength()));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
	
			// Deserialize the object.
			TestObject received = TestObject.deserialize(packet.getData(), packet.getOffset(), packet.getLength()); // Does not work.
			//TestObject received = TestObject.deserialize(packet.getData()); // Works fine?
			System.out.println("Server received object with value " + received.value);
		}
	}
}
