package xmltools;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

import security.HashMd5;

public class XMLTool {
	
	/**
	 * Compares 2 metadata xml files and returns a list of commands needed to establish mirrored folders
	 * 
	 *  needs improving for folders within the root folder
	 *  
	 * @param serverXML
	 * @param clientXML
	 * @param deletedList
	 * @return list of commands Strings
	 */
	public static ArrayList<String> compare(File serverXML, File clientXML, ArrayList<String> deletedList){
		
		ArrayList<String> commands = new ArrayList<String>();
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document recentDoc = dBuilder.parse(serverXML);
			Document oldDoc = dBuilder.parse(clientXML);
			
			Element rootElementS = recentDoc.getDocumentElement();
			Element rootElementC = oldDoc.getDocumentElement();
			
			NodeList sNodes = rootElementS.getChildNodes();
			NodeList cNodes = rootElementC.getChildNodes();
			
			// create list of server files
			ArrayList<Element> sElements = new ArrayList<Element>();
			for (int i=0; i < sNodes.getLength(); i++){
				if (sNodes.item(i) instanceof Element){
					sElements.add((Element) sNodes.item(i));
				}
			}
			//create list of client files
			ArrayList<Element> cElements = new ArrayList<Element>();	
			for (int i=0; i < cNodes.getLength(); i++){
				if (cNodes.item(i) instanceof Element){
					cElements.add((Element) cNodes.item(i));
				}
			}
			
			Element serverElem, clientElem;
			
			boolean comparing = true;
			
			while(comparing){
				if(cElements.size()>0 && sElements.size()>0){
					clientElem = cElements.remove(0);
					String name = clientElem.getAttribute("name");
					String hash = clientElem.getAttribute("hash");
					long lastmod = Long.parseLong(clientElem.getAttribute("lastModified"));
					boolean match = false;
					int counter = 0;								//search for a match in server list
					while (!match && counter < sElements.size()){
						serverElem = sElements.get(counter);
						counter++;
//						System.out.println(name +" vs "+ serverElem.getAttribute("name"));
						if (serverElem.getAttribute("name").equals(name)){
							if (serverElem.getAttribute("hash").equals(hash)){			
								match = true;						//identical file server & client
								sElements.remove(counter-1);
							}
							else{
								long slastmod = Long.parseLong(serverElem.getAttribute("lastModified")); 
								if(lastmod > slastmod){ 		//if client version is more recent
									commands.add("DeleteServerCopyNoLog "+name);
									commands.add("Export "+ name);
									match = true;
									sElements.remove(counter-1);
								}
								else{
									commands.add("DeleteClientCopy "+ name);
									commands.add("Import "+ name);
									match = true;
									sElements.remove(counter-1);
								}
							}				
						}
						//todo improve deletion detection to check hash and date
						else if (deletedList.contains(name)){
							commands.add("DeleteClientCopy "+ name);
							match = true;
						}
						else{
							commands.add("Export "+ name);
							match = true;
						}
					}
				}
				if(cElements.size() == 0 && sElements.size() == 0){
					comparing = false;								// Finished
				}
				else if (cElements.size() > 0){						// elements remaining in client list
					clientElem = cElements.remove(0);
					String name = clientElem.getAttribute("name");
					if (deletedList.contains(name)){
						commands.add("DeleteClientCopy "+ name);
					}
					else{
						commands.add("Export "+name);
					}
				}
				else{												//elements remaining in server list
					serverElem = sElements.remove(0);
					String name = serverElem.getAttribute("name");
					commands.add("Import "+name);
					
				}
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
		root.setAttribute("name", "syncBox");
        doc.appendChild(root);
        root.appendChild( doc.createTextNode("\n") );
        
        //Get list of files
        sBox = new File(filepath);
        File[] li = sBox.listFiles();
        
        //extract names and generate hash
        String path;
        String name;
        String hexHash;
        long lastmod;
        
        for (int i = 0; i<li.length; i++){      	
        	path = li[i].getAbsolutePath();
        	name = li[i].getName();
        	lastmod = li[i].lastModified();
        	hexHash = HashMd5.generateHash(path);
        	//System.out.println(path+"\n"+name+"\n"+hexHash);
        	Element el = doc.createElement("file");
        	el.setAttribute("name", name);
        	el.setAttribute("hash", hexHash);
        	el.setAttribute("lastModified", Long.toString(lastmod));
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
