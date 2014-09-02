package engine.gamestate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
    
    //---- LOGIC METHODS ------------------------------------------------------/
    public void incrementScoreP1()
    {
    	this.score_1++;
    }
    public void incrementScoreP2()
    {
    	this.score_2++;
    }
    //---- SERIALIZATION ------------------------------------------------------/
	public static byte[] serialize(GameState o)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			// get the byte array of the object
			byte[] obj = baos.toByteArray();
			baos.close();
			return obj;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static GameState deserialize(byte[] data, int offset, int length)
	{
		try
		{
			ObjectInputStream iStream = new ObjectInputStream(
					new ByteArrayInputStream(data, offset, length));
			GameState obj = (GameState) iStream.readObject();
			iStream.close();
			return obj;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
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
    
    
    
}
