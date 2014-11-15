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
import taskManager.view.EditTaskView.Mode;
import taskManager.view.EditTaskView;
import taskManager.view.TaskView;
import taskManager.controller.EditTaskController;
import taskManager.controller.TabController;

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
	private TabController tabC;

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
		this.tabC = JanewayModule.tabC;
		this.view = view;
		this.model = model;
		sm = model.getStage();
		wfm = sm.getWorkflow();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditTaskView etv = new EditTaskView(Mode.EDIT);
		tabC.addTaskTab(etv, Mode.EDIT);

		// uses the title field to hold the unique id
		etv.getTitle().setName(this.model.getID());

		// uses description field to hold the name of the stage
		etv.getDescription().setName(
				this.model.getStage().getName());

		// populate editable fields with this tasks info
		etv.setTitle(model.getName());
		etv.setDescription(model.getDescription());
		etv.setDate(model.getDueDate());
		etv.setEstEffort(model.getEstimatedEffort());
		etv.setActEffort(model.getActualEffort());


		// figures out the index of the stage, then sets the drop down to the
		// stage at that index

		List<StageModel> stages = wfm.getStages();
		for (int i = 0; i < stages.size(); i++) {
			if (stages.get(i) == this.sm) {
				etv.setStageDropdown(i);
				break;
			}
		}
	}
}
