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
	
	private double x_center; // X location in the plane.
	private double y_center; // Y location in the plane.
	
	private double speed;     // Double value indicating the speed of the ball. (> 1)
	private Angle  direction; // Direction in radians (0 - 360 degrees).

	private int radius;
	
	//---- CONSTRUCTORS ------------------------------------------------------//
	public Ball(int x_bound, int y_bound)
	{
		// Initialize the ball.
		this.speed      = BALL_DEFAULT_SPEED;
		this.direction  = Angle.randomAngle();
		this.x_center      = x_boundary / 2;
		this.y_center      = y_boundary / 2;
		this.y_boundary = y_bound;
		this.x_boundary = x_bound;
	}
	
	public Ball(int speed, Angle direction, int x, int y, int x_bound, int y_bound, int radius)
	{
		// Initialize the ball.
		this.speed      = speed;
		this.direction  = direction;
		this.x_center      = x;
		this.y_center      = y;
		this.y_boundary = y_bound;
		this.x_boundary = x_bound;
		this.radius = radius;
	}
	
	//---- COLLIDABLE INTERFACE METHODS --------------------------------------//
	@Override
	public boolean collidesWith(Collision object)
	{
		// Iterate over all the points on the circumference.
		for (int x = (int) (this.x_center - radius); x <= this.x_center; x++)
		{
		    for (int y = (int) (this.y_center - radius) ; y <= this.y_center; y++)
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
		return Math.pow(x - this.x_center, 2) + Math.pow(y - this.y_center, 2) < Math.pow(this.radius, 2);
	}
	//---- DRAW INTERFACE ----------------------------------------------------///**
	/* Draws the ball on the given graphics.
	 * @param g
	 */
	public void draw(Graphics g)
	{
		// Calculate draw point.
		double drawx = this.x_center - (radius/2);
		double drawy = this.y_center - (radius/2);
		
		g.setColor(Color.white);
		g.fillOval((int)drawx, (int)drawy, this.radius * 2, this.radius * 2);
	}
	//---- LOGIC METHODS -----------------------------------------------------//
	/**
	 * Moves the ball one tick.
	 * @param player1 
	 */
	public int move(Paddle player1, Paddle player2)
	{
		// Store current location coordinates in case of a collision.
		double oldX = this.x_center;
		double oldY = this.y_center;
		
		// Move the ball.
		this.updateLocation();
		
		// Check for a collision with player 1 (left).
		if(player1.collidesWith(this))
		{
			// Restore the location because the next one is a collision.
			restoreLocation(oldX, oldY);
			bounce(new Angle(90));
		}
		// Check for a collision with player 2 (right).
		if(player2.collidesWith(this))
		{
			restoreLocation(oldX, oldY);
			bounce(new Angle(90));
		}
		// Check to see if we are hitting the walls.
		// Right wall
		if(this.x_center >= x_boundary)
			return -1;
		// Left wall
		if(this.x_center <= 0)
			return 1;
		
		if(this.y_center + radius >= y_boundary || this.y_center - radius <= 0)
		{
			restoreLocation(oldX, oldY);
			bounce(new Angle(0));
		}
		return 0;
	}
	
	private void updateLocation()
	{
		// Calculate the addition to the coordinates.
		double dx = speed * Math.cos(direction.getRadians());
		double dy = speed * Math.sin(direction.getRadians());
		// Set the next coordinates to check for a collision.
		this.x_center += dx;
		this.y_center += dy;
	}
	/**
	 * Restores the location of the ball to the given coordinates.
	 * @param oldX
	 * @param oldY
	 */
	private void restoreLocation(double oldX, double oldY)
	{
		this.x_center = oldX;
		this.y_center = oldY;
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
		return (int) Math.round(this.x_center);
	}
	
	public double getX_loc()
	{
		return x_center;
	}

	public void setX_loc(double x_loc)
	{
		this.x_center = x_loc;
	}

	public double getY_loc()
	{
		return y_center;
	}

	public int getY()
	{
		return (int) Math.round(y_center);
	}
	public void setY_loc(double y_loc)
	{
		this.y_center = y_loc;
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
