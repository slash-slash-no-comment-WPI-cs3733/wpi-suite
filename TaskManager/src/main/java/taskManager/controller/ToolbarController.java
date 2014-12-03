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

import taskManager.JanewayModule;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.TabPaneView;
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

	private final TabPaneView tabPaneV;
	private final TabPaneController tabPaneC;

	/**
	 * 
	 * @param tabV
	 *            tabView used to add tabs to the tab-bar
	 */
	public ToolbarController(TabPaneView tabV) {
		this.tabPaneV = tabV;
		this.tabPaneC = JanewayModule.tabPaneC;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();
			switch (name) {
			case ToolbarView.CREATE_TASK:
				this.tabPaneC.addCreateTaskTab();
				break;
			case ToolbarView.CREATE_STAGE:
				// add a new stage from workflow controller
				tabPaneV.getWorkflowController().addStageToView();
				break;
			case ToolbarView.REPORT:
				break;

			case ToolbarView.REFRESH:
				tabPaneV.refreshWorkflow();
				break;
			}
		}
	}

	/**
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent e) {
		Component target = e.getDropTargetContext().getComponent();
		if (target instanceof JLabel) {
			String name = ((JLabel) target).getName();

			Transferable trans = e.getTransferable();
			if (trans.isDataFlavorSupported(DDTransferHandler.getTaskFlavor())) {
				TaskView taskV;
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
						JanewayModule.tabPaneC.getTabView().reloadWorkflow();
						JanewayModule.tabPaneC.getTabView()
								.getWorkflowController().repaintView();
						WorkflowModel.getInstance().save();
						DDTransferHandler.dragSaved = true;
					}
					break;
				case ToolbarView.ARCHIVE:
					taskV.getController().setArchived(
							!taskV.getController().isArchived());
					// Reload and save workflow.
					JanewayModule.tabPaneC.getTabView().reloadWorkflow();
					JanewayModule.tabPaneC.getTabView().getWorkflowController()
							.repaintView();
					WorkflowModel.getInstance().save();
					DDTransferHandler.dragSaved = true;
					break;
				}
			} else if (trans.isDataFlavorSupported(DDTransferHandler
					.getStageFlavor())) {
				StageView stageV;
				try {
					stageV = (StageView) trans
							.getTransferData(DDTransferHandler.getStageFlavor());
				} catch (Exception ex) {
					System.out.println(ex.getStackTrace());
					return;
				}
				StageController stageC = stageV.getController();
				WorkflowModel model = WorkflowModel.getInstance();
				List<StageModel> stages = model.getStages();

				if (name == ToolbarView.DELETE) {
					// Delete only when there are 2 or more stages.
					if (stages.size() >= 2) {
						// If the stage has tasks, show a confirmation dialog,
						// else
						// just delete the stage.
						if (!stageC.isEmpty()) {
							Integer choice = JOptionPane
									.showConfirmDialog(
											tabPaneV,
											"The "
													+ stageV.getName()
													+ " stage contains tasks. Are you sure you want to delete this stage?",
											"Warning - Deleting a stage containing tasks",
											JOptionPane.YES_NO_OPTION);
							if (choice.equals(JOptionPane.NO_OPTION)) {
								return;
							}
						}
					}
					stageC.deleteStage();
					DDTransferHandler.dragSaved = true;
					model.save();
					tabPaneC.getTabView().reloadWorkflow();
				}

			}

		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// Reload the workflow view.
		JanewayModule.tabPaneC.getTabView().reloadWorkflow();
	}
}
