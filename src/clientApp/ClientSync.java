package clientApp;

import java.io.DataOutputStream;
import java.net.Socket;

public class ClientSync {

	private String serverAddress;
	private int serverPort;
	private Socket clientSocket;
	
	public ClientSync(String serverAddress, int serverPort) throws Exception{
		System.out.println("creating client");
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	public void connect() throws Exception{
		try{
			
			clientSocket = new Socket(serverAddress, serverPort);
			System.out.println("attempting to connect");
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

			outToClient.writeBytes("quit");

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
