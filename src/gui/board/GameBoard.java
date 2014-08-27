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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import engine.ball.Ball;
import engine.paddle.Paddle;
import engine.util.Angle;


public class GameBoard extends JPanel implements ActionListener
{
	private static final long           serialVersionUID = 1L;
	// Non final, yet constant variables.
	private int PADDLE_IMG_SIZE  = 15;
	// Constants
	private final int BOARD_WIDTH      = 1000; // Board width
    private final int BOARD_HEIGHT     = 1000; // Board heigth
    private final int BALL_SIZE        = 10;  // Size the ball
    private final int PADDLE__SIZE     = 45;

    private final int REFRESH_RATE     = 1;  // Rate of the timer to refresh the screen.
    
    // Runtime variables
    private Ball ball;
    private Paddle player1;
    
    // Images
    private Image img_ball;
    private Image img_paddle;
    
    // Private working variables
    private Timer timer;
    
    // Key handler
    private boolean keyUp = false, keyDown = false;
    
    /**************************************************************************/
    /*** SETUP ****************************************************************/
    /**************************************************************************/
    public GameBoard()
    {
    	// Load the images.
    	ImageIcon a = new ImageIcon(getClass().getResource("/ball.png"));
    	img_ball = a.getImage();
    	ImageIcon b = new ImageIcon(getClass().getResource("/paddle.png"));
    	img_paddle = b.getImage();
    	
    	// Get the height of the image.
    	PADDLE_IMG_SIZE = img_paddle.getHeight(null);
    	
    	// Init ball and paddle.
    	ball = new Ball(BOARD_WIDTH - BALL_SIZE, BOARD_HEIGHT - BALL_SIZE);
    	player1 = new Paddle(BOARD_HEIGHT, 1.0D, 150, PADDLE__SIZE);
    	
    	// Listen for keys.
    	this.addKeyListener(new TAdapter());
    	
    	// Config JPanel.
    	this.setFocusable(true); // Required for the keylistener to work.
    	this.setBackground(Color.BLACK);
    	this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    	
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
		ball.move();
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
		// Draw the ball.
		g.drawImage(img_ball, ball.getX(), ball.getY(), this);
		
		// Draw the paddle.
		int height = 0;
		while(height < PADDLE__SIZE / PADDLE_IMG_SIZE)
		{
			g.drawImage(img_paddle, 10, (height * PADDLE_IMG_SIZE) +  player1.getY(), this);
			height++;
		}
		g.drawImage(img_paddle, 10, player1.getY(), this);
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
    /**************************************************************************/
    /*** KEYADAPTER TO HANDLE KEYEVENTS FROM USER *****************************/
    /**************************************************************************/
    /**
     * The KeyAdapter makes sure that impossible scenarios are ignored.
     * E.g.: snake is going up and user presses down is an invalid move.
     * @author ChristopheRosaFreddy
     */
	private class TAdapter extends KeyAdapter implements ActionListener
	{
		// This timer will be started when we press an up key.
		// When it is released we stop the timer.
		// This makes for a smooth movement.
		private Timer keyTimer;
		
		public TAdapter()
		{
			keyTimer = new Timer((int) player1.getSpeed(), this);
		}
		/**
		 * Handles what has to be done every tick of
		 * the keyTimer timer. I.e., makes the paddle move.
		 * @param e
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(keyUp)
				player1.moveUp();
			if(keyDown)
				player1.moveDown();
			
		}
		@Override
		public void keyPressed(KeyEvent e)
		{
			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_UP))
				keyUp = true;
			
			if ((key == KeyEvent.VK_DOWN))
				keyDown = true;
			
			if((key == KeyEvent.VK_SPACE))
			{
				if(timer.isRunning())
				    timer.stop();
				else
					timer.start();
			}
			keyTimer.start();
		}
		@Override
		public void keyReleased(KeyEvent e)
		{
			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_UP))
				keyUp = false;
			
			if ((key == KeyEvent.VK_DOWN))
				keyDown = false;
			keyTimer.stop();
		}

	}


}
