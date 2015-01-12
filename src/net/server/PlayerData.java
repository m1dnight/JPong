package net.server;

import java.net.InetAddress;

import net.packets.Side;

public class PlayerData
{
	private String nickname;
	private InetAddress playerAddress;
	private int playerPort;
	private PlayerData oponent;
	private Side playerSide;
	
	//-------------------------------------------------------------------------/
	//---- CONSTRUCTOR --------------------------------------------------------/
	//-------------------------------------------------------------------------/
	public PlayerData(String nickname, InetAddress playerAddress, Side playerSide, int playerPort)
	{
		super();
		this.nickname = nickname;
		this.playerAddress = playerAddress;
		this.playerSide = playerSide;
		this.playerPort = playerPort;
	}
	//-------------------------------------------------------------------------/
	//---- GETTERS AND SETTERS ------------------------------------------------/
	//-------------------------------------------------------------------------/
	public String getNickname()
	{
		return nickname;
	}
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	public InetAddress getPlayerAddress()
	{
		return playerAddress;
	}
	public void setPlayerAddress(InetAddress playerAddress)
	{
		this.playerAddress = playerAddress;
	}
	public PlayerData getOponent()
	{
		return oponent;
	}
	public void setOponent(PlayerData oponent)
	{
		this.oponent = oponent;
	}
	public void setSide(Side side)
	{
		this.playerSide = side;
	}
	public Side getSide()
	{
		return this.playerSide;
	}
	public int getPlayerPort()
	{
		return playerPort;
	}
	public void setPlayerPort(int playerPort)
	{
		this.playerPort = playerPort;
	}
	
	
	
}
