package junit.xmltests;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import org.junit.Test;

import xmlTools.XMLTool;

public class XmlCompareTests {

	@Test
	public void test() {
		File serverXML = new File("/C:/Users/John/workspace/SyncBox/src/junit/Test XML docs/emptyServer.xml");
		File clientXML = new File ("/C:/Users/John/workspace/SyncBox/src/junit/Test XML docs/client.xml");
		ArrayList<String> deletedList = new ArrayList<String>();
		ArrayList<String> commands = new ArrayList<String>();
		deletedList.add("1234");
		
		commands = XMLTool.compare(serverXML, clientXML, deletedList);
		for (String s: commands){
			assertTrue(s.equals("Delete /SyncBox/file2.txt"));
		}
	}
}
