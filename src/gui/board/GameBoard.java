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

import engine.Angle;
import engine.ball.Ball;


public class GameBoard extends JPanel implements ActionListener, Runnable
{
	private static final long           serialVersionUID = 1L;
	
	// Constants
	private final int BOARD_WIDTH    = 300; // Board width
    private final int BOARD_HEIGHT   = 300; // Board heigth
    private final int BALL_SIZE      = 10;  // Size the ball
    private final int REFRESH_RATE   = 1;  // Rate of the timer to refresh the screen.
    
    // Runtime variables
    private Ball ball;
    // Images
    private Image img_ball;
    
    // Private working variables
    private Timer timer;
    
    /**************************************************************************/
    /*** SETUP ****************************************************************/
    /**************************************************************************/
    public GameBoard()
    {
    	// Load the images.
    	ImageIcon a = new ImageIcon(getClass().getResource("/ball.png"));
    	img_ball = a.getImage();
    	ball = new Ball();
    	
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
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}


}
