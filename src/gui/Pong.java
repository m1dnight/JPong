package gui;


import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import engine.board.GameBoard;


public class Pong extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Simple constructor that creates a new JFrame and adds a 
	 * new board.
	 */
	public Pong()
	{
		this.add(new GameBoard());
		this.setLayout(new FlowLayout());
		setTitle("Pong");
		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


}