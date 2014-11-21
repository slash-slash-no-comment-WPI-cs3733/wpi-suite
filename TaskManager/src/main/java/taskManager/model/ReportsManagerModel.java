package taskManager.model;

import java.util.Date;
import java.util.HashMap;
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
		for (StageModel stage : workflow.getStages()) {
			for (TaskModel task : stage.getTasks()) {
				for (User user : task.getAssignedUsers()) {
					Map<Date, Double> userData;
					if (data.keySet().contains(user)) {
						userData = data.get(user);
					} else {
						userData = new TreeMap<Date, Double>();
					}
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
		return data;
	}

	// public Map<User, Map<StageModel, List<TaskModel>>>
	// This seems a bit absurd. Maybe a Map<String, Integer> for stage name,
	// number of tasks
}
