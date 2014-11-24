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

import taskManager.JanewayModule;

/**
 * Description
 *
 * @author Samee Swartz
 * @version Nov 21, 2014
 */
public class TaskInfoPreviewController implements ActionListener {
	private TaskController taskC;

	public TaskInfoPreviewController(TaskController taskC) {
		this.taskC = taskC;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		taskC.editTask();
		JanewayModule.tabPaneC.getTabView().getWorkflowController()
				.setTaskInfo(null);
	}
}
