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
	 * See description for getVelocityForStage below.
	 *
	 * @param users
	 * @param start
	 * @param end
	 * @param averageCredit
	 * @return
	 */
	public Map<String, Map<Date, Double>> getVelocity(Set<String> users,
			Date start, Date end, boolean averageCredit) {
		// Assume the completion stage is the final stage
		final List<StageModel> stageList = workflow.getStages();
		final StageModel finalStage = stageList.get(stageList.size() - 1);
		return getVelocity(users, start, end, averageCredit, finalStage);
	}

	/**
	 * Return data to allow the calculation of "Velocity", i.e. amount of effort
	 * completed/time. Will return data in a nested map, the outer one is a map
	 * of users, the inner one is a map of completed task times to effort
	 *
	 *
	 * @param users
	 *            The set of usernames to get data about
	 * @param start
	 *            The time before which we do not care about
	 * @param end
	 *            The time after which we do not care about
	 * @param averageCredit
	 *            If true, average credit for the task across all assigned
	 *            users.
	 *
	 * @param stage
	 *            The stage to consider as the completion stage
	 * @return HashMap<Username : TreeMap<Task completion date : Effort >>
	 */
	public Map<String, Map<Date, Double>> getVelocity(Set<String> users,
			Date start, Date end, boolean averageCredit, StageModel stage) {
		if (!workflow.getStages().contains(stage)) {
			throw new IllegalArgumentException("Invalid stage");
		}

		Map<String, Map<Date, Double>> data = new HashMap<String, Map<Date, Double>>();
		for (String username : users) {
			data.put(username, new TreeMap<Date, Double>());
		}
		for (TaskModel task : stage.getTasks()) {
			Date completed = null;
			// We are going to iterate backward through the activities, and take
			// the final MOVE event. This event must have been to the current
			// stage (it hasn't been moved after), and since we're in the final
			// stage, this MOVE event must actually be a completion event.
			for (int i = task.getActivities().size() - 1; i >= 0; i--) {
				ActivityModel activity = task.getActivities().get(i);
				if (activity.getType() == ActivityModel.activityModelType.MOVE) {
					completed = activity.getDateCreated();
					System.out.println("Found completed event");
					if (completed.compareTo(start) < 0
							|| completed.compareTo(end) > 0) {
						completed = null; // Pretend as if we didn't find the
											// completion event.
					}
					break;
				}
			}
			if (completed != null) {
				for (String username : task.getAssigned()) {
					if (users.contains(username)) {
						Map<Date, Double> userData = data.get(username);
						if (averageCredit) {
							userData.put(completed,
									(double) task.getEstimatedEffort()
											/ task.getAssigned().size());
						} else {
							userData.put(completed,
									(double) task.getEstimatedEffort());
						}
						data.put(username, userData);
					}
				}
			} // End if (inDateRange)
		} // End for (TaskModel)
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
