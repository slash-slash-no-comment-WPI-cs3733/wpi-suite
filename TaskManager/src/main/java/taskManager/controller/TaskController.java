/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

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
public class TaskController implements MouseListener {

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
	public void mouseClicked(MouseEvent e) {
		// makes the delete button unclickable
		etv.enableDelete();

		// uses the title field to hold the unique id
		etv.getTitle().setName(model.getID());

		// uses description field to hold the name of the stage
		etv.getDescription().setName(model.getStage().getName());

		// populate editable fields with this tasks info
		JanewayModule.etv.setTitle(model.getName());
		JanewayModule.etv.setDescription(model.getDescription());
		JanewayModule.etv.setDate(model.getDueDate());
		JanewayModule.etv.setEstEffort(model.getEstimatedEffort());
		JanewayModule.etv.setActEffort(model.getActualEffort());

		JanewayModule.wfv.setVisible(false);
		JanewayModule.etv.setVisible(true);

		// figures out the index of the stage, then sets the drop down to the
		// stage at that index

		List<StageModel> stages = wfm.getStages();
		for (int i = 0; i < stages.size(); i++) {
			if (stages.get(i) == sm) {
				JanewayModule.etv.setStageDropdown(i);
				break;
			}
		}
	}
	
	public void MousePressed(MouseEvent e){
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	}

