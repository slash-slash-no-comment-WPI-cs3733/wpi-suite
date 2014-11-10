package taskManager.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;

import taskManager.view.TaskView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * UserView deals with displaying a User and containing its related information
 * 
 * @author samee
 *
 */
public class UserView extends JPanel {

	private static final long serialVersionUID = -159086497774697511L;
	private ActionListener controller;
	private ArrayList<TaskView> tasks;

	/**
	 * Creates a panel with a user's name and adds all tasks associated with
	 * that user
	 * 
	 * @param user
	 *            The user being dealt with
	 */
	public UserView(User user) {
		// organizes the data in a vertical list
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(raisedbevel,
				user.getName());
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);
		this.setMinimumSize(new Dimension(300, 100));

		// TODO: Add the user's tasks to the tasks list
	}

	/**
	 * addTask adds a TaskView to the task list
	 * 
	 * @param task
	 *            is the TaskView to add to the task list
	 */
	public void addTask(TaskView task) {
		this.tasks.add(task);
	}

	/**
	 * Removes a task from the task list
	 */
	public void removeTask(TaskView task) {
		this.tasks.remove(task); // TODO: will this work? Write a test
	}

	public void addController(ActionListener controller) {
		this.controller = controller;
	}
}
