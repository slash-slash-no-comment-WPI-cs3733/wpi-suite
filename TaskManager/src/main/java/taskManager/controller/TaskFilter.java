/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.activityModelType;
import taskManager.model.TaskModel;
import taskManager.model.TaskModel.TaskCategory;

/**
 * Filter object for filtering tasks
 *
 * @author Sam Khalandovsky
 * @version Dec 10, 2014
 */
public class TaskFilter {
	public enum StringField {
		NAME, DESCRIPTION, COMMENTS
	};

	public enum ArchiveState {
		ARCHIVED, NOT_ARCHIVED
	}

	// * Example usage:
	// * List<TaskCategory> cats = new ArrayList<TaskCategory>();
	// * cats.add(TaskCategory.RED);
	// * cats.add(TaskCategory.BLUE);
	// *
	// * Taskfilter filter = new TaskFilter();
	// * filter.setString("searchstring");
	// * filter.setCategories(cats);
	// * filter.setArchive(ArchiveState.ARCHIVED, ArchiveState.NOT_ARCHIVED);
	// *
	// * WorkflowController.reloadData(filter);

	// string not case sensitive, should be always set lowercase
	private String filterString;
	private Set<StringField> stringFields;
	private Set<ArchiveState> archiveStates;
	private Set<TaskCategory> categories;

	/**
	 * Create filter with default configuration
	 *
	 */
	public TaskFilter() {
		// null values means "allow all"
		filterString = null;
		stringFields = null;
		archiveStates = new HashSet<ArchiveState>();
		archiveStates.add(ArchiveState.NOT_ARCHIVED);
		categories = null;
	}

	/**
	 * Set filter string across default fields
	 *
	 * @param s
	 *            string to filter by
	 */
	public void setString(String s) {
		setString(s, StringField.NAME, StringField.DESCRIPTION,
				StringField.COMMENTS);
	}

	/**
	 * Set filter string across specified fields
	 *
	 * @param s
	 *            string to filter by
	 * @param fields
	 *            fields to filter across
	 */
	public void setString(String s, StringField... fields) {
		filterString = s.toLowerCase();
		stringFields = new HashSet<StringField>(Arrays.asList(fields));
	}

	/**
	 * Categories to be included in filter
	 *
	 * @param categories
	 */
	public void setCategories(List<TaskCategory> categories) {
		this.categories = new HashSet<TaskCategory>(categories);
	}

	/**
	 * Archive states to be included in filter
	 *
	 * @param states
	 */
	public void setArchive(ArchiveState... states) {
		archiveStates = new HashSet<ArchiveState>(Arrays.asList(states));
	}

	/**
	 * Check if a task passes the filter
	 *
	 * @param task
	 *            task to check
	 * @return whether the task passes the filter
	 */
	public boolean check(TaskModel task) {
		return validString(task) && validArchive(task) && validCategory(task);
	}

	/**
	 * Check if task passes string filter
	 *
	 * @param task
	 * @return whether it passes
	 */
	private boolean validString(TaskModel task) {
		if (filterString == null) {
			return true;
		}

		for (StringField field : stringFields) {
			if (validString(task, field)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if the filter string is found in the specified field of the task
	 *
	 * @param task
	 *            task to search in
	 * @param field
	 *            field to search in
	 * @return whether the task passes
	 */
	private boolean validString(TaskModel task, StringField field) {
		switch (field) {
		case NAME:
			return task.getName().toLowerCase().contains(filterString);
		case DESCRIPTION:
			return task.getDescription().toLowerCase().contains(filterString);
		case COMMENTS:
			List<ActivityModel> activities = task.getActivities();
			for (ActivityModel activity : activities) {
				if (activity.getType().equals(activityModelType.COMMENT)
						&& activity.getDescription().toLowerCase()
								.contains(filterString)) {
					return true;
				}
			}
			return false;
		default:
			return false;
		}
	}

	/**
	 * Check if task passes archive filter
	 *
	 * @param task
	 * @return whether it passes
	 */
	private boolean validArchive(TaskModel task) {
		if (archiveStates == null) {
			return true;
		}

		return task.isArchived()
				&& archiveStates.contains(ArchiveState.ARCHIVED)
				|| !task.isArchived()
				&& archiveStates.contains(ArchiveState.NOT_ARCHIVED);

	}

	/**
	 * Check if task passes category filter
	 *
	 * @param task
	 * @return whether it passes
	 */
	private boolean validCategory(TaskModel task) {
		if (categories == null) {
			return true;
		}

		return categories.contains(task.getCategory());
	}
}
