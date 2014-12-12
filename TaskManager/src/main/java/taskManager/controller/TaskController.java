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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.view.Colors;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.TaskView;

/**
 * Controller for Tasks.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class TaskController implements MouseListener {

	private final TaskView view;
	private final TaskModel model;
	private final Color background;

	public static Boolean anyTaskInfoOut = false;
	private Boolean thisTaskInfoOut = false;

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
		this.view = view;
		this.model = model;

		// Set the background to orange if the task is archived.
		if (model.isArchived()) {
			view.setBackground(Colors.ARCHIVE);
		} else {
			view.setBackground(Colors.TASK);
		}

		background = view.getBackground();
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

	/**
	 * Returns whether or not the task is archived
	 *
	 * @return the boolean.
	 */
	public boolean isArchived() {
		return model.isArchived();
	}

	/**
	 * 
	 * Sets task's archived property to given boolean.
	 *
	 * @param bool
	 *            The boolean to set the task's isArchived field.
	 */
	public void setArchived(boolean bool) {
		model.setArchived(bool);
	}

	public String getID() {
		return model.getID();
	}

	/**
	 * 
	 * Populates the EditTaskView with the information from this task.
	 *
	 */
	public void editTask() {
		new EditTaskController(model).getView().focusOnTitleField();
	}

	/**
	 * 
	 * Make the task a darker color. Used for when the mouse is over a task or
	 * when a bubble is out for this task
	 *
	 */
	public void changeToHoverColor() {
		if (!thisTaskInfoOut
				&& !(ToolbarController.getInstance().getView().isFunMode() && anyTaskInfoOut)) {
			view.setBackground(Colors.TASK_HOVER);
		}
	}

	/**
	 * 
	 * Resets the task background to its original color
	 *
	 */
	public void resetBackground() {
		if (background != null) {
			view.setBackground(background);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		// Create the taskinfo bubble
		final Point stageLoc = view.getParent().getParent().getParent()
				.getParent().getLocation();
		final Point stagesPanelLoc = view.getParent().getParent().getParent()
				.getParent().getParent().getLocation();
		final Point infoLoc = new Point(stagesPanelLoc.x + stageLoc.x,
				view.getLocation().y);
		WorkflowController.getInstance().setTaskInfo(
				new TaskInfoPreviewView(model, this, infoLoc));

		// Set the correct flags
		thisTaskInfoOut = true;
		TaskController.anyTaskInfoOut = true;
		// make the associated task a darker color while the bubble is out
		if (isArchived()) {
			view.setBackground(Colors.ARCHIVE_CLICKED);
		} else {
			view.setBackground(Colors.TASK_CLICKED);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// do nothing

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		changeToHoverColor();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// only reset the background if there is no taskInfo bubble out for this
		// task
		if (!thisTaskInfoOut) {
			resetBackground();
		}
	}

	/**
	 * @return the thisTaskInfoOut
	 */
	public Boolean getThisTaskInfoOut() {
		return thisTaskInfoOut;
	}

	/**
	 * @param thisTaskInfoOut
	 *            the thisTaskInfoOut to set
	 */
	public void setThisTaskInfoOut(Boolean thisTaskInfoOut) {
		this.thisTaskInfoOut = thisTaskInfoOut;
	}
}
