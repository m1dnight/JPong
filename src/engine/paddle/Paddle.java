package engine.paddle;

public class Paddle
{
	private final double PADDLE_DEFAULT_SPEED  = 1.0D;
	private final int    PADDLE_DEFAULT_HEIGHT = 45;
	
	// Boundaries for movement.
	private int    y_boundary;
	private int    padding;


	private int height;
	private int width;
	
	// Current status variables.
	private double y_loc;      // Y location in the plane.
	private double speed;     // Double value indicating the speed of the ball. (> 1)
	
	//---- CONSTRUCTORS ------------------------------------------------------//
	public Paddle(int y_boundary, double speed, int height, int width, int padding)
	{
		this.y_boundary = y_boundary;
		this.speed      = speed; // Set default speed.
		this.y_loc      = 0;
		this.height     = height;
		this.padding    = padding;
		this.width      = width;
	}

	//---- LOGIC METHODS -----------------------------------------------------//

	public Paddle(int bOARD_HEIGHT, double d, int i, int pADDLE_HEIGHT,
			int pADDLE_WIDTH, int pADDLE_PADDING)
	{
		// TODO Auto-generated constructor stub
	}
	public void moveDown()
	{
		if(this.y_loc + height < y_boundary)
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
	
	public int getPadding()
	{
		return padding;
	}
	
	public void setPadding(int padding)
	{
		this.padding = padding;
	}
	
	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}
	
}
