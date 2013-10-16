package xmlTools;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

public class XMLTool {
	
	/**
	 * Compares 2 metadata xml files and returns a list of commands needed to reestablish mirrored folders
	 * 
	 *  needs improving for folders within the root folder
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

	
	public static File generateXML(String filepath){
		return new File("c:/t.txt");
	}
}
