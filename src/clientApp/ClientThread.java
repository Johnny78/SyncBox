package clientApp;

public class ClientThread {
	
	public static void main(String[] args)throws Exception{
		ClientSync client = new ClientSync ("192.168.1.177", 20661);
		client.connect();
	}
	
}
