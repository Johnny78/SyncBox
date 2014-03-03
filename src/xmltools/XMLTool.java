package xmltools;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

import md5hash.HashMd5;

public class XMLTool {
	
	/**
	 * Compares 2 metadata xml files and returns a list of commands needed to reestablish mirrored folders
	 * 
	 *  needs improving for folders within the root folder
	 *  
	 *  small edit for test
	 * @param recentFile
	 * @param oldFile
	 * @param deletedList
	 * @return list of commands Strings
	 */
	public static ArrayList<String> compare(File recentFile, File oldFile, ArrayList<String> deletedList){
		
		ArrayList<String> commands = new ArrayList<String>();
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document recentDoc = dBuilder.parse(recentFile);
			Document oldDoc = dBuilder.parse(oldFile);
			
			Element rootElementR = recentDoc.getDocumentElement();
			Element rootElementO = oldDoc.getDocumentElement();
			
			NodeList rNodes = rootElementR.getChildNodes();
			NodeList oNodes = rootElementO.getChildNodes();
			
			ArrayList<Element> rElements = new ArrayList<Element>();
			for (int i=0; i < rNodes.getLength(); i++){
				if (rNodes.item(i) instanceof Element){
					rElements.add((Element) rNodes.item(i));
				}
			}
			
			ArrayList<Element> oElements = new ArrayList<Element>();	
			for (int i=0; i < oNodes.getLength(); i++){
				if (oNodes.item(i) instanceof Element){
					oElements.add((Element) oNodes.item(i));
				}
			}
			
			Element oldElem, recentElem;
			String rpath = "/" + rootElementR.getAttribute("name");
			String opath = "/" + rootElementO.getAttribute("name");
			
			while (rElements.size()>0 && oElements.size()>0){
				oldElem = oElements.remove(0);
				recentElem = rElements.remove(0);
			
				//System.out.println("comparing "+recentElem.getAttribute("name")+" with "+oldElem.getAttribute("name"));
				
				if (!oldElem.getAttribute("hash").equals(recentElem.getAttribute("hash"))){
					if(deletedList.contains(oldElem.getAttribute("hash"))){
						rElements.add(0, recentElem);
						commands.add("Delete "+ opath + "/" +oldElem.getAttribute("name"));
					}
					else{
						oElements.add(0, oldElem);
						commands.add("Import "+ rpath + "/" + recentElem.getAttribute("name"));
					}
				}
			}
			while (rElements.size()>0){
				
				recentElem = 
						rElements.remove(0);
				commands.add("Import "+ rpath+ "/" + recentElem.getAttribute("name"));
			}
		}
		catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		
		return commands;
	}

/**
 * generateXML will read the state of a directory
 * and generate an XML file containing file names and hashcodes.
 * the xml file is created in the working directory.
 * @param filepath
 * @throws Exception
 */
	public static void generateXML(String filepath) throws Exception{
		File sBox = new File(filepath);
		if (!sBox.exists()){
			System.out.println("No such file !");
		}
		//Create DOM object
		Document doc = null;
		try {
		       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		       DocumentBuilder builder = factory.newDocumentBuilder();
		       doc = builder.newDocument();
		      }
		      catch (FactoryConfigurationError fce){
			  System.err.println("Could not create DocumentBuilderFactory");
		      }
		      catch (ParserConfigurationException pce) { 
		          System.out.println("Could not locate a JAXP parser"); 
		      }
		//Get file elements
		Element root = doc.createElement("RootDir");
		root.setAttribute("name", "SynchBox");
        doc.appendChild(root);
        root.appendChild( doc.createTextNode("\n") );
        
        //Get list of files
        sBox = new File(filepath);
        File[] li = sBox.listFiles();
        
        //extract names and generate hash
        String path;
        String name;
        String hexHash;
        
        for (int i = 0; i<li.length; i++){      	
        	path = li[i].getAbsolutePath();
        	name = li[i].getName();
        	hexHash = HashMd5.generateHash(path);
        	//System.out.println(path+"\n"+name+"\n"+hexHash);
        	Element el = doc.createElement("file");
        	el.setAttribute("name", name);
        	el.setAttribute("hash", hexHash);
        	root.appendChild(el);
            root.appendChild( doc.createTextNode("\n") );
        	
        }
        
        //Write to file
        File sBoxFolder = new File(filepath);
        File parent = sBoxFolder.getParentFile();
        String parentPath = parent.getPath();
        
        (new SerializeHack(doc, new File(parentPath +"\\metadata.xml") )).write();

	}
}
