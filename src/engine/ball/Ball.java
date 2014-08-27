package engine.ball;

import utils.Printer;
import engine.paddle.Paddle;
import engine.util.Angle;

public class Ball
{
	// Constant variables.
	private final double BALL_DEFAULT_SPEED = 1.0D;

	private int y_boundary;
	private int x_boundary;
	
	private double x_loc; // X location in the plane.
	private double y_loc; // Y location in the plane.
	
	private double speed;     // Double value indicating the speed of the ball. (> 1)
	private Angle  direction; // Direction in radians (0 - 360 degrees).
	
	//---- CONSTRUCTORS ------------------------------------------------------//
	public Ball(int x_bound, int y_bound)
	{
		// Initialize the ball.
		this.speed      = BALL_DEFAULT_SPEED;
		this.direction  = Angle.randomAngle();
		this.x_loc      = x_boundary / 2;
		this.y_loc      = y_boundary / 2;
		this.y_boundary = y_bound;
		this.x_boundary = x_bound;
	}
	
	public Ball(int speed, Angle direction, int x, int y, int x_bound, int y_bound)
	{
		// Initialize the ball.
		this.speed      = speed;
		this.direction  = direction;
		this.x_loc      = x;
		this.y_loc      = y;
		this.y_boundary = y_bound;
		this.x_boundary = x_bound;
	}
	
	//---- LOGIC METHODS -----------------------------------------------------//
	/**
	 * Moves the ball one tick.
	 * @param player1 
	 */
	public void move(Paddle player1)
	{
		
		// Calculate the addition to the coordinates.
		double dx = speed * Math.cos(direction.getRadians());
		double dy = speed * Math.sin(direction.getRadians());
		
		// Check for a collision with player 1 (left).
		if(this.x_loc < player1.getPadding() + player1.getWidth() &&
		   this.y_loc > player1.getY() && 
		   this.y_loc < player1.getY() + player1.getHeight())
		{
			Printer.debugMessage(this.getClass(),  "Collision with left player");
			// Make sure we are out of the bounds of the paddle.
			this.x_loc = player1.getPadding() + player1.getWidth() + 1;
			
			bounce(new Angle(90));
		}
		// Check to see if we are hitting the walls.
		if(this.x_loc + dx >= x_boundary || this.x_loc + dx <= 0)
		{
			Printer.debugMessage(this.getClass(),  String.format("(%f, %f) bounced on left/right wall", this.x_loc, this.y_loc));
			// Reflect off the sides (90°).
			bounce(new Angle(90));
		}
		if(this.y_loc + dy >= y_boundary || this.y_loc + dy <= 0)
		{
			// Reflect off the top or bottom (0°).
			Printer.debugMessage(this.getClass(),  String.format("(%f, %f) bounced on floor/ceiling", this.x_loc, this.y_loc));;
			bounce(new Angle(0));
		}
		
		// Nothing hit, update the new location.
		this.x_loc = Math.max(this.x_loc + dx, 0);
		this.y_loc = Math.max(this.y_loc + dy,  0);
		Printer.debugMessage(this.getClass(),  String.format("(%f, %f) New coordinates", this.x_loc, this.y_loc));;
		
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
		Printer.debugMessage(this.getClass(), String.format("new direction: %d", this.direction.getDegrees()));
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
		return y_boundary;
	}

	public int getWidth()
	{
		return x_boundary;
	}
	
}
