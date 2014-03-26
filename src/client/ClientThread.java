package client;

public class ClientThread {
	
	public static void main(String[] args)throws Exception{
		ClientControl client = new ClientControl ("192.168.1.141", 20661);

		//client.getServerMetaData();
		//client.getFile("ex.mkv");
		//client.sendFile("vid.avi");
	}
	
}
