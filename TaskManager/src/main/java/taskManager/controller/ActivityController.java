/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.util.ArrayList;
import java.util.List;

import taskManager.model.ActivityModel;
import taskManager.model.TaskModel;
import taskManager.view.ActivityPanel;

/**
 * Controller for a single task's activities. Controlls both the comments
 * ActivityPanel and the all activities ActivityPanel.
 *
 * @author Clark Jacobsohn
 */
public class ActivityController {

	public static final String SUBMIT = "submit";

	private TaskModel taskM;
	private List<ActivityModel> activities;
	private ActivityPanel activityPanel;
	private ActivityPanel commentsPanel;

	/**
	 * Constructs an ActivityController for a given task
	 * 
	 * @param taskM
	 *            The task whose activities are being controlled. If this is
	 *            null, then activities are being tracked for a new task.
	 */
	public ActivityController(TaskModel taskM) {
		this.taskM = taskM;
		if (taskM != null) {
			activities = taskM.getActivities();
		} else {
			// If the task is null, make a list for the new task's activities
			activities = new ArrayList<ActivityModel>();
		}
		this.activityPanel = new ActivityPanel(ActivityPanel.Type.ALL,
				activities, this);
		this.commentsPanel = new ActivityPanel(ActivityPanel.Type.COMMENTS,
				activities, this);
	}

	/**
	 * 
	 * Reloads the activities panel.
	 *
	 */
	public void reloadActivitiesPanel() {
		if (taskM != null) {
			activities = taskM.getActivities();
		}
		activityPanel.reloadActivities(activities);
		commentsPanel.reloadActivities(activities);
	}

	/**
	 * 
	 * Adds an activity.
	 *
	 * @param act
	 *            the activity.
	 */
	public void addActivity(ActivityModel act) {
		if (taskM != null) {
			taskM.addActivity(act);
		} else {
			activities.add(act);
		}
		reloadActivitiesPanel();
	}

	/**
	 * Returns the activities panel for this task, showing all activities
	 * 
	 * @return the activities panel for this task
	 */
	public ActivityPanel getActivitiesPanel() {
		return activityPanel;
	}

	/**
	 * Returns the comments panel for this task, showing just comments
	 * 
	 * @return the comments panel for this task
	 */
	public ActivityPanel getCommentsPanel() {
		return commentsPanel;
	}

	/**
	 * Returns the list of activities
	 * 
	 * @return the list of activities
	 */
	public List<ActivityModel> getActivities() {
		return activities;
	}

	public void scrollActivitiesToBottom() {
		commentsPanel.scrollActivitiesToBottom();
		activityPanel.scrollActivitiesToBottom();
	}
}
