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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;

import taskManager.JanewayModule;
import taskManager.model.ActivityModel;
import taskManager.model.FetchWorkflowObserver;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.Colors;
import taskManager.view.EditTaskView;
import taskManager.view.EditTaskView.Mode;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.TaskView;
import taskManager.view.ToolbarView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * Controller for Tasks.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class TaskController implements MouseListener, MouseMotionListener,
		ActionListener {

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
		view.setVisible(false);
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

	/**
	 * Called when the user clicks the view with the left mouse button.
	 */
	private void leftMouseClick() {

		// Show task bubble only if there are no stage title textboxes out AND
		// the ignoreAllResponses flag has not been set OR
		// another taskinfo bubble is already out
		if ((!FetchWorkflowObserver.ignoreAllResponses || TaskController.anyTaskInfoOut)
				&& !StageController.anyChangeTitleOut) {
			// Don't reload (so the correct task can be highlighted while the
			// bubble is up
			FetchWorkflowObserver.ignoreAllResponses = true;

			// Create the taskinfo bubble
			final Point stageLoc = view.getParent().getParent().getParent()
					.getParent().getLocation();
			final Point stagesPanelLoc = view.getParent().getParent()
					.getParent().getParent().getParent().getLocation();
			final Point infoLoc = new Point(stagesPanelLoc.x + stageLoc.x,
					view.getLocation().y);
			WorkflowController.getInstance().setTaskInfo(
					new TaskInfoPreviewView(model, this, infoLoc));

			// Set the correct flags
			setThisTaskInfoOut(true);
			TaskController.anyTaskInfoOut = true;
			// make the associated task a darker color while the bubble is out
			if (isArchived()) {
				view.setBackground(Colors.ARCHIVE_CLICKED);
			} else {
				view.setBackground(Colors.TASK_CLICKED);
			}
		}
	}

	/**
	 * Called when the user clicks the view with the middle mouse button.
	 */
	private void middleMouseClicked() {

	}

	/**
	 * Called when the user clicks the view with the right mouse button.
	 */
	private void rightMouseClick() {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			leftMouseClick();
			break;
		case MouseEvent.BUTTON2:
			middleMouseClicked();
			break;
		case MouseEvent.BUTTON3:
			rightMouseClick();
			break;
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

		final boolean isArchived = model.isArchived();
		if (isArchived) {
			ToolbarController.getInstance().getView()
					.setArchiveIcon(ToolbarView.UNARCHIVE);
		} else {
			ToolbarController.getInstance().getView()
					.setArchiveIcon(ToolbarView.ARCHIVE);
		}
		ToolbarController.getInstance().getView().setArchiveEnabled(true);
		ToolbarController.getInstance().getView().setDeleteEnabled(isArchived);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		if (((JMenuItem) e.getSource()).getText().equals("Delete Task"))
			deleteTask();
		if (((JMenuItem) e.getSource()).getText().equals("Edit Task"))
			leftMouseClick();
		if (((JMenuItem) e.getSource()).getText().equals("New Task"))
			leftMouseClick();
		if (((JMenuItem) e.getSource()).getName().equals("Move To")) {
			int i = 0;
			while (i < WorkflowModel.getInstance().getStages().size()) {
				StageModel a = WorkflowModel.getInstance().getStages().get(i);
				if (a.getName().equals(((JMenuItem) e.getSource()).getText())) {
					moveToStage(a, 0);
					WorkflowModel.getInstance().save();
					WorkflowController.getInstance().reloadData();
					
				}
				i++;
			}
		}
		System.out.println(((JMenuItem) e.getSource()).getText());
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
