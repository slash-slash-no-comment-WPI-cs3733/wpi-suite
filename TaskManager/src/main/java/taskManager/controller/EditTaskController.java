/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.WorkflowView;

/**
 * The controller for editing and creating a new task
 * 
 * @author Beth Martino
 *
 */
public class EditTaskController implements ActionListener {

	private final EditTaskView etv;
	private final WorkflowModel wfm;

	private String taskID;

	/**
	 * Constructor, attaches the edit task view to this controller
	 * 
	 * @param wfm
	 *            The workflowModel that belongs to this controller.
	 */
	public EditTaskController(WorkflowModel wfm, EditTaskView etv) {
		this.etv = etv;
		this.wfm = wfm;

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

			switch (name) {

			case EditTaskView.SAVE:
				// if editing
				if (exists) {
					// set the task to be edited

					TaskModel task = currentStage.findTaskByID(taskID);
					this.setTaskData(task, desiredStage);
					// moves the task to that stage on the model level
					wfm.moveTask(task, currentStage, desiredStage);
					wfm.save();

					this.setTaskID("000000");
				}
				// if creating a new task
				else {
					// creates a new task model
					TaskModel task = new TaskModel(etv.getTitle().getText(),
							currentStage);
					this.setTaskData(task, wfm.findStageByName("New"));
				}

				// exit the edit view, this refreshes the workflow
				this.returnToWorkflowView();
				// makes all the fields blank again
				etv.resetFields();
				// Save entire workflow whenever a task is saved
				wfm.save();
				break;

			case "delete":
				// delete this task
				StageModel s = wfm.findStageByName((String) etv.getStages()
						.getSelectedItem());
				TaskModel task = s.findTaskByID(taskID);
				s.getTasks().remove(task);
				etv.resetFields();

				// Save entire workflow whenever a task is deleted
				wfm.save();
				break;

			case EditTaskView.ADD_USER:
				// add a user to this task
				System.out.println("You've pressed the add user button");
				break;

			case EditTaskView.ADD_REQ:
				// add a requirement to this task
				System.out.println("You've pressed the add requirement button");
				break;

			case EditTaskView.CANCEL:
				// go back to workflow view
				etv.resetFields();
				break;

			case EditTaskView.SUBMIT_COMMENT:
				// creates a new activity
				System.out.println("You've pressed the submit comment button");
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
	}

	/**
	 * switches back to workflow view
	 */
	private void returnToWorkflowView() {
		JanewayModule.tabPaneC.removeTabByComponent(etv);
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
	private void setTaskData(TaskModel t, StageModel s) {
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
		t.save();
	}

}
