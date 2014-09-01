package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import engine.board.GameBoard;

public class GameClient extends Thread
{
	private static final int PORT = 1234;
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private GameBoard game;
	
	public GameClient(GameBoard game, String ipAddress)
	{
		this.game = game;
		try
		{
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
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
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data,  data.length);
			try
			{
				socket.receive(packet);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			System.out.println("Client recevied: >> " + new String(packet.getData()));
		}
	}
	
	public void sendData(byte[] data)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, PORT);
		try
		{
			socket.send(packet);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
