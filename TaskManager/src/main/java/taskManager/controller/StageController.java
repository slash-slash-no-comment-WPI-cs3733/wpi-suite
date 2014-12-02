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

import javax.swing.JPanel;

import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DropAreaSaveListener;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.TaskView;

/**
 * Controller for stages.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class StageController implements DropAreaSaveListener {

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
					task.getEstimatedEffort());
			tkv.setController(new TaskController(tkv, task));
			this.view.addTaskView(tkv);
		}

	}

	/*
	 * @see
	 * taskManager.draganddrop.DropAreaSaveListener#saveDrop(javax.swing.JPanel,
	 * int)
	 */
	@Override
	public void saveDrop(JPanel panel, int index) {
		// Make sure we cast safely
		if (!(panel instanceof TaskView)) {
			return;
		}
		TaskController tc = ((TaskView) panel).getController();
		boolean changed = tc.moveToStage(model, index);

		if (changed) {
			WorkflowModel.getInstance().save();
			DDTransferHandler.dragSaved = true;
		}

	}

	/**
	 * Move associated stage to index in workflow
	 *
	 * @param index
	 *            index to be moved to
	 * @return whether the workflow changed as a result
	 */
	public boolean moveStageToIndex(int index) {
		return WorkflowModel.getInstance().addStage(model, index);

	}

}
