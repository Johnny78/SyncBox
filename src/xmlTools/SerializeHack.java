package xmlTools;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import java.io.File;
import java.io.IOException;

// modified from code at 
// http://www.cafeconleche.org/books/xmljava/chapters/ch09s09.html


public class SerializeHack {

    Document doc;
    File out;

    public SerializeHack(Document doc, File out){
	this.doc = doc;
	this.out = out;
    } 

    public void write(){
	try {
	    // Write the document using an identity transform
	    TransformerFactory xformFactory 
		= TransformerFactory.newInstance();
	    Transformer idTransform = xformFactory.newTransformer();
	    Source input = new DOMSource(doc);
	    Result output = new StreamResult(out);
	    idTransform.transform(input, output);
	}
	catch (TransformerFactoryConfigurationError e) { 
	    System.out.println("Could not locate a factory class"); 
	}
	catch (TransformerConfigurationException e) { 
	    System.out.println("This DOM does not support transforms."); 
	}
	catch (TransformerException e) { 
	    System.out.println("Transform failed."); 
	}
   
    }

}