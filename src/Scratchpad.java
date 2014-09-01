import net.GameClient;
import net.GameServer;


public class Scratchpad
{
	static GameServer server;
	static GameClient client;
	public static void main(String[] args)
	{
		server = new GameServer(null);
		server.start();
		client = new GameClient(null, "localhost");
		client.start();
		
		client.sendData("hello world".getBytes());
		
	}
}
