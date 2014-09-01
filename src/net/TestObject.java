package net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TestObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int value;
	public double anotherValue;
	public byte[] fillMe;

	public TestObject(int value)
	{
		super();
		this.value = value;
		fillMe = new byte[123];
	}

	public TestObject(int value, double anotherValue)
	{
		super();
		this.value = value;
		this.anotherValue = anotherValue;
	}

	public static byte[] serialize(TestObject o)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			// get the byte array of the object
			byte[] obj = baos.toByteArray();
			baos.close();
			return obj;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static TestObject deserialize(byte[] data)
	{
		try
		{
			ObjectInputStream iStream = new ObjectInputStream(
					new ByteArrayInputStream(data));
			TestObject obj = (TestObject) iStream.readObject();
			iStream.close();
			return obj;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
