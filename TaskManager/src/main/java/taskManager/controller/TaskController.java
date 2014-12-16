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
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;

import javax.swing.JMenuItem;

import taskManager.model.FetchWorkflowObserver;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.Colors;
import taskManager.view.StageView;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.TaskView;
import taskManager.view.ToolbarView;

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

	private Boolean taskInfoPreviewOut = false;

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
	 * 
	 * Make the task a darker color. Used for when the mouse is over a task or
	 * when a bubble is out for this task
	 *
	 */
	public void changeToHoverColor() {
		// don't highlight while task info is out in fun mode, because the clip
		// bounds passed to the rotation view are sometimes not correct
		if (ToolbarController.getInstance().getView().isFunMode()) {
			return;
		}
		if (isArchived() && !taskInfoPreviewOut) {
			view.setBackground(Colors.ARCHIVE_HOVER);
		} else if (!taskInfoPreviewOut) {

			view.setBackground(Colors.TASK_HOVER);
		}
	}

	/**
	 * 
	 * Resets the task background to its original color
	 *
	 */
	public void resetBackground() {
		if (isArchived() && taskInfoPreviewOut) {
			view.setBackground(Colors.ARCHIVE_CLICKED);
		} else if (isArchived()) {
			view.setBackground(Colors.ARCHIVE);
		} else if (taskInfoPreviewOut) {
			view.setBackground(Colors.TASK_CLICKED);
		} else {
			view.setBackground(Colors.TASK);
		}
	}

	/**
	 * Called when the user clicks the view with the left mouse button.
	 */
	private void leftMouseClick() {
		WorkflowController.getInstance().removeChangeTitles();
		WorkflowController.pauseInformation = false;

		// Create the taskinfo bubble
		final Container stageContainer = SwingUtilities.getAncestorOfClass(
				StageView.class, view);
		final Point stageLoc = stageContainer.getLocation();
		final Point stagesPanelLoc = stageContainer.getParent().getLocation();
		final Point infoLoc = new Point(stagesPanelLoc.x + stageLoc.x,
				view.getLocation().y);
		WorkflowController.getInstance().setTaskInfo(
				new TaskInfoPreviewView(model, this, infoLoc));

		// Set the correct flags
		taskInfoPreviewOut = true;
		// make the associated task a darker color while the bubble is out
		if (isArchived()) {
			view.setBackground(Colors.ARCHIVE_CLICKED);
		} else {
			view.setBackground(Colors.TASK_CLICKED);
		}
	}

	/**
	 * Called when the user clicks the view with the middle mouse button.
	 */
	private void middleMouseClick() {

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
			middleMouseClick();
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
		resetBackground();
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
		JMenuItem item = (JMenuItem)e.getSource();
		if (item.getText().equals("Delete Task")) {
			deleteTask();
			WorkflowModel.getInstance().save();
			WorkflowController.getInstance().reloadData();
		}
		else if (item.getText().equals("Edit Task"))
			this.editTask();
		else if (item.getName().equals("Archive Task")) {
			model.setArchived(!model.isArchived());
			WorkflowModel.getInstance().save();
			WorkflowController.getInstance().reloadData();
		}
		else if (item.getName().equals("Move To")) {
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
		return taskInfoPreviewOut;
	}

	/**
	 * @param thisTaskInfoOut
	 *            the thisTaskInfoOut to set
	 */
	public void setThisTaskInfoOut(Boolean thisTaskInfoOut) {
		this.taskInfoPreviewOut = thisTaskInfoOut;
	}

	/**
	 * 
	 * If the taskInfo bubble for this task was removed from view. Resets the
	 * flag to correctly color the task.
	 *
	 */
	public void taskInfoRemoved() {
		taskInfoPreviewOut = false;
		resetBackground();
	}

	/**
	 * Generate string for table export (Excel format)
	 *
	 * @return export string
	 */
	public String getExportString() {
		String fields[] = { "Name", "Description", "Due Date",
				"Assigned Users", "Estimated Effort", "Actual Effort" };
		String values[] = { model.getName(), model.getDescription(),
				new SimpleDateFormat("MM/dd/yy").format(model.getDueDate()),
				String.join(",", model.getAssigned()),
				Integer.toString(model.getEstimatedEffort()),
				Integer.toString(model.getActualEffort()) };

		String export = "";
		for (int i = 0; i < fields.length; i++) {
			// remove newlines and tabs
			values[i] = values[i].replace("\t", "        ");
			values[i] = values[i].replace("\n", " ");
			values[i] = values[i].replace("\r", "");

			export += fields[i] + "\t" + values[i] + "\n";
		}
		return export;
	}
}
