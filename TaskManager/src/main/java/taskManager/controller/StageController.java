/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.util.List;

import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.view.StageView;
import taskManager.view.TaskView;

/**
 * Controller for stages.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class StageController {

	private final StageView view;
	private final StageModel model;

	/**
	 * Constructor for the StageController gets all the tasks from the
	 * StageModel, creates the corresponding TaskView and TaskControllers for
	 * each, and final adds all of the TaskViews to the UI.
	 * 
	 * @param view
	 *            the corresponding StageView object
	 * @param model
	 *            the corresponding StageModel object
	 */
	public StageController(StageView view, StageModel model) {
		this.view = view;
		this.model = model;

		// Get all the tasks associated with this Stage.
		final List<TaskModel> tasks = this.model.getTasks();

		// Add the tasks.
		for (TaskModel task : tasks) {
			// create stage view and controller.
			TaskView tkv = new TaskView(task.getName(), task.getDueDate(),
					task.getEstimatedEffort(), task.getID());
			tkv.setController(new TaskController(tkv, task));
			this.view.addTaskView(tkv);
		}

	}

	public StageModel getModel() {
		return model;
	}

}
