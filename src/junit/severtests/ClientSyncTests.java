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

	@Test
	public void tSendFileFromClient() throws Exception {
		
		//create test file
		String content = "This is the file content";
		File file = new File("client\\SyncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl ("192.168.1.141", 20661);
		client.sendFile(fileName);		
		File serverCopy = new File("server\\SyncBox\\"+fileName); 
				
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
		File file = new File("server\\SyncBox\\"+ fileName);
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ClientControl client = new ClientControl ("192.168.1.141", 20661);
		client.getFile(fileName);		
		File clientCopy = new File("client\\SyncBox\\"+fileName); 
				
		assertTrue(clientCopy.exists());
		
		//delete test files 
		if(!clientCopy.delete()){
			System.out.println(clientCopy.getName() + " deletion failed");
		}		
		if(!file.delete()){
			System.out.println(file.getName() + " deletion failed");
		}
	}
}
