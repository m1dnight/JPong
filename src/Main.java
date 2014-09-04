import gui.Pong;
import net.server.GameServer;

public class Main
{
	public static void main(String[] args)
	{

		// Check to see if we need to run the server instead.
		if (args.length > 0 && args[0].equals("--server"))
		{
			GameServer server = new GameServer();
			server.start();
			System.out.println("Running server mode");
		} else
		{
			Pong s = new Pong();
			s.setVisible(true);
		}
	}
}