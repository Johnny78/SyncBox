package clientApp;

public class ClientThread {
	
	public static void main(String[] args)throws Exception{
		ClientSync client = new ClientSync ("192.168.1.141", 20661);
		//client.wait(1000);
		client.connect();
	}
	
}
