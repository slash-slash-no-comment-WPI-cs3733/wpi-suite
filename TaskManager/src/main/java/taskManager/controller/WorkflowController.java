/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.util.List;

import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.WorkflowView;

/**
 * A controller for the workflow view
 *
 * @author Jon Sorrells
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class WorkflowController {

	private final WorkflowView view;
	private final WorkflowModel model;

	/**
	 * Constructor for the WorkflowController, gets all the stages from the
	 * WorkflowView, creates the corresponding StageView and StageControllers,
	 * and adds the StageViews to the UI.
	 * 
	 * @param view
	 *            the corresponding WorkflowView object
	 * @param model
	 *            the corresponding WorkflowModel object
	 */
	public WorkflowController(WorkflowView view, WorkflowModel model) {
		this.view = view;
		this.model = model;

		// get all the stages in this workflow
		final List<StageModel> stages = this.model.getStages();
		// and add them all to the view
		for (StageModel stage : stages) {
			// create stage view and controller.
			StageView stv = new StageView(stage.getName());
			stv.setController(new StageController(stv, stage));

			// add stage view to workflow
			this.view.addStageView(stv);
		}
	}

}
