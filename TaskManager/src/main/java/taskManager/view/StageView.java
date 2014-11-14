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
		label.setMaximumSize(new Dimension(175, 25));
		JLabel labelText = new JLabel(name);
		labelText.setName(name);
		label.add(labelText);
		this.add(label);

		// creates the scroll containing the stage view and adds it to the block
		stage = new JScrollPane(tasks);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175, 450));
		stage.setSize(new Dimension(175, 450));
		stage.setPreferredSize(new Dimension(175, 450));

		updateTasks();
	}

	/**
	 * repopulates the tasks list into the scroll pane
	 */
	public void updateTasks() {
		this.remove(stage);
		stage = new JScrollPane(tasks);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175, 350));
		this.add(stage);
	}

	/*
	 * @param data for new task view will be entered by the user
	 */
	public void addTaskView(TaskView tkv) {
		tasks.add(tkv);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * Adds the stage controller to this view
	 * 
	 * @param controller
	 */
	public void setController(StageController controller) {
		this.controller = controller;
	}

	/**
	 * Adds the stage controller to this view
	 * 
	 * @return the controller attached to this view
	 */
	public StageController getController() {
		return this.controller;
	}

}
