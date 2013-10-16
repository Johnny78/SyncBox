package clientApp;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
			
			outToClient.writeBytes("get metadata");
			
			//receive xmlfile
			
			int bytesRead = 0;
			int currentTotal = 0;
			int bufferSize = 0;
			
	        byte [] bytearray  = new byte [bufferSize];
	        InputStream is = clientSocket.getInputStream();
	        FileOutputStream fos = new FileOutputStream("copy.doc");
	        BufferedOutputStream bos = new BufferedOutputStream(fos);
	        bytesRead = is.read(bytearray,0,bytearray.length);
	        currentTotal = bytesRead;
	 
	        do {
	           bytesRead =
	              is.read(bytearray, currentTotal, (bytearray.length-currentTotal));
	           if(bytesRead >= 0) currentTotal += bytesRead;
	        } while(bytesRead > -1);
	 
	        bos.write(bytearray, 0 , currentTotal);
	        bos.flush();
	        bos.close();
			
			//compare xmlfile
			
			outToClient.writeBytes("quit");

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
