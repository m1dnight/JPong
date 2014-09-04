package net.server.packets;

import java.io.Serializable;

import net.packets.ReplyStatus;
import net.packets.Side;

public class HelloClient implements Serializable
{
	private static final long serialVersionUID = -1276179357873173107L;

	private String username;

	private Side side;
	private ReplyStatus replyStatus;
	
	//-------------------------------------------------------------------------/
	//---- CONSTRUCTOR --------------------------------------------------------/
	//-------------------------------------------------------------------------/
	public HelloClient(String username, Side side, ReplyStatus replyStatus)
	{
		this.username = username;
		this.side = side;
		this.replyStatus = replyStatus;
	}
	//-------------------------------------------------------------------------/
	//---- GETTERS AND SETTERS ------------------------------------------------/
	//-------------------------------------------------------------------------/
	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}
	/**
	 * @return the side
	 */
	public Side getSide()
	{
		return side;
	}
	/**
	 * Set the game side.
	 * @param side
	 */
	public void setSide(Side side)
	{
		this.side = side;
	}
	/**
	 * @return the replyStatus
	 */
	public ReplyStatus getReplyStatus()
	{
		return replyStatus;
	}
	/**
	 * Sets the reply status.
	 * @param newStatus the new reply status.
	 */
	public void setReplyStatus(ReplyStatus newStatus)
	{
		this.replyStatus = newStatus;
	}

}
