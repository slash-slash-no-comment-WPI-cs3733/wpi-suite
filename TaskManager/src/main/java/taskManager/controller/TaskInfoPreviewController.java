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
import taskManager.model.FetchWorkflowObserver;
import taskManager.view.TaskInfoPreviewView;

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
		Object button = e.getSource();
		if (button instanceof JButton) {
			switch (((JButton) button).getName()) {
			case TaskInfoPreviewView.EDIT:
				taskC.editTask();
				// Fall through
			case TaskInfoPreviewView.X:
				FetchWorkflowObserver.ignoreAllResponses = false;
				JanewayModule.tabPaneC.getTabView().getWorkflowController()
						.reloadData();
				break;
			}
		}
	}
}
