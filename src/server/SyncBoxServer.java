package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import xmltools.XMLTool;

public class SyncBoxServer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3820360252850625238L;
	private int port;
	private boolean clientConnected;
	private String syncBoxDir = "server\\syncbox\\";
	private String metaDataDir = "server\\metadata.xml";
	private String delLogDir = "server\\deleted.obj";


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
				System.out.println(clientCommand);

				switch (clientCommand){

				case "get metadata":
					System.out.println("Generating metadata");
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
					break;

				case "get deleted":

					f = new File (delLogDir);
					if (!f.exists()){
						f.createNewFile();
						System.out.println("generating empty log");
						ArrayList<String> deleted = new ArrayList<String>();													
						FileOutputStream fos = new FileOutputStream("server\\deleted.obj");
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(deleted);
						oos.close();
					}					
					RandomAccessFile raf = new RandomAccessFile(delLogDir, "r");
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
					break;


				case "send file":
					String fileName = inFromClient.readLine();
					fileName = syncBoxDir + fileName;
					f = new File(fileName);
					if (!f.exists()){
						System.out.println("No such File");
					}
					else{
						raf = new RandomAccessFile(fileName, "r");
						byte[] buff = new byte[8192];
						try{
							long length1 = raf.length();
							outToClient.writeLong(length1);
							while ((raf.read(buff, 0, 8192)) != -1){
								outToClient.write(buff);
							}
						} 
						finally {
							raf.close();
							outToClient.flush();
						}
						System.out.println(fileName+" sent");		
					}
					break;



				case "recieve file":
					try{
						outToClient.writeBytes("Server Ready\n");
						fileName = inFromClient.readLine();
						System.out.println("Receiving file "+ fileName);

						DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
						long len = dis.readLong();
						System.out.println("file content is of "+len +"bytes");

						byte[] buff = new byte[8192];
						int result, read = 0;
						FileOutputStream fileOuputStream = 
								new FileOutputStream(syncBoxDir + fileName);
						if (len != 0){
							do {
								result = dis.read(buff);
								read += result;
								fileOuputStream.write(buff);
								//System.out.println("read so far: "+read);
							}
							while (read < len || result == -1);		
						}
						fileOuputStream.close();
						System.out.println(read + " bytes recieved from file "+fileName);
					}
					catch (Exception e){
						e.printStackTrace();
					}
					break;


				case "delete file":
					try{
						outToClient.writeBytes("Server Ready\n");
						fileName = inFromClient.readLine();
						System.out.println(fileName);
						File f1 = new File(syncBoxDir+fileName);

						if (f1.exists()){
							System.out.println("deleting file "+ fileName);
							if(f1.delete()){

								//add delted file name to log								
								ArrayList<String> deleted = null;
								File deletedArray = new File("server\\deleted.obj");
								if (deletedArray.exists()){
									FileInputStream fis = new FileInputStream("server\\deleted.obj");
									ObjectInputStream ois = new ObjectInputStream(fis);
									deleted = (ArrayList<String>) ois.readObject();
									ois.close();
								}
								else{
									deleted = new ArrayList<String>();
								}							
								deleted.add(fileName);
								FileOutputStream fos = new FileOutputStream("server\\deleted.obj");
								ObjectOutputStream oos = new ObjectOutputStream(fos);
								oos.writeObject(deleted);
								oos.close();								

								outToClient.writeBytes("Delete Successful\n");
							}else{
								System.out.println("Delete failed");
								outToClient.writeBytes("Delete or move failed\n");
							}
						}
						else{
							System.out.println("no such file available for delete");
						}
					}
					catch (Exception e){
						e.printStackTrace();
					}
					break;


				case "delete file no log":
					try{
						outToClient.writeBytes("Server Ready\n");
						fileName = inFromClient.readLine();
						System.out.println(fileName);
						File f1 = new File(syncBoxDir+fileName);

						if (f1.exists()){
							System.out.println("deleting file "+ fileName);
							if(f1.delete()){
								outToClient.writeBytes("Delete Successful\n");
							}else{
								System.out.println("Delete failed");
								outToClient.writeBytes("Delete or move failed\n");
							}
						}
						else{
							System.out.println("no such file available for delete");
						}
					}
					catch (Exception e){
						e.printStackTrace();
					}
					break;


				case "quit":
					clientConnected = false;
					outToClient.writeBytes("Ending connection\n");
					clientSocket.close();
					System.out.println("Exiting\n\n");
					break;
				}
			}
		}
	}
}
