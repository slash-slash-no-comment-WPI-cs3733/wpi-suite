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

import javax.swing.JComboBox;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.TaskView;

/**
 * Controller for Tasks.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class TaskController implements ActionListener {

	private final TaskView view;
	private final TaskModel model;
	private StageModel sm;
	private WorkflowModel wfm;
	private final EditTaskView etv = JanewayModule.etv;

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
		sm = model.getStage();
		wfm = sm.getWorkflow();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// makes the delete button unclickable
		etv.enableDelete();

		// uses the title field to hold the unique id
		etv.getTitle().setName(model.getID());

		// uses description field to hold the name of the stage
		etv.getDescription().setName(model.getStage().getName());

		// populate editable fields with this tasks info
		etv.setTitle(model.getName());
		etv.setDescription(model.getDescription());
		etv.setDate(model.getDueDate());
		etv.setEstEffort(model.getEstimatedEffort());
		etv.setActEffort(model.getActualEffort());

		// changes the view from workflow to edit
		JanewayModule.wfv.setVisible(false);
		etv.setVisible(true);

		// figures out the index of the stage, then sets the drop down to the
		// stage at that index
		JComboBox<String> stages = etv.getStages();
		for (int i = 0; i < stages.getItemCount(); i++) {
			if (etv.getStages().getItemAt(i) == sm.getName()) {
				etv.setStageDropdown(i);
				break;
			}
		}

		// Enable stage dropdown when editing a task.
		etv.getStages().setSelectedItem(model.getStage());
		etv.setStageSelectorEnabled(true);

		// Enable save button when editing a task.
		etv.enableSave();

	}
}
