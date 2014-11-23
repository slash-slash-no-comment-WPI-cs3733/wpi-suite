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

	/**
	 * 
	 * Returns the velocity (units of work completed) per day per user.
	 *
	 * Note: Not completely implemented, start and end don't work. Implement or
	 * remove arguments.
	 *
	 * @param users
	 *            The list of users we're finding data for
	 * @param start
	 *            The earliest date a task can be due by and still be included
	 *            in the velocity calculations.
	 * @param end
	 *            The latest date a task can be due and still be included in the
	 *            data.
	 * @param averageCredit
	 *            If false, splits the task's effort between users, otherwise
	 *            gives all assigned users full credit for the task(s).
	 * @return A map of Usernames to a Map of Dates to the amount of effort done
	 *         by the user.
	 */
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

	/**
	 * 
	 * Finds the list of all tasks associated with the given users.
	 *
	 * @param users
	 *            The list of users that we're finding tasks for.
	 * @return A map of Usernames to a list of tasks the user is associated
	 *         with.
	 */
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
