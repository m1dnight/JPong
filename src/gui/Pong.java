package gui;

import gui.board.GameBoard;

import javax.swing.JFrame;


public class Pong extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Simple constructor that creates a new JFrame and adds a 
	 * new board.
	 */
	public Pong()
	{
		this.add(new GameBoard());
		setTitle("Pong");
		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


}