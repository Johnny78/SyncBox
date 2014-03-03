package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

import xmltools.XMLTool;

public class SyncBoxServer {

	private int port;
	private boolean clientConnected;
	private String syncBoxDir = "server\\SyncBox";
	private String metaDataDir = "server\\metadata.xml";


	public void run (int port) throws Exception{

		boolean serving = true;
		ServerSocket welcomeSocket = new ServerSocket(port);

		while (serving){
			System.out.println("*************");
			System.out.println("Serving...");
			Socket clientSocket = welcomeSocket.accept();
			System.out.println("Client connected");

			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			clientConnected = true;

			while (clientConnected){

				String clientCommand = inFromClient.readLine();

				switch (clientCommand){
								
				case "get metadata":
					System.out.println("Generating metadata");

					//generate meta data
					XMLTool.generateXML(syncBoxDir);

					File f = new File (metaDataDir);
					if (!f.exists()){
						System.out.println("No metadata file found");
					}
					else{						
						RandomAccessFile raf = new RandomAccessFile(metaDataDir, "r");
						byte[] data = null;
						int length = 0;
						try{
							long longlength = raf.length();
							length = (int) longlength;
							data = new byte[length];
							raf.readFully(data);
						} 
						finally {
							raf.close();
						}
						outToClient.writeInt(data.length);
						outToClient.write(data);
						System.out.println(length + " bytes of metadata sent");
					}
					


				case "send file":
					//todo
				case "receive file":
					//todo
				case "delete file":
					//todo
				case "quit":
					clientConnected = false;
					outToClient.writeBytes("Ending connection\n");
					clientSocket.close();
					System.out.println("Exiting\n\n");
					

				}
			}
		}
	}
}
