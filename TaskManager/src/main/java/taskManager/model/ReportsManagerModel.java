package taskManager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class ReportsManagerModel {
	private WorkflowModel workflow;

	public ReportsManagerModel(WorkflowModel workflow) {
		this.workflow = workflow;
	}

	public Map<User, Map<Date, Double>> getVelocity(Set<User> users,
			Date start, Date end, boolean averageCredit) {
		Map<User, Map<Date, Double>> data = new HashMap<User, Map<Date, Double>>();
		for (User user : users) {
			data.put(user, new TreeMap<Date, Double>());
		}
		for (StageModel stage : workflow.getStages()) {
			for (TaskModel task : stage.getTasks()) {
				for (User user : task.getAssignedUsers()) {
					if (data.keySet().contains(user)) { // If we are tracking
														// the user
						Map<Date, Double> userData = data.get(user);
						if (averageCredit) {
							userData.put(task.getDueDate(),
									(double) task.getEstimatedEffort()
											/ task.getAssigned().size());
						} else {
							userData.put(task.getDueDate(),
									(double) task.getEstimatedEffort());
						}
						data.put(user, userData);
					}
				}
			}
		}
		return data;
	}

	public Map<User, List<TaskModel>> getUserTasks(Set<User> users) {
		Map<User, List<TaskModel>> data = new HashMap<User, List<TaskModel>>();
		for (User user : users) {
			data.put(user, new ArrayList<TaskModel>());
		}
		for (StageModel stage : workflow.getStages()) {
			for (TaskModel task : stage.getTasks()) {
				for (User user : task.getAssignedUsers()) {
					if (data.keySet().contains(user)) { // If we are tracking
														// the user
						List<TaskModel> userData = data.get(user);
						if (!userData.contains(task)) {
							userData.add(task);
						}
						data.put(user, userData);
					}
				}
			}
		}
		return data;
	}

	// public Map<User, Map<StageModel, List<TaskModel>>>
	// This seems a bit absurd. Maybe a Map<String, Integer> for stage name,
	// number of tasks
}
