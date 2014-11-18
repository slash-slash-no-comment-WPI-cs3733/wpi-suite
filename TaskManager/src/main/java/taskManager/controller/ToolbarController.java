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
