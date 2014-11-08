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

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

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

	// Current stage that task belongs to. This is not a link to the StageModel
	// itself, since that would serialize poorly.
	private String stage;

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

	/**
	 * Constructor assigns name, task id, and stage.
	 *
	 * @param name
	 *            name of the new task
	 * @param stage
	 *            stage that it enters in
	 * @param workflow
	 */
	public TaskModel(String name, String stage, WorkflowModel workflow) {
		this.name = name;
		id = name;
		assigned = new HashSet<User>();
		activities = new ArrayList<ActivityModel>();
		workflow.addTask(this, stage);
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
	 *            set the internal id
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
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            Change the current task stage. This is only a duplicate, and
	 *            the stage that the task belongs to should be updated too.
	 */
	public void setStage(String stage) {
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
		assigned.remove(user);
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
		// TODO Auto-generated method stub
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean identify(Object o) {
		boolean identify = false;
		if (o instanceof TaskModel) {
			identify = ((TaskModel) o).id.equals(id);
		}
		return identify;
	}
}
