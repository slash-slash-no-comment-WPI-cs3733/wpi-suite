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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import taskManager.view.TaskInfoPreviewView;

/**
 * Controller for the TaskInfoPreviewView aka the taskInfo bubble.
 *
 * @author Samee Swartz
 * @version Nov 21, 2014
 */
public class TaskInfoPreviewController implements ActionListener, MouseListener {
	private final TaskController taskC;
	private final TaskInfoPreviewView taskInfo;

	/**
	 * Constructor for the controller for the task preview view
	 *
	 * @param taskC
	 *            the controller for the task
	 * @param t
	 *            the taskInfoPreviewView being controlled
	 */
	public TaskInfoPreviewController(TaskController taskC, TaskInfoPreviewView t) {
		this.taskC = taskC;
		taskInfo = t;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Object button = e.getSource();
		if (button instanceof JButton) {
			switch (((JButton) button).getName()) {
			case TaskInfoPreviewView.EDIT:
				// open the editTask window for the task
				taskC.editTask();
				// Fall through
			case TaskInfoPreviewView.X:
				// reset the flag
				WorkflowController.pauseInformation = false;
				// remove any taskInfo bubbles from the workflow
				WorkflowController.getInstance().removeTaskInfos(false);
				WorkflowController.getInstance().reloadData();
				WorkflowController.getInstance().repaintView();
				break;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Do nothing
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
		taskInfo.setCloseBorder(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		taskInfo.setCloseBorder(false);
	}
}
