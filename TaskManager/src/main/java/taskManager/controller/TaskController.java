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
import java.util.Date;

import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.TaskModel.TaskCategory;
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

	public static Boolean anyTaskInfoOut = false;
	private Boolean thisTaskInfoOut = false;

	private boolean isHovered;

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

		view.setBackground(Colors.TASK);

		if (model.getDueDate().before(new Date())) {
			view.setDateColor(Color.RED);
		} else {
			view.setDateColor(Color.BLACK);
		}

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

	/**
	 * Get the ID of the task
	 *
	 * @return the task ID
	 */
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
	 * returns whether or not the task is being hovered over
	 * 
	 * @return true if the task is being hovered over, false if it is not
	 */
	public boolean isHovered() {
		return this.isHovered;
	}

	/**
	 * 
	 * Make the task a darker color. Used for when the mouse is over a task or
	 * when a bubble is out for this task
	 *
	 */
	public void changeToHoverColor() {
		// don't highlight while task info is out in fun mode, because the clip
		// bounds passed to the rotation view are sometimes not correct
		if (ToolbarController.getInstance().getView().isFunMode()
				&& anyTaskInfoOut) {
			return;
		}
		if (!thisTaskInfoOut) {
			view.setBackground(Colors.TASK_HOVER);
			view.setBorderColor(view.getBackground(), false);
		}
		isHovered = true;
	}

	/**
	 * get the color corresponding the models category. If there is no color,
	 * return the TASK or ARCHIVE color
	 * 
	 * @return the color corresponding to the category of the task
	 */
	public Color getCategoryColor() {
		// if the task has no category, give the color for clicking non colored
		// tasks
		Color catColor = Colors.TASK_CLICKED;
		// otherwise, grab the color of the category
		for (int i = 0; i < TaskCategory.values().length; i++) {
			if (TaskCategory.values()[i].equals(model.getCategory())) {
				catColor = Colors.CAT_COLORS[i];
			}
		}
		return catColor;
	}

	/**
	 * 
	 * Resets the task background to its original color
	 *
	 */
	public void resetBackground() {
		if (!thisTaskInfoOut) {
			if (!isArchived()) {
				view.setBackground(Colors.TASK);
				view.setBorderColor(Colors.TASK, false);
			} else {
				view.setBorderColor(Colors.TASK_HOVER, false);
			}
		}
		view.repaint();
		isHovered = false;
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
				new TaskInfoPreviewView(model, this, infoLoc,
						getCategoryColor()));

		// Set the correct flags
		thisTaskInfoOut = true;
		TaskController.anyTaskInfoOut = true;

		// set the correct background color
		if (!isArchived()) {
			if ((model.getCategory() != null)) {
				view.setBackground(Colors.TASK);
			} else {
				view.setBackground(Colors.TASK_CLICKED);
			}
		}
		// set the appropriate border and category colors
		view.setCategoryColor(getCategoryColor(), true);
		view.setBorderColor(getCategoryColor(), true);

		isHovered = true;
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
		resetBackground();
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

	/**
	 * 
	 * If the taskInfo bubble for this task was removed from view. Resets the
	 * flag to correctly color the task.
	 *
	 */
	public void taskInfoRemoved() {
		thisTaskInfoOut = false;
		resetBackground();
	}
}
