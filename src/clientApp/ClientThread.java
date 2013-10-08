package clientApp;

public class ClientThread {
	
	public static void main(String[] args)throws Exception{
		ClientSync client = new ClientSync ("localhost", 20661);
		client.connect();
	}
	
}
