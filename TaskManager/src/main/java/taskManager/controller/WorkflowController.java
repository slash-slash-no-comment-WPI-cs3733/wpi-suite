/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import taskManager.model.FetchWorkflowObserver;
import taskManager.model.GetUsersObserver;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.WorkflowView;

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

	/**
	 * Constructor for the WorkflowController, gets all the stages from the
	 * WorkflowView, creates the corresponding StageView and StageControllers,
	 * and adds the StageViews to the UI.
	 * 
	 * @param view
	 *            the corresponding WorkflowView object
	 */
	public WorkflowController(WorkflowView view) {
		this.view = view;
		this.model = WorkflowModel.getInstance();

		reloadData();

		view.addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorRemoved(AncestorEvent event) {
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
				if (SwingUtilities.getWindowAncestor(view) != null) {
					SwingUtilities.getWindowAncestor(view).addWindowListener(
							new WindowAdapter() {

								@Override
								public void windowClosing(WindowEvent we) {
									WorkflowModel.dispose();
								}
							});
					view.removeAncestorListener(this);
				}
			}
		});
	}

	/**
	 * Reloads all the data on the view to match the data in the model
	 *
	 */
	public synchronized void reloadData() {
		// Only reloadData if you are getting new information from the serv
		if (!FetchWorkflowObserver.ignoreAllResponses) {
			// clear the stages previously on the view
			this.removeTaskInfos();
			this.removeChangeTitles();
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
			view.revalidate();
		}
	}

	/**
	 * Asks the model to pull from the server. When the server responds, model
	 * data is updated and reloadData is called.
	 *
	 */
	public void fetch() {
		model.update();
	}

	public void fetchUsers() {
		model.updateUsers();
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

	/**
	 * 
	 * Adds the taskInfo bubble to the workflow.
	 *
	 * @param ti
	 *            The TaskInfoPreviewView that should be displayed
	 */
	public void setTaskInfo(TaskInfoPreviewView ti) {
		if (ti != null) {
			view.addTaskInfo(ti);
		}
	}

	/**
	 * 
	 * Removes all instances of TaskInfoPreviewView from the workflow.
	 *
	 */
	public void removeTaskInfos() {
		for (Component c : view.getComponents()) {
			if (c instanceof TaskInfoPreviewView) {
				view.remove(c);
				((TaskInfoPreviewView) c).getTaskController().thisTaskInfoOut = false;
				((TaskInfoPreviewView) c).getTaskController().resetBackground();
			}
			TaskController.anyTaskInfoOut = false;
		}
		// display without reloading
		this.repaintView();
	}

	/**
	 * 
	 * Remove all instances of stage titles being changable.
	 *
	 */
	public void removeChangeTitles() {
		for (Component c : view.getComponents()) {
			if (c instanceof StageView) {
				((StageView) c).getController().thisChangeTitleOut = false;
			}
		}
		StageController.anyChangeTitleOut = false;
	}

	/**
	 * 
	 * repaints the WorkflowView.
	 *
	 */
	public void repaintView() {
		view.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!StageController.anyChangeTitleOut) {
			// Removes the task info bubble from the screen
			FetchWorkflowObserver.ignoreAllResponses = false;
			this.reloadData();
		}
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
