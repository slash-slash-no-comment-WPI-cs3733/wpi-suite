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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import taskManager.controller.TaskController;
import taskManager.draganddrop.TaskPanel;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;

/**
 * @author Beth Martino
 * @author Stefan Alexander
 * @author Thane Hunt
 * @version November 18, 2014
 */

public class TaskView extends TaskPanel {

	private static final long serialVersionUID = 1L;

	private TaskController controller;
	private ContextMenu contextMenu;
	private JMenu moveTo;
	private final JMenuItem addTask = new JMenuItem("Add Task");
	private final JMenuItem editTask = new JMenuItem("Edit Task");
	private final JMenuItem delete = new JMenuItem("Delete Task");

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
	public TaskView(String name, Date duedate, int estEffort) {
		// organizes the data in a vertical list
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		final TitledBorder title = BorderFactory
				.createTitledBorder(raisedbevel);
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);
		this.setMinimumSize(new Dimension(200, 100));

		// Creates the ContextMenu.
		contextMenu = new ContextMenu(this);
		contextMenu.add(addTask);
		contextMenu.add(editTask);
		// Creates sub Menu that contains the names of all of the other stages.
		moveTo = new JMenu("Move To");
		moveTo.add(new JMenuItem("One"));
		moveTo.add(new JMenuItem("Two"));
		moveTo.add(new JMenuItem("Three"));
		moveTo.add(new JMenuItem("Four"));
		contextMenu.add(moveTo);
		// Finished with sub menu
		contextMenu.add(delete);
		//finished with context menu
		
		//
		// convert Date object to Calendar object to avoid using deprecated
		// Date methods.
		final Calendar date = Calendar.getInstance();
		date.setTime(duedate);

		// adds the data to the view
		// note: the Calendar.MONTH value ranges between 0-11 so here we add 1
		// to the month.

		JLabel nameLabel = new JLabel();
		JLabel dueLabel = new JLabel("Due: " + (date.get(Calendar.MONTH) + 1)
				+ "/" + date.get(Calendar.DATE) + "/"
				+ (date.get(Calendar.YEAR)));

		// This creates a maximum text-string length before the name gets
		// truncated in the view

		nameLabel.setText("Average Name Length");
		final Dimension size = nameLabel.getPreferredSize();

		nameLabel.setMaximumSize(size);
		nameLabel.setPreferredSize(size);
		nameLabel.setText(name);

		this.add(nameLabel);
		this.add(dueLabel);

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
		this.addMouseListener(this.controller);
		this.addTask.addActionListener(controller);
		this.delete.addActionListener(controller);
		this.editTask.addActionListener(controller);

	}

	public TaskController getController() {
		return controller;
	}

	@Override
	public void setVisible(boolean visible) {
		controller.resetBackground();
		super.setVisible(visible);
	}

}
