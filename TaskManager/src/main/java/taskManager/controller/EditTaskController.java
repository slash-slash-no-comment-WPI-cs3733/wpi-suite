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
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import taskManager.TaskManager;
import taskManager.localization.Localizer;
import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.ActivityModelType;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.TaskModel.TaskCategory;
import taskManager.model.WorkflowModel;
import taskManager.view.ActivityView;
import taskManager.view.Colors;
import taskManager.view.EditTaskView;
import taskManager.view.EditTaskView.Mode;
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
	private final List<String> toRemove = new ArrayList<String>();
	private TaskModel model;
	private final ActivityController activityC;

	/**
	 * Creates an EditTaskController and EditTaskView form for a *new* Task.
	 *
	 */
	public EditTaskController() {
		activityC = new ActivityController(null, this);

		etv = new EditTaskView(Mode.CREATE, activityC, null);
		etv.setController(this);
		etv.setFieldController(new TaskInputController(etv));

		etv.setCategories(Colors.CATEGORY_NAMES);
		// set the drop down to say "select category"
		etv.setSelectedCategory(Colors.CATEGORY_NAMES[0]);

		// Disable save button when creating a task.
		etv.setSaveEnabled(false);

		// Clear all activities, reset fields.
		etv.resetFields();

		// fills the user lists
		final List<String> projectUserNames = new ArrayList<String>();
		for (String u : TaskManager.users) {
			if (!projectUserNames.contains(u)) {
				projectUserNames.add(u);
			}
		}
		etv.getProjectUsersList().addAllToList(projectUserNames);

		TabPaneController.getInstance().addTab("CreateTask", etv, true, true);

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
		this.model = model;
		activityC = new ActivityController(model, this);

		etv = new EditTaskView(Mode.EDIT, activityC, model.getID());
		etv.setName(model.getName());
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

		TabPaneController.getInstance().addEditTaskTab(etv);

		// populates the project users list
		final List<String> projectUserNames = new ArrayList<String>();
		for (String u : TaskManager.users) {
			if (!projectUserNames.contains(u)
					&& !model.getAssigned().contains(u)) {
				projectUserNames.add(u);
			}
		}
		etv.getProjectUsersList().addAllToList(projectUserNames);

		// populates the assigned users panel
		final List<String> assignedUserNames = new ArrayList<String>();
		for (String u : model.getAssigned()) {
			if (!assignedUserNames.contains(u)) {
				assignedUserNames.add(u);
			}
		}
		etv.getUsersList().addAllToList(assignedUserNames);

		// Disable save button until user starts making edits.
		etv.setSaveEnabled(false);

		// makes the archive button clickable
		etv.enableArchive();

		etv.checkArchive(model.isArchived());

		etv.setDeleteEnabled(model.isArchived());

		etv.setCategories(Colors.CATEGORY_NAMES);
		// set the category in the drop down to "select category"
		etv.setSelectedCategory(Colors.CATEGORY_NAMES[0]);
		int i = 0;
		for (TaskCategory c : TaskCategory.values()) {
			if (c.equals(model.getCategory())) {
				etv.setSelectedCategory(Localizer
						.getString(Colors.CATEGORY_NAMES[i]));
			}
			i++;
		}
		this.reloadData();
		etv.setSelectedStage(model.getStage().getName());
		// set the requirement dropdown
		if (model.getReq() != null) {
			etv.setSelectedRequirement(model.getReq().getName());
		} else {
			etv.setSelectedRequirement(EditTaskView.NO_REQ);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Object button = e.getSource();
		if (button instanceof JButton) {
			final String name = ((JButton) button).getName();

			switch (name) {

			case EditTaskView.SAVE:
				if (etv.getFieldController().checkEditFields()) {
					// if editing
					if (isEditingTask()) {
						save();
					}
					// if creating a new task
					else {
						// grabs the stage from the dropdown box
						final StageModel desiredStage = WorkflowModel
								.getInstance().findStageByName(
										etv.getSelectedStage());

						// creates a new task model
						model = new TaskModel(etv.getTitleText(), desiredStage);
						etv.setViewID(model.getID());
						// add pending activities/comments
						for (ActivityModel act : activityC.getActivities()) {
							model.addActivity(act);
						}
						save();
					}
				} else {
					etv.setSaveEnabled(false);
				}

				break;

			case EditTaskView.DELETE:
				final Integer choice = JOptionPane.showConfirmDialog(etv,
						Localizer.getString("DeleteWarning"),
						Localizer.getString("DeleteWarningTitle"),
						JOptionPane.YES_NO_OPTION);
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
						currentStage.removeTask(model);
						etv.resetFields();

						WorkflowModel.getInstance().save();
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

				final Requirement requirement = RequirementModel.getInstance()
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
				final JTabbedPane tabPane = (JTabbedPane) c;

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
			case EditTaskView.CANCEL_COMMENT:
				etv.doneEditingComment();
				activityC.setEditedTask(null);
				break;
			case EditTaskView.SUBMIT_COMMENT:
				// the user is currently editing a comment
				if (activityC.getEditedTask() != null) {
					activityC.getEditedTask().getModel()
							.setDescription(etv.getCommentsFieldText());
					// reset
					activityC.setEditedTask(null);
				}
				// the user is creating a new comment
				else {
					final ActivityModel comment = new ActivityModel(
							ActivityModelType.COMMENT,
							etv.getCommentsFieldText());
					// add the activity
					activityC.addActivity(comment);
					activityC.scrollActivitiesToBottom();
				}
				etv.doneEditingComment();
				WorkflowModel.getInstance().save();
				break;
			case ActivityView.EDIT:
				activityC.setEditedTask((ActivityView) SwingUtilities
						.getAncestorOfClass(ActivityView.class,
								(JButton) button));

				etv.startEditingComment(((ActivityView) SwingUtilities
						.getAncestorOfClass(ActivityView.class,
								(JButton) button)).getComment());
				break;
			}
		}
	}

	/**
	 * refreshes the data on the view
	 */
	public void reloadData() {

		final List<String> stageNames = new ArrayList<String>();
		String selectedStage = etv.getSelectedStage();
		for (StageModel stage : WorkflowModel.getInstance().getStages()) {
			stageNames.add(stage.getName());
			// grab the stage name of the stage the model is in
			if (model != null && model.getStage().equals(stage)) {
				selectedStage = stage.getName();
			}
		}
		etv.setStages(stageNames);
		if (selectedStage != null) {
			etv.setSelectedStage(selectedStage);
		}

		final List<Requirement> reqs = RequirementModel.getInstance()
				.getRequirements();
		final String selectedReq = etv.getSelectedRequirement();

		final List<String> reqNames = new ArrayList<String>();
		if (reqs != null) {
			for (Requirement req : reqs) {
				reqNames.add(req.getName());
			}
		}
		etv.setRequirements(reqNames);
		if (selectedReq != null) {
			etv.setSelectedRequirement(selectedReq);
		}

		if (model != null) {
			etv.checkArchive(model.isArchived());
		}

		etv.getFieldController().validate();
	}

	/**
	 * switches back to workflow view
	 */
	private void returnToWorkflowView() {

		TabPaneController.getInstance().removeTabByComponent(etv);
		WorkflowController.getInstance().removeTaskInfos(false);
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

		final StageModel s = WorkflowModel.getInstance().findStageByName(
				etv.getSelectedStage());
		final Requirement r = RequirementModel.getInstance()
				.getRequirementByName(etv.getSelectedRequirement());

		for (int i = 0; i < TaskCategory.values().length; i++) {
			if (etv.getSelectedCategory().equals(
					Localizer.getString(Colors.CATEGORY_NAMES[i]))) {
				model.setCategory(TaskCategory.values()[i]);
			}
		}

		// Try to set the effort values.
		try {
			model.setEstimatedEffort(Integer.parseInt(etv.getEstEffort()));
		} catch (java.lang.NumberFormatException e2) {
			// Set to null.
			model.setEstimatedEffort(null);
		}

		try {
			model.setActualEffort(Integer.parseInt(etv.getActEffort()));
		} catch (java.lang.NumberFormatException e2) {
			model.setActualEffort(null);
		}

		// sets the due date from the calendar
		model.setDueDate(etv.getDate());

		// move the stage
		s.addTask(model);

		// adds or removes users
		for (String name : etv.getUsersList().getAllValues()) {
			if (!model.getAssigned().contains(name)) {
				model.addAssigned(name);
			}
		}
		for (String n : toRemove) {
			if (model.getAssigned().contains(n)) {
				model.removeAssigned(n);
			}
		}
		model.setReq(r);

		// Set archived to checkbox value.
		model.setArchived(etv.isArchived());

		// exit the edit view, this refreshes the workflow
		this.returnToWorkflowView();
		// makes all the fields blank again
		etv.resetFields();

		WorkflowModel.getInstance().save();
	}

	/**
	 * adds selected usernames to the assigned users list and removes them from
	 * the project user list.
	 */
	public void addUsersToList() {
		final int[] toAdd = etv.getProjectUsersList().getSelectedIndices();
		final List<String> namesToAdd = new ArrayList<String>();

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
				if (toRemove.contains(n1)) {
					toRemove.remove(n1);
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
		final int[] usersToRemove = etv.getUsersList().getSelectedIndices();
		final List<String> namesToRemove = new ArrayList<String>();
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
				if (!toRemove.contains(n2)) {
					toRemove.add(n2);
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
			if (!(etv.getTitleText().isEmpty()
					&& etv.getDescription().isEmpty()
					&& !checkDate(null)
					&& etv.getSelectedStage().equals(
							WorkflowModel.getInstance().getStages().get(0)
									.getName()) && !checkUsers(null)
					&& etv.getEstEffort().isEmpty()
					&& etv.getActEffort().isEmpty()
					&& (etv.getSelectedRequirement() == null) && !etv
						.isArchived())) {
				edited = true;
			}
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

		// Category.
		else if (checkCategories()) {
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
		// Archived.
		else if (model.isArchived() != etv.isArchived()) {
			edited = true;
		}
		return edited;
	}

	/**
	 * Checks if the date in the view and on the task are the same
	 *
	 * @param task
	 *            The task to check against
	 * @return true if there are edits
	 */
	public Boolean checkDate(TaskModel task) {
		if (etv.getDate() == null) {
			return false;
		}
		// if the task had a due date, check if it changed
		final Date dueDate;
		if (task != null) {
			dueDate = task.getDueDate();
		} else {
			dueDate = new Date();
		}

		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();

		cal1.setTime(dueDate);
		cal2.setTime(etv.getDate());

		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);

		return !sameDay;
	}

	/**
	 * returns whether or not the name assigned to the category of the task
	 * model matches the name selected in the view
	 * 
	 * @return true if they match, false if they don't
	 */
	public boolean checkCategories() {
		boolean hasChange = false;
		String modelCatName = null;
		for (int i = 0; i < TaskCategory.values().length; i++) {
			if (TaskCategory.values()[i].equals(model.getCategory())) {
				modelCatName = Colors.CATEGORY_NAMES[i];
			}
		}
		if (modelCatName != null) {
			if (etv.getSelectedCategory() != null) {
				if (!etv.getSelectedCategory().equals(
						Localizer.getString(modelCatName))) {
					hasChange = true;
				}
			}
		}
		return hasChange;
	}

	/**
	 * 
	 * Checks whether the users in the view and the users stored in the task are
	 * the same.
	 *
	 * @param task
	 *            The task to check with.
	 * @return true if there are edits.
	 */
	private boolean checkUsers(TaskModel task) {
		boolean edited = false;
		final Set<String> taskAssigned;
		if (task != null) {
			taskAssigned = task.getAssigned();
		} else {
			taskAssigned = new HashSet<String>();
		}
		final Set<String> usersAssigned = new HashSet<String>();
		usersAssigned.addAll(etv.getUsersList().getAllValues());

		if (!usersAssigned.equals(taskAssigned)) {
			edited = true;
		}
		if ((usersAssigned.size() == 0) && (taskAssigned == null)) {
			edited = false;
		}
		return edited;
	}

	/**
	 * 
	 * Checks whether the estimated effort value in the task and on the view are
	 * equivalent.
	 *
	 * @param task
	 *            The task to check if.
	 * @return true if there are edits.
	 */
	private boolean checkEstEffort(TaskModel task) {
		boolean edited = false;
		if (!task.isEstimatedEffortSet()) {
			if (etv.getEstEffort().isEmpty()) {
				edited = false;
			} else {
				edited = true;
			}
		} else {
			final Integer taskEffort = task.getEstimatedEffort();
			final Integer etvEffort;
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
	 * @param task
	 *            The task to check if.
	 * @return true if there are edits.
	 */
	private boolean checkActEffort(TaskModel task) {
		boolean edited = false;
		if (!task.isActualEffortSet()) {
			if (etv.getActEffort().isEmpty()) {
				edited = false;
			} else {
				edited = true;
			}
		} else {
			final Integer taskEffort = task.getActualEffort();
			final Integer etvEffort;
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
	 * @param task
	 *            The task to check if.
	 * @return true if there are edits.
	 */
	private boolean checkReq(TaskModel task) {
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
	public boolean isEditingTask() {
		// Sadly isn't equivalent to model == null;
		return Mode.EDIT.equals(etv.getMode());
	}

	/**
	 * 
	 * Returns whether this controller is editing a comment.
	 *
	 * @return Whether we are creating (false) or editing (true) a comment
	 */
	public boolean isEditingComment() {
		return activityC.getEditedTask() != null;
	}

	/**
	 * 
	 * Returns whether there is text in the comment textbox or not.
	 *
	 * @return true if there is text in the comment textbox, false otherwise
	 */
	public boolean isWritingComment() {
		return (isEditingComment() || (!etv.getCommentsFieldText().equals("")));
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

	/**
	 * If this controller and the other controller are duplicates (use the same
	 * model)
	 *
	 * @param other
	 * @return true if they're the same.
	 */
	public boolean isDuplicate(EditTaskController other) {
		if (isEditingTask() && model != null) {
			return model.getID().equals(other.model.getID());
		}
		// EditTaskControllers that are creating a task are always unique
		return false;
	}
}
