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

	// The name of this stage; treated as ID and uniqueness enforced
	private String name;

	// Workflow that stage belongs to; not serialized
	private transient WorkflowModel workflow;

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
		this(workflow, name, -1, true);
		// TODO better way than passing -1 for index?
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
	 *            Index in the list to add stages too. -1 will add to end of
	 *            list
	 * @param removable
	 *            Whether or not the stage can be removed.
	 */
	public StageModel(WorkflowModel workflow, String name, int index,
			boolean removable) {
		// Enforce uniqueness of Stage names
		if (workflow.findStageByName(name) == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.removable = removable;
		this.workflow = workflow;
		taskList = new ArrayList<TaskModel>();
		if (index == -1) {
			workflow.addStage(this);
		} else {
			workflow.addStage(this, index);
		}
	}

	/**
	 * Get the workflow this stage belongs to
	 * 
	 * @return the workflow
	 */
	public WorkflowModel getWorkflow() {
		return workflow;
	}

	/**
	 * Set the workflow this stage belongs to
	 * 
	 * @param workflow
	 *            the workflow to set
	 */
	public void setWorkflow(WorkflowModel workflow) {
		this.workflow = workflow;
	}

	/**
	 * Get the name of the Stage.
	 *
	 * @return the name of the stage
	 */
	public String getName() {
		return name;
	}

	public List<TaskModel> getTasks() {
		return taskList;
	}

	public boolean isRemovable() {
		return removable;
	}

	/**
	 * Check if the current stage contains a given task.
	 *
	 * @param task
	 *            The task to look for
	 *
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
	 * 
	 * @return the task if found, null otherwise.
	 */
	public TaskModel findTaskByID(String id) {
		TaskModel task = null;
		for (TaskModel existingTask : taskList) {
			if (existingTask.getID().equals(id)) {
				task = existingTask;
				break;
			}
		}
		return task;
	}

	/**
	 * Check if the current stage contains a given task name
	 *
	 * @param name
	 *            The name of the task to look for
	 *
	 * 
	 * @return The number of different tasks by that name in the stage.
	 */
	public List<TaskModel> findTaskByName(String name) {
		final List<TaskModel> tasks = new ArrayList<TaskModel>();
		for (TaskModel existingTask : taskList) {
			if (existingTask.getName().equals(name)) {
				tasks.add(existingTask);
			}
		}
		return tasks;
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

	// TODO: Do the tasks need ordering? If not, let's replace this taskList
	// method and use a collection for speed.
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
	 * 
	 * @return The removed task, null if no task removed.
	 */
	public TaskModel removeTaskByID(String id) {
		for (TaskModel existingTask : taskList) {
			if (existingTask.getID().equals(id)) {
				taskList.remove(existingTask);
				return existingTask;
			}
		}
		// TODO: Log task non-existence, claim success?
		return null;
	}

	/**
	 * Remove a task by object. This does not need to do any additional
	 * processing.
	 *
	 * @param task
	 *            The task to add
	 *
	 * 
	 * @return The removed task, null if no task removed.
	 */
	public TaskModel removeTask(TaskModel task) {
		if (!taskList.contains(task)) {
			return null;
		}
		taskList.remove(task);
		return task;
	}

	public void makeIdenticalTo(StageModel stage) {
		taskList = stage.getTasks();
		name = stage.getName();
		workflow = stage.getWorkflow();
	}

	@Override
	public void save() {
		// TODO: Autogenerated method stub
	}

	@Override
	// TODO: Autogenerated method stub
	public void delete() {
	}

	@Override
	public String toJson() {
		// TODO: Autogenerated method stub
		return null;
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof StageModel) {
			return ((StageModel) o).name.equals(name);
		}
		return false;
	}

}
