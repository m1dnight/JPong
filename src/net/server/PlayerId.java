package net.server;

import java.net.InetAddress;

public class PlayerId
{
	private InetAddress playerAddress;
	private int playerPort;
	//-------------------------------------------------------------------------/
	//---- CONSTRUCTOR --------------------------------------------------------/
	//-------------------------------------------------------------------------/
	public PlayerId(InetAddress playerAddress, int playerPort)
	{
		super();
		this.playerAddress = playerAddress;
		this.playerPort = playerPort;
	}
	//-------------------------------------------------------------------------/
	//---- COMPARISON ---------------------------------------------------------/
	//-------------------------------------------------------------------------/
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((playerAddress == null) ? 0 : playerAddress.hashCode());
		result = prime * result + playerPort;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerId other = (PlayerId) obj;
		if (playerAddress == null)
		{
			if (other.playerAddress != null)
				return false;
		} else if (!playerAddress.equals(other.playerAddress))
			return false;
		if (playerPort != other.playerPort)
			return false;
		return true;
	}

	//-------------------------------------------------------------------------/
	//---- GETTERS AND SETTERS ------------------------------------------------/
	//-------------------------------------------------------------------------/
	public InetAddress getPlayerAddress()
	{
		return playerAddress;
	}
	public void setPlayerAddress(InetAddress playerAddress)
	{
		this.playerAddress = playerAddress;
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
