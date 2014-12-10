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
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import taskManager.draganddrop.DDTransferHandler;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ReportsToolbarView;
import taskManager.view.StageView;
import taskManager.view.TaskView;
import taskManager.view.ToolbarView;

/**
 * A controller for the toolbar view
 *
 * @author Beth Martino
 * @author Sam Khalandovsky
 */
public class ToolbarController extends DropTargetAdapter implements
		ActionListener, ItemListener {

	private ToolbarView view;

	private static ToolbarController instance;

	/**
	 * Hide Singleton constructor
	 */
	private ToolbarController() {
		reset();
	}

	public void reset() {
		view = new ToolbarView(this);
	}

	/**
	 * Returns the singleton instance of ToolbarController. Creates one if
	 * needed.
	 * 
	 * @return the ToolbarController singleton
	 */
	public static ToolbarController getInstance() {
		if (instance == null) {
			instance = new ToolbarController();
		}
		return instance;
	}

	/**
	 * Returns the associated ToolbarView.
	 * 
	 * @return The associated ToolbarView
	 */
	public ToolbarView getView() {
		return view;
	}

	/**
	 * Set's the visible title in the toolbar
	 *
	 * @param title
	 */
	public void setProjectTitle(String title) {
		view.setTitle(title);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object button = e.getSource();
		if (button instanceof JButton) {
			final String name = ((JButton) button).getName();
			// close the task preview pane
			WorkflowController.getInstance().removeTaskInfos(true);
			switch (name) {
			case ToolbarView.CREATE_TASK:
				TabPaneController.getInstance().addCreateTaskTab();
				break;
			case ToolbarView.CREATE_STAGE:
				// add a new stage from workflow controller
				WorkflowController.getInstance().addStageToView();
				break;
			case ToolbarView.REPORT:
				ReportsToolbarView rtv = new ReportsToolbarView();
				rtv.setController(new ReportsManager(rtv));
				TabPaneController.getInstance().addReportsTab(rtv);
				break;
			}
		}
	}

	/**
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent e) {
		final Component target = e.getDropTargetContext().getComponent();
		if (target instanceof JLabel) {
			final String name = ((JLabel) target).getName();

			final Transferable trans = e.getTransferable();
			if (trans.isDataFlavorSupported(DDTransferHandler.getTaskFlavor())) {
				final TaskView taskV;
				try {
					taskV = (TaskView) trans.getTransferData(DDTransferHandler
							.getTaskFlavor());
				} catch (Exception ex) {
					System.out.println(ex.getStackTrace());
					return;
				}

				switch (name) {
				case ToolbarView.DELETE:
					if (target.isEnabled()) {
						taskV.getController().deleteTask(); // remove from model
						taskV.getParent().remove(taskV); // remove from view
						// Reload and save workflow.
						WorkflowController.getInstance().reloadData();
						WorkflowController.getInstance().repaintView();
						WorkflowModel.getInstance().save();
						DDTransferHandler.dragSaved = true;
					}
					break;
				case ToolbarView.ARCHIVE:
					taskV.getController().setArchived(
							!taskV.getController().isArchived());
					// Reload and save workflow.
					WorkflowController.getInstance().reloadData();
					WorkflowController.getInstance().repaintView();
					WorkflowModel.getInstance().save();
					DDTransferHandler.dragSaved = true;
					break;
				} // end switch
			} else if (trans.isDataFlavorSupported(DDTransferHandler
					.getStageFlavor())) {
				final StageView stageV;
				try {
					stageV = (StageView) trans
							.getTransferData(DDTransferHandler.getStageFlavor());
				} catch (Exception ex) {
					System.out.println(ex.getStackTrace());
					return;
				}
				final StageController stageC = stageV.getController();
				final WorkflowModel model = WorkflowModel.getInstance();
				final List<StageModel> stages = model.getStages();

				if (ToolbarView.DELETE.equals(name)) {
					// Delete only when there are 2 or more stages.
					if (stages.size() >= 2) {
						// If the stage has tasks, show a confirmation dialog,
						// else
						// just delete the stage.
						if (!stageC.isEmpty()) {
							final Integer choice = JOptionPane
									.showConfirmDialog(
											TabPaneController.getInstance()
													.getView(),
											"The "
													+ stageV.getName()
													+ " stage contains tasks. Are you sure you want to delete this stage?",
											"Warning - Deleting a stage containing tasks",
											JOptionPane.YES_NO_OPTION);
							if (choice.equals(JOptionPane.NO_OPTION)) {
								return;
							}
						}
						stageC.deleteStage();
						DDTransferHandler.dragSaved = true;
						model.save();
						WorkflowController.getInstance().reloadData();
					} else {
						JOptionPane.showConfirmDialog(TabPaneController
								.getInstance().getView(),
								"You cannot delete the last stage.",
								"Warning - Invalid stage deletion",
								JOptionPane.CLOSED_OPTION);
					}
				}
			}
		} // End instanceof
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// Reload the workflow view.
		WorkflowController.getInstance().reloadData();
	}
}
