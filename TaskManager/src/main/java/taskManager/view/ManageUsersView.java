package taskManager.view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.util.ArrayList;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import taskManager.view.UserList;

/**
 * This is the view for the ManageUsers pane in the workflow.
 * 
 * @author samee
 *
 */
public class ManageUsersView extends JPanel {

	private static final long serialVersionUID = -4921811814567755329L;
	// TODO: Change ActionListener to ManageUsersController when it exists
	private ActionListener controller;
	private ArrayList<UserView> usersList = new ArrayList<UserView>();
	private JPanel usersBlock;
	private JPanel tasks;
	private JPanel buttons;

	/**
	 * Constructor to create the layout of the window
	 */
	public ManageUsersView() {
		this.setLayout(new FlowLayout());
		usersBlock = new JPanel();
		usersBlock.setLayout(new BoxLayout(usersBlock, BoxLayout.Y_AXIS));

		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(175, 25));
		JLabel usersLabel = new JLabel("Users");

		label.add(usersLabel);
		usersBlock.add(label);

		JScrollPane users = new JScrollPane(new UserList());
		users.setBorder(BorderFactory.createLineBorder(Color.black));
		users.setPreferredSize(new Dimension(175, 350));
		usersBlock.add(users);
		// tasks = new JPanel();
		// buttons = new JPanel();

		// tasks.setLayout(new BoxLayout(tasks, BoxLayout.Y_AXIS));
		// buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		this.add(usersBlock);
		// this.add(tasks);
		// this.add(buttons);

	}

	/**
	 * @return The list of Users that are displayed
	 */
	public ArrayList<UserView> getUsersList() {
		return this.usersList;
	}

	public void addController(ActionListener controller) {
		this.controller = controller;
	}

}
