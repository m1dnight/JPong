package engine.paddle;

public class Paddle
{
	private final double PADDLE_DEFAULT_SPEED = 1.0D;
	private final int PADDLE_DEFAULT_HEIGHT = 45;
	
	// Boundaries for movement.
	private int    y_boundary;
	
	private int height;
	
	// Current status variables.
	private double y_loc;      // Y location in the plane.
	private double speed;     // Double value indicating the speed of the ball. (> 1)
	
	//---- CONSTRUCTORS ------------------------------------------------------//
	public Paddle(int y_bound)
	{
		this.y_boundary = y_bound;
		this.speed      = PADDLE_DEFAULT_SPEED; // Set default speed.
		this.y_loc      = (y_boundary - PADDLE_DEFAULT_HEIGHT) / 2; // Center the paddle.
		this.height = PADDLE_DEFAULT_HEIGHT;
	}
	public Paddle(int y_bound, Double speed, int y_location, int height)
	{
		this.y_boundary = y_bound;
		this.speed      = speed; // Set default speed.
		this.y_loc      = y_location;
		this.height = height;
	}
	//---- LOGIC METHODS -----------------------------------------------------//

	public void moveDown()
	{
		if(this.y_loc + PADDLE_DEFAULT_HEIGHT < y_boundary)
			this.y_loc += 1;
		
	}
	public void moveUp()
	{
		if(this.y_loc > 0)
		this.y_loc -= 1;
		
	}
	//---- GETTERS AND SETTERS -----------------------------------------------//
	public int getY_boundary()
	{
		return y_boundary;
	}
	public void setY_boundary(int y_boundary)
	{
		this.y_boundary = y_boundary;
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
	
}
