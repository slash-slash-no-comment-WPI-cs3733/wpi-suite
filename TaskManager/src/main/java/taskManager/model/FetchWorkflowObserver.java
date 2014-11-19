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
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Observer for Workflow fetch requests
 *
 * @author Sam Khalandovsky
 * @version Nov 11, 2014
 */
public class FetchWorkflowObserver extends GenericRequestObserver {

	public static boolean ignoreNextResponse = false;
	public static boolean ignoreAllResponses = false;

	final WorkflowModel model;
	WorkflowController controller;

	/**
	 * Constructor for Observer
	 *
	 * @param model
	 *            the workflow model being fetched/overwritten
	 * @param controller
	 *            will be called upon success if it is passed; null is allowed
	 */
	public FetchWorkflowObserver(WorkflowModel model,
			WorkflowController controller) {
		this.model = WorkflowModel.getInstance();
		this.controller = controller;
	}

	/**
	 * @see taskManager.model.GenericRequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {

		if (ignoreNextResponse) {
			System.out.println("Ignoring response due to recent local changes");
			ignoreNextResponse = false;
			return;
		}
		if (ignoreAllResponses) {
			System.out.println("Ignoring response due to global ignore flag");
			return;
		}

		ResponseModel response = iReq.getResponse();
		String body = response.getBody();
		System.out.println("Response:" + body);

		WorkflowModel[] workflows = AbstractJsonableModel.fromJson(body,
				WorkflowModel[].class);
		if (workflows == null) {
			System.out.println("Workflow not found on server");
			return;
		}

		WorkflowModel workflow = workflows[0];
		model.makeIdenticalTo(workflow);
		model.rebuildAllRefs();

		// Allow calling with null controller to permit testing without
		// controller
		if (controller != null) {
			controller.reloadData();
		}

	}

}
