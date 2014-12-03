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
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import taskManager.JanewayModule;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.model.WorkflowModel;
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
			case ToolbarView.MANAGE_STAGES:
				this.tabPaneC.addManageStagesTab();
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
			TaskView taskV;
			try {
				taskV = (TaskView) e.getTransferable().getTransferData(
						DDTransferHandler.getTaskFlavor());
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
					WorkflowModel.getInstance().save();
				}
				break;
			case ToolbarView.ARCHIVE:
				taskV.getController().setArchived(
						!taskV.getController().isArchived());
				// Reload and save workflow.
				JanewayModule.tabPaneC.getTabView().reloadWorkflow();
				WorkflowModel.getInstance().save();
				break;
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// Reload the workflow view.
		JanewayModule.tabPaneC.getTabView().reloadWorkflow();
	}
}
