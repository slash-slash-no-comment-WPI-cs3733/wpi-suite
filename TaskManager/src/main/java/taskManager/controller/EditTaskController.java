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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

	/**
	 * Constructor, attaches the edit task view to this controller
	 * 
	 * @param wfm
	 *            The workflowModel that belongs to this controller.
	 */
	public EditTaskController(EditTaskView etv) {
		this.etv = etv;
		this.wfm = WorkflowModel.getInstance();

		reloadData();
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
			StageModel currentStage = wfm.findStageByName("New");
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
				// if editing
				if (exists) {
					// set the task to be edited
					task = currentStage.findTaskByID(taskID);
					this.setTaskData(task, desiredStage, requirement);

					// Move task if stages are not equal.
					if (!currentStage.getName().equals(desiredStage.getName())) {
						wfm.moveTask(task, currentStage, desiredStage);
					}

					this.setTaskID("000000");
				}
				// if creating a new task
				else {
					// creates a new task model
					task = new TaskModel(etv.getTitle().getText(), desiredStage);
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
				break;

			case EditTaskView.DELETE:
				// delete this task
				task = currentStage.findTaskByID(taskID);
				currentStage.getTasks().remove(task);
				etv.resetFields();

				// Save entire workflow whenever a task is deleted
				wfm.save();
				returnToWorkflowView();
				break;

			case EditTaskView.ADD_USER:
				// add a user to this task
				if (!etv.getProjectUsersList().isSelectionEmpty()) {
					String toAdd = etv.getProjectUsersList().getSelectedValue();
					if (!etv.getUsersList().contains(toAdd)) {
						etv.getUsersList().addToList(toAdd);
						etv.getProjectUsersList().removeFromList(toAdd);
					}
				}

				break;

			case EditTaskView.REMOVE_USER:
				// add a user to this task
				if (!etv.getUsersList().isSelectionEmpty()) {
					int indexToRemove = etv.getUsersList().getSelectedIndex();
					String nameToRemove = etv.getUsersList().getSelectedValue();
					if (!etv.getProjectUsersList().contains(nameToRemove)) {
						etv.getUsersList().removeFromList(indexToRemove);
						etv.getProjectUsersList().addToList(nameToRemove);
					}
					this.toRemove.add(nameToRemove);
				}
				break;

			case EditTaskView.VIEW_REQ:
				// view requirement in requirement manager

				if (requirement == null) {
					// TODO: warn user that no requirement is selected
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
		JanewayModule.tabPaneC.reloadWorkflow();
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

		try {
			t.setEstimatedEffort(Integer.parseInt(etv.getEstEffort().getText()));
		} catch (java.lang.NumberFormatException e2) {
			// TODO: handle error
		}

		try {
			t.setActualEffort(Integer.parseInt(etv.getActEffort().getText()));
		} catch (java.lang.NumberFormatException e2) {
			// TODO: handle error
		}

		// sets the due date from the calendar
		t.setDueDate(etv.getDateField().getDate());
		// sets the stage from the dropdown
		t.setStage(s);

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

}
