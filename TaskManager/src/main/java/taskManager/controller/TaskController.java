/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import taskManager.model.TaskModel;
import taskManager.view.TaskView;

/**
 * Controller for Tasks.
 *
 * @author Stefan Alexander
 */
public class TaskController {
	
	private final TaskView view;
	private final TaskModel model;

	/**
	 * Constructor
	 *
	 */
	public TaskController(TaskView view, TaskModel model) {
		this.view = view;
		this.model = model;
	}

}
