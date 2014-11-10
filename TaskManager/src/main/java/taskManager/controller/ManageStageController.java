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

import taskManager.model.WorkflowModel;
import taskManager.view.ManageStageView;

/**
 * The controller for the Manage Stage View
 *
 * @author Jon Sorrells
 */
public class ManageStageController implements ActionListener {

	private final ManageStageView view;
	private final WorkflowModel model;

	/**
	 * Creates a new ManageStageController with the given view and model
	 *
	 */
	public ManageStageController(ManageStageView view, WorkflowModel model) {
		this.view = view;
		this.model = model;
	}

	/*
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object button = event.getSource();
		if (button instanceof JButton) {

			// get the name of the stage
			String stageName = ((JButton) button).getParent().getName();
			// get which button was pressed
			String name = ((JButton) button).getName();

			// take the appropriate action
			switch (name) {
			case "Delete":
				view.removeStage(stageName);
				break;
			case "Move Up":
				// TODO
				break;
			case "Move Down":
				// TODO
				break;
			case "Add new stage":
				view.addStage(view.getNewStageNameField().getText());
				break;
			default:
				System.out.println("Unknown button pushed");
				break;
			}

			// refresh the view, because the information (likely) changed
			view.updateUI();
		}

	}
}
