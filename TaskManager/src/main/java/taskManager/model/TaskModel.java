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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
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

public class TaskModel extends AbstractModel {

	// Task name
	private String name;

	// Internal task name, unique.
	private String id;

	// Task description
	private String description;

	// Current stage that task belongs to. This will not be serialized.
	private transient StageModel stage;

	// List of users assigned to this task
	private final Set<User> assigned;

	// Due date timestamp
	private Date dueDate;

	// Estimated effort required for completion
	private int estimatedEffort;

	// Effort actually expended to complete
	private int actualEffort;

	// Actions and comments relevant to task
	private final List<ActivityModel> activities;

	// Associated requirement that this task corresponds to
	private Requirement req;

	static private TaskRequestObserver observer = new TaskRequestObserver();

	/**
	 * Constructor assigns name, task id, and stage.
	 *
	 * @param name
	 *            name of the new task
	 * @param stage
	 *            stage that it enters in
	 */
	public TaskModel(String name, StageModel stage) {
		this.name = name;
		id = name;
		assigned = new HashSet<User>();
		activities = new ArrayList<ActivityModel>();
		this.stage = stage;
		stage.addTask(this);
	}

	/**
	 * Constructor assigns name, task id, and stage.
	 *
	 * @param name
	 *            name of the new task
	 * @param stage
	 *            stage that it enters in
	 */
	public TaskModel(String name, String stage) {
		this.name = name;
		id = name;
		assigned = new HashSet<User>();
		activities = new ArrayList<ActivityModel>();
	}

	/**
	 * Blank constructor Necessary for creating dummy objects when querying
	 * database
	 */
	public TaskModel() {
		assigned = null;
		activities = null;
	}

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
	 * @return the internal id
	 */
	public String getID() {
		return id;
	}

	/**
	 * @param id
	 *            set the internal id. Should only be used when intializing the
	 *            task.
	 */
	public void setID(String id) {
		this.id = id;
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
		if (actualEffort <= 0) {
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
		assigned.add(user);
	}

	/**
	 * Removes assigned user if user exists in assigned
	 *
	 * @param user
	 *            to be removed
	 */
	public void removeAssigned(User user) {
		if (!assigned.contains(user)) {
			// TODO: Log user non-existence in set
		} else {
			assigned.remove(user);
		}
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

	@Override
	public void save() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/task", HttpMethod.POST);
		request.setBody(toJson());
		request.addObserver(observer);
		request.send();
	}

	@Override
	public void delete() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/task", HttpMethod.DELETE);
		request.setBody(toJson());
		request.addObserver(observer);
		request.send();
	}

	@Override
	public String toJson() {
		final Gson gson = new GsonBuilder().registerTypeAdapter(
				TaskModel.class, new TaskModelSerializer()).create();
		return gson.toJson(this, TaskModel.class);
	}

	/**
	 * Static method for deserializing object from JSON
	 *
	 * @param serialized
	 *            JSON string
	 * @return the deserialized TaskModel
	 */
	public static TaskModel fromJson(String serialized) {
		// TODO
		return null;
	}

	@Override
	public Boolean identify(Object o) {
		if (o instanceof TaskModel) {
			return ((TaskModel) o).id.equals(id);
		}
		return false;
	}

	/**
	 * Returns the list of activities as a JsonArray
	 *
	 * @return a JsonArray of the activities
	 */
	public JsonArray getActivitiesAsJson() {
		final JsonArray activityList = new JsonArray();
		for (ActivityModel activity : activities) {
			activityList.add(new JsonPrimitive(activity.toJson()));
		}
		return null;
	}

	/**
	 * Return the list of assigned users as a JsonArray.
	 *
	 * @return a JsonArray of the users
	 */
	public JsonArray getAssignedAsJson() {
		final JsonArray assignedList = new JsonArray();
		for (User user : assigned) {
			assignedList.add(new JsonPrimitive(user.toJson()));
		}
		return null;
	}
}
