package junit.securitytests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

import security.PBE2;
import security.PassPrompt;

import org.junit.Ignore;
import org.junit.Test;

public class PBE2tests {
	
	File file;
	char[] password = {
			'p','a','s','s','w','o','r','d'
	};

	
	public void createClientFile() throws IOException{
		String content = "This is the server file content";
		file = new File("client\\syncBox\\file.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
	}
	
	@Test
	public void testEncrypt() throws Exception {
		//File f = new File("client\\syncBox\\t.mkv");
		createClientFile();
		PBE2 passBased = new PBE2(password);	
		passBased.encrypt(file);
	}
	
	@Test
	public void testDecrypt() throws Exception{
		//File f = new File("client\\syncBox\\t.mkv.encrypted");
		file = new File("client\\syncBox\\file.txt.AES");
		PBE2 passBased = new PBE2(password);	
		passBased.decrypt(file);
	}
	
}
