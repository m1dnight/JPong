import net.TestObject;
import net.client.GameClient;
import net.server.GameServer;


public class Scratchpad
{
	static GameServer server;
	static GameClient client;
	static GameClient client2;
	public static void main(String[] args)
	{
		// Start the server
		server = new GameServer();
		server.start();
		
		// Init client (sender).
		client = new GameClient("client1", null, "localhost");
		
		// Create object to send.
		TestObject tester = new TestObject(1234);
		
		// Send the object.
		client.sendData(TestObject.serialize(tester));
		//client2.sendData("hello world".getBytes());
	}
}
