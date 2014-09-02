package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import utils.Printer;
import engine.board.GameBoard;
import engine.gamestate.GameState;
import engine.paddle.Paddle;

public class GameServer extends Thread
{	private static int BUFFER_SIZE = 1024; //64k buffer
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
			GameState received = GameState.deserialize(packet.getData(), packet.getOffset(), packet.getLength()); // Does not work.
			Paddle player1 = this.game.gameState.getPlayer1();
			this.game.gameState = received;
			this.game.gameState.setPlayer1(player1);
			//TestObject received = TestObject.deserialize(packet.getData()); // Works fine?
			System.out.println("Server received object with value " + received.getPlayer1().getY());
		}
	}

	
}
