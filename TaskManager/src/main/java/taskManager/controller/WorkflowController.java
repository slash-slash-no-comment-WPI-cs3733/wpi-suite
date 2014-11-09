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
import taskManager.view.WorkflowView;

/**
 * Description A controller for the workflow view
 *
 * @author Jon Sorrells
 */
public class WorkflowController {

	private final WorkflowView view;
	private final WorkflowModel model;

	public WorkflowController(WorkflowView view, WorkflowModel model) {
		this.view = view;
		this.model = model;
		
		// get all the stages in this workflow
		List<StageModel> stages = this.model.getStages();
		// and add them all to the view
		for (StageModel stage : stages) {
			this.view.addStageView(stage);
		}
	}

}
