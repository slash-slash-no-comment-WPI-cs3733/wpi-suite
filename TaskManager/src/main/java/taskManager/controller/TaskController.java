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
import java.awt.event.MouseMotionListener;

import taskManager.JanewayModule;
import taskManager.model.FetchWorkflowObserver;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.view.Colors;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.TaskView;
import taskManager.view.ToolbarView;

/**
 * Controller for Tasks.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class TaskController implements MouseListener, MouseMotionListener {

	private final TaskView view;
	private final TaskModel model;
	private Color background;

	public static Boolean anyTaskInfoOut = false;
	public Boolean thisTaskInfoOut = false;

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

		this.background = view.getBackground();
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
		new EditTaskController(model);
	}

	/**
	 * 
	 * Make the task a darker color. Used for when the mouse is over a task or
	 * when a bubble is out for this task
	 *
	 */
	public void setToHoverColor() {

		view.setBackground(Colors.TASK_HOVER);
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
		// Show task bubble only if there are no stage title textboxes out AND
		// the ignoreAllResponses flag has not been set OR
		// another taskinfo bubble is already out
		if ((!FetchWorkflowObserver.ignoreAllResponses || TaskController.anyTaskInfoOut)
				&& !StageController.anyChangeTitleOut) {
			// Don't reload (so the correct task can be highlighted while the
			// bubble is up
			FetchWorkflowObserver.ignoreAllResponses = true;

			// Create the taskinfo bubble
			Point stageLoc = view.getParent().getParent().getParent()
					.getParent().getLocation();
			Point stagesPanelLoc = view.getParent().getParent().getParent()
					.getParent().getParent().getLocation();
			Point infoLoc = new Point(stagesPanelLoc.x + stageLoc.x,
					view.getLocation().y);
			JanewayModule.tabPaneC.getTabView().getWorkflowController()
					.setTaskInfo(new TaskInfoPreviewView(model, this, infoLoc));

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
		setToHoverColor();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// only reset the background if there is no taskInfo bubble out for this
		// task
		if (!thisTaskInfoOut) {
			resetBackground();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Enable/disable the archive and delete icons when dragged.
		boolean isArchived = model.isArchived();
		if (isArchived) {
			JanewayModule.toolV.setArchiveIcon(ToolbarView.UNARCHIVE);
		} else {
			JanewayModule.toolV.setArchiveIcon(ToolbarView.ARCHIVE);
		}
		JanewayModule.toolV.setArchiveEnabled(true);
		JanewayModule.toolV.setDeleteEnabled(isArchived);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
