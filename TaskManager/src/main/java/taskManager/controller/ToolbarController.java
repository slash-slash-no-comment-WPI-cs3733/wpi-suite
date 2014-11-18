/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import taskManager.JanewayModule;
import taskManager.view.TabPaneView;
import taskManager.view.ToolbarView;

/**
 * A controller for the toolbar view
 *
 * @author Beth Martino
 */
public class ToolbarController implements ActionListener {

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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();
			switch (name) {
			case ToolbarView.CREATE_TASK:

				this.tabPaneC.addCreateTaskTab();

	stuff here

				// Set the dropdown menu to New stage and disable the menu.
		//		StageModel newStage = workflowModel.findStageByName("New");
		//		List<StageModel> stages = workflowModel.getStages();
			//	for (int i = 0; i < stages.size(); i++) {
				//	if (stages.get(i) == newStage) {
				//		JanewayModule.etv.setStageDropdown(i);
					//	break;
				//	}
				//}

				
					//JanewayModule.etv.getActEffort().setEnabled(false);

				//JanewayModule.etv.setStageSelectorEnabled(false);

				// Disable save button when creating a task.
				//JanewayModule.etv.disableSave();

				break;
			case ToolbarView.MANAGE_STAGES:
				this.tabPaneC.addManageStagesTab();
				break;
			case ToolbarView.MANAGE_USERS:
				this.tabPaneC.addManageUsersTab();
				break;
			case ToolbarView.REPORT:
				break;

			case ToolbarView.REFRESH:
				tabPaneV.refreshWorkflow();
				break;
			}
		}
	}

}
