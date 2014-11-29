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
 *
 */
public class EditTaskController implements ActionListener {

	private final EditTaskView etv;
	private String taskID;
	private ArrayList<String> toRemove = new ArrayList<String>();
	private TaskModel model;

	/**
	 * Constructor, attaches the edit task view to this controller
	 * 
	 */
	public EditTaskController(EditTaskView etv) {
		this.etv = etv;

		model = new TaskModel();

		reloadData();
	}

	/**
	 * 
	 * Creates a new EditTaskController with a new EditTaskView
	 *
	 * @param viewMode
	 *            The type of EditTaskController/View to be created (EG: Edit,
	 *            CREATE...)
	 * @param model
	 *            The Task this controller is associated with.
	 */
	public EditTaskController(Mode viewMode, TaskModel model) {
		etv = new EditTaskView(viewMode);
		etv.setController(this);
		etv.setFieldController(new TaskInputController(etv));
		this.model = model;

		// etv.setStageDropdown(0);
		if (Mode.CREATE.equals(viewMode)) {
			etv.setRefreshEnabled(false);
			// Disable save button when creating a task.
			etv.setSaveEnabled(false);

			// Clear all activities, reset fields.
			etv.clearActivities();
			etv.resetFields();

			// fills the user lists
			List<String> projectUserNames = new ArrayList<String>();
			for (User u : JanewayModule.users) {
				String name = u.getUsername();
				if (!projectUserNames.contains(name)) {
					projectUserNames.add(name);
				}
			}
			etv.getProjectUsersList().addAllToList(projectUserNames);

		}

		reloadData();
	}

	public EditTaskController(Mode viewMode) {
		this(viewMode, new TaskModel()); // TODO Fix inevitable errors with
											// using dumb taskModel constructor
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();

			taskID = getTaskID();

			// check to see if the task exists in the workflow and grabs the
			// stage that the task is in
			WorkflowModel wfm = WorkflowModel.getInstance();
			StageModel currentStage = null;
			for (StageModel stage : wfm.getStages()) {
				if (stage.containsTaskByID(taskID)) {
					currentStage = stage;
					break;
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
				// if editing
				if (currentStage != null) {
					// set the task to be edited
					model = currentStage.findTaskByID(taskID);
					this.setTaskData(model, desiredStage, requirement);

					this.setTaskID("000000");
				}
				// if creating a new task
				else {
					// creates a new task model
					model = new TaskModel(etv.getTitle().getText(),
							desiredStage);
					this.setTaskData(model, desiredStage, requirement);
				}

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
				wfm.save();
				break;

			case EditTaskView.DELETE:
				Integer choice = JOptionPane.showConfirmDialog(etv,
						"Are you sure you want to delete this task?",
						"Warning - Deleting a task", JOptionPane.YES_NO_OPTION);
				if (choice.equals(JOptionPane.YES_OPTION)) {
					// delete this task
					model = currentStage.findTaskByID(taskID);
					currentStage.getTasks().remove(model);
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
				if (currentStage != null) {
					// Clear the activities list.
					etv.clearActivities();

					// set activities pane
					model = currentStage.findTaskByID(taskID);
					List<ActivityModel> tskActivities = model.getActivities();
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
		for (StageModel stage : WorkflowModel.getInstance().getStages()) {
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
		JanewayModule.tabPaneC.getTabView().reloadWorkflow();
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
		t.setName(etv.getTitle().getText());
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
		WorkflowModel.getInstance().save();
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

	public void initializeView(TabPaneController tabPaneC) {

		// makes the delete button unclickable
		etv.enableDelete();

		taskID = model.getID();

		// makes the delete button unclickable
		etv.enableDelete();

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
			if (etv.getStages().getItemAt(i) == model.getStage().getName()) {
				etv.setStageDropdown(i);
				break;
			}
		}

		etv.getStages().setSelectedItem(model.getStage().getName());

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

		etv.setRefreshEnabled(true);

		// set the requirement dropdown
		if (model.getReq() != null) {
			etv.getRequirements().setSelectedItem(model.getReq().getName());
		} else {
			etv.getRequirements().setSelectedItem(EditTaskView.NO_REQ);
		}

	}

	public void addTab(TabPaneController tpc) {
		tpc.addTab("Create Task", etv, true);

	}

	public boolean isDuplicateView(EditTaskView etv2) {
		return getTaskID() != null
				&& getTaskID().equals(etv2.getController().getTaskID());
	}

	private String getTaskID() {
		return taskID;
	}

	/**
	 * 
	 * Returns a boolean of whether or not the task is edited.
	 * 
	 * @return boolean stating whether the task is edited.
	 */
	public Boolean isEdited() {
		Boolean edited = false;

		// Get the stage of the task.
		WorkflowModel wfm = WorkflowModel.getInstance();
		StageModel currentStage = null;
		for (StageModel stage : wfm.getStages()) {
			if (stage.containsTaskByID(getTaskID())) {
				currentStage = stage;
				break;
			}
		}
		if (currentStage == null) {
			return false;
		}

		// Compare the task info with the filled in info.
		TaskModel task = currentStage.findTaskByID(getTaskID());

		// Title.
		if (!task.getName().equals(etv.getTitle().getText())) {
			edited = true;
		}
		// Description.
		else if (!task.getDescription().equals(etv.getDescription().getText())) {
			edited = true;
		}
		// Due Date.
		else if (!task.getDueDate().equals(etv.getDateField().getDate())) {
			edited = true;
		}
		// Stage.
		else if (!task.getStage().getName().equals(etv.getSelectedStage())) {
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

	/**
	 * 
	 * Checks whether the users in the view and the users stored in the task are
	 * the same.
	 *
	 * @param The
	 *            task to check with.
	 * @return true if there are edits.
	 */
	public Boolean checkUsers(TaskModel task) {
		Boolean edited = false;
		Set<String> taskAssigned = new HashSet<String>();
		taskAssigned = task.getAssigned();
		Set<String> usersAssigned = new HashSet<String>();
		usersAssigned.addAll(etv.getUsersList().getAllValues());
		if (!taskAssigned.equals(usersAssigned)) {
			edited = true;
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
	public Boolean checkEstEffort(TaskModel task) {
		Boolean edited = false;
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
	public Boolean checkActEffort(TaskModel task) {
		Boolean edited = false;
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
	public Boolean checkReq(TaskModel task) {
		Boolean edited = false;
		if (task.getReq() == null) {
			if (etv.getRequirements().getSelectedItem().toString()
					.equals("[None]")) {
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
