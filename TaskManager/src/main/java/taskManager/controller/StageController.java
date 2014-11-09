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

/**
 * Controller for stages.
 *
 * @author Stefan Alexander
 */
public class StageController {
	
	private final StageView view;
	private final StageModel model;

	/**
	 * Constructor
	 */
	public StageController(StageView view, StageModel model) {
		this.view = view;
		this.model = model;
		
		// Get all the tasks associated with this Stage.
		List<TaskModel> tasks = this.model.getTasks();
		
		// Add the tasks.
		for (TaskModel task : tasks) {
			this.view.addTaskView(task.getName());
		}
	}

}
