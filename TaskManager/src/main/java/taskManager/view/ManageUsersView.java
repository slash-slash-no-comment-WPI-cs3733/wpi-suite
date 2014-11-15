/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	private List<UserView> usersList = new ArrayList<UserView>();
	private JPanel window;
	private JLabel title;
	// JPanel's on the window
	private JPanel usersBlock;
	private JPanel tasksBlock;
	private JPanel buttons;

	/**
	 * Constructor to create the layout of the window
	 */
	public ManageUsersView() {
		// The title and window
		// TODO: Center the Title (properly) and add padding below it
		title = new JLabel("Manage Users");
		title.setFont(new Font("Serif", Font.PLAIN, 15));
		window = new JPanel();
		window.setLayout(new FlowLayout());
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Users panel
		usersBlock = new JPanel();
		usersBlock.setLayout(new BoxLayout(usersBlock, BoxLayout.Y_AXIS));
		// Label for the users panel
		JPanel uLabel = new JPanel();
		uLabel.setPreferredSize(new Dimension(175, 25));
		JLabel usersLabel = new JLabel("Users");
		// Add label to panel
		uLabel.add(usersLabel);
		usersBlock.add(uLabel);
		// Create the scrollPane with a UserList inside it
		JScrollPane users = new JScrollPane(new UserList());
		users.setBorder(BorderFactory.createLineBorder(Color.black));
		users.setPreferredSize(new Dimension(200, 350));
		usersBlock.add(users);

		// Button panel
		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		JButton addUser = new JButton("Add User");
		JButton removeUser = new JButton("Remove User");
		JButton done = new JButton("Done");
		// TODO: add spacing between the buttons
		buttons.add(addUser);
		buttons.add(removeUser);
		buttons.add(done);

		// Task panel
		tasksBlock = new JPanel();
		tasksBlock.setLayout(new BoxLayout(tasksBlock, BoxLayout.Y_AXIS));
		// Label for the task panel
		JPanel tLabel = new JPanel();
		tLabel.setPreferredSize(new Dimension(175, 25));
		JLabel tasksLabel = new JLabel("Associated Tasks");
		// Add label to panel
		tLabel.add(tasksLabel);
		tasksBlock.add(tLabel);
		// Create the scrollPane with a StageView inside it
		// TODO: change StageView to a TaskListView. Do this
		JScrollPane tasks = new JScrollPane(new StageView("Tasks"));
		tasks.setBorder(BorderFactory.createLineBorder(Color.black));
		tasks.setPreferredSize(new Dimension(200, 350));
		tasksBlock.add(tasks);

		// Add panels to the window
		window.add(usersBlock);
		window.add(buttons);
		window.add(tasksBlock);

		// Add title and window to this
		this.add(title);
		this.add(window);
	}

	/**
	 * 
	 * Sets this view's Controller. This method should probably be named
	 * setController.
	 *
	 * @param controller
	 *            The new controller (that implements ActionListener)
	 */
	public void addController(ActionListener controller) {
		this.controller = controller;
	}

	/*
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible && controller != null) {
			// TODO: uncomment once controller exists
			// controller.reloadData();
		}
		super.setVisible(visible);
	}

}
