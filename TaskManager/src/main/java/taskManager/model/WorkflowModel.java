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
 * An entire program workflow. Contains a number of {@link StageModel Stages}.
 *
 * @author Joseph Blackman
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 6, 2014
 */
public class WorkflowModel extends AbstractModel {
	List<StageModel> stageList;
	List<TaskModel> taskList;
	String name;

	/**
	 * Constructor for WorkflowModel.
	 */
	public WorkflowModel() {
		stageList = new ArrayList<StageModel>();
		// TODO Add default stages
	}

	/**
	 * Moves a stage currently in the WorkFlowModel to the given position on its
	 * list.
	 *
	 * @param s
	 *            Stage to be moved.
	 * @param index
	 *            Target location in the list
	 *
	 */
	public void moveStage(int index, StageModel s) {
		if (!stageList.contains(s)) {
			throw new IllegalArgumentException(
					"Stage being moved must already be in the workflow.");
		}
		if (stageList.size() <= index) {
			index = stageList.size();
		}
		if (index < 0) {
			index = 0;
		}
		stageList.remove(s);
		stageList.add(index, s);
		return;
	}

	/**
	 * Adds a stage to index items from the beginning of the WorkFlowModel Stage
	 * is added to the end of the current list.
	 *
	 * @param newStage
	 *            Stage to be added.
	 */
	public void addStage(StageModel newStage) {
		addStage(newStage, stageList.size());
	}

	/**
	 * Adds a stage to index items from the beginning of the WorkFlowModel
	 *
	 * @param newStage
	 *            Stage to be added.
	 * @param index
	 *            Index in the list of stages where we are adding the new stage.
	 */
	public void addStage(StageModel newStage, int index) {
		stageList.add(index, newStage);
	}

	/**
	 * Check if the workflow has a given stage
	 *
	 * @param stage
	 *            the stage to look for
	
	 * @return if the workflow contains the given stage */
	public boolean hasStage(StageModel stage) {
		return stageList.contains(stage);

	}

	/**
	 * Find a StageModel in the workflow via its unique name
	 *
	 * @param stage
	 *            the name of the stage
	
	 * @return the StageModel, null if non-existent */
	public StageModel findStageByName(String stage) {
		for (StageModel existingStage : stageList) {
			if (existingStage.getName().equals(stage)) {
				return existingStage;
			}
		}
		return null;
	}

	/**
	 * Gets a list of the stages in this workflow.
	 *
	 * @return the list of stages
	 */
	public List<StageModel> getStages() {
		return stageList;
	}

	/**
	 * Add a task to the current workflow. Naively assumes addition to the first
	 * stage.
	 *
	 * @param task
	 *            the task to add
	 *
	
	 * @return error code, 1 on stage non-existence. */
	public int addTask(TaskModel task) {
		return addTask(task, stageList.get(0));
	}

	/**
	 * Add a task to a given stage using the stage object.
	 *
	 * @param task
	 *            The new task to add
	 * @param stage
	 *            The stage to add the task to
	 *
	
	 * @return error code, 1 on stage non-existence. */
	public int addTask(TaskModel task, StageModel stage) {
		if (!stageList.contains(stage)) {
			addStage(stage);
			// TODO: Log stage addition
		}

		boolean isDuplicate;
		do {
			isDuplicate = false;
			// This code is not very efficient. It would be more efficient to
			// keep an internal (sorted, likely) list of task ids at the
			// workflow level.
			for (StageModel existingStage : stageList) {
				if (existingStage.containsTask(task)) {
					isDuplicate = true;
					task.setID(task.getID() + '#');
					break;
				}
			}
		} while (isDuplicate);
		// Our task should now have a unique internal ID.
		taskList.add(task);
		stage.addTask(task);
		task.setStage(stage);
		return 0;
	}

	/**
	 * Add a task to a given stage using the stage name.
	 *
	 * @param task
	 *            The new task to add
	 * @param stage
	 *            The stage to add the task to
	 *
	
	 * @return error code, 1 on stage non-existence. */
	public int addTask(TaskModel task, String stage) {
		for (StageModel existingStage : stageList) {
			if (existingStage.getName().equals(stage)) {
				return addTask(task, existingStage);
			}
		}
		return 1; // Stage does not exist.
		// TODO: Log stage non-existence.
	}

	/**
	 * Move a task from one stage to another by using the IDs of the task and
	 * stages
	 *
	 * @param taskID
	 *            The ID of the task to move
	 * @param fromStageName
	 *            The name of the stage moving from
	 * @param toStageName
	 *            The name of the stage moving to
	 *
	
	 * @return int error code, 1: task not found, 2: From stage not found, 3: To
	 *         stage not found. */
	public int moveTaskByID(String taskID, String fromStageName,
			String toStageName) {
		final TaskModel task = findTaskByID(taskID);
		if (task == null) {
			return 1;
		}
		final StageModel fromStage = findStageByName(fromStageName);
		if (fromStage == null) {
			return 2;
		}
		final StageModel toStage = findStageByName(toStageName);
		if (toStage == null) {
			return 3;
		}
		return moveTask(task, fromStage, toStage);
	}

	/**
	 * Move a task from one stage to another by using the IDs of the task and
	 * stages
	 *
	 * @param taskID
	 *            the ID of the task to move
	 * @param fromStage
	 *            the stage to move from
	 * @param toStage
	 *            the stage to move to
	
	 * @return int error code, 1: task not found, 2: From stage not found, 3: To
	 *         stage not found. */
	public int moveTaskByID(String taskID, StageModel fromStage,
			StageModel toStage) {
		final TaskModel task = findTaskByID(taskID);
		if (task == null) {
			return 1;
		}
		if (!hasStage(fromStage)) {
			return 2;
		}
		if (!hasStage(toStage)) {
			return 3;
		}
		return moveTask(task, fromStage, toStage);
	}

	/**
	 * Move a task using its non-unique task name
	 *
	 * @param task
	 *            the name of the task
	 * @param fromStageName
	 *            The name of the stage to move from
	 * @param toStageName
	 *            the name of the stage to move to
	
	 * @return int error code, 1: task not found, 2: From stage not found, 3: To
	 *         stage not found. */
	public int moveTaskByName(String task, String fromStageName,
			String toStageName) {
		final StageModel fromStage = findStageByName(fromStageName);
		if (fromStage == null) {
			return 2;
		}
		final StageModel toStage = findStageByName(toStageName);
		if (toStage == null) {
			return 3;
		}
		return moveTaskByName(task, fromStage, toStage);
	}

	/**
	 * Move a task using its non-unique task name
	 *
	 * @param task
	 *            The name of the task
	 * @param fromStage
	 *            the stage to move from
	 * @param toStage
	 *            the stage to move to
	
	 * @return int error code, 1: task not found, 2: From stage not found, 3: To
	 *         stage not found. */
	public int moveTaskByName(String task, StageModel fromStage,
			StageModel toStage) {
		final List<TaskModel> tasks = fromStage.findTaskByName(task);
		switch (tasks.size()) {
		case 0:
			return 1;
		case 1:
			return moveTask(tasks.get(0), fromStage, toStage);
		default:
			// TODO: Log multiplicity of possible tasks
			// TODO: Prompt user for action, of:
			// 1. Specify a particular task (of the possible)
			// 2. No action
			return -1;
		}
	}

	/**
	 * Move a task from one stage to another by using object references to the
	 * task and stages
	 *
	 * @param task
	 *            The task to move
	 * @param fromStage
	 *            The stage moving from
	 * @param toStage
	 *            The stage moving to
	 *
	
	 * @return int error code, 1: task not found, 2: From stage not found, 3: To
	 *         stage not found. */
	public int moveTask(TaskModel task, StageModel fromStage, StageModel toStage) {
		if (!stageList.contains(toStage)) {
			// TODO: Log to stage non-existence
			addStage(toStage);
		}
		if (!stageList.contains(fromStage)) {
			// TODO: Log from stage non-existence
			return 2;
		}
		if (!fromStage.containsTask(task)) {
			// TODO: Log task non-existence
			return 1;
		}
		toStage.addTask(task); // TODO: Check for duplicate on addition?
		fromStage.removeTask(task);
		return 0;
	}

	/**
	 * Check if the workflow has a given task
	 *
	 * @param task
	 *            The task to look for
	
	 * @return If the workflow has the task */
	public boolean hasTask(TaskModel task) {
		return taskList.contains(task);
	}

	/**
	 * Find a list of all tasks with the given name.
	 *
	 * @param task
	 *            the name to look for
	
	 * @return the list of possible tasks. Empty if none found. */
	public List<TaskModel> findTaskByName(String task) {
		final List<TaskModel> tasks = new ArrayList<TaskModel>();
		for (TaskModel existingTask : taskList) {
			if (existingTask.getName().equals(task)) {
				tasks.add(existingTask);
			}
		}
		return tasks;
	}

	/**
	 * Search for a task in the workflow by unique ID.
	 *
	 * @param task
	 *            the unique ID of the task.
	
	 * @return the TaskModel if exists, null otherwise. */
	public TaskModel findTaskByID(String task) {
		for (TaskModel existingTask : taskList) {
			if (existingTask.getID().equals(task)) {
				return existingTask;
			}
		}
		return null;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}

	/**
	 * Serializes this Workflow model into a JSON string.
	 *
	 * @return the JSON representation of this Workflow
	 */
	@Override
	public String toJson() {
		return null;
	}

	/**
	 * Compare a given object to the current workflow
	 *
	 * @param o
	 *            The object to compare against
	 * @return Whether the object matches the current workflow.
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof WorkflowModel) {
			return ((WorkflowModel) o).name.equals(name);
		}
		return false;
	}
}
