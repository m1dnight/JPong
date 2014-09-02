package engine.util;

import java.io.Serializable;


public class Angle implements Serializable
{
	private static final long serialVersionUID = -8958293345412515241L;

	private static final double DEG_360 = 6.28318531D;
	
	private int    degrees;
	private double radians;

	//---- CONSTRUCTORS ------------------------------------------------------//
	public Angle(int degrees)
	{
		this.radians = toRadians(degrees);
		this.degrees = degrees;
	}
	public Angle(Double radians)
	{
		// Convert to degrees.
		this.radians = radians;
		this.degrees = toDegrees(radians);
	}
	//---- LOGIC METHODS -----------------------------------------------------//
	public Angle multiply(int multiplier)
	{
		this.degrees *= multiplier;
		this.radians *= multiplier;
		return this;
	}
	public Angle add(Double addition)
	{
		this.radians += addition;
		this.degrees = toDegrees(radians);
		return this;
	}
	public Angle add(int addition)
	{
		this.degrees  = (this.degrees + addition) % 360;
		this.radians = toRadians(this.degrees);
		return this;
	}
	public Angle minus(Angle angle)
	{
		this.degrees -= angle.getDegrees();
		this.radians -= angle.getRadians();
		return this;
	}
	//---- STATIC METHODS ----------------------------------------------------//
	public static Angle randomAngle()
	{
		return new Angle(DEG_360 * Math.random());
	}
	public static Angle randomAngle(int min, int max)
	{
		return new Angle(Math.floor(Math.random() * (max - min + 1)) + min);
	}
	//---- CONVERSION METHODS ------------------------------------------------//
	private Double toRadians(int degrees)
	{
		return (degrees * Math.PI) / 180;
	}
	private int toDegrees(Double radians)
	{
		return (int) ((radians * 180) / Math.PI);
	}
	//---- GETTERS AND SETTERS -----------------------------------------------//
	public int getDegrees()
	{
		return degrees;
	}
	public void setDegrees(int degrees)
	{
		this.degrees = degrees;
	}
	public double getRadians()
	{
		return radians;
	}
	public void setRadians(double radians)
	{
		this.radians = radians;
	}
}
