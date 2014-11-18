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
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import taskManager.controller.TaskController;

/**
 * @author Beth Martino
 * @author Stefan Alexander
 * @version November 9, 2014
 */

public class TaskView extends JPanel implements ITaskView {

	private static final long serialVersionUID = 1L;

	private JButton edit;

	private TaskController controller;

	/**
	 * Constructor, creates a list-like view for the following information: the
	 * name of the task, the due date and the estimated effort
	 * 
	 * @param name
	 *            the name of the task
	 * @param duedate
	 *            the due date of the task
	 * @param estEffort
	 *            the estimated effort for the task
	 * @param taskID
	 *            The ID of the task being displayed
	 */
	public TaskView(String name, Date duedate, int estEffort, String taskID) {
		// organizes the data in a vertical list
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		final TitledBorder title = BorderFactory.createTitledBorder(
				raisedbevel, name);
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);
		this.setMinimumSize(new Dimension(300, 100));

		// convert Date object to Calendar object to avoid using deprecated
		// Date methods.
		final Calendar date = Calendar.getInstance();
		date.setTime(duedate);

		// adds the data to the view
		// note: the Calendar.MONTH value ranges between 0-11 so here we add 1
		// to the month.
		this.add(new JLabel("Name: " + name));
		this.add(new JLabel("Due: " + (date.get(Calendar.MONTH) + 1) + "/"
				+ date.get(Calendar.DATE) + "/" + (date.get(Calendar.YEAR))));
	
		
		
		
		edit = new JButton("Edit");
		edit.setMaximumSize(new Dimension(17, 15)); 
		edit.setName(taskID);
		edit.setFont(new Font("Edit", Font.PLAIN, 4));
		edit.setForeground(Color.RED);
		edit.setBackground(Color.red);
		this.add(edit);

	}

	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * Attaches the task controller to this view
	 * 
	 * @param controller
	 *            the controller to be attached to this view
	 */
	public void setController(TaskController controller) {
		this.controller = controller;
		edit.addActionListener(controller);
	}

}
