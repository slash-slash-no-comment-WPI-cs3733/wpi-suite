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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import taskManager.JanewayModule;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DropAreaSaveListener;
import taskManager.model.FetchWorkflowObserver;
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

public class StageController implements DropAreaSaveListener, MouseListener,
		ActionListener {

	private final StageView view;
	private final StageModel model;

	public static Boolean anyChangeTitleOut = false;
	public Boolean thisChangeTitleOut = false;

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

		// Get state of archive shown check box.
		boolean showArchive = JanewayModule.toolV.isArchiveShown();

		// Add the tasks.
		for (TaskModel task : tasks) {
			// Add only if task is not archived or when task is archived and
			// archive shown is set to true.
			if (!task.isArchived() || (task.isArchived() && showArchive)) {
				// create stage view and controller.
				TaskView tkv = new TaskView(task.getName(), task.getDueDate(),
						task.getEstimatedEffort(), task.isArchived());
				tkv.setController(new TaskController(tkv, task));
				this.view.addTaskView(tkv);
			}
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

	/**
	 * 
	 * Changes which title is visible, the label or the textbox. If editable is
	 * true, the textbox is visible.
	 *
	 * @param editable
	 *            true to make the textbox visible, false to make the label
	 *            visible
	 */
	public void switchTitle(Boolean editable) {
		if (editable) {
			for (Component c : view.getComponents()) {
				if (c.getName() == StageView.TITLE) {
					c.setVisible(false);
				} else if (c.getName() == StageView.CHANGE_TITLE) {
					c.setVisible(true);
				}
			}
		} else {
			for (Component c : view.getComponents()) {
				if (c.getName() == StageView.TITLE) {
					c.setVisible(true);
				} else if (c.getName() == StageView.CHANGE_TITLE) {
					c.setVisible(false);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// only bring up the title textbox if nothing else has set
		// ignoreAllResponses
		if (!FetchWorkflowObserver.ignoreAllResponses && !anyChangeTitleOut) {
			// double clicked on the title
			if (e.getClickCount() == 2 && e.getSource() instanceof JLabel) {
				// Don't reload while changing a stage name is open.
				FetchWorkflowObserver.ignoreAllResponses = true;
				anyChangeTitleOut = true;
				thisChangeTitleOut = true;
				// bring up the title textbox
				switchTitle(true);
			}
		}
		// If there are no changeTitle textboxes out, clear the workflow
		else if (!StageController.anyChangeTitleOut) {
			// reset the flag
			FetchWorkflowObserver.ignoreAllResponses = false;
			// this will remove any changeTitle textboxes or taskInfo bubbles
			// from the workflow
			JanewayModule.tabPaneC.getTabView().getWorkflowController()
					.reloadData();
			JanewayModule.tabPaneC.getTabView().getWorkflowController()
					.repaintView();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {

			switch (((JButton) button).getName()) {
			case StageView.CHECK:
				try {
					model.changeStageName(view.getLabelText());
				} catch (IllegalArgumentException ex) {
					JOptionPane.showConfirmDialog(
							view,
							"Another stage already has the name "
									+ view.getLabelText()
									+ ". Please choose another name.",
							"Warning - Duplicate stage names",
							JOptionPane.CLOSED_OPTION);
					return;
				}
				// fall through
			case StageView.X:
				// reset the flags
				thisChangeTitleOut = false;
				FetchWorkflowObserver.ignoreAllResponses = false;
				// reload which will remove the textbox
				JanewayModule.tabPaneC.getTabView().getWorkflowController()
						.reloadData();
				break;
			}
		}

	}
}
