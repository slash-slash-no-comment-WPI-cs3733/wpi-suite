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

import taskManager.view.TabView;
import taskManager.view.Tab;
import taskManager.JanewayModule;

/**
 * A controller for the toolbar view
 *
 * @author Beth Martino
 */
public class ToolbarController implements ActionListener {

	private final TabView tv;
	private final TabController tc;

	/**
	 * 
	 * @param tv
	 *            tabView used to add tabs to the tab-bar
	 */
	public ToolbarController(TabView tv) {
		this.tv = tv;
		this.tc = JanewayModule.tabC;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();
			switch (name) {
			case "createTask":
				this.tc.addCreateTaskTab();
				break;
			case "manageStages":
				this.tc.addManageStagesTab();
				break;
			case "manageUsers":
				this.tc.addManageUsersTab();
				break;
			case "reports":
				break;

			case "refresh":
				workflowController.fetch();
			}
		}
	}

}
