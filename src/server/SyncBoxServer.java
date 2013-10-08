package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncBoxServer {

	private int port;
	
	public void run (int port) throws Exception{
		boolean serving = true;
		ServerSocket welcomeSocket = new ServerSocket(port);
		
		while (serving){
			System.out.println("serving");
			Socket clientSocket = welcomeSocket.accept();
			System.out.println("welcome client !");
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
			boolean clientConnected = true;
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (clientConnected){
				
				String clientCommand = inFromClient.readLine();
				
				if (clientCommand.equals("quit")){
					clientConnected = false;
					System.out.println("exiting");
				}
				else if (clientCommand != null){
					System.out.println(clientCommand);
				}
			}
		}
	}
}
