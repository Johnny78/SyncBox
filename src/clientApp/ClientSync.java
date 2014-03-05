package clientApp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientSync {

	private String serverAddress;
	private int serverPort;
	private Socket clientSocket;
	private DataOutputStream outToServer;
	private DataInputStream dis;
	private BufferedReader inFromServer;

	public ClientSync(String serverAddress, int serverPort) throws Exception{
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	public void getServerMetaData() throws Exception{
		try{

			clientSocket = new Socket(serverAddress, serverPort);
			System.out.println("Connecting...");
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			dis = new DataInputStream(clientSocket.getInputStream());
			outToServer.writeBytes("get metadata\n");
			System.out.println("Asking for metadata");

			int len = dis.readInt();
			byte[] data = new byte[len];
			if (len > 0) {
				dis.read(data);
			}

			FileOutputStream fileOuputStream = 
					new FileOutputStream("client\\ServerMetaData.xml"); 
			fileOuputStream.write(data);
			fileOuputStream.close();
			System.out.println(len + " bytes of metadata recieved");


			outToServer.writeBytes("quit\n");
			String response = inFromServer.readLine();
			System.out.println(response);

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	
	public void getFile(String filePath){
		try{
			clientSocket = new Socket(serverAddress, serverPort);
			System.out.println("Connecting...");
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			dis = new DataInputStream(clientSocket.getInputStream());
			outToServer.writeBytes("send file\n");
			outToServer.writeBytes(filePath + "\n");
			System.out.println("Asking for file "+ filePath);

			long len = dis.readLong();
			byte[] buff = new byte[8192];
			int result, read = 0;
			FileOutputStream fileOuputStream = 
					new FileOutputStream("client\\SyncBox\\"+ filePath);
			
			do {
				result = dis.read(buff);
				read += result;
				fileOuputStream.write(buff);
				//System.out.println("read so far: "+read);
			}
			while (read < len && result!= -1);			
			fileOuputStream.close();
			System.out.println(len + " bytes recieved from file "+filePath);

			outToServer.writeBytes("quit\n");
			String response = inFromServer.readLine();
			System.out.println(response);

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void sendFile(String filePath){
		
	}
}
