package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import xmltools.XMLTool;

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
				
				switch (clientCommand){
					case "quit":
						clientConnected = false;
						System.out.println("exiting");
					case "get metadata":
						 //generate meta data
						String dir = System.getProperty("user.dir");
						dir = dir + "\\SyncBox";
						XMLTool.generateXML(dir);
						 File transferFile = new File ("metadata.xml");
			              byte [] bytearray  = new byte [(int)transferFile.length()];
			              FileInputStream fin = new FileInputStream(transferFile);
			              BufferedInputStream bin = new BufferedInputStream(fin);
			              bin.read(bytearray,0,bytearray.length);
			              OutputStream os = clientSocket.getOutputStream();
			              //System.out.println("Sending Files...");
			              os.write(bytearray,0,bytearray.length);
			              os.flush();
					case "send file":
						//todo
					case "receive file":
						//todo
					case "delete file":
						//todo
				}
				if (clientCommand != null){
					System.out.println(clientCommand);
				}
			}
		}
	}
}
