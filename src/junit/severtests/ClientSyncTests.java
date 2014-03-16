package junit.severtests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.Test;

import clientApp.*;
import server.*;

public class ClientSyncTests {
	String fileName = "myFile.txt";
	String fileName1 = "myFile1.txt";
	String ip = "localhost";
	int port = 20661;
	
	public void cleanUp(){
		(new File("server\\metadata.xml")).delete();
		(new File("server\\deleted.obj")).delete();
		(new File("client\\metadata.xml")).delete();
		(new File("client\\ServerMetaData.xml")).delete();
		(new File("client\\deleted.obj")).delete();
		(new File("client\\syncBox\\myFile.txt")).delete();
		(new File("server\\syncBox\\myFile.txt")).delete();
		
		

	}
	
	@Test
	public void tsyncExport1() throws Exception {
		
		//create text file
		String content = "This is the file content";
		File file = new File("client\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.sync(client.compareMetadata());
		cleanUp();
	}
	
	@Test
	public void tsyncImport1() throws Exception {
		
		//create text file
		String content = "This is the file content";
		File file = new File("server\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.sync(client.compareMetadata());
		cleanUp();
	}
@Test
public void tsyncDeleteFromClient() throws Exception {
		
		//create text file
		String content = "This is the file content";
		File file = new File("client\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		File fcopy = new File("server\\syncBox\\"+ fileName);
		file.createNewFile();
		fw = new FileWriter(fcopy.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.deleteFileFromServer("myFile.txt");
		client.sync(client.compareMetadata());
		//cleanUp();
	}

	@Test
	public void tSendFileFromClient() throws Exception {
		
		//create test file
		String content = "This is the file content";
		File file = new File("client\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.sendFile(fileName);		
		File serverCopy = new File("server\\syncBox\\"+fileName); 
				
		assertTrue(serverCopy.exists());
		
		//delete test files 
		if(!serverCopy.delete()){
			System.out.println(serverCopy.getName() + " deletion failed");
		}		
		if(!file.delete()){
			System.out.println(file.getName() + " deletion failed");
		}
	}
	
	@Test
	public void tRecieveFileFromServer() throws Exception {
		
		//create test file
		String content = "This is the file content";
		File file = new File("server\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.getFile(fileName);		
		File clientCopy = new File("client\\syncBox\\"+fileName); 
				
		assertTrue(clientCopy.exists());
		
		//delete test files 
		if(!clientCopy.delete()){
			System.out.println(clientCopy.getName() + " deletion failed");
		}		
		if(!file.delete()){
			System.out.println(file.getName() + " deletion failed");
		}
	}
	@Test
	public void tDeleteFileFromServer() throws Exception {
		
		//create test file
		String content = "This is the file content";
		File file = new File("server\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.deleteFileFromServer(fileName);		
				
		assertTrue(!file.exists());	
		File deletedlog = new File("server\\deleted.obj");
		assertTrue(deletedlog.exists());	

		//delete test files 
		if(!deletedlog.delete()){
			System.out.println(deletedlog.getName() + " deletion from deleted folder failed");
		}		
	}
	
	@Test
	public void tDeleteFileFromServerNoLog() throws Exception {
		
		//create test file
		String content = "This is the file content";
		File file = new File("server\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.deleteFileFromServerNoLog(fileName);		
				
		assertTrue(!file.exists());	
		File deletedlog = new File("server\\deleted.obj");
		assertTrue(!deletedlog.exists());	
		
	}
	
	@Test
	public void tDelete2FileFromServer() throws Exception {
		
		//create test file
		String content = "This is the file content";
		File file = new File("server\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		String content1 = "This is the file content";
		File file1 = new File("server\\syncBox\\"+ fileName1);
		file1.createNewFile();
		FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
		BufferedWriter bw1 = new BufferedWriter(fw1);
		bw1.write(content);
		bw1.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.deleteFileFromServer(fileName);		
				
		assertTrue(!file.exists());	
		File deletedlog = new File("server\\deleted.obj");
		assertTrue(deletedlog.exists());
		
		client.deleteFileFromServer(fileName1);
		
		assertTrue(!file.exists());	

		//delete test files 
		if(!deletedlog.delete()){
			System.out.println(deletedlog.getName() + " deletion from deleted folder failed");
		}		
	}
	
	@Test
	public void tDeleteFileFromClient() throws Exception {
		
		//create test file
		String content = "This is the file content";
		File file = new File("client\\syncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl (ip, port);
		client.deleteFileFromClient(fileName);		
				
		assertTrue(!file.exists());		
	}
	@Test
	public void tgetMetadataNdeleted() throws Exception{
		ClientControl client = new ClientControl (ip, port);
		client.getServerMetaData();
		
		assertTrue(new File("server\\metadata.xml").exists());
		assertTrue(new File("client\\ServerMetadata.xml").exists());	
		assertTrue(new File("client\\deleted.obj").exists());	
		
		new File("server\\metadata.xml").delete();
		new File("client\\ServerMetaData.xml").delete();
		new File("client\\deleted.obj").delete();
	}
}
