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
import taskManager.view.WorkflowView;
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
	private final WorkflowView wfv;

	private String taskID;

	/**
	 * Constructor, attaches the edit task view to this controller
	 * 
	 * @param wfm
	 *            The workflowModel that belongs to this controller.
	 */
	public EditTaskController(WorkflowModel wfm) {
		etv = JanewayModule.etv;
		this.wfm = wfm;
		wfv = JanewayModule.wfv;

		reloadData();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();

			taskID = etv.getTitle().getName();

			// check to see if the task exists in the workflow
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

			// grabs the stage from the dropdow box
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
					// moves the task to that stage on the model level
					wfm.moveTask(task, currentStage, desiredStage);
					wfm.save();

					this.setTaskID("000000");
				}
				// if creating a new task
				else {
					// creates a new task model
					task = new TaskModel(etv.getTitle().getText(), currentStage);
					this.setTaskData(task, wfm.findStageByName("New"),
							requirement);
				}

				// Added the newly added activities.
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
				StageModel s = wfm.findStageByName((String) etv.getStages()
						.getSelectedItem());
				task = s.findTaskByID(taskID);
				s.getTasks().remove(task);
				// Save entire workflow whenever a task is deleted
				wfm.save();
				this.returnToWorkflowView();
				etv.resetFields();
				break;

			case EditTaskView.ADD_USER:
				// add a user to this task
				System.out.println("You've pressed the add user button");
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
				this.returnToWorkflowView();
				etv.resetFields();
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
		etv.setVisible(false);
		wfv.setVisible(true);
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
		t.setName(etv.getTitle().getText());
		t.setDescription(etv.getDescription().getText());
		t.setEstimatedEffort(Integer.parseInt(etv.getEstEffort().getText()));
		try {
			t.setActualEffort(Integer.parseInt(etv.getActEffort().getText()));
		} catch (java.lang.NumberFormatException e2) {
			// TODO: handle error
		}
		t.setDueDate(etv.getDateField().getDate());
		t.setStage(s);
		t.setReq(r);

	}

}
