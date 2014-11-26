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

import taskManager.model.GetUsersObserver;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.WorkflowView;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * A controller for the workflow view
 *
 * @author Jon Sorrells
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class WorkflowController implements MouseListener {

	private final WorkflowView view;
	private final WorkflowModel model;
	private TaskInfoPreviewView taskInfoV;

	public static boolean alive = true;

	/**
	 * Constructor for the WorkflowController, gets all the stages from the
	 * WorkflowView, creates the corresponding StageView and StageControllers,
	 * and adds the StageViews to the UI. Polls the server every 1 second until
	 * it receives the workflow model.
	 * 
	 * @param view
	 *            the corresponding WorkflowView object
	 * @param model
	 *            the corresponding WorkflowModel object
	 */
	public WorkflowController(WorkflowView view) {
		this.view = view;
		this.model = WorkflowModel.getInstance();

		Thread thread = new Thread() {
			public void run() {
				GetRequirementsController reqController = GetRequirementsController
						.getInstance();
				while (alive) {
					try {
						sleep(5000);
						fetch();
						reqController.retrieveRequirements();
						final Request request = Network.getInstance()
								.makeRequest("core/user", HttpMethod.GET);
						request.addObserver(new GetUsersObserver());
						request.send();
					} catch (NullPointerException e) {
						// this is expected, do nothing
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.setName("polling");
		thread.setDaemon(true);
		thread.start();

		reloadData();
	}

	/**
	 * Reloads all the data on the view to match the data in the model
	 *
	 */
	public synchronized void reloadData() {
		// clear the stages previously on the view
		view.removeAll();

		// get all the stages in this workflow
		final List<StageModel> stages = model.getStages();
		// and add them all to the view
		for (StageModel stage : stages) {
			// create stage view and controller.
			StageView stv = new StageView(stage.getName());
			stv.setController(new StageController(stv, stage));

			// add stage view to workflow
			view.addStageView(stv);
		}
		// TODO: Check if taskInfoV's task is in the workflow and don't show if
		// it isn't (been deleted)
		if (taskInfoV != null) {
			view.addTaskInfo(taskInfoV);
			// taskInfoV.getTaskController().setToHoverColor();
		}

		view.revalidate();
		view.repaint();
	}

	/**
	 * Asks the model to pull from the server. When the server responds, model
	 * data is updated and reloadData is called.
	 *
	 */
	public void fetch() {
		model.update(this);
	}

	/**
	 * 
	 * Returns the workflow model.
	 *
	 * @return the WorkflowModel
	 */
	public WorkflowModel getModel() {
		return model;
	}

	public void setTaskInfo(TaskInfoPreviewView ti) {
		if (this.taskInfoV == null) {
			this.view.removeTaskInfos();
		}
		this.taskInfoV = ti;
		this.reloadData();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Removes the task info bubble from the screen
		this.setTaskInfo(null);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing

	}
}
