import net.GameClient;
import net.GameServer;
import net.TestObject;


public class Scratchpad
{
	static GameServer server;
	static GameClient client;
	static GameClient client2;
	public static void main(String[] args)
	{
		server = new GameServer(null);
		server.start();
		client2 = new GameClient("client2", null, "localhost");
		client2.start();
		
		client = new GameClient("client1", null, "localhost");
		client.start();
		
		TestObject tester = new TestObject(1234);
		
		client.sendData(TestObject.serialize(tester));
		//client2.sendData("hello world".getBytes());
		
	}
}
