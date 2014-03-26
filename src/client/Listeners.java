package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Listeners implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//get xml files from server
		File serverXML = new File("/C:/Users/John/workspace/syncBox/src/clientApp/xml examples/server.xml");
		File clientXML = new File ("/C:/Users/John/workspace/syncBox/src/clientApp/xml examples/client.xml");
		
		//get xml files from local disk
		
		
		//compare files for updates
		
	}

}
