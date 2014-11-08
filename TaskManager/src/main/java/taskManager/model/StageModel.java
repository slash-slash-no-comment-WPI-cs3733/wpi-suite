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
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * One stage in development for the process. Stages are saved per
 * {@link WorkflowModel Workflow}, and contain a number of {@link TaskModel
 * Tasks}.
 *
 * @author Joseph Blackman
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 6, 2014
 */
public class StageModel extends AbstractModel {

	// List of tasks in this stage
	private List<TaskModel> taskList;

	// The public name of this stage
	private String name;

	// The private name of this stage. May contain octothorps to ensure
	// uniqueness within this workflow.
	private String id;

	// Whether users can remove this stage
	private boolean removable;

	/**
	 * Constructor for Stage Models. Defaults to removable, adding stage at end
	 * of list.
	 *
	 * @param workflow
	 *            The workflow that the stage is a part of.
	 * @param name
	 *            The name of the stage.
	 */
	public StageModel(WorkflowModel workflow, String name) {
		this(workflow, name, true);
	}

	/**
	 * Constructor for Stage Models. Defaults to adding stage at end of list.
	 *
	 * @param workflow
	 *            The workflow that the stage is a part of.
	 * @param name
	 *            The name of the stage.
	 * @param removable
	 *            Whether or not the stage can be removed.
	 */
	public StageModel(WorkflowModel workflow, String name, boolean removable) {
		this.name = name;
		id = name;
		this.removable = removable;
		taskList = new ArrayList<TaskModel>();
		workflow.addStage(this);
	}

	/**
	 * Constructor for Stage Models. Defaults to removable.
	 *
	 * @param workflow
	 *            The workflow that the stage is a part of.
	 * @param name
	 *            The name of the stage.
	 * @param index
	 *            Index in the list to add stages too.
	 */
	public StageModel(WorkflowModel workflow, String name, int index) {
		this(workflow, name, index, true);
	}

	/**
	 * Constructor for Stage Models. *
	 *
	 * @param workflow
	 *            The workflow that the stage is a part of.
	 * @param name
	 *            The name of the stage.
	 * @param index
	 *            Index in the list to add stages too.
	 * @param removable
	 *            Whether or not the stage can be removed.
	 */
	public StageModel(WorkflowModel workflow, String name, int index,
			boolean removable) {
		this.name = name;
		id = name;
		this.removable = removable;
		taskList = new ArrayList<TaskModel>();
		workflow.addStage(this, index);
	}

	/**
	 * Get the name of the Stage.
	 *
	 * @return the name of the stage
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the internal id of the Stage
	 *
	 * @return the id of the Stage
	 */
	public String getID() {
		return id;
	}

	/**
	 * Set the internal id of the Stage.
	 *
	 * @param id
	 *            set the id of the stage.
	 */
	public void setID(String id) {
		this.id = id;
	}

	public List<TaskModel> getTasks() {
		return taskList;
	}

	/**
	 * Check if the current stage contains a given task.
	 *
	 * @param task
	 *            The task to look for
	 * 
	 * @return If the stage contains the task
	 */
	public boolean containsTask(TaskModel task) {
		return taskList.contains(task);
	}

	/**
	 * Check if the current stage contains a given task ID.
	 *
	 * @param id
	 *            The id of the task to look for
	 * 
	 * @return If the stage contains the task
	 */
	public boolean containsTaskID(String id) {
		boolean containsTaskID = false;
		for (TaskModel existingTask : taskList) {
			if (existingTask.getID().equals(id)) {
				containsTaskID = true;
				break;
			}
		}
		return containsTaskID;
	}

	/**
	 * Check if the current stage contains a given task name
	 *
	 * @param name
	 *            The name of the task to look for
	 * 
	 * @return The number of different tasks by that name in the stage.
	 */
	public int containsTaskName(String name) {
		int count = 0;
		for (TaskModel existingTask : taskList) {
			if (existingTask.getName().equals(name)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Add a task to the end of the task list
	 *
	 * @param task
	 *            the task to add.
	 */
	public void addTask(TaskModel task) {
		addTask(taskList.size(), task);
	}

	// TODO: Do the tasks need ordering? If not, let's replac this taskList
	// method and use a sorted collection for speed.
	/**
	 * Duplicate task names are handled by the Workflow.
	 *
	 * @param index
	 *            the index to insert the task at
	 * @param task
	 *            the task to add
	 */
	public void addTask(int index, TaskModel task) {
		taskList.add(index, task);
	}

	/**
	 * Remove a task from the current stage using the task name. Since there are
	 * multiple tasks with the same public name, this may be unclear. If there
	 * is only one task in the current stage with the given name, no issue
	 * should be raised.
	 *
	 * @param taskName
	 *            The name of the task to search for.
	 * 
	 * @return The removed task, null if no task removed.
	 */
	public TaskModel removeTaskByName(String taskName) {
		int count = 0;
		final List<TaskModel> possibleTasks = new ArrayList<TaskModel>();
		for (TaskModel existingTask : taskList) {
			if (existingTask.getName().equals(taskName)) {
				count++;
				possibleTasks.add(existingTask);
			}
		}
		switch (count) {
		case 0:
			// TODO: Log task non-existence, claim success?
			return null;
		case 1:
			taskList.remove(possibleTasks.get(0));
			return possibleTasks.get(0);
		default:
			// TODO: Log multiplicity of possible tasks
			// TODO: Prompt user for action, of:
			// 1. Specify a particular task (of the possible)
			// 2. Remove all
			// 3. Remove first (may be part of 1)
			// 4. No action
			return null;
		}
	}

	/**
	 * Remove a task from the current stage by id. Since id is unique, this will
	 * not have any duplication issues.
	 *
	 * @param id
	 *            The id of the task to remove.
	 * 
	 * @return The removed task, null if no task removed.
	 */
	public TaskModel removeTaskByID(String id) {
		TaskModel removedTask = null;
		for (TaskModel existingTask : taskList) {
			if (existingTask.getID().equals(id)) {
				taskList.remove(existingTask);
				removedTask = existingTask;
				break;
			}
		}
		return removedTask;
		// TODO: Log task non-existence, claim success?
	}

	/**
	 * Remove a task by object. This does not need to do any additional
	 * processing.
	 *
	 * @param task
	 *            The task to add
	 * 
	 * @return The removed task, null if no task removed.
	 */
	public TaskModel removeTask(TaskModel task) {
		if (taskList.contains(task)) {
			taskList.remove(task);
		}
		return task;
	}

	@Override
	public void save() {
	}

	@Override
	public void delete() {
	}

	@Override
	public String toJson() {
		return (new Gson()).toJson(this);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		boolean identify = false;
		if (o instanceof StageModel) {
			identify = ((StageModel) o).id.equals(id);
		}
		return identify;
	}

}
