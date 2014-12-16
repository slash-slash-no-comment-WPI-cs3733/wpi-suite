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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.jdesktop.swingx.prompt.PromptSupport;

import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DropAreaSaveListener;
import taskManager.localization.Localizer;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.TaskView;
import taskManager.view.WorkflowView;

/**
 * A controller for the workflow view
 *
 * @author Jon Sorrells
 * @author Stefan Alexander
 * @version November 9, 2014
 */

public class WorkflowController implements DropAreaSaveListener, MouseListener {

	private WorkflowView view;
	private WorkflowModel model;
	private boolean hasNewStageView;

	private static WorkflowController instance;
	public static boolean pauseInformation;

	/**
	 * Hide Singleton constructor
	 */
	private WorkflowController() {
		reset();
	}

	/**
	 * Reset for the WorkflowController, gets all the stages from the
	 * WorkflowView, creates the corresponding StageView and StageControllers,
	 * and adds the StageViews to the UI.
	 * 
	 * @param view
	 *            the corresponding WorkflowView object
	 */
	public void reset() {
		view = new WorkflowView(this);
		model = WorkflowModel.getInstance();
		model.reset();

		hasNewStageView = false;

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

		// clear the stages previously on the view
		this.removeChangeTitles();
		this.removeTaskInfos(false);
		hasNewStageView = false;
		for (Component c : view.getComponents()) {
			if (!(c instanceof TaskInfoPreviewView)) {
				view.remove(c);
			}
		}

		// get all the stages in this workflow
		final List<StageModel> stages = model.getStages();

		// and add them all to the view
		for (StageModel stage : stages) {
			// create stage view and controller.
			StageView stv = new StageController(stage).getView();

			// add stage view to workflow
			view.addStageView(stv);
		}

		// view needs to be repainted before we can find positions of components
		view.revalidate();
		view.repaint();

		// if this doesn't run in the EDT, it sometimes doesn't work
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// get the mouse position relative to the workflow
				Point p = view.getMousePosition();
				if (p != null) {
					Component mouseC = view.findComponentAt(p);
					// if we're over a TaskView, call mouse entered on it.
					while (mouseC != null) {
						if (mouseC instanceof TaskView) {
							// calling this on the TaskView's parent or children
							// does not work; it has to be on the TaskView
							// itself
							mouseC.dispatchEvent(new MouseEvent(mouseC,
									MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0, 0,
									false));
							break;
						}
						mouseC = mouseC.getParent();
					}
				}
			}
		});
	}

	/**
	 * Asks the model to pull from the server. When the server responds, model
	 * data is updated and reloadData is called.
	 *
	 */
	public void fetch() {
		WorkflowModel.update();
	}

	/**
	 * Requests the list of users from the server
	 *
	 */
	public void fetchUsers() {
		WorkflowModel.updateUsers();
	}

	/**
	 * Adds a new stage panel to the workflow view
	 */
	public void addStageToView() {
		if (!hasNewStageView) {
			hasNewStageView = true;
			final StageView newStageV = new StageController().getView();
			newStageV.getController().switchTitle(true);

			PromptSupport.setPrompt(Localizer.getString("NewStageName"),
					newStageV.getLabelField());
			PromptSupport.setFocusBehavior(
					PromptSupport.FocusBehavior.SHOW_PROMPT,
					newStageV.getLabelField());

			view.addStageView(newStageV);
			removeTaskInfos(false);

			view.revalidate();
			view.repaint();
		}
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

	@Override
	public void saveDrop(JPanel panel, int index) {
		// Make sure we cast safely
		if (!(panel instanceof StageView)) {
			return;
		}
		final StageController tc = ((StageView) panel).getController();
		final boolean changed = tc.moveStageToIndex(index);

		if (changed) {
			model.save();
			DDTransferHandler.dragSaved = true;
		}
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
	 * Removes all instances of TaskInfoPreviewView from the workflow. The
	 * repaint boolean is necessary because clicking from one task and then
	 * another to switch the taskInfo bubbles, does not call reload data to
	 * remove the previous bubble from the view.
	 *
	 * @param repaint
	 *            whether or not to repaint the Workflow View
	 */
	public void removeTaskInfos(Boolean repaint) {
		for (Component c : view.getComponents()) {
			if (c instanceof TaskInfoPreviewView) {
				view.remove(c);
				((TaskInfoPreviewView) c).getTaskController().taskInfoRemoved();
				((TaskInfoPreviewView) c).getTaskController().resetBackground();
			}
		}
		if (repaint) {
			// display without reloading
			this.repaintView();
		}
	}

	/**
	 * 
	 * Remove all instances of stage titles being changable.
	 *
	 */
	public void removeChangeTitles() {
		view.removeChangeTitles();
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
		// Removes the task info bubble from the screen
		WorkflowController.pauseInformation = false;
		removeTaskInfos(false);
		this.reloadData();
		this.repaintView();
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

	/**
	 * Returns the associated WorkflowView.
	 * 
	 * @return The associated WorkflowView
	 */
	public WorkflowView getView() {
		return view;
	}

	/**
	 * Returns the singleton instance of WorkflowController. Creates one if
	 * needed.
	 * 
	 * @return the WorkflowController singleton
	 */
	public static WorkflowController getInstance() {
		if (instance == null) {
			instance = new WorkflowController();
		}
		return instance;
	}
}
