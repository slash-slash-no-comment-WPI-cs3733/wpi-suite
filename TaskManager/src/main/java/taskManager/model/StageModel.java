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
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

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

public class StageModel extends AbstractJsonableModel<StageModel> {

	// Generic logger
	private static final Logger logger = Logger.getLogger(StageModel.class
			.getName());

	// List of tasks in this stage
	private List<TaskModel> taskList;

	// The name of this stage; treated as ID and uniqueness enforced
	private String name;

	// Workflow that stage belongs to; not serialized
	private transient final WorkflowModel workflow = WorkflowModel
			.getInstance();

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
	public StageModel(String name) {
		this(name, true);
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
	public StageModel(String name, boolean removable) {
		this(name, -1, removable);
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
	public StageModel(String name, int index) {
		this(name, index, true);
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
	public StageModel(String name, int index, boolean removable) {
		// Set name as ID
		super(name);
		// Enforce uniqueness of Stage names
		if (workflow.findStageByName(name) != null) {
			throw new IllegalArgumentException("Stage name must be unique.");
		}
		this.name = name;
		this.removable = removable;

		taskList = new ArrayList<TaskModel>();
		if (index == -1) {
			workflow.addStage(this);
		} else {
			workflow.addStage(this, index);
		}
	}

	/**
	 * Required to create dummy instance Necessary for passing TaskModel type
	 * into DataStore *
	 *
	 *
	 */
	public StageModel() {
	};

	/**
	 * For each task in this stage, rebuild reference to stage
	 *
	 */
	public void rebuildTaskRefs() {
		for (TaskModel task : taskList) {
			task.setStage(this);
		}
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
	 *
	 * @return the task if found, null otherwise.
	 */
	public TaskModel findTaskByID(String id) {
		for (TaskModel existingTask : taskList) {
			if (existingTask.getID().equals(id)) {
				return existingTask;
			}
		}
		return null;
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
	public boolean containsTaskByID(String id) {
		boolean contains = false;
		for (TaskModel existingTask : taskList) {
			if (existingTask.getID().equals(id)) {
				contains = true;
				break;
			} else {
				contains = false;
			}
		}
		return contains;
	}

	/**
	 * Check if the current stage contains a given task name
	 *
	 * @param name
	 *            The name of the task to look for
	 *
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
	 * Add a task to the end of the task list. If it is already in the stage, do
	 * nothing.
	 *
	 * @param task
	 *            the task to add.
	 * @return whether the stage changed as a result
	 */
	public boolean addTask(TaskModel task) {
		if (taskList.contains(task)) {
			return false;
		}
		return addTask(task, -1);
	}

	/**
	 * Add task to a given index in this stage
	 * 
	 * @param task
	 *            the task to add
	 * @param index
	 *            the index to insert the task at
	 * @return whether the stage changed as a result
	 */
	public boolean addTask(TaskModel task, int index) {
		if (index == -1 || index > taskList.size()) {// add to end of list
			index = taskList.size();
		}

		// if nothing changed
		if (index != taskList.size() && taskList.get(index).equals(task)) {
			return false;
		}

		StageModel oldStage = task.getStage();
		if (oldStage != null) {
			if (oldStage.containsTask(task)) {
				oldStage.removeTask(task); // remove from old parent, or this
											// stage
			}

			// Only add add activity if coming from different stage
			if (!this.equals(oldStage)) {

				// since this is a move, add relevant activity
				final ActivityModel movedTask = new ActivityModel("Moved task "
						+ task.getName() + " from stage " + oldStage.getName()
						+ " to stage " + name + ".",
						ActivityModel.activityModelType.MOVE);
				task.addActivity(movedTask);
			}
		}

		taskList.add(index, task);
		task.setStage(this);

		return true;
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
	 *
	 * @return The removed task, null if no task removed.
	 */
	public TaskModel removeTaskByName(String taskName) {
		final List<TaskModel> possibleTasks = findTaskByName(taskName);
		switch (possibleTasks.size()) {
		case 0:
			logger.log(Level.WARNING,
					"Tried to remove a task that did not exist.");
			throw new IndexOutOfBoundsException("No such task.");
		case 1:
			taskList.remove(possibleTasks.get(0));
			return possibleTasks.get(0);
		default:
			logger.log(Level.FINE,
					"Tried to remove a task, but multiple available");
			throw new IllegalArgumentException(
					"Referenced task could refer to multiple.");
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
	 *
	 * @return The removed task, null if no task removed.
	 */
	public TaskModel removeTaskByID(String id) {
		for (TaskModel existingTask : taskList) {
			if (existingTask.getID().equals(id)) {
				taskList.remove(existingTask);
				logger.log(Level.FINER, "Removed task by id: " + id + ".");
				return existingTask;
			}
		}
		logger.log(Level.WARNING, "Tried to remove a task that did not exist.");
		throw new IndexOutOfBoundsException("No such task.");
	}

	/**
	 * Remove a task by object. This does not need to do any additional
	 * processing.
	 *
	 * @param task
	 *            The task to add
	 *
	 *
	 *
	 * @return The removed task, null if no task removed.
	 */
	public TaskModel removeTask(TaskModel task) {
		if (!taskList.contains(task)) {
			logger.log(Level.WARNING,
					"Tried to remove a task that did not exist.");
			throw new IndexOutOfBoundsException("No such task.");
		}
		taskList.remove(task);
		return task;
	}

	/**
	 * Changes this stagemodel to be identical to the inputted stage model,
	 * while maintaining the pointer
	 *
	 * @param stage
	 *            The stage to copy
	 */
	public void makeIdenticalTo(StageModel stage) {
		setID(stage.getID());
		name = stage.getName();
	}

	@Override
	public void save() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/stage", HttpMethod.POST);
		request.setBody(toJson());
		request.addObserver(getObserver());
		request.send();
	}

	@Override
	public void delete() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/stage", HttpMethod.DELETE);
		request.setBody(toJson());
		request.addObserver(getObserver());
		request.send();
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
