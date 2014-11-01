package ButtonModule.buttonModule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * 
 * Class to show an informational dialog
 * Takes a static message and a textbox
 *
 */
public class TestListener implements ActionListener {
	String message;
	JTextField textbox;
	
	public TestListener(String message, JTextField textbox) {
		this.message = message;
		this.textbox = textbox;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// Show a message
		JOptionPane.showMessageDialog(null, 
				message + "\nThe text box contains \"" + textbox.getText() + "\"",
				"dialog box",JOptionPane.INFORMATION_MESSAGE);
	}
}