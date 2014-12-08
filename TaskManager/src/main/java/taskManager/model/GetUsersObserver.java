/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import taskManager.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Observer for User get requests
 *
 * @author Jon Sorrells
 */
public class GetUsersObserver extends GenericRequestObserver {

	/**
	 * @see taskManager.model.GenericRequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final String body = response.getBody();
		System.out.println("Response:" + body);

		JanewayModule.users = AbstractJsonableModel
				.fromJson(body, User[].class);

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
		if (WorkflowModel.alive) {
			WorkflowModel.getInstance().updateUsers();
		}
	}

}
