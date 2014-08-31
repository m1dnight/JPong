package engine.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import engine.ball.Ball;
import engine.paddle.Paddle;
import engine.util.Angle;


public class GameBoard extends JPanel implements ActionListener
{
	private static final long           serialVersionUID = 1L;
	// Non final, yet constant variables.
	// Constants
	private final int BOARD_WIDTH; // Board width
    private final int BOARD_HEIGHT; // Board heigth
    private final int BALL_SIZE      = 15;  // Size the ball
    private final int PADDLE_HEIGHT  = 50;  // Size of the paddle
    private final int PADDLE_WIDTH   = 10;
    private final int PADDLE_PADDING = 10; // Padding between paddle and wall.
    private final int REFRESH_RATE   = 2;  // Rate of the timer to refresh the screen.
    
    // Runtime variables
    private Ball   ball;
    private Paddle player1;
    private Paddle player2;
    private int    score_1  = 0;
    private int    score_2  = 0;
    // Private working variables
    private Timer timer;
    
    // Key handler
    private boolean keyUpPlayer1 = false, keyDownPlayer1 = false,
    				keyUpPlayer2 = false, keyDownPlayer2 = false;
    
    /**************************************************************************/
    /*** SETUP ****************************************************************/
    /**************************************************************************/
    public GameBoard(int width, int height)
    {
    	// Initialize board parameters
    	this.BOARD_WIDTH = width;
    	this.BOARD_HEIGHT = height;
    	
    	
    	ball = new Ball(1, Angle.randomAngle(0,90).add(135), BOARD_WIDTH /2, BOARD_HEIGHT / 2, BOARD_WIDTH - BALL_SIZE, 
        	            BOARD_HEIGHT - BALL_SIZE, BALL_SIZE / 2);
    	ball = new Ball(1, new Angle(0), BOARD_WIDTH /2, BOARD_HEIGHT / 2, BOARD_WIDTH - BALL_SIZE, 
	            BOARD_HEIGHT - BALL_SIZE, BALL_SIZE / 2);
		player1 = new Paddle(
				BOARD_HEIGHT / 2,
				PADDLE_PADDING,
				1.0D, // Speed of the paddle.
				PADDLE_HEIGHT, // Height of the paddle in pixels
				PADDLE_WIDTH); // Paddle padding from wall.
		player2 = new Paddle(
				BOARD_HEIGHT / 2,
				BOARD_WIDTH - PADDLE_PADDING - PADDLE_WIDTH,
				1.0D, // Speed of the paddle.
				PADDLE_HEIGHT, // Height of the paddle in pixels
				PADDLE_WIDTH); // Paddle padding from wall.
		
    	// Listen for keys.
    	this.addKeyListener(new TAdapter());
    	
    	// Config JPanel.
    	this.setFocusable(true); // Required for the keylistener to work.
    	this.setBackground(Color.BLACK);
    	this.setSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
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
		int out = ball.move(player1, player2);
		if(out != 0)
		{
			// Update the score.
			if(out == -1)
			{
				score_1++;
				// Player 1 gets to serve.
		    	ball = new Ball(1, Angle.randomAngle(0,90).add(135), BOARD_WIDTH / 2, BOARD_HEIGHT / 2, BOARD_WIDTH - BALL_SIZE, 
	    	            BOARD_HEIGHT - BALL_SIZE, BALL_SIZE / 2);
			}
			else
			{
				score_2++;
		    	ball = new Ball(1, Angle.randomAngle(0,90).add(225), BOARD_WIDTH / 2, BOARD_HEIGHT / 2, BOARD_WIDTH - BALL_SIZE, 
	    	            BOARD_HEIGHT - BALL_SIZE, BALL_SIZE / 2);
			}
			

		}
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
		ball.draw(g);
		
		// Draw the paddles.
		player1.draw(g);
		player2.draw(g);
		
		// Draw the score
		drawScore(g);
		
		// Draw the line.
		drawLine(g);
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
	/**
	 * Draws the line in the middle of the gameboard.
	 * @param g
	 */
	private void drawLine(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect((BOARD_WIDTH / 2) - 5, 0, 11, BOARD_HEIGHT);
	}
    private void drawScore(Graphics g)
    {
    	int middle = BOARD_WIDTH / 2;
    	
    	String scoreMsg = String.format("%2s %-2s", score_1,score_2);
        Font big = new Font("Helvetica", Font.BOLD, 48);
        FontMetrics metr = getFontMetrics(big);

        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(scoreMsg,(middle - (metr.stringWidth(scoreMsg) / 2)), 50);
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
			if(keyUpPlayer1)
				player1.moveUp();
			if(keyDownPlayer1)
				player1.moveDown();
			if(keyDownPlayer2)
				player2.moveDown();
			if(keyUpPlayer2)
				player2.moveUp();
		}
		@Override
		public void keyPressed(KeyEvent e)
		{
			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_UP))
				keyUpPlayer1 = true;
			
			if ((key == KeyEvent.VK_DOWN))
				keyDownPlayer1 = true;
			
			if ((key == KeyEvent.VK_Q))
				keyUpPlayer2 = true;
			
			if ((key == KeyEvent.VK_W))
				keyDownPlayer2 = true;
			
			if((key == KeyEvent.VK_SPACE))
			{
				if(timer.isRunning())
				    timer.stop();
				else
					timer.start();
			}
			if(keyDownPlayer2 || keyUpPlayer1 || keyDownPlayer1 || keyUpPlayer2)
				keyTimer.start();
		}
		@Override
		public void keyReleased(KeyEvent e)
		{
			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_UP))
				keyUpPlayer1 = false;
			
			if ((key == KeyEvent.VK_DOWN))
				keyDownPlayer1 = false;
			
			if ((key == KeyEvent.VK_Q))
				keyUpPlayer2 = false;
			
			if ((key == KeyEvent.VK_W))
				keyDownPlayer2 = false;
			
			if(!keyDownPlayer2 && !keyUpPlayer1 && !keyDownPlayer1 && !keyUpPlayer2)
				keyTimer.stop();
		}

	}


}
