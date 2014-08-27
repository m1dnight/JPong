package engine;

import engine.ball.Ball;

public class GameThread implements Runnable
{
	private Ball ball;
	public GameThread()
	{
		this.ball = new Ball();
	}
	@Override
	public void run()
	{
		
	}

}
