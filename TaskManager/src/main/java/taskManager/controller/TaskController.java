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
import java.util.List;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
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
		this.sm = model.getStage();
		this.wfm = sm.getWorkflow();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// sets the text fields to the values of the task

		JanewayModule.etv.getTitle().setName(this.model.getID());
		JanewayModule.etv.setTitle(model.getName());
		JanewayModule.etv.setDescription(model.getDescription());
		JanewayModule.etv.setDate(model.getDueDate());
		JanewayModule.etv.setEstEffort(model.getEstimatedEffort());
		JanewayModule.etv.setActEffort(model.getActualEffort());

		// figures out the index of the stage, then sets the drop down to the
		// stage at that index
		List<StageModel> x = this.wfm.getStages();
		for (int i = 0; i == x.size(); i++) {
			if (x.get(i) == this.sm) {
				JanewayModule.etv.setStageDropdown(i);
			}
		}
		JanewayModule.wfv.setVisible(false);
		JanewayModule.etv.setVisible(true);
	}
}
