package client;

import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class SyncGUI extends JFrame{
	
	public SyncGUI(){
		super("SyncBox");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container pane = this.getContentPane();
		pane.setLayout(new FlowLayout());
		JButton button = new JButton("Sync");
		button.addActionListener(new Listeners());
		pane.add(button);
	
		this.pack();
	}
	public static void main(String[] args){
		JFrame window = new SyncGUI();
		window.setVisible(true);
	}

}
