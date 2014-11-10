package taskManager.view;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

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

	/**
	 * Constructor to create the layout of the window
	 */
	public ManageUsersView() {
		this.setLayout(new FlowLayout());
		JPanel users = new JPanel();
		JPanel tasks = new JPanel();
		JPanel buttons = new JPanel();

		users.setLayout(new BoxLayout(users, BoxLayout.Y_AXIS));
		tasks.setLayout(new BoxLayout(tasks, BoxLayout.Y_AXIS));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
	}

	/**
	 * @return The list of Users that are displayed
	 */
	public ArrayList<UserView> getUsersList() {
		return this.usersList;
	}

	/**
	 * addUser creates a new UserView with the given user
	 * 
	 * @param user
	 *            is the user to add to the user list
	 */
	public void addUser(User user) {
		this.usersList.add(new UserView(user));

	}

	/**
	 * Removes a user from the user list
	 */
	public void removeUser(UserView user) {
		this.usersList.remove(user); // TODO: will this work? Write a test
	}

	public void addController(ActionListener controller) {
		this.controller = controller;
	}

}
