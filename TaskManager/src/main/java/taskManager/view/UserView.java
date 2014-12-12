/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * UserView deals with displaying a User and containing its related information
 * 
 * @author samee
 *
 */
public class UserView extends JPanel {

	private static final long serialVersionUID = -159086497774697511L;
	// TODO: Change ActionListener to UserController when it exists
	private ActionListener controller;
	private List<TaskView> tasks;

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
		final Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		final TitledBorder title = BorderFactory.createTitledBorder(raisedbevel, "");
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);
		this.setMinimumSize(new Dimension(300, 100));
		// Displayed in the view
		this.add(new JLabel(user.getName()));
		this.add(new JLabel("Username: " + user.getUsername()));

		// TODO: Add the user's tasks to the tasks list
	}

	/**
	 * addTask adds a TaskView to the task list
	 * 
	 * @param task
	 *            is the TaskView to add to the task list
	 */
	public void addTask(TaskView task) {
		tasks.add(task);
	}

	/**
	 * Removes a task from the task list
	 * 
	 * @param task
	 *            Task to be removed
	 */
	public void removeTask(TaskView task) {
		tasks.remove(task); // TODO: will this work? Write a test
	}

	/**
	 * 
	 * Sets this view's controller.
	 *
	 * @param controller
	 *            the controller associated with this view.
	 */
	public void addController(ActionListener controller) {
		this.controller = controller;
	}
}
