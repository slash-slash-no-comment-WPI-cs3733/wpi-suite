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

import javax.swing.JButton;
import javax.swing.JComboBox;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.WorkflowView;

/**
 * The controller for editing and creating new tasks
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

			StageModel desiredStage = wfm.findStageByName((String) etv
					.getStages().getSelectedItem());

			switch (name) {

			case "save":
				// find the appropriate stage
				// create new task

				if (exists) {

					// set the task to be edited
					TaskModel t = currentStage.findTaskByID(taskID);

					// updates text fields
					t.setName(etv.getTitle().getText());
					t.setDescription(etv.getDescription().getText());
					t.setEstimatedEffort(Integer.parseInt(etv.getEstEffort()
							.getText()));
					t.setActualEffort(Integer.parseInt(etv.getActEffort()
							.getText()));

					t.setDueDate(etv.getDate().getDate());

					// grabs the correct stage model from the workflow model and
					// moves the task to that stage
					wfm.moveTask(t, currentStage, desiredStage);
					t.setStage(desiredStage);
					this.returnToWorkflowView();
					this.setTaskID("000000");
				} else {

					// creates a new task model
					TaskModel task = new TaskModel(etv.getTitle().getText(),
							currentStage);

					// sets all task values according to fields
					task.setDueDate(etv.getDate().getDate());
					task.setEstimatedEffort(Integer.parseInt(etv.getEstEffort()
							.getText()));
					String actEffort = etv.getActEffort().getText();
					try {
						task.setActualEffort(Integer.parseInt(actEffort));
					} catch (java.lang.NumberFormatException e2) {
						// TODO: handle error
					}
					task.setDescription(etv.getDescription().getText());
				}

				// makes all the fields blank again
				etv.resetFields();
				// exit the edit view, this refreshes the workflow
				this.returnToWorkflowView();

				// Save entire workflow whenever a task is saved
				wfm.save();
				break;

			case "delete":
				// delete this task
				StageModel s = wfm.findStageByName((String) etv.getStages()
						.getSelectedItem());
				TaskModel task = s.findTaskByID(taskID);
				s.getTasks().remove(task);
				this.returnToWorkflowView();
				etv.resetFields();

				// Save entire workflow whenever a task is deleted
				wfm.save();
				break;

			case "addUser":
				// add a user to this task
				System.out.println("You've pressed the add user button");
				break;

			case "addReq":
				// add a requirement to this task
				System.out.println("You've pressed the add requirement button");
				break;

			case "cancel":
				// go back to workflow view
				this.returnToWorkflowView();
				etv.resetFields();
				break;

			case "submitComment":
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

}
