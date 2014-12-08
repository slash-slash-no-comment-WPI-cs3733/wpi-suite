/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.controller;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import taskManager.JanewayModule;
import taskManager.model.ActivityModel;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.RequirementManager;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;

/**
 * The controller for editing and creating a new task
 * 
 * @author Beth Martino
 *
 */
public class EditTaskController implements ActionListener {

	private final EditTaskView etv;
	private final WorkflowModel wfm;
	private final User[] projectUsers = JanewayModule.users;
	private String taskID;
	private ArrayList<String> toRemove = new ArrayList<String>();
	private TaskInputController fieldController;

	/**
	 * Constructor, attaches the edit task view to this controller
	 * 
	 * @param wfm
	 *            The workflowModel that belongs to this controller.
	 */
	public EditTaskController(EditTaskView etv) {
		this.etv = etv;
		this.wfm = WorkflowModel.getInstance();

		this.reloadData();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();

			taskID = etv.getTitle().getName();

			// check to see if the task exists in the workflow and grabs the
			// stage that the task is in
			boolean exists = false;
			StageModel currentStage = wfm.getStages().get(0);
			for (StageModel stage : wfm.getStages()) {
				if (stage.containsTaskByID(taskID)) {
					exists = true;
					currentStage = stage;
					break;
				} else {
					exists = false;
				}
			}

			// grabs the stage from the dropdown box
			StageModel desiredStage = wfm.findStageByName((String) etv
					.getStages().getSelectedItem());
			Requirement requirement = RequirementModel.getInstance()
					.getRequirementByName(
							(String) etv.getRequirements().getSelectedItem());

			switch (name) {

			case EditTaskView.SAVE:
				TaskModel task;

				if (etv.getFieldController().checkFields()) {
					// if editing
					if (exists) {
						// set the task to be edited
						task = currentStage.findTaskByID(taskID);
						this.setTaskData(task, desiredStage, requirement);
						this.setTaskID(task.getID());
					}
					// if creating a new task
					else {
						// creates a new task model
						task = new TaskModel(etv.getTitle().getText(),
								desiredStage);
						this.setTaskData(task, desiredStage, requirement);
					}

					// Add the newly added activities.
					List<ActivityModel> newActivities = etv.getNewActivities();
					for (ActivityModel act : newActivities) {
						task.addActivity(act);
					}

					// exit the edit view, this refreshes the workflow
					this.returnToWorkflowView();
					// makes all the fields blank again
					etv.resetFields();
					// Save entire workflow whenever a task is saved
					wfm.save();
				} else {
					etv.setSaveEnabled(false);
				}
				break;

			case EditTaskView.ARCHIVE:

				// archive this task
				task = currentStage.findTaskByID(taskID);
				boolean isArchived = task.isArchived();
				if (isArchived) {
					etv.getArchiveButton().setText("Archive");
				} else {
					etv.getArchiveButton().setText("Unarchive");
				}
				task.setArchived(!isArchived);
				etv.setDeleteEnabled(!isArchived);

				// Save and reload the workflow.
				WorkflowController.getInstance().reloadData();
				wfm.save();

				break;

			case EditTaskView.DELETE:
				Integer choice = JOptionPane.showConfirmDialog(etv,
						"Are you sure you want to delete this task?",
						"Warning - Deleting a task", JOptionPane.YES_NO_OPTION);
				if (choice.equals(JOptionPane.YES_OPTION)) {
					// delete this task
					task = currentStage.findTaskByID(taskID);
					currentStage.getTasks().remove(task);
					etv.resetFields();

					// Save entire workflow whenever a task is deleted
					wfm.save();
					returnToWorkflowView();
				}
				break;

			case EditTaskView.ADD_USER:
				this.addUsersToList();

				break;

			case EditTaskView.REMOVE_USER:
				this.removeUsersFromList();
				break;

			case EditTaskView.VIEW_REQ:
				// view requirement in requirement manager

				// TODO: this button should be disabled when [None] selected so
				// requirement would never be null.
				if (requirement == null) {
					return;
				}

				// get the tab pane
				Container c = etv;
				while (c != null) {
					if ("Janeway Tab Pane".equals(c.getName())) {
						break;
					}
					c = c.getParent();
				}
				JTabbedPane tabPane = (JTabbedPane) c;

				// switch to the requirement manager tab
				tabPane.setSelectedIndex(tabPane.indexOfTab(RequirementManager
						.staticGetName()));

				// open the editor to this requirement
				ViewEventController.getInstance().editRequirement(
						RequirementModel.getInstance().getRequirementByName(
								(String) etv.getRequirements()
										.getSelectedItem()));
				break;

			case EditTaskView.CANCEL:
				// go back to workflow view
				etv.resetFields();
				returnToWorkflowView();
				break;

			case EditTaskView.SUBMIT_COMMENT:
				// adds a comment activity
				etv.addComment();
				break;

			case EditTaskView.REFRESH:
				if (exists) {
					// Clear the activities list.
					etv.clearActivities();

					// set activities pane
					task = currentStage.findTaskByID(taskID);
					List<ActivityModel> tskActivities = task.getActivities();
					etv.setActivities(tskActivities);
					etv.setActivitiesPanel(tskActivities);
				}
				break;
			}
		}
	}

	/**
	 * refreshes the data on the view
	 */
	public void reloadData() {
		JComboBox<String> stages = etv.getStages();
		stages.removeAllItems();
		for (StageModel stage : wfm.getStages()) {
			stages.addItem(stage.getName());
		}

		List<Requirement> reqs = RequirementModel.getInstance()
				.getRequirements();
		JComboBox<String> requirements = etv.getRequirements();
		requirements.removeAllItems();
		requirements.addItem(EditTaskView.NO_REQ);
		for (Requirement req : reqs) {
			requirements.addItem(req.getName());
		}
	}

	/**
	 * switches back to workflow view
	 */
	private void returnToWorkflowView() {
		JanewayModule.tabPaneC.removeTabByComponent(etv);
		// JanewayModule.tabPaneC.getTabView().getWorkflowController()
		//		.clearWorkflow(false);
		// JanewayModule.tabPaneC.getTabView().reloadWorkflow();
		WorkflowController.getInstance().reloadData();
	}

	/**
	 * Enter the task id that will be edited
	 * 
	 * @param id
	 *            the id that new task info will be saved to
	 */
	public void setTaskID(String id) {
		taskID = id;
	}

	/**
	 * sets the fields of the given task object to the values on the fields of
	 * the edit task view and saves the task data
	 * 
	 * @param t
	 *            the task to be edited
	 */
	private void setTaskData(TaskModel t, StageModel s, Requirement r) {
		// sets the text fields
		t.setName(etv.getTitle().getText().trim());
		t.setDescription(etv.getDescription().getText());

		// Try to set the effort values.
		try {
			t.setEstimatedEffort(Integer.parseInt(etv.getEstEffort().getText()));
		} catch (java.lang.NumberFormatException e2) {
			// Set to false since this value is not set.
			t.setHasEstimatedEffort(false);
		}

		try {
			t.setActualEffort(Integer.parseInt(etv.getActEffort().getText()));
		} catch (java.lang.NumberFormatException e2) {
			t.setHasActualEffort(false);
		}

		// sets the due date from the calendar
		t.setDueDate(etv.getDateField().getDate());

		// move the stage
		s.addTask(t);

		// adds or removes users
		for (String name : etv.getUsersList().getAllValues()) {
			if (!t.getAssigned().contains(name)) {
				t.addAssigned(findUserByName(name));
			}
		}
		for (String n : this.toRemove) {
			if (t.getAssigned().contains(n)) {
				t.removeAssigned(findUserByName(n));
			}
		}
		t.setReq(r);
		wfm.save();
	}

	/**
	 * returns the user object with the given name from the list of project
	 * users
	 * 
	 * @param name
	 *            the name of the user to find
	 * @return the user with the given name
	 */
	private User findUserByName(String name) {
		for (User u : projectUsers) {
			if (u.getUsername().equals(name)) {
				return u;
			}
		}
		return null;

	}

	/**
	 * adds selected usernames to the assigned users list and removes them from
	 * the project user list.
	 */
	public void addUsersToList() {
		int[] toAdd = etv.getProjectUsersList().getSelectedIndices();
		ArrayList<String> namesToAdd = new ArrayList<String>();

		for (int ind : toAdd) {
			namesToAdd.add(etv.getProjectUsersList().getValueAtIndex(ind));
		}

		for (String n1 : namesToAdd) {
			etv.getProjectUsersList().removeFromList(n1);
			if (!etv.getUsersList().contains(n1)) {
				// add the new username to the assigned user list
				etv.getUsersList().addToList(n1);
				etv.getProjectUsersList().removeFromList(n1);
				// if this user was to be removed take it out of the
				// list
				if (this.toRemove.contains(n1)) {
					this.toRemove.remove(n1);
				}

			}
		}
	}

	/**
	 * removes selected usernames from the assigned users list and adds them to
	 * the project user list. marks selected usernames to be removed from the
	 * model
	 */
	public void removeUsersFromList() {
		// grab all of the indices of the usernames selected in
		// assigned users and grab the associated strings
		int[] usersToRemove = etv.getUsersList().getSelectedIndices();
		ArrayList<String> namesToRemove = new ArrayList<String>();
		for (int ind : usersToRemove) {
			namesToRemove.add(etv.getUsersList().getValueAtIndex(ind));
		}

		// for every name that is selected, remove it from assigned
		// users and add it to project users
		for (String n2 : namesToRemove) {
			etv.getUsersList().removeFromList(n2);
			if (!etv.getProjectUsersList().contains(n2)) {
				etv.getProjectUsersList().addToList(n2);
				etv.getUsersList().removeFromList(n2);
				if (!this.toRemove.contains(n2)) {
					this.toRemove.add(n2);
				}
			}

		}
	}

	/**
	 * 
	 * Returns the task ID.
	 *
	 * @return the task ID
	 */
	public String getTaskID() {
		return etv.getTitle().getName();
	}

	/**
	 * 
	 * Returns a boolean of whether or not the task is edited.
	 * 
	 * @return boolean stating whether the task is edited.
	 */
	public boolean isEdited() {
		boolean edited = false;

		// Get the stage of the task.
		boolean exists = false;

		StageModel currentStage = wfm.getStages().get(0);
		for (StageModel stage : wfm.getStages()) {
			if (stage.containsTaskByID(getTaskID())) {
				exists = true;
				currentStage = stage;
				break;
			} else {
				exists = false;
			}
		}

		TaskModel task = null;
		if (!exists) {
			// make a task with the default values to compare to
			task = new TaskModel("", null);
			task.setDescription("");
		} else {
			task = currentStage.findTaskByID(getTaskID());
		}

		// Compare the task info with the filled in info.
		// Title.
		if (!task.getName().equals(etv.getTitle().getText())) {
			edited = true;
		}
		// Description.
		else if (!task.getDescription().equals(etv.getDescription().getText())) {
			edited = true;
		}
		// Due Date.
		else if (checkDate(task)) {
			edited = true;
		}
		// Stage.
		else if (!currentStage.getName().equals(etv.getSelectedStage())) {
			edited = true;
		}
		// Users.
		else if (checkUsers(task)) {
			edited = true;
		}
		// Estimated effort.
		else if (checkEstEffort(task)) {
			edited = true;
		}
		// Actual effort.
		else if (checkActEffort(task)) {
			edited = true;
		}
		// Requirements.
		else if (checkReq(task)) {
			edited = true;
		}
		return edited;
	}

	public Boolean checkDate(TaskModel task) {
		Date dueDate = task.getDueDate();

		// if the task had a due date, check if it changed
		if (dueDate != null && dueDate.equals(etv.getDateField().getDate())) {
			return false;
		} else {
			// check if it has the default date (today)
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(etv.getDateField().getDate());
			cal2.setTime(Calendar.getInstance().getTime());

			// check if the two dates are the same day
			if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
					&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
							.get(Calendar.DAY_OF_YEAR)) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 
	 * Checks whether the users in the view and the users stored in the task are
	 * the same.
	 *
	 * @param The
	 *            task to check with.
	 * @return true if there are edits.
	 */
	public boolean checkUsers(TaskModel task) {
		boolean edited = false;
		Set<String> taskAssigned = new HashSet<String>();
		taskAssigned = task.getAssigned();
		Set<String> usersAssigned = new HashSet<String>();
		usersAssigned.addAll(etv.getUsersList().getAllValues());
		if (!usersAssigned.equals(taskAssigned)) {
			edited = true;
		}
		if (usersAssigned.size() == 0 && taskAssigned == null) {
			edited = false;
		}
		return edited;
	}

	/**
	 * 
	 * Checks whether the estimated effort value in the task and on the view are
	 * equivalent.
	 *
	 * @param The
	 *            task to check if.
	 * @return true if there are edits.
	 */
	public boolean checkEstEffort(TaskModel task) {
		boolean edited = false;
		if (task.getEstimatedEffort() == 0) {
			if (etv.getEstEffort().getText().isEmpty()) {
				edited = false;
			} else {
				edited = true;
			}
		} else {
			Integer taskEffort = task.getEstimatedEffort();
			Integer etvEffort;
			try {
				etvEffort = Integer.parseInt(etv.getEstEffort().getText());
				if (!taskEffort.equals(etvEffort)) {
					edited = true;
				}
			} catch (NumberFormatException e) {
				edited = true;
			}
		}
		return edited;
	}

	/**
	 * 
	 * Checks whether the actual effort value in the task and on the view are
	 * equivalent.
	 *
	 * @param The
	 *            task to check if.
	 * @return true if there are edits.
	 */
	public boolean checkActEffort(TaskModel task) {
		boolean edited = false;
		if (task.getActualEffort() == 0) {
			if (etv.getActEffort().getText().isEmpty()) {
				edited = false;
			} else {
				edited = true;
			}
		} else {
			Integer taskEffort = task.getActualEffort();
			Integer etvEffort;
			try {
				etvEffort = Integer.parseInt(etv.getActEffort().getText());
				if (!taskEffort.equals(etvEffort)) {
					edited = true;
				}
			} catch (NumberFormatException e) {
				edited = true;
			}
		}
		return edited;
	}

	/**
	 * 
	 * Checks whether the requirement in the task and on the view are
	 * equivalent.
	 *
	 * @param The
	 *            task to check if.
	 * @return true if there are edits.
	 */
	public boolean checkReq(TaskModel task) {
		boolean edited = false;
		if (task.getReq() == null) {
			if (etv.getRequirements().getSelectedItem().toString()
					.equals(EditTaskView.NO_REQ)) {
				edited = false;
			} else {
				edited = true;
			}
		} else if (!task.getReq().getName()
				.equals(etv.getRequirements().getSelectedItem().toString())) {
			edited = true;
		}
		return edited;
	}
}
