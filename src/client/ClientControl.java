package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import security.PBE2;
import xmltools.XMLTool;

//test
public class ClientControl {

	private char[] password;

	private String serverAddress;
	private int serverPort;
	private Socket clientSocket;
	private DataOutputStream outToServer;
	private DataInputStream dis;
	private BufferedReader inFromServer;
	
	String syncBoxPath = "client\\syncBox\\";
	String clearPath = "client\\clearBox\\";
	static final String EXTENTION = ".aes";

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


			outToServer.writeBytes("get deleted\n");
			len = dis.readInt();
			data = new byte[len];
			if (len > 0) {
				dis.read(data);
			}
			fileOuputStream = 
					new FileOutputStream("client\\deleted.obj"); 
			fileOuputStream.write(data);
			fileOuputStream.close();


			outToServer.writeBytes("quit\n");
			outToServer.flush();
			String response = inFromServer.readLine();
			System.out.println(response);
			clientSocket.close();

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
					new FileOutputStream("client\\syncbox\\"+ filePath);

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
			outToServer.flush();
			String response = inFromServer.readLine();
			System.out.println(response);
			clientSocket.close();

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public void sendFile(String fileName){
		String filePath = "client\\syncbox\\" + fileName;
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


				outToServer.writeBytes("recieve file\n");	
				String response = inFromServer.readLine();		//wait till server is ready
				System.out.println(response);
				System.out.println("Sending file");
				outToServer.writeBytes(fileName + "\n");		//send path
				RandomAccessFile raf = new RandomAccessFile(filePath, "r");
				byte[] buff = new byte[8192];
				try{
					long length = raf.length();
					outToServer.writeLong(length);
					while ((raf.read(buff, 0, 8192)) != -1){
						outToServer.write(buff);
						outToServer.flush();
					}
				} 
				finally {
					System.out.println(filePath+" sent");
					raf.close();
					outToServer.writeBytes("quit\n");
					outToServer.flush();
					response = inFromServer.readLine();
					System.out.println(response);
					clientSocket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void deleteFileFromServer(String fileName){
		try {
			clientSocket = new Socket(serverAddress, serverPort);
			System.out.println("Connecting...");
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			outToServer.writeBytes("delete file\n");	
			String response = inFromServer.readLine();		//wait till server is ready
			System.out.println(response);
			outToServer.writeBytes(fileName + "\n");		//send path
			response = inFromServer.readLine();				//wait for confirmation
			System.out.println(response);
			outToServer.writeBytes("quit\n");
			outToServer.flush();
			response = inFromServer.readLine();
			System.out.println(response);
			clientSocket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteFileFromServerNoLog(String fileName){
		try {
			clientSocket = new Socket(serverAddress, serverPort);
			System.out.println("Connecting...");
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			outToServer.writeBytes("delete file no log\n");	
			String response = inFromServer.readLine();		//wait till server is ready
			System.out.println(response);
			outToServer.writeBytes(fileName + "\n");		//send path
			response = inFromServer.readLine();				//wait for confirmation
			System.out.println(response);
			outToServer.writeBytes("quit\n");
			outToServer.flush();
			response = inFromServer.readLine();
			System.out.println(response);
			clientSocket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteFileFromClient(String fileName){
		System.out.println("Deleting file "+ fileName + " from Client");
		File f = new File("client\\syncbox\\"+ fileName);
		if(!f.exists()){
			System.out.println("No such file");
		}
		if(!f.delete()){
			System.out.println("Delete failed");
		}
		else{
			System.out.println("Delete successful");
		}
	}

	public void sync(ArrayList<String> commands){
		for (String c: commands){
			String[] s = c.split(" ", 2);
			String command = s[0];
			String fileName = s[1];

			switch (command){
			case "Import":
				getFile(fileName);
				break;

			case "Export":
				sendFile(fileName);
				break;

			case "DeleteClientCopy":
				deleteFileFromClient(fileName);
				break;


			case "DeleteServerCopyNoLog":
				deleteFileFromServerNoLog(fileName);
				break;				
			}
		}
	}

	public ArrayList<String> compareMetadata() throws Exception{
		getServerMetaData();
		XMLTool.generateXML("client\\syncBox");

		File sMetaData = new File("client\\serverMetaData.xml");
		File cMetaData = new File("client\\metadata.xml");

		FileInputStream fis = new FileInputStream("client\\deleted.obj");
		ObjectInputStream ois = new ObjectInputStream(fis);
		ArrayList<String> deleted = (ArrayList<String>) ois.readObject();
		ois.close();

		ArrayList<String> commands = XMLTool.compare(sMetaData, cMetaData, deleted);

		return commands;
	}

	public void encryptSync(){

		File syncBox = new File(syncBoxPath);
		ArrayList<String> cipherFiles = new ArrayList<String>(Arrays.asList(syncBox.list()));;
		File clearBox = new File(clearPath);
		ArrayList<String> clearFiles = new ArrayList<String>(Arrays.asList(clearBox.list()));

		File clear = null;
		File cipher = null;

		for (String fileName: clearFiles){
			clear = new File(clearPath+fileName);
			if (cipherFiles.contains(fileName+EXTENTION)){
				cipher = new File(syncBoxPath+fileName+EXTENTION);
				if (clear.lastModified() >= cipher.lastModified()){
					//encrypt clear and send to syncBox folder
					PBE2 aesAlgo = new PBE2(password);
					cipher.delete();
					aesAlgo.encrypt(clear);	
					System.out.println("re-encrypting file "+ fileName);
				}
				else{
					System.out.println("no changes to "+ fileName +", do nothing.");
				}
				
			}
			else{
				//encrypt clear and send to syncBox folder
				PBE2 aesAlgo = new PBE2(password);
				if (cipher != null && cipher.exists()){
					cipher.delete();
				}				
				aesAlgo.encrypt(clear);					
			}
		}
	}
	
	public void decryptSync(){

		File syncBox = new File(syncBoxPath);
		ArrayList<String> cipherFiles = new ArrayList<String>(Arrays.asList(syncBox.list()));;
		File clearBox = new File(clearPath);
		ArrayList<String> clearFiles = new ArrayList<String>(Arrays.asList(clearBox.list()));

		File clear = null;
		File cipher = null;

		for (String fileName: cipherFiles){
			cipher = new File(syncBoxPath+fileName);
			String clearName = fileName.substring(0, fileName.length()-EXTENTION.length());
			if (clearFiles.contains(clearName)){
				clear = new File(clearPath+clearName);
				if (clear.lastModified() <= cipher.lastModified()){
					//decrypt cipher and send to clearBox folder
					PBE2 aesAlgo = new PBE2(password);
					clear.delete();
					aesAlgo.decrypt(cipher);	
					System.out.println("decrypting file "+ fileName);
				}
				else{
					System.out.println("no changes to "+ fileName +", do nothing.");
				}
				
			}
			else{
				//decrypt cipher and send to clearBox folder
				PBE2 aesAlgo = new PBE2(password);				
				aesAlgo.decrypt(cipher);		
				System.out.println("Decrypting new file "+ fileName);
			}
		}
	}
}
