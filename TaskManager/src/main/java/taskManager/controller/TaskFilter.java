/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.util.List;

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

	public TaskFilter() {

	}

	/**
	 * Set filter string across default fields
	 *
	 * @param s
	 *            string to filter by
	 */
	public void setString(String s) {

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

	}

	/**
	 * Categories to be included in filter
	 *
	 * @param categories
	 */
	public void setCategories(List<TaskCategory> categories) {

	}

	/**
	 * Archive states to be included in filter
	 *
	 * @param states
	 */
	public void setArchive(ArchiveState... states) {

	}
}
