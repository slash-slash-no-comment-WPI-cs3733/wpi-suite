/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;

import taskManager.JanewayModule;
import taskManager.model.ActivityModel;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.view.Colors;
import taskManager.view.EditTaskView;
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
public class TaskController implements MouseListener, MouseMotionListener {

	private final TaskView view;
	private final TaskModel model;
	private StageModel sm;
	private TabPaneController tabPaneC;
	private EditTaskView etv;
	private Requirement req;
	private final User[] projectUsers = JanewayModule.users;
	private Set<String> assignedUsers;

	private boolean thisTaskInfoOut = false;

	/**
	 * Constructor for the TaskController, currently just sets the corresponding
	 * view and model parameters.
	 *
	 * @param view
	 *            the corresponding TaskView object
	 * @param model
	 *            the corresponding TaskModel object
	 */
	public TaskController(TaskModel model) throws IllegalArgumentException {
		if (model == null) {
			throw new IllegalArgumentException(
					"You must pass a valid TaskModel.");
		}

		this.tabPaneC = TabPaneController.getInstance();
		this.model = model;
		this.view = new TaskView(this.model.getName(), this.model.getDueDate(),
				this);
		sm = model.getStage();

		etv = new EditTaskController(model).getView();

		assignedUsers = this.model.getAssigned();

		req = this.model.getReq();

		// Set the background to orange if the task is archived.
		if (this.model.isArchived()) {
			view.setBackground(Colors.ARCHIVE);
		} else {
			view.setBackground(Colors.TASK);
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
	public void setArchived(boolean a) {
		model.setArchived(a);
	}

	/**
	 * 
	 * Populates the EditTaskView with the information from this task.
	 *
	 */
	public void editTask() {
		// uses the title field to hold the unique id
		etv.getTitle().setName(this.model.getID());

		// uses description field to hold the name of the stage
		etv.getDescription().setName(this.model.getStage().getName());

		// populate editable fields with this tasks info
		etv.setTitle(model.getName());
		etv.setDescription(model.getDescription());
		etv.setDate(model.getDueDate());

		// Sets the effort values only if user specified them.
		if (model.isEstimatedEffortSet()) {
			etv.setEstEffort(model.getEstimatedEffort());
		}
		if (model.isActualEffortSet()) {
			etv.setActEffort(model.getActualEffort());
		}

		tabPaneC.addEditTaskTab(etv);

		// figures out the index of the stage, then sets the drop down to the
		// stage at that index
		JComboBox<String> stages = etv.getStages();
		for (int i = 0; i < stages.getItemCount(); i++) {
			if (etv.getStages().getItemAt(i) == sm.getName()) {
				etv.setStageDropdown(i);
				break;
			}
		}

		etv.getStages().setSelectedItem(model.getStage().getName());

		// populates the project users list
		ArrayList<String> projectUserNames = new ArrayList<String>();
		for (User u : projectUsers) {
			String name = u.getUsername();
			if (!projectUserNames.contains(name)
					&& !model.getAssigned().contains(name)) {
				projectUserNames.add(name);
			}
		}
		etv.getProjectUsersList().addAllToList(projectUserNames);

		// populates the assigned users panel
		ArrayList<String> assignedUserNames = new ArrayList<String>();
		for (String u : assignedUsers) {
			if (!assignedUserNames.contains(u)) {
				assignedUserNames.add(u);
			}
		}
		etv.getUsersList().addAllToList(assignedUserNames);

		// Enable save button when editing a task.
		etv.setSaveEnabled(true);

		// Clear the activities list.
		etv.clearActivities();

		// set activities pane
		List<ActivityModel> tskActivities = model.getActivities();
		etv.setActivities(tskActivities);
		etv.setActivitiesPanel(tskActivities);

		// set the requirement dropdown
		if (req != null) {
			etv.getRequirements().setSelectedItem(req.getName());
		} else {
			etv.getRequirements().setSelectedItem(EditTaskView.NO_REQ);
		}

		// makes the archive button clickable
		etv.enableArchive();

		// Set text for archive button.
		if (model.isArchived()) {
			etv.setArchiveButtonText("Unarchive");
		} else {
			etv.setArchiveButtonText("Archive");
		}
		etv.setDeleteEnabled(model.isArchived());

		TabPaneController.getInstance().addEditTaskTab(etv);
	}

	/**
	 * 
	 * Make the task a darker color. Used for when the mouse is over a task.
	 * Does not change the color of a task that has a taskInfo bubble out.
	 *
	 */
	public void setToHoverColor() {
		if (isArchived() && !thisTaskInfoOut) {
			view.setBackground(Colors.ARCHIVE_HOVER);
		} else if (!thisTaskInfoOut) {
			view.setBackground(Colors.TASK_HOVER);
		}
	}

	/**
	 * 
	 * Resets the task background to its original color. This is orange/yellow
	 * for Archived tasks, Green for a task which has a taskInfo bubble out, and
	 * beige for a normal task.
	 *
	 */
	public void resetBackground() {
		if (isArchived() && thisTaskInfoOut) {
			view.setBackground(Colors.ARCHIVE_CLICKED);
		} else if (isArchived()) {
			view.setBackground(Colors.ARCHIVE);
		} else if (thisTaskInfoOut) {
			view.setBackground(Colors.TASK_CLICKED);
		} else {
			view.setBackground(Colors.TASK);
		}
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

	/**
	 *
	 * 
	 * @return The TaskView associated with this controller
	 */
	public TaskView getView() {
		return view;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Create the taskinfo bubble
		Point stageLoc = view.getParent().getParent().getParent().getParent()
				.getLocation();
		Point stagesPanelLoc = view.getParent().getParent().getParent()
				.getParent().getParent().getLocation();
		Point infoLoc = new Point(stagesPanelLoc.x + stageLoc.x,
				view.getLocation().y);
		WorkflowController.getInstance().setTaskInfo(
				new TaskInfoPreviewView(model, this, infoLoc));

		thisTaskInfoOut = true;

		// make the associated task a different color while the bubble is out
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
		setToHoverColor();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		resetBackground();
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
		// Do nothing
	}
}
