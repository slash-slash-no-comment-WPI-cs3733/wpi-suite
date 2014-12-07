package taskManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import taskManager.model.GenericEntityManager;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationEntityManager;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class MockRequest extends Request {

	private static Map<String, EntityManager> map;
	private static Session session;

	private String id;
	private EntityManager mgr;

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
		this.mgr = getEntityManager(pathPieces[0] + pathPieces[1]);
		if (pathPieces.length > 2) {
			this.id = pathPieces[3];
		}
	}

	@Override
	public void send() throws IllegalStateException {
		String response;
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
			Data data = new MockData(new HashSet<Object>());
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
			session = new Session(user, "MockSSID");
		}
		return session;
	}
}
