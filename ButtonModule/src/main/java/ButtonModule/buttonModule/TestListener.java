/**
 * @author Jon Sorrells
 */

package ButtonModule.buttonModule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Class to show an informational dialog
 *
 */
public class TestListener implements ActionListener {
	String message;
	JTextField textbox;
	
	/**
	 * @param message A string to be displayed in each message
	 * @param textbox A textbox whose value will be included in the message
	 */
	public TestListener(String message, JTextField textbox) {
		this.message = message;
		this.textbox = textbox;
	}

	/**
	 * Displays a message dialog informing the user of the value of the textbox
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// Show a message
		JOptionPane.showMessageDialog(null, 
				message + "\nThe text box contains \"" + textbox.getText() + "\"",
				"dialog box",JOptionPane.INFORMATION_MESSAGE);
	}
}