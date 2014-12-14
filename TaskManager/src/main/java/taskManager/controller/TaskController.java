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
		if (!thisTaskInfoOut) {
			view.setBackground(Colors.TASK_HOVER);

		}
		if (model.getCategory() == null) {
			view.setCategoryColor(view.getBackground(), false);
		}
		view.setBorderColor(view.getBackground());
		isHovered = true;
	}

	/**
	 * return whether or not the hover effect is on
	 * 
	 * @return true if the hover effect is activated, false if it isn't
	 */
	public boolean isHovered() {
		return isHovered;
	}

	/**
	 * get the color corresponding the models category. If there is no color,
	 * return the TASK or ARCHIVE color
	 * 
	 * @return the color corresponding to the category of the task
	 */
	public Color getCategoryColor() {

		Color catColor = Colors.TASK;

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
		if (thisTaskInfoOut && !isArchived()) {
			if (model.getCategory() == null) {
				view.setBorderColor(Colors.TASK_HOVER);
			} else {
				view.setBorderColor(getCategoryColor());
			}
			view.setBackground(Colors.TASK_HOVER);
		} else if (!isArchived()) {
			view.setBackground(Colors.TASK);
			view.setBorderColor(view.getBackground());
		}
		view.setCategoryColor(getCategoryColor(), true);
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
		if ((model.getCategory() == null) && !isArchived()) {
			view.setBorderColor(Colors.TASK_HOVER);
		} else if (!isArchived()) {
			view.setBorderColor(getCategoryColor());
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
