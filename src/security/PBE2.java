package security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


public class PBE2 {

	PBEKeySpec pbeKeySpec;
	PBEParameterSpec pbeParamSpec;
	SecretKeyFactory keyFac;
	Cipher pbeCipher;
	SecretKey pbeKey;
	char[] password;

	// Salt
	byte[] salt = {
			(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
			(byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
	};

	// Iteration count
	int count = 20;

	public PBE2(char[] password) {
		this.password = password;
	}
	
	public void makeCipher()throws Exception{
		// Create PBE parameter set
		pbeParamSpec = new PBEParameterSpec(salt, count);


		// convert password into a SecretKey object, using a PBE key
		// factory.
		pbeKeySpec = new PBEKeySpec(password);
		keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		pbeKey = keyFac.generateSecret(pbeKeySpec);
		
		// Create PBE Cipher
		pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
	}
	
		
	
	public void encrypt(File clearFile) throws Exception{
		makeCipher();	
		pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
		
		String path = clearFile.getAbsolutePath()+".encrypted";
		File cipherFile = new File(path);	 
		// if file doesn't exists, then create it
		if (!cipherFile.exists()) {
			cipherFile.createNewFile();
		}

		byte[] buffer = new byte[256];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		
		long counter = 0;
		long fileLength = clearFile.length();
 
		try {
			fis = new FileInputStream(clearFile); 
			bis = new BufferedInputStream(fis);
				
			fos = new FileOutputStream(cipherFile); 
			bos = new BufferedOutputStream(fos);

			boolean endOfFile = false;
			while (!endOfFile) {
				counter += bis.read(buffer);

				// Encrypt the cleartext
				byte[] cipher = pbeCipher.update(buffer);
	
				
				bos.write(cipher);
				bos.flush();
				if (counter >= fileLength){
					endOfFile = true;
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bis.close();
				fos.close();
				bos.close();
				clearFile.delete();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void decrypt(File cipherFile) throws Exception{
		makeCipher();	
		pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
		
		String path = cipherFile.getAbsolutePath();
		path = path.substring(0, path.length()-10);
		File clearFile = new File(path);	 
		// if file doesn't exists, then create it
		if (!clearFile.exists()) {
			clearFile.createNewFile();
		}

		byte[] buffer = new byte[256];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		
		long counter = 0;
		long fileLength = cipherFile.length();
 
		try {
			fis = new FileInputStream(cipherFile); 
			bis = new BufferedInputStream(fis);
				
			fos = new FileOutputStream(clearFile); 
			bos = new BufferedOutputStream(fos);

			boolean endOfFile = false;
			while (!endOfFile) {
				counter += bis.read(buffer);

				// Encrypt the cleartext
				byte[] cipher = pbeCipher.update(buffer);
	
				
				bos.write(cipher);
				bos.flush();
				if (counter >= fileLength){
					endOfFile = true;
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bis.close();
				fos.close();
				bos.close();
				cipherFile.delete();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}

