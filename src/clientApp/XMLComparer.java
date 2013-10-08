package clientApp;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

public class XMLComparer {
	
	public static ArrayList<String> compare(File serverFile, File localFile){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document Serverdoc = dBuilder.parse(serverFile);
			Document localdoc = dBuilder.parse(localFile);
			
			NodeList sNodeList = Serverdoc.getElementsByTagName("dir");
			NodeList lNodeList = Serverdoc.getElementsByTagName("dir");
			
			for(int i=0; i<sNodeList.getLength();i++){
				Node localNode = null;
				try{
					localNode = lNodeList.item(i);
				}					
				catch (NullPointerException e){
					System.out.println("missing node");
				}
				if (sNodeList.item(i).isEqualNode(localNode)){
					System.out.println("node identycal");
					
				}
				else{
					System.out.println("diff nodes");
				}
			}
			
			
		}
		catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}

		
		
		return new ArrayList<String>();
	}

}
