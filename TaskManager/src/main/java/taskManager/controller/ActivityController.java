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
import taskManager.view.ActivityView;

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
	private ActivityPanel activityTabs;

	/**
	 * Constructs an ActivityController for a given task
	 * 
	 * @param taskM
	 *            The task whose activities are being controlled. If this is
	 *            null, then activities are being tracked for a new task.
	 * 
	 * @etc The controller used for the edit buttons on ActivityView's
	 */
	public ActivityController(TaskModel taskM, EditTaskController etc) {
		this.taskM = taskM;
		if (taskM != null) {
			activities = taskM.getActivities();
		} else {
			// If the task is null, make a list for the new task's activities
			activities = new ArrayList<ActivityModel>();
		}
		this.activityTabs = new ActivityPanel(activities, etc);
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
		activityTabs.reloadActivities(activities);
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
		return activityTabs;
	}

	/**
	 * Returns the list of activities
	 * 
	 * @return the list of activities
	 */
	public List<ActivityModel> getActivities() {
		return activities;
	}

	/**
	 * 
	 * Scrolls the comments and all activities scrollPane's to the bottom.
	 *
	 */
	public void scrollActivitiesToBottom() {
		activityTabs.scrollActivitiesToBottom();
	}

	/**
	 * 
	 * Set which ActivityView is currently being edited.
	 *
	 * @param v
	 *            the ActivityView being edited.
	 */
	public void setEditedTask(ActivityView v) {
		activityTabs.setEditedTask(v);
	}

	/**
	 *
	 * @return the ActivityView being edited
	 */
	public ActivityView getEditedTask() {
		return activityTabs.getEditedTask();
	}
}
