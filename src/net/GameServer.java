package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import engine.board.GameBoard;

public class GameServer extends Thread
{
	private static final int PORT = 1234;
	private DatagramSocket socket;
	private GameBoard game;
	
	public GameServer(GameBoard game)
	{
		this.game = game;
		try
		{
			this.socket = new DatagramSocket(PORT);
		} catch (SocketException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(true)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data,  data.length);
			try
			{
				socket.receive(packet);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			String message = new String(packet.getData());
			System.out.println("Server received: >> " + message);
			//if(message.trim().equalsIgnoreCase("ping"))
				
			sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
		}
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try
		{
			socket.send(packet);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
