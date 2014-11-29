/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView.Mode;
import taskManager.view.TaskView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Controller for Tasks.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class TaskController implements MouseListener {

	private final TaskView view;
	private final TaskModel model;
	private final WorkflowModel wfm;
	private TabPaneController tabPaneC;
	private EditTaskController etc;
	private final User[] projectUsers = JanewayModule.users;
	private Color background;

	/**
	 * Constructor for the TaskController, currently just sets the corresponding
	 * view and model parameters.
	 *
	 * @param view
	 *            the corresponding TaskView object
	 * @param model
	 *            the corresponding TaskModel object
	 */
	public TaskController(TaskView view, TaskModel model) {
		this.tabPaneC = JanewayModule.tabPaneC;
		this.view = view;
		this.model = model;

		wfm = WorkflowModel.getInstance();
		etc = new EditTaskController(Mode.EDIT, model);
	}

	/**
	 * Move this task to given stage
	 *
	 * @param destination
	 *            target StageModel
	 * @param index
	 * @return whether the stage changed as a result
	 */
	public boolean moveToStage(StageModel destination, int index) {
		return destination.addTask(model, index);
	}

	/**
	 * Delete this task
	 *
	 */
	public void deleteTask() {
		model.getStage().removeTask(model);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		etc.initializeView(tabPaneC);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		background = view.getBackground();
		view.setBackground(Color.lightGray);
		view.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		resetBackground();
	}

	public void resetBackground() {
		if (background != null) {
			view.setBackground(background);
		}
	}

}
