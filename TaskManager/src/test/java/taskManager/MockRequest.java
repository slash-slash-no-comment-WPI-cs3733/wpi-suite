/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import java.util.HashMap;
import java.util.Map;

import taskManager.model.GenericEntityManager;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationEntityManager;
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

	private static Map<String, EntityManager> map;
	private static Session session;

	private String id;
	private EntityManager mgr;

	// Sets whether requests will actually save or just pretend to
	private boolean trueSave;

	/**
	 * Constructor for MockRequest
	 * 
	 * @param networkConfiguration
	 * @param path
	 * @param requestMethod
	 */
	public MockRequest(NetworkConfiguration networkConfiguration, String path,
			HttpMethod requestMethod, boolean trueSave) {
		super(networkConfiguration, path, requestMethod);

		this.trueSave = trueSave;
		if (trueSave) {
			String delims = "[/]+";
			String[] pathPieces = path.split(delims);
			this.mgr = getEntityManager(pathPieces[0] + pathPieces[1]);
			if (pathPieces.length > 2) {
				this.id = pathPieces[2];
			}
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
		// If trueSave is false, don't save anything
		if (!trueSave) {
			System.out.println("Save faked");
			return;
		}

		String response = null;
		Model[] m;
		try {
			switch (getHttpMethod()) {
			case GET:
				if (id == null || id.equalsIgnoreCase("")) {
					m = mgr.getAll(getSession());
				} else {
					m = mgr.getEntity(getSession(), id);
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
				response = mgr.update(getSession(), getBody()).toJson();
				break;
			case PUT:
				response = mgr.makeEntity(getSession(), getBody()).toJson();
				break;
			case DELETE:
				response = mgr.deleteEntity(getSession(), id) ? "success"
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

	/**
	 * Retrieve the appropriate EntityManager
	 *
	 * @param path
	 * @return the EntityManager
	 */
	public static EntityManager getEntityManager(String path) {
		// Initialize map
		if (map == null) {
			ClientDataStore data = ClientDataStore.getDataStore();
			map = new HashMap<String, EntityManager>();
			map.put("taskmanager" + "task",
					new GenericEntityManager<TaskModel>(data, TaskModel.class));
			map.put("taskmanager" + "stage",
					new GenericEntityManager<StageModel>(data, StageModel.class));
			map.put("taskmanager" + "workflow",
					new GenericEntityManager<WorkflowModel>(data,
							WorkflowModel.class));
			map.put("requirementmanager" + "iteration",
					new IterationEntityManager(data));
			map.put("requirementmanager" + "requirement",
					new RequirementEntityManager(data));
		}

		return map.get(path);
	}

	public static Session getSession() {
		if (session == null) {
			User user = new User("MockUser", "mockuser", "MockPassword", 99);
			Project project = new Project("MockProject", "MockProjectID");
			session = new Session(user, project, "MockSSID");
		}
		return session;
	}
}
