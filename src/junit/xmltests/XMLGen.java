package junit.xmltests;


import static org.junit.Assert.*;

import org.junit.Test;

import xmltools.XMLTool;

public class XMLGen {

	@Test
	public void testGenerateXML() throws Exception {	
		XMLTool.generateXML("C:\\Users\\John\\Dropbox\\My Documents\\University\\3rd year\\Final year project\\SyncBox");
		assertTrue(true);	//manually check the output file
	}
}
