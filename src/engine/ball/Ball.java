package engine.ball;

import engine.Angle;

public class Ball
{
	// Constant variables.
	private final double BALL_DEFAULT_SPEED = 3.0D;
	// Temp variables to indicate boundaries.
	private final int height = 300;
	private final int width  = 300;
	
	private double x_loc; // X location in the plane.
	private double y_loc; // Y location in the plane.
	
	private double speed;     // Double value indicating the speed of the ball. (> 1)
	private Angle  direction; // Direction in radians (0 - 360 degrees).
	
	//---- CONSTRUCTORS ------------------------------------------------------//
	public Ball()
	{
		// Initialize the ball.
		this.speed     = BALL_DEFAULT_SPEED;
		this.direction = Angle.randomAngle();
		this.x_loc     = width / 2;
		this.y_loc     = height / 2;
	}
	
	public Ball(int speed, Angle direction, int x, int y)
	{
		// Initialize the ball.
		this.speed     = speed;
		this.direction = direction;
		this.x_loc     = x;
		this.y_loc     = y;
	}
	
	//---- LOGIC METHODS -----------------------------------------------------//
	/**
	 * Moves the ball one tick.
	 */
	public void move()
	{
		// Calculate the addition to the coordinates.
		double dx = speed * Math.cos(direction.getRadians());
		double dy = speed * Math.sin(direction.getRadians());
		
		// Check to see if we are hitting the walls.
		if(this.x_loc + dx >= width || this.x_loc + dx <= 0)
		{
			// Reflect off the sides (90°).
			bounce(new Angle(90));
		}
		if(this.y_loc + dy >= height || this.y_loc + dy <= 0)
		{
			// Reflect off the top or bottom (0°).
			bounce(new Angle(0));
		}
		
		// Nothing hit, update the new location.
		this.x_loc += dx;
		this.y_loc += dy;
	}
	/**
	 * Reflects the ball off of a surface. 
	 * @param surfaceAngle Angle of the surface the ball is reflecting of
	 */
	private void bounce(Angle surfaceAngle)
	{
		//https://webfiles.uci.edu/sdeng/www/academia/coding/vector.html
		// Calculate new direction.
		this.direction = surfaceAngle.multiply(2).minus(this.direction);
		this.direction = new Angle(surfaceAngle.getDegrees() % 360);
	}
	//---- GETTERS AND SETTERS -----------------------------------------------//
	public int getX()
	{
		return (int) Math.round(this.x_loc);
	}
	
	public double getX_loc()
	{
		return x_loc;
	}

	public void setX_loc(double x_loc)
	{
		this.x_loc = x_loc;
	}

	public double getY_loc()
	{
		return y_loc;
	}

	public int getY()
	{
		return (int) Math.round(y_loc);
	}
	public void setY_loc(double y_loc)
	{
		this.y_loc = y_loc;
	}

	public double getSpeed()
	{
		return speed;
	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	public Angle getDirection()
	{
		return direction;
	}

	public void setDirection(Angle direction)
	{
		this.direction = direction;
	}

	public int getHeight()
	{
		return height;
	}

	public int getWidth()
	{
		return width;
	}
	
}
