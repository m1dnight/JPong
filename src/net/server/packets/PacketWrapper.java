package net.server.packets;

import java.io.Serializable;

public class PacketWrapper implements Serializable
{
	private static final long serialVersionUID = 327084766227844534L;
	private long miliSeconds;
	private Object data;
	
	public PacketWrapper(Object data, long miliSeconds)
	{

		this.miliSeconds = miliSeconds;
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
	 * @return the milis
	 */
	public long getMilis()
	{
		return miliSeconds;
	}

	/**
	 * @param milis the milis to set
	 */
	public void setMilis(long milis)
	{
		this.miliSeconds = milis;
	}
	

	
}
