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
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationEntityManager;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Mock Network object for testing
 *
 * @author Sam Khalandovsky
 * @version Dec 6, 2014
 */
public class MockNetwork extends Network {

	public static Map<String, EntityManager> entityManagers;
	public static Session session;

	// Sets whether requests will actually save or just pretend to
	private boolean trueSave;

	/**
	 * Reset MockData when creating a new MockNetwork
	 * 
	 * @param trueSave
	 *            perform a save when requests are sent
	 */
	public MockNetwork(boolean trueSave) {
		super();
		// Clean database
		this.trueSave = trueSave;
		ClientDataStore.deleteDataStore();

		// if actually saving, setup DB
		if (trueSave) {
			ClientDataStore data = ClientDataStore.getDataStore();

			// setup EntityManager map
			entityManagers = new HashMap<String, EntityManager>();
			entityManagers.put("taskmanager" + "task",
					new GenericEntityManager<TaskModel>(data, TaskModel.class));
			entityManagers
					.put("taskmanager" + "stage",
							new GenericEntityManager<StageModel>(data,
									StageModel.class));
			entityManagers.put("taskmanager" + "workflow",
					new GenericEntityManager<WorkflowModel>(data,
							WorkflowModel.class));
			entityManagers.put("requirementmanager" + "iteration",
					new IterationEntityManager(data));
			entityManagers.put("requirementmanager" + "requirement",
					new RequirementEntityManager(data));

			data.enableRecursiveDelete(WorkflowModel.class);
			data.enableRecursiveDelete(StageModel.class);
			data.enableRecursiveDelete(TaskModel.class);

			// Init session
			User user = new User("MockUser", "mockuser", "MockPassword", 99);
			Project project = new Project("MockProject", "MockProjectID");
			session = new Session(user, project, "MockSSID");
		}
	}

	/**
	 * Default constructor that only fakes saving
	 */
	public MockNetwork() {
		this(false);
	}

	/**
	 * Method makeRequest.
	 * 
	 * @param path
	 *            String
	 * @param requestMethod
	 *            HttpMethod
	 * 
	 * @return MockRequest
	 */
	@Override
	public Request makeRequest(String path, HttpMethod requestMethod) {
		if (requestMethod == null) {
			throw new NullPointerException("requestMethod may not be null");
		}

		// Use arbitrary config
		if (defaultNetworkConfiguration == null) {
			defaultNetworkConfiguration = new NetworkConfiguration(
					"http://wpisuitetng");
		}

		return new MockRequest(defaultNetworkConfiguration, path,
				requestMethod, trueSave);
	}
}
