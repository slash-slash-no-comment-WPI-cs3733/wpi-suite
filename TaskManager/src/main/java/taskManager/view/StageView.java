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
import taskManager.prototypeDnD.StagePanel;

/**
 * @author Beth Martino
 * @version November 9, 2014
 */
public class StageView extends JPanel {

	private static final long serialVersionUID = 1L;
	private StageController controller;

	StagePanel tasks = new StagePanel();
	JScrollPane stage = new JScrollPane(tasks,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	/**
	 * 
	 * Creates a new stage interface with a name with all the pretty UI elements
	 *
	 * @param name
	 *            The title of the stage being drawn
	 */
	public StageView(String name) {

		// stage view is a panel that contains the title and the scroll pane
		// w/tasks
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// this.setPreferredSize(new Dimension(250, 450));

		// organizes the tasks in a vertical list
		tasks.setLayout(new BoxLayout(tasks, BoxLayout.Y_AXIS));
		// tasks.setLayout(new FlowLayout());
		// tasks.setMinimumSize(new Dimension(175, 450));
		// tasks.setSize(new Dimension(175, 450));
		// tasks.setPreferredSize(new Dimension(175, 450));
		// tasks.setMaximumSize(new Dimension(175, 450));

		// creates the label for the name of the stage and adds it to the block
		JPanel label = new JPanel();
		label.setMaximumSize(new Dimension(175, 25));
		JLabel labelText = new JLabel(name);
		labelText.setName(name);
		label.add(labelText);
		this.add(label);

		// creates the scroll containing the stage view and adds it to the block
		stage = new JScrollPane(tasks,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
		stage = new JScrollPane(tasks,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175, 350));
		this.add(stage);
	}

	/**
	 * @param tkv
	 *            for new task view will be entered by the user
	 */
	public void addTaskView(TaskView tkv) {
		tkv.setAlignmentX(CENTER_ALIGNMENT);
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
		tasks.setModel(controller.getModel());
	}

	/**
	 * Adds the stage controller to this view
	 * 
	 * @return the controller attached to this view
	 */
	public StageController getController() {
		return controller;
	}

}
