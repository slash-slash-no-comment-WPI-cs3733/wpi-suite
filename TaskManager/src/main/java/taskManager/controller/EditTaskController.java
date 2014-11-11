/**
 * 
 */
package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * @author Beth Martino
 *
 */
public class EditTaskController implements ActionListener {

	// TODO: change from JPanel to edit task view
	private JPanel etv;

	/**
	 * Constructor, attaches the edit task view to this controller
	 * 
	 * @param etv
	 */
	public EditTaskController(JPanel etv) {
		this.etv = etv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// fetch fields here
	}

}
