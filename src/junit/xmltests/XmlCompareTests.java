package junit.xmltests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

import clientApp.ClientControl;
import xmltools.*;

public class XmlCompareTests {
	
	String serverPath = "server\\syncBox";
	String clientPath = "client\\syncBox";
	String clientmeta = "client\\metadata.xml";
	String servermeta = "server\\metadata.xml";
	String content = "This is the file content";
	
	@Test
	public void tXMLGenerate() throws Exception{

		XMLTool.generateXML(serverPath);
		assertTrue(new File(serverPath).exists());
	}
	
	@Test
	public void tXMLCompareEmpty() throws Exception{
		ArrayList<String> delList = new ArrayList<String>();		
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);				
		assertTrue(commands.size() == 0);
		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}
	
	@Test
	public void tXMLCompareImport1() throws Exception{
		
		File file = new File("server\\syncBox\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ArrayList<String> delList = new ArrayList<String>();		
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);
		assertTrue(commands.get(0).equals("Import file.txt"));
		file.delete();
		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}

	@Test
	public void tXMLCompareExport1() throws Exception{
		
		File file = new File("client\\syncBox\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ArrayList<String> delList = new ArrayList<String>();		
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);
		assertTrue(commands.get(0).equals("Export file.txt"));
		file.delete();
		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}
	
	@Test
	public void tXMLCompareIdentical() throws Exception{
		
		
		File file = new File(clientPath +"\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		File fcopy = new File(serverPath+"\\file.txt");
		file.createNewFile();
		fw = new FileWriter(fcopy.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		ArrayList<String> delList = new ArrayList<String>();		
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);
		assertTrue(commands.size() == 0);
		file.delete();
		fcopy.delete();
		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}
	
	@Test
	public void tXMLCompareSameNameNewContentServer() throws Exception{
		
		
		File file = new File(clientPath +"\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		File fcopy = new File(serverPath+"\\file.txt");
		file.createNewFile();
		fw = new FileWriter(fcopy.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(content+"I'm some extra content!");
		bw.close();
		
		ArrayList<String> delList = new ArrayList<String>();		
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);
		assertTrue(commands.get(0).equals("DeleteClientCopy file.txt"));
		assertTrue(commands.get(1).equals("Import file.txt"));
		file.delete();
		fcopy.delete();
		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}
	
	@Test
	public void tXMLCompareSameNameNewContentclient() throws Exception{
		
		
		File file = new File(serverPath +"\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		File fcopy = new File(clientPath+"\\file.txt");
		file.createNewFile();
		fw = new FileWriter(fcopy.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(content+"I'm some extra content!");
		bw.close();
		
		ArrayList<String> delList = new ArrayList<String>();		
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);
//		for (int i = 0; i<commands.size(); i++){
//			System.out.println(commands.get(i));
//		}
		assertTrue(commands.get(0).equals("DeleteServerCopyNoLog file.txt"));
		assertTrue(commands.get(1).equals("Export file.txt"));
		file.delete();
		fcopy.delete();
		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}
	
	
	@Test
	public void tXMLCompareExport1AndImport1() throws Exception{
		
		
		File file = new File(serverPath +"\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		File fcopy = new File(clientPath+"\\file1.txt");
		file.createNewFile();
		fw = new FileWriter(fcopy.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(content+"I'm some extra content!");
		bw.close();
		
		ArrayList<String> delList = new ArrayList<String>();		
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);
		assertTrue(commands.get(0).equals("Export file1.txt"));
		assertTrue(commands.get(1).equals("Import file.txt"));
		file.delete();
		fcopy.delete();
		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}
	
	@Test
	public void tXMLCompareDeletedFromServer() throws Exception{
		
		
		File file = new File(clientPath +"\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

		
		ArrayList<String> delList = new ArrayList<String>();
		delList.add("file.txt");
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);
		assertTrue(commands.get(0).equals("DeleteClientCopy file.txt"));

		file.delete();

		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}
	
	
	@Test
	public void tXMLCompareDeletedFromServerAndImport1() throws Exception{
	
		
		File file = new File(clientPath +"\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		File fcopy = new File(serverPath+"\\file1.txt");
		file.createNewFile();
		fw = new FileWriter(fcopy.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(content+"I'm some extra content!");
		bw.close();

		
		ArrayList<String> delList = new ArrayList<String>();
		delList.add("file.txt");
		XMLTool.generateXML(serverPath);
		XMLTool.generateXML(clientPath);	
		ArrayList<String> commands = 
				XMLTool.compare(new File(servermeta), new File(clientmeta), delList);
		assertTrue(commands.get(0).equals("DeleteClientCopy file.txt"));
		assertTrue(commands.get(1).equals("Import file1.txt"));

		file.delete();
		fcopy.delete();

		(new File(servermeta)).delete();
		(new File(clientmeta)).delete();
	}
}
