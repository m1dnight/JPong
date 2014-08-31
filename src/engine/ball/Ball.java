package engine.ball;

import java.awt.Color;
import java.awt.Graphics;

import utils.Printer;
import engine.Collision;
import engine.Draw;
import engine.paddle.Paddle;
import engine.util.Angle;

public class Ball implements Collision, Draw
{
	// Constant variables.
	private final double BALL_DEFAULT_SPEED = 1.0D;

	private int y_boundary;
	private int x_boundary;
	
	private double x_loc; // X location in the plane.
	private double y_loc; // Y location in the plane.
	
	private double speed;     // Double value indicating the speed of the ball. (> 1)
	private Angle  direction; // Direction in radians (0 - 360 degrees).

	private int radius;
	
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
	
	public Ball(int speed, Angle direction, int x, int y, int x_bound, int y_bound, int radius)
	{
		// Initialize the ball.
		this.speed      = speed;
		this.direction  = direction;
		this.x_loc      = x;
		this.y_loc      = y;
		this.y_boundary = y_bound;
		this.x_boundary = x_bound;
		this.radius = radius;
	}
	
	//---- COLLIDABLE INTERFACE METHODS --------------------------------------//
	@Override
	public boolean collidesWith(Collision object)
	{
		// Iterate over all the points on the circumference.
		for (int x = (int) (this.x_loc - radius); x <= this.x_loc; x++)
		{
		    for (int y = (int) (this.y_loc - radius) ; y <= this.y_loc; y++)
		    {
		        if (inHitbox(x, y)) // we don't have to take the root, it's slow
		        {
		            if(object.inHitbox(x, y))
		            	return true;
		            if(object.inHitbox(-x, y))
		            	return true;
		            if(object.inHitbox(x, -y))
		            	return true;
		            if(object.inHitbox(-x, -y))
		            	return true;
		        }
		    }
		}
		return false;
	}

	@Override
	public boolean inHitbox(int x, int y)
	{
		// Determine if given x and y coordinates are in our hitbox.
		return Math.pow(x - this.x_loc, 2) + Math.pow(y - this.y_loc, 2) < Math.pow(this.radius, 2);
	}
	//---- DRAW INTERFACE ----------------------------------------------------///**
	/* Draws the ball on the given graphics.
	 * @param g
	 */
	public void draw(Graphics g)
	{
		// Calculate the center of the ball.
		g.setColor(Color.white);
		g.fillOval((int)this.x_loc, (int)this.y_loc, this.radius * 2, this.radius * 2);
	}
	//---- LOGIC METHODS -----------------------------------------------------//
	/**
	 * Moves the ball one tick.
	 * @param player1 
	 */
	public int move(Paddle player1, Paddle player2)
	{
		// Store current location coordinates
		double oldX = this.x_loc;
		double oldY = this.y_loc;
		// Calculate the addition to the coordinates.
		double dx = speed * Math.cos(direction.getRadians());
		double dy = speed * Math.sin(direction.getRadians());
		
		// Set the next coordinates to check for a collision.
		this.x_loc += dx;
		this.y_loc += dy;
		
		// Check for a collision with player 1 (left).
		if(player1.collidesWith(this))
		{
			// Make sure we are out of the bounds of the paddle.
			//this.x_loc = player1.getX_loc() + player1.getWidth() + 1;
			// Restore old coordinates
			this.x_loc = oldX;
			this.y_loc = oldY;
			bounce(new Angle(90));
		}
		// Check for a collision with player 2 (right).
		if(player2.collidesWith(this))
		{
			// Make sure we are out of the bounds of the paddle.
			//this.x_loc = player2.getX_loc();
			// Restore old coordinates
			this.x_loc = oldX;
			this.y_loc = oldY;
			bounce(new Angle(90));
		}
		// Check to see if we are hitting the walls.
		// Right wall
		if(this.x_loc >= x_boundary)
			return -1;
		// Left wall
		if(this.x_loc <= 0)
			return 1;
		
		if(this.y_loc + dy >= y_boundary || this.y_loc + dy <= 0)
		{
			// Reflect off the top or bottom (0°).
			Printer.debugMessage(this.getClass(),  String.format("(%f, %f) bounced on floor/ceiling", this.x_loc, this.y_loc));;			// Restore old coordinates
			this.x_loc = oldX;
			this.y_loc = oldY;
			bounce(new Angle(0));
		}
	
		// Nothing hit, update the new location.
		this.x_loc = Math.max(this.x_loc, 0);
		this.y_loc = Math.max(this.y_loc,  0);
		Printer.debugMessage(this.getClass(),  String.format("(%f, %f) New coordinates", this.x_loc, this.y_loc));
		return 0;
		
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
