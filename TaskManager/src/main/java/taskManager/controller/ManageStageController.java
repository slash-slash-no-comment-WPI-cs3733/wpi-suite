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

import taskManager.model.StageModel;
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
	 * @param view
	 *            View associated with this controller.
	 * @param model
	 *            The data associated with this controller.
	 */
	public ManageStageController(ManageStageView view) {
		this.view = view;
		this.model = WorkflowModel.getInstance();

		reloadData();
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
			String stageID = ((JButton) button).getParent().getName();
			// get which button was pressed
			String buttonName = ((JButton) button).getName();
			// get the current list of stages
			List<StageModel> stages = model.getStages();

			StageModel stage = null;

			for (StageModel s : stages) {
				if (s.getName().equals(stageID)) {
					stage = s;
					break;
				}
			}

			// take the appropriate action
			switch (buttonName) {
			case ManageStageView.DELETE:
				if (stages.size() >= 2) {
					view.removeStage(stageID);
					model.getStages()
							.removeIf(s -> s.getName().equals(stageID));
					// refresh the view
					view.updateUI();
				}
				reloadData();
				break;
			case ManageStageView.MOVE_UP:
				// move the stage up by 1
				for (int i = 0; i < stages.size(); i++) {
					if (stage == stages.get(i)) {
						model.moveStage(i - 1, stage);
						break;
					}
				}
				// need to reload all the components to reorder them
				reloadData();
				break;
			case ManageStageView.MOVE_DOWN:
				// move the stage down by 1
				stages = model.getStages();
				for (int i = 0; i < stages.size(); i++) {
					if (stage == stages.get(i)) {
						model.moveStage(i + 1, stage);
						break;
					}
				}
				// need to reload all the components to reorder them
				reloadData();
				break;
			case ManageStageView.ADD_NEW_STAGE:
				// Create a new stage at the end
				String newStageName = view.getNewStageNameField().getText();
				StageModel newStage = new StageModel(newStageName);
				view.addStage(newStage.getName(), newStage.getName(), true);
				// refresh the view
				view.getNewStageNameField().setText(
						ManageStageView.NEW_STAGE_NAME);
				view.updateUI();
				reloadData();
				break;
			default:
				System.out.println("Unknown button pushed");
				break;
			}

			// Save entire workflow whenever a stage is added of edited
			model.save();
		}
	}

	/**
	 * Reloads all the data on the view to match the data in the model
	 *
	 */
	public void reloadData() {
		view.removeAllStages();
		List<StageModel> stages = model.getStages();

		// If there are more than 2 stages display all of them as deletable.
		// If there is one stage, the remaining stage is not deletable.
		if (stages.size() >= 2) {
			for (StageModel stage : stages) {
				view.addStage(stage.getName(), stage.getName(), true);
			}
		} else if (stages.size() == 1) {
			view.addStage(stages.get(0).getName(), stages.get(0).getName(),
					false);
		}

		view.getNewStageNameField().setText(ManageStageView.NEW_STAGE_NAME);
		view.updateUI();
	}
}
