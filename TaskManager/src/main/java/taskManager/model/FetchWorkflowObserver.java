/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import taskManager.controller.WorkflowController;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * Observer for Workflow fetch requests
 *
 * @author Sam Khalandovsky
 * @version Nov 11, 2014
 */
public class FetchWorkflowObserver extends GenericRequestObserver {

	WorkflowModel model;
	WorkflowController controller;

	public FetchWorkflowObserver(WorkflowModel model,
			WorkflowController controller) {
		this.model = model;
		this.controller = controller;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		String body = iReq.getBody();
		WorkflowModel[] workflows = AbstractJsonableModel.fromJson(body,
				WorkflowModel[].class);
		WorkflowModel workflow = workflows[0];
		workflow.rebuildAllRefs();
		model.makeIdenticalTo(workflow);

		// Allow calling with null controller to permit testing without
		// controller
		if (controller != null) {
			controller.reloadData();
		}

	}

}
