/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Stores data of each task in the workflow. Tasks are saved per
 * {@link StageModel Stage}.
 *
 * @author Sam Khalandovsky
 * @author Joseph Blackman
 * @author Ezra Davis
 * @version Nov 6, 2014
 */

public class TaskModel extends AbstractJsonableModel<TaskModel> {

	// Generic logger
	private static final Logger logger = Logger.getLogger(TaskModel.class
			.getName());

	// Task name
	private String name;

	// Task description
	private String description;

	// Current stage that task belongs to. This will not be serialized.
	private transient StageModel stage;

	// List of users assigned to this task
	private Set<User> assigned;

	// // Due date timestamp
	// private Date dueDate;
	// temporarily making this a string for testing
	// TODO turn it back into a date
	private Date dueDate;

	// Estimated effort required for completion
	private int estimatedEffort;

	// Effort actually expended to complete
	private int actualEffort;

	// Actions and comments relevant to task
	private List<ActivityModel> activities;

	// Associated requirement that this task corresponds to
	private Requirement req;

	/**
	 * Constructor assigns name, task id, and stage.
	 *
	 * @param stage
	 *            stage that it enters in
	 * @param name
	 *            name of the new task
	 */

	public TaskModel(String name, StageModel stage) {

		super(stage.getWorkflow().findUniqueTaskID(name));
		final ActivityModel createTask = new ActivityModel("Created task "
				+ name + " in stage " + stage.getName() + ".",
				ActivityModel.activityModelType.CREATION);
		this.name = name;

		assigned = new HashSet<User>();
		activities = new ArrayList<ActivityModel>();
		activities.add(createTask);
		this.stage = stage;

		// Allow creation of null objects for database
		if (stage != null) {
			stage.addTask(this);
		}
	}

	/**
	 * Required to create dummy instance Necessary for passing TaskModel type
	 * into DataStore *
	 */
	public TaskModel() {
	};

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Change the name of the task. Does not change the internal
	 *            name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the current stage
	 */
	public StageModel getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            Change the current task stage. The stage should be updated as
	 *            well.
	 */
	public void setStage(StageModel stage) {
		this.stage = stage;
	}

	/**
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the estimatedEffort
	 */
	public int getEstimatedEffort() {
		return estimatedEffort;
	}

	/**
	 * @param estimatedEffort
	 *            the estimatedEffort to set
	 */
	public void setEstimatedEffort(int estimatedEffort) {
		if (estimatedEffort <= 0) {
			throw new IllegalArgumentException(
					"estimatedEffort must be non-negative");
		}
		this.estimatedEffort = estimatedEffort;
	}

	/**
	 * @return the actualEffort
	 */
	public int getActualEffort() {
		return actualEffort;
	}

	/**
	 * @param actualEffort
	 *            the actualEffort to set
	 */
	public void setActualEffort(int actualEffort) {
		if (actualEffort < 0) {
			throw new IllegalArgumentException(
					"actualEffort must be non-negative");
		}
		this.actualEffort = actualEffort;
	}

	/**
	 * @return the requirement
	 */
	public Requirement getReq() {
		return req;
	}

	/**
	 * @param req
	 *            the requirement to set
	 */
	public void setReq(Requirement req) {
		this.req = req;
	}

	/**
	 * @return the assigned users
	 */
	public Set<User> getAssigned() {
		return assigned;
	}

	/**
	 * Adds user to assigned list
	 *
	 * @param user
	 *            new user to be added
	 */
	public void addAssigned(User user) {
		final ActivityModel addUser = new ActivityModel("User "
				+ user.getName() + " added to task",
				ActivityModel.activityModelType.USER_ADD, user);
		assigned.add(user);
		addActivity(addUser);
		logger.log(Level.FINER, "Added user " + user.getName() + " to task "
				+ name + ".");
	}

	/**
	 * Removes assigned user if user exists in assigned
	 *
	 * @param user
	 *            to be removed
	 */
	public void removeAssigned(User user) {
		if (!assigned.contains(user)) {
			logger.log(Level.WARNING,
					"Tried to remove a user from a task they were not assigned to.");
			throw new IndexOutOfBoundsException("User not in suggested task");
		}
		assigned.remove(user);
		final ActivityModel delUser = new ActivityModel("Removed user "
				+ user.getName() + " from task " + name + ".",
				ActivityModel.activityModelType.USER_ADD, user);
		addActivity(delUser);
		logger.log(Level.FINER, "Removed user " + user.getName()
				+ " from task " + name + ".");
	}

	/**
	 * @return the activities
	 */
	public List<ActivityModel> getActivities() {
		return activities;
	}

	/**
	 * Adds ActivityModel to list of activites
	 *
	 * @param activity
	 */
	public void addActivity(ActivityModel activity) {
		activities.add(activity);
	}

	public void addComment(String comment, User user) {
		final ActivityModel commentActivity = new ActivityModel(comment,
				ActivityModel.activityModelType.COMMENT, user);
		addActivity(commentActivity);
	}

	public void editActivity(int index, String newText) {
		final ActivityModel toEdit = activities.get(index);
		toEdit.setDescription(newText);
	}

	/**
	 * Changes this taskmodel to be identical to the inputed task model, while
	 * maintaining the pointer
	 *
	 * @param task
	 *            The task to copy
	 */
	public void makeIdenticalTo(TaskModel task) {
		setID(task.getID());
		name = task.getName();
		description = task.getDescription();
		stage = task.getStage();
		assigned = task.getAssigned();
		dueDate = task.getDueDate();
		estimatedEffort = task.getEstimatedEffort();
		actualEffort = task.getActualEffort();
		activities = task.getActivities();
		req = task.getReq();
	}

	@Override
	public void save() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/task", HttpMethod.POST);
		request.setBody(toJson());
		request.addObserver(getObserver());
		request.send();
	}

	@Override
	public void delete() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/task", HttpMethod.DELETE);
		request.setBody(toJson());
		request.addObserver(getObserver());
		request.send();
	}

	@Override
	public Boolean identify(Object o) {
		if (o instanceof TaskModel) {
			return ((TaskModel) o).getID().equals(this.getID());
		}
		return false;
	}
}
