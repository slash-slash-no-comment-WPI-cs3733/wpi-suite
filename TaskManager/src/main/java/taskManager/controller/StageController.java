/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.view.StageView;
import taskManager.view.TaskView;

/**
 * Controller for stages.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class StageController implements MouseListener, ActionListener {

	private final StageView view;
	private final StageModel model;

	/**
	 * Constructor for the StageController gets all the tasks from the
	 * StageModel, creates the corresponding TaskView and TaskControllers for
	 * each, and final adds all of the TaskViews to the UI.
	 * 
	 * @param view
	 *            the corresponding StageView object
	 * @param model
	 *            the corresponding StageModel object
	 */
	public StageController(StageView view, StageModel model) {
		this.view = view;
		this.model = model;

		// Get all the tasks associated with this Stage.
		final List<TaskModel> tasks = this.model.getTasks();

		// Add the tasks.
		for (TaskModel task : tasks) {
			// create stage view and controller.
			TaskView tkv = new TaskView(task.getName(), task.getDueDate(),
					task.getEstimatedEffort());
			tkv.setController(new TaskController(tkv, task));
			this.view.addTaskView(tkv);
		}

	}

	/**
	 * Add a task to this stage
	 *
	 * @param tc
	 *            task controller for task
	 * @param index
	 *            index at which to add it
	 * @return whether the stage changed as a result
	 */
	public boolean addTask(TaskController tc, int index) {
		return tc.moveToStage(model, index);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// double clicked on the title
		if (e.getClickCount() == 2 && e.getSource() instanceof JLabel) {
			for (Component c : ((JLabel) e.getSource()).getParent().getParent()
					.getComponents()) {
				if (c.getName() == StageView.TITLE) {
					c.setVisible(false);
				} else if (c.getName() == StageView.CHANGE_TITLE) {
					c.setVisible(true);
				}
			}
			JanewayModule.tabPaneC.getTabView().getWorkflowController()
					.setEdittedStageName(view.getName());
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			switch (((JButton) button).getName()) {
			case StageView.CHECK:
				for (Component c : ((JButton) button).getParent()
						.getComponents()) {
					if (c instanceof JTextField
							&& c.getName() == StageView.TEXT_LABEL) {
						view.setStageName(((JTextField) c).getText());
					}
				}
				// fall through
			case StageView.X:
				// JanewayModule.tabPaneC.getTabView().getWorkflowController()
				// .setEdittedStageName("");
				break;
			}
		}

	}
}
