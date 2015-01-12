package net.packets;

import java.io.Serializable;

public class HelloServer implements Serializable
{
	private static final long serialVersionUID = -2762411968485177484L;
	private String username;
	private Side side;
	
	//-------------------------------------------------------------------------/
	//---- CONSTRUCTOR --------------------------------------------------------/
	//-------------------------------------------------------------------------/
	public HelloServer(String username, Side side)
	{
		super();
		this.username = username;
		this.side = side;
	}
	//-------------------------------------------------------------------------/
	//---- GETTERS AND SETTERS ------------------------------------------------/
	//-------------------------------------------------------------------------/
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public Side getSide()
	{
		return side;
	}
	public void setSide(Side side)
	{
		this.side = side;
	}


}
