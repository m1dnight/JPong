package net.server.packets;

import java.io.Serializable;

import org.joda.time.DateTime;

public class PacketWrapper implements Serializable
{
	private static final long serialVersionUID = 327084766227844534L;
	private DateTime timeStamp;
	private Object data;
	
	public PacketWrapper(Object data)
	{
		this.timeStamp = new DateTime();
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public Object getData()
	{
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data)
	{
		this.data = data;
	}

	/**
	 * @return the timeStamp
	 */
	public DateTime getTimeStamp()
	{
		return timeStamp;
	}

	
}
