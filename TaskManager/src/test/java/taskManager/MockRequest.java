/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Response;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Mocks a Request object, and mimics the behavior of ManagerLayer and
 * WPICoreServelet
 *
 * @author Sam Khalandovsky
 * @version Dec 8, 2014
 */
public class MockRequest extends Request {

	private String id;
	private EntityManager mgr;
	private Session session;

	/**
	 * Constructor for MockRequest
	 * 
	 * @param networkConfiguration
	 * @param path
	 * @param requestMethod
	 */
	public MockRequest(NetworkConfiguration networkConfiguration, String path,
			HttpMethod requestMethod) {
		super(networkConfiguration, path, requestMethod);

		String delims = "[/]+";
		String[] pathPieces = path.split(delims);
		mgr = MockNetwork.entityManagers.get(pathPieces[0] + pathPieces[1]);
		session = MockNetwork.session;
		if (pathPieces.length > 2) {
			id = pathPieces[2];
		}
	}

	/**
	 * This method is blocking, unlike in it's parent. The MockDatabase is
	 * accessed immediately.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.Request#send()
	 */
	@Override
	public void send() throws IllegalStateException {
		String response = null;
		Model[] m;
		try {
			switch (getHttpMethod()) {
			case GET:
				if (id == null || id.equalsIgnoreCase("")) {
					m = mgr.getAll(session);
				} else {
					m = mgr.getEntity(session, id);
				}
				if (m != null) {
					response = "[";
					for (Model n : m) {
						response = response.concat(n.toJson() + ",");
					}
					if (m.length > 0) {
						response = response.substring(0, response.length() - 1); // remove
																					// trailing
																					// comma
					}
					response = response.concat("]");
				}
				break;
			case POST:
				response = mgr.update(session, getBody()).toJson();
				break;
			case PUT:
				response = mgr.makeEntity(session, getBody()).toJson();
				break;
			case DELETE:
				response = mgr.deleteEntity(session, id) ? "success"
						: "failure";
				break;
			}
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}

		if (response != null) {
			ResponseModel resp = new Response(418, "TestResponse",
					getHeaders(), response);

			// set the Request's response
			setResponse(resp);

			notifyObserversResponseSuccess();
		}
	}
}
