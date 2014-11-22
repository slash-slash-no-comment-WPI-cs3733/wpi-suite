package taskManager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReportsManagerModel {
	private WorkflowModel workflow;

	public ReportsManagerModel() {
		this.workflow = WorkflowModel.getInstance();
	}

	public ReportsManagerModel(WorkflowModel workflow) { // This method is used
															// for testing.
		this.workflow = workflow;
	}

	public Map<String, Map<Date, Double>> getVelocity(Set<String> users,
			Date start, Date end, boolean averageCredit) {
		Map<String, Map<Date, Double>> data = new HashMap<String, Map<Date, Double>>();
		for (String username : users) {
			data.put(username, new TreeMap<Date, Double>());
		}
		for (StageModel stage : workflow.getStages()) {
			for (TaskModel task : stage.getTasks()) {
				for (String username : task.getAssigned()) {
					if (users.contains(username)) {
						Map<Date, Double> userData = data.get(username);
						if (averageCredit) {
							userData.put(task.getDueDate(),
									(double) task.getEstimatedEffort()
											/ task.getAssigned().size());
						} else {
							userData.put(task.getDueDate(),
									(double) task.getEstimatedEffort());
						}
						data.put(username, userData);
					}
				}
			}
		}
		return data;
	}

	public Map<String, List<TaskModel>> getStringTasks(Set<String> users) {
		Map<String, List<TaskModel>> data = new HashMap<String, List<TaskModel>>();
		for (String username : users) {
			data.put(username, new ArrayList<TaskModel>());
		}
		for (StageModel stage : workflow.getStages()) {
			for (TaskModel task : stage.getTasks()) {
				for (String username : task.getAssigned()) {
					if (users.contains(username)) {
						List<TaskModel> userData = data.get(username);
						if (!userData.contains(task)) {
							userData.add(task);
						}
						data.put(username, userData);
					}
				}
			}
		}
		return data;
	}
}
