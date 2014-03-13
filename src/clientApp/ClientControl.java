package clientApp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.Socket;


public class ClientControl {

	private String serverAddress;
	private int serverPort;
	private Socket clientSocket;
	private DataOutputStream outToServer;
	private DataInputStream dis;
	private BufferedReader inFromServer;

	public ClientControl(String serverAddress, int serverPort) throws Exception{
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
	
	public void sendFile(String fileName){
		String filePath = "client\\syncBox\\" + fileName;
		File f = new File(filePath);
		if (!f.exists()){
			System.out.println("No such File");
		}
		else{
			try {
				clientSocket = new Socket(serverAddress, serverPort);
				System.out.println("Connecting...");
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				System.out.println("here");
				outToServer.writeBytes("recieve file\n");	
				String response = inFromServer.readLine();		//wait till server is ready
				System.out.println(response);
				outToServer.writeBytes(fileName + "\n");		//send path
				RandomAccessFile raf = new RandomAccessFile(filePath, "r");
				byte[] buff = new byte[8192];
				try{
					long length = raf.length();
					outToServer.writeLong(length);
					while ((raf.read(buff, 0, 8192)) != -1){
						outToServer.write(buff);
					}
				} 
				finally {
					raf.close();
					outToServer.flush();
					outToServer.writeBytes("quit\n");
					response = inFromServer.readLine();
					System.out.println(response);
				}
				System.out.println(filePath+" sent");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
