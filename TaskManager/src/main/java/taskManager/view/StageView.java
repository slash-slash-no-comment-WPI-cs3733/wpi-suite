/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

/*
 * @author Beth Martino
 */

import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import taskManager.controller.StageController;

/**
 * @author Beth Martino
 * @version November 9, 2014
 */
public class StageView extends JPanel implements IStageView {

	private static final long serialVersionUID = 1L;
	private StageController controller;

	JPanel tasks = new JPanel();
	JScrollPane stage = new JScrollPane(tasks);

	public StageView(String name) {

		// stage view is a panel that contains the title and the scroll pane
		// w/tasks
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(200, 450));

		// organizes the tasks in a vertical list
		tasks.setLayout(new BoxLayout(tasks, BoxLayout.Y_AXIS));

		// creates the label for the name of the stage and adds it to the block
		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(175, 25));
		JLabel labelText = new JLabel(name);
		labelText.setName(name);
		label.add(labelText);
		this.add(label);

		// adds example tasks
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");

		// creates the scroll containing the stage view and adds it to the block
		stage = new JScrollPane(tasks);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175, 350));

		updateTasks();
	}

	private void updateTasks() {
		this.remove(stage);
		stage = new JScrollPane(tasks);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175, 350));
		this.add(stage);
	}

	/*
	 * @param data for new task view will be entered by the user
	 */
	public void addTaskView(String name) {
		tasks.add(new TaskView(name, new Date(), 0)); // Not sure that this is
														// right...
		// TODO Make sure that this is right
	}

	/**
	 * Constructor for StageView.
	 */
	public StageView() {
		// organizes the tasks in a vertical list
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	/**
	 * Method addTaskView.
	 * 
	 * @param tkv
	 *            data for new task view will be entered by the user
	 */
	public void addTaskView(TaskView tkv) {
		this.add(tkv);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setController(StageController controller) {
		this.controller = controller;
	}

}
