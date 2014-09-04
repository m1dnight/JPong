package engine.gamestate;

import java.io.Serializable;

import net.packets.ReplyStatus;
import engine.ball.Ball;
import engine.paddle.Paddle;

public class GameState implements Serializable
{
	private static final long serialVersionUID = 7929580719189448552L;
	private Ball   ball;
    private Paddle player1;
    private Paddle player2;
    private int    score_1  = 0;
    private int    score_2  = 0;
    private ReplyStatus gameStatus;
    
    //---- LOGIC METHODS ------------------------------------------------------/
    public void incrementScoreP1()
    {
    	this.score_1++;
    }
    public void incrementScoreP2()
    {
    	this.score_2++;
    }
    //---- getters and setters ------------------------------------------------/
    public GameState(Paddle player1, Paddle player2, Ball ball)
    {
    	this.ball = ball;
    	this.player1 = player1;
    	this.player2 = player2;
    }

	public Ball getBall()
	{
		return ball;
	}

	public void setBall(Ball ball)
	{
		this.ball = ball;
	}

	public Paddle getPlayer1()
	{
		return player1;
	}

	public void setPlayer1(Paddle player1)
	{
		this.player1 = player1;
	}

	public Paddle getPlayer2()
	{
		return player2;
	}

	public void setPlayer2(Paddle player2)
	{
		this.player2 = player2;
	}

	public int getScore_1()
	{
		return score_1;
	}

	public void setScore_1(int score_1)
	{
		this.score_1 = score_1;
	}

	public int getScore_2()
	{
		return score_2;
	}

	public void setScore_2(int score_2)
	{
		this.score_2 = score_2;
	}
	/**
	 * @return the gameStatus
	 */
	public ReplyStatus getGameStatus()
	{
		return gameStatus;
	}
	/**
	 * @param gameStatus the gameStatus to set
	 */
	public void setGameStatus(ReplyStatus gameStatus)
	{
		this.gameStatus = gameStatus;
	}
    
    
    
}
