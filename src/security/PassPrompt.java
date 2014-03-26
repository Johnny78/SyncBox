package security;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class PassPrompt extends JPanel implements ActionListener {

	private JFrame parentFrame;
	private JPasswordField passwordField;
	private char[] password;
	private boolean entered = false;
	
	public PassPrompt(){
		JFrame frame = new JFrame("SyncBox password");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PassPromptCons(frame);
	}
	
	public void PassPromptCons(JFrame f){
		parentFrame = f;
		
		passwordField = new JPasswordField(10);
        passwordField.setActionCommand("ok");
        passwordField.addActionListener(this);
 
        JLabel label = new JLabel("Enter the password: ");
        label.setLabelFor(passwordField);
 
        JComponent buttonPane = createButton();
 
        //Lay out everything.
        JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        textPane.add(label);
        textPane.add(passwordField);
 
        add(textPane);
        add(buttonPane);
        
        this.setOpaque(true); //content panes must be opaque
        parentFrame.setContentPane(this);
 
        //Make sure the focus goes to the right component
        //whenever the frame is initially given the focus.
//        parentFrame.addWindowListener(new WindowAdapter() {
//            public void windowActivated(WindowEvent e) {
//                newContentPane.resetFocus();
//            }
//        });
 
        //Display the window.
        parentFrame.pack();
        parentFrame.setVisible(true);
	}
	
	 protected JComponent createButton() {
	        JPanel p = new JPanel(new GridLayout(0,1));
	        JButton okButton = new JButton("OK");
	        okButton.setActionCommand("ok");
	        okButton.addActionListener(this);
	        p.add(okButton);
	        return p;
	    }
	
	public void close(){
		parentFrame.setVisible(false);
		Arrays.fill(password,'0');	//destroy password record
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		 
        if ("ok".equals(cmd)) {
        	password = passwordField.getPassword();
        	entered = true;
        }
	}
	
	
	private char[] getPassword() {
		return password;
	}

	private boolean isEntered() {
		return entered;
	}
	

	public char[] getUserPassword(){
		while(!isEntered()){
			Thread.yield();
			try {
				Thread.sleep(1000);				//wait for user to type password
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return getPassword();

	}
}