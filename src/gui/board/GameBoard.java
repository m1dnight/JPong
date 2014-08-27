package gui.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class GameBoard extends JPanel implements ActionListener 
{
	// Gametime variables
	private int player1_y;
	
	private int ball_angle = 45; // Zero degrees = horizontal.
	private int ball_x     = 150;
	private int ball_y     = 300;
	private int x_traj     = 1;
	private int y_traj     = 1;
	
	// Board Size
	private static final long serialVersionUID = 1L;
	// Constants
	private final int B_WIDTH        = 600; // Board width
    private final int B_HEIGHT       = 300; // Board heigth
    private final int BALL_SIZE      = 10;  // Size the ball
    private final int PADDLE_WIDTH   = 5;  // Length of the paddle
    private final int PADDLE_HEIGHT  = 15;
    private final int PADDLE_PADDING = 5; // Padding from the side of the screen.
    private final int REFRESH_RATE   = 5; // Rate of the timer to refresh the screen.
    
    private int STEP_SIZE     = 10;
    private int BALL_STEPSIZE = 5;
    // Images
    private Image ball;
    private Image paddle;
    
    // Private working variables
    private Timer timer;
    
    /**************************************************************************/
    /*** SETUP ****************************************************************/
    /**************************************************************************/
    public GameBoard()
    {
    	// Load the images.
    	ImageIcon a = new ImageIcon(getClass().getResource("/ball.png"));
    	ball = a.getImage();
    	ImageIcon b = new ImageIcon(getClass().getResource("/paddle.png"));
    	paddle = b.getImage();
    	
    	// Listen for keys.
    	this.addKeyListener(new TAdapter());
    	
    	// Config JPanel.
    	this.setFocusable(true); // Required for the keylistener to work.
    	this.setBackground(Color.BLACK);
    	this.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

    	// Set initial game state.
    	player1_y = 10;
    	
    	// Configure the timer.
    	timer = new Timer(REFRESH_RATE, this);
    	timer.start();
    }
    
    /**************************************************************************/
    /*** OVERRIDDEN METHODS ***************************************************/
    /**************************************************************************/
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    /**
     * This method defines what happens every tick of the timer 'timer'.
     */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Update the trajectory of the ball.
		updateBall();
        repaint();
	}
    /**************************************************************************/
    /*** HELPER METHODS *******************************************************/
    /**************************************************************************/

    /**
     * Draw the entire gameboard.
     * @param g
     */
	private void doDrawing(Graphics g)
	{
		drawScore(g);
		// Draw the ball.
		g.drawImage(ball, ball_x, ball_y, this);

		// Draw the paddle.
		g.drawImage(paddle, this.PADDLE_PADDING, player1_y, this);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private void updateBall()
	{
		// http://gamedev.stackexchange.com/questions/73593/calculating-ball-trajectory-in-pong
		// If the ball is not hitting anything, we simply move it.
		// http://en.wikipedia.org/wiki/Polar_coordinate_system
		if (ballHits())
		{
			// Bounce the ball off the wall.
			updateTrajectory();
		}
		// http://en.wikipedia.org/wiki/Polar_coordinate_system
		// Convert the angle to radians.
		double angle = (ball_angle * Math.PI) / 180;

		// Calculate the next point using polar coordinates.
		ball_x = ball_x + (int) (x_traj * BALL_STEPSIZE * Math.cos(angle));
		ball_y = ball_y + (int) (y_traj * BALL_STEPSIZE * Math.sin(angle));
		System.out.printf("Ball: (%d,%d) @ %d\n", ball_x, ball_y, ball_angle);
	}

	private void updateTrajectory()
	{
		// Depending on the collision type, we update the variables differently.
		System.out.printf("Ball angle changed from %d -> %d\n", ball_angle,
				180 - ball_angle);
		ball_angle = 180 - ball_angle;
	}
    private boolean ballHits()
    {
    	// If we came out of bounds just reset it.
    	ball_y = Math.max(0,  ball_y);
    	ball_x = Math.max(0,  ball_x);
    	// Check to see if it hits any walls.
    	// Top
    	if(ball_y <= 0)
    	{
    		System.out.println("Collision on top");
    		y_traj *= -1;
    		x_traj *= -1;
    		return true;
    	}
    	// Left
    	if(ball_x <= 0)
    	{
    		System.out.println("Collision on left");
    		//y_traj *= -1;
    		//x_traj *= -1;
    		return true;
    	}
    	// Right
    	if(ball_x >= B_WIDTH)
    	{
    		System.out.println("Collision on right");
    		//y_traj *= -1;
    		//x_traj *= -1;
    		return true;
    	}
    	// Bottom
    	if(ball_y >= B_HEIGHT)
    	{
    		System.out.println("Collision on bottom");
    		y_traj *= -1;
    		x_traj *= -1;
    		return true;
    	}
    	return false;
    }
    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }
    
    private void drawScore(Graphics g)
    {
//    	String scoreMsg = "Score: " + gameState.getSnakeSize();
//        Font small = new Font("Helvetica", Font.BOLD, 14);
//        FontMetrics metr = getFontMetrics(small);
//
//        g.setColor(Color.white);
//        g.setFont(small);
//        g.drawString(scoreMsg,2,12);
    }
    /**************************************************************************/
    /*** KEYADAPTER TO HANDLE KEYEVENTS FROM USER *****************************/
    /**************************************************************************/
    /**
     * The KeyAdapter makes sure that impossible scenarios are ignored.
     * E.g.: snake is going up and user presses down is an invalid move.
     * @author ChristopheRosaFreddy
     */
	private class TAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_UP))
			{
				player1_y -= STEP_SIZE;
			}
			if ((key == KeyEvent.VK_DOWN))
			{
				player1_y += STEP_SIZE;
			}
			if((key == KeyEvent.VK_SPACE))
			{
				if(timer.isRunning())
				    timer.stop();
				else
					timer.start();
			}
		}
	}
}
