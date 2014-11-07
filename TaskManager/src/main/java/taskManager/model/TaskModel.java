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
 * Stores data of each task in the workflow
 *
 * @author Sam Khalandovsky
 * @version Nov 6, 2014
 */

public class TaskModel extends AbstractModel {

	// Task title
	private String title;

	// Internal task title, unique.
	private String id;

	// Task description
	private String description;

	// Current stage that task belongs to
	private StageModel status;

	// List of users assigned to this task
	private final Set<User> assigned;

	// Due-date timestamp
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
	 * Constructor assigns title and task id
	 *
	 * @param title
	 */
	public TaskModel(String title, StageModel stage) {
		this.title = title;
		this.id = title;
		assigned = new HashSet<User>();
		activities = new ArrayList<ActivityModel>();
		stage.addTask(this);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the id
	 */
	public String getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @return the status
	 */
	public StageModel getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(StageModel status) {
		this.status = status;
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
		if (o instanceof TaskModel) {
			return ((TaskModel) o).getID() == this.getID();
		}
		return false;
	}
}
