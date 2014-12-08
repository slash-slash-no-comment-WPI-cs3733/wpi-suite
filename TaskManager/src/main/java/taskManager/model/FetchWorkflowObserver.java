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

	public static boolean ignoreAllResponses = false;
	private boolean keepOpen;

	final WorkflowModel model;

	/**
	 * Constructor for Observer. Defaults to keeping the connection with the
	 * server open
	 *
	 */
	public FetchWorkflowObserver() {
		this(true);
	}

	/**
	 * Constructor for Observer
	 *
	 * @param keepOpen
	 *            If the connection should be kept
	 */
	public FetchWorkflowObserver(boolean keepOpen) {
		model = WorkflowModel.getInstance();
		this.keepOpen = keepOpen;
	}

	/**
	 * @see taskManager.model.GenericRequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {

		if (ignoreAllResponses) {
			System.out.println("Ignoring response due to global ignore flag");

			// restart the connection
			restartConnection();

			return;
		}

		ResponseModel response = iReq.getResponse();
		String body = response.getBody();
		System.out.println("Response:" + body);

		WorkflowModel[] workflows = AbstractJsonableModel.fromJson(body,
				WorkflowModel[].class);
		if (workflows == null) {
			System.out.println("Workflow not found on server");
			// restart the connection
			restartConnection();
			return;
		}

		WorkflowModel workflow = workflows[0];
		model.makeIdenticalTo(workflow);
		model.rebuildAllRefs();

		WorkflowController.getInstance().reloadData();

		// restart the connection
		restartConnection();
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		super.responseError(iReq);

		// restart the connection
		restartConnection();
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest,
	 *      java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		super.fail(iReq, exception);

		// restart the connection
		restartConnection();
	}

	/**
	 * The current architecture doesn't let us keep pushing data through open
	 * connections, so we have to close and reopen the connection each time
	 *
	 */
	private void restartConnection() {
		if (WorkflowModel.alive && keepOpen) {
			WorkflowModel.getInstance().update();
		}
	}

}
