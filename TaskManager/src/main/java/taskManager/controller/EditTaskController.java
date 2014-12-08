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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import taskManager.JanewayModule;
import taskManager.model.ActivityModel;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.EditTaskView.Mode;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.RequirementManager;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;

/**
 * The controller for editing and creating a new task
 * 
 * @author Beth Martino
 * @author Ezra Davis
 *
 */
public class EditTaskController implements ActionListener {

	private final EditTaskView etv;
	private ArrayList<String> toRemove = new ArrayList<String>();
	private TaskModel model;

	/**
	 * Creates an EditTaskController and EditTaskView form for a *new* Task.
	 *
	 */
	public EditTaskController() {
		etv = new EditTaskView(Mode.CREATE);

		etv.setController(this);
		etv.setFieldController(new TaskInputController(etv));

		// Disable save button when creating a task.
		etv.setSaveEnabled(false);

		// Clear all activities, reset fields.
		etv.clearActivities();
		etv.resetFields();

		// fills the user lists
		ArrayList<String> projectUserNames = new ArrayList<String>();
		for (User u : JanewayModule.users) {
			String name = u.getUsername();
			if (!projectUserNames.contains(name)) {
				projectUserNames.add(name);
			}
		}
		etv.getProjectUsersList().addAllToList(projectUserNames);

		JanewayModule.tabPaneC.addTab("Create Task", etv, true);

		reloadData();
	}

	/**
	 * 
	 * Creates an EditTaskController and EditTaskView form for an *already
	 * existing* Task.
	 *
	 * @param model
	 */
	public EditTaskController(TaskModel model) {
		etv = new EditTaskView(Mode.EDIT);
		this.model = model;

		etv.setController(this);
		etv.setFieldController(new TaskInputController(etv));

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

		JanewayModule.tabPaneC.addEditTaskTab(etv);

		etv.setSelectedStage(model.getStage().getName());

		// populates the project users list
		ArrayList<String> projectUserNames = new ArrayList<String>();
		for (User u : JanewayModule.users) {
			String name = u.getUsername();
			if (!projectUserNames.contains(name)
					&& !model.getAssigned().contains(name)) {
				projectUserNames.add(name);
			}
		}
		etv.getProjectUsersList().addAllToList(projectUserNames);

		// populates the assigned users panel
		ArrayList<String> assignedUserNames = new ArrayList<String>();
		for (String u : model.getAssigned()) {
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
		if (model.getReq() != null) {
			etv.setSelectedRequirement(model.getReq().getName());
		} else {
			etv.setSelectedRequirement(null);
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

		this.reloadData();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();

			switch (name) {

			case EditTaskView.SAVE:

				if (etv.getFieldController().checkFields()) {
					// if editing
					if (isEditingTask()) {

						save();
					}
					// if creating a new task
					else {
						// grabs the stage from the dropdown box
						StageModel desiredStage = WorkflowModel.getInstance()
								.findStageByName(
										(String) etv.getSelectedStage());

						// creates a new task model
						model = new TaskModel(etv.getTitleText(), desiredStage);
						save();
					}

				} else {
					etv.setSaveEnabled(false);
				}
				break;

			case EditTaskView.ARCHIVE:

				// archive this task
				boolean isArchived = model.isArchived();
				if (isArchived) {
					etv.setArchiveButtonText("Archive");
				} else {
					etv.setArchiveButtonText("Unarchive");
				}
				model.setArchived(!isArchived);
				etv.setDeleteEnabled(!isArchived);

				// Save and reload the workflow.
				WorkflowController.getInstance().reloadData();

				WorkflowModel.getInstance().save(); // TODO switch to
													// appropriate save method

				break;

			case EditTaskView.DELETE:
				Integer choice = JOptionPane.showConfirmDialog(etv,
						"Are you sure you want to delete this task?",
						"Warning - Deleting a task", JOptionPane.YES_NO_OPTION);
				if (choice.equals(JOptionPane.YES_OPTION)) {
					// delete this task
					if (model != null) {
						StageModel currentStage = null;

						for (StageModel stage : WorkflowModel.getInstance()
								.getStages()) {
							if (stage.containsTaskByID(model.getID())) {
								currentStage = stage;
								break;
							}
						}
						currentStage.findTaskByID(model.getID());
						currentStage.getTasks().remove(model);
						etv.resetFields();

						// Save entire workflow whenever a task is deleted
						WorkflowModel.getInstance().save();
						// TODO don't save entire workflow
					}
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

				Requirement requirement = RequirementModel.getInstance()
						.getRequirementByName(etv.getSelectedRequirement());

				// This button should be disabled when [None] selected
				// but that may not happen
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
								etv.getSelectedRequirement()));
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
			}
		}
	}

	/**
	 * refreshes the data on the view
	 */
	public void reloadData() {

		List<String> stageNames = new LinkedList<String>();
		for (StageModel stage : WorkflowModel.getInstance().getStages()) {
			stageNames.add(stage.getName());
		}
		etv.setStages(stageNames);

		List<Requirement> reqs = RequirementModel.getInstance()
				.getRequirements();
		List<String> reqNames = new LinkedList<String>();
		for (Requirement req : reqs) {
			reqNames.add(req.getName());
		}
		etv.setRequirements(reqNames);
	}

	/**
	 * switches back to workflow view
	 */
	private void returnToWorkflowView() {
		JanewayModule.tabPaneC.removeTabByComponent(etv);
		WorkflowController.getInstance().reloadData();
	}

	/**
	 * sets the fields of the given task object to the values on the fields of
	 * the edit task view and saves the task data
	 * 
	 */
	private void save() {
		if (model == null) {
			throw new IllegalStateException(
					"Don't call EditTaskController's save method if its model is null");
		}

		// sets the text fields
		model.setName(etv.getTitleText().trim());
		model.setDescription(etv.getDescription());

		// grabs the stage from the dropdown box
		StageModel s = WorkflowModel.getInstance().findStageByName(
				(String) etv.getSelectedStage());
		Requirement r = RequirementModel.getInstance().getRequirementByName(
				(String) etv.getSelectedRequirement());

		// Try to set the effort values.
		try {
			model.setEstimatedEffort(Integer.parseInt(etv.getEstEffort()));
		} catch (java.lang.NumberFormatException e2) {
			// Set to false since this value is not set.
			model.setHasEstimatedEffort(false);
		}

		try {
			model.setActualEffort(Integer.parseInt(etv.getActEffort()));
		} catch (java.lang.NumberFormatException e2) {
			model.setHasActualEffort(false);
		}

		// sets the due date from the calendar
		model.setDueDate(etv.getDate());

		// move the stage
		s.addTask(model);

		// adds or removes users
		for (String name : etv.getUsersList().getAllValues()) {
			if (!model.getAssigned().contains(name)) {
				model.addAssigned(findUserByName(name));
			}
		}
		for (String n : this.toRemove) {
			if (model.getAssigned().contains(n)) {
				model.removeAssigned(findUserByName(n));
			}
		}
		model.setReq(r);
		WorkflowModel.getInstance().save(); // TODO make this call an
											// appropriate save method.

		// Add the newly added activities.
		List<ActivityModel> newActivities = etv.getNewActivities();
		for (ActivityModel act : newActivities) {
			model.addActivity(act);
		}

		// exit the edit view, this refreshes the workflow
		this.returnToWorkflowView();
		// makes all the fields blank again
		etv.resetFields();

		// Save entire workflow whenever a task is saved
		WorkflowModel.getInstance().save(); // TODO make this call an
											// appropriate method
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
		for (User u : JanewayModule.users) {
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
	 * Returns a boolean of whether or not the task is edited.
	 * 
	 * @return boolean stating whether the task is edited.
	 */
	public boolean isEdited() {
		boolean edited = false;

		// Compare the task info with the filled in info.
		if (model == null) { // If we're creating a task
			edited = true;
		}
		// Title.
		else if (!model.getName().equals(etv.getTitleText())) {
			edited = true;
		}
		// Description.
		else if (!model.getDescription().equals(etv.getDescription())) {
			edited = true;
		}
		// Due Date.
		else if (checkDate(model)) {
			edited = true;
		}
		// Stage.
		else if (!model.getStage().getName().equals(etv.getSelectedStage())) {
			edited = true;
		}
		// Users.
		else if (checkUsers(model)) {
			edited = true;
		}
		// Estimated effort.
		else if (checkEstEffort(model)) {
			edited = true;
		}
		// Actual effort.
		else if (checkActEffort(model)) {
			edited = true;
		}
		// Requirements.
		else if (checkReq(model)) {
			edited = true;
		}
		return edited;
	}

	public boolean checkDate(TaskModel task) {
		Date dueDate = task.getDueDate();

		// if the task had a due date, check if it changed
		if (dueDate != null && dueDate.equals(etv.getDate())) {
			return false;
		} else {
			// check if it has the default date (today)
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(etv.getDate());
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
			if (etv.getEstEffort().isEmpty()) {
				edited = false;
			} else {
				edited = true;
			}
		} else {
			Integer taskEffort = task.getEstimatedEffort();
			Integer etvEffort;
			try {
				etvEffort = Integer.parseInt(etv.getEstEffort());
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
			if (etv.getActEffort().isEmpty()) {
				edited = false;
			} else {
				edited = true;
			}
		} else {
			Integer taskEffort = task.getActualEffort();
			Integer etvEffort;
			try {
				etvEffort = Integer.parseInt(etv.getActEffort());
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
			if (etv.getSelectedRequirement() == null) {
				edited = false;
			} else {
				edited = true;
			}
		} else if (!task.getReq().getName()
				.equals(etv.getSelectedRequirement())) {
			edited = true;
		}
		return edited;
	}

	/**
	 * 
	 * Returns whether this controller is editing a task.
	 *
	 * @return Whether we are creating (false) or editing (true) a task
	 */
	private boolean isEditingTask() {
		// Sadly isn't equivalent to model == null;
		return Mode.EDIT.equals(etv.getMode());
	}

	/**
	 * 
	 * Returns this controller's view.
	 *
	 * @return The Form to edit a task
	 */
	public EditTaskView getView() {
		return etv;
	}

	public boolean isDuplicate(EditTaskController other) {
		// "Create" views are never duplicates -- note: a little different from
		// isEditingTask();
		if (other.model == null || this.model == null) {
			return false;
		} else {
			return model.getID().equals(other.model.getID());
		}
	}
}
