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
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserSerializer;

/**
 * An entire program workflow. Contains a number of {@link StageModel Stages}.
 *
 * @author Joseph Blackman
 * @author Sam Khalandovsky
 * @author Ezra
 * @version Nov 6, 2014
 */
public class WorkflowModel extends AbstractModel {
	List<StageModel> stagesList;
	String name;

	public WorkflowModel() {
		stagesList = new ArrayList<StageModel>();
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
		if (!stagesList.contains(s)) {
			throw new IllegalArgumentException(
					"Stage being moved must already be in the workflow.");
		}
		if (stagesList.size() <= index) {
			index = stagesList.size();
		}
		if (index < 0) {
			index = 0;
		}
		stagesList.remove(s);
		stagesList.add(index, s);
		return;
	}

	/**
	 * Adds a stage to index items from the beginning of the WorkFlowModel Stage
	 * is added to the end of the current list.
	 *
	 * @param StageModel
	 *            newStage Stage to be added.
	 */
	public void addStage(StageModel newStage) {
		addStage(newStage, stagesList.size());
	}

	/**
	 * Adds a stage to index items from the beginning of the WorkFlowModel
	 *
	 * @param int index Index in the list of stages where we are adding the new
	 *        stage.
	 * @param StageModel
	 *            newStage Stage to be added.
	 */
	public void addStage(StageModel newStage, int index) {
		for (StageModel stage : stagesList) {
			if (stage.getID().equals(newStage.getID())) {
				newStage.setID(newStage.getID() + '#');
				addStage(newStage, index);
				return;
			}
		}
		stagesList.add(index, newStage);
	}

	/**
	 * Add a task to the current workflow. Naively assumes addition to the first
	 * stage.
	 *
	 * @param task
	 *            the task to add
	 * @return error code, 1 on stage non-existence.
	 */
	public int addTask(TaskModel task) {
		return addTask(task, stagesList.get(0));
	}

	/**
	 * Add a task to a given stage using the stage object.
	 *
	 * @param task
	 *            The new task to add
	 * @param stage
	 *            The stage to add the task to
	 * @return error code, 1 on stage non-existence.
	 */
	public int addTask(TaskModel task, StageModel stage) {
		if (!stagesList.contains(stage)) {
			addStage(stage);
			// TODO: Log stage addition
		}

		boolean isDuplicate;
		do {
			isDuplicate = false;
			// This code is not very efficient. It would be more efficient to
			// keep an internal (sorted, likely) list of task ids at the
			// workflow level.
			for (StageModel existingStage : stagesList) {
				if (existingStage.containsTaskID(task.getID())) {
					isDuplicate = true;
					task.setID(task.getID() + '#');
					break;
				}
			}
		} while (isDuplicate == true);
		// Our task should now have a unique internal ID.
		stage.addTask(task);
		task.setStage(stage.getName());
		return 0;
	}

	/**
	 * Add a task to a given stage using the stage name.
	 *
	 * @param task
	 *            The new task to add
	 * @param stage
	 *            The stage to add the task to
	 * @return error code, 1 on stage non-existence.
	 */
	public int addTask(TaskModel task, String stage) {
		for (StageModel existingStage : stagesList) {
			if (existingStage.getName().equals(stage)) {
				return addTask(task, existingStage);
			}
		}
		return 1; // Stage does not exist.
		// TODO: Log stage non-existence.
	}

	public int moveTaskByID(String task, String fromStage, String toStage) {
		StageModel fromStageObject = null;
		StageModel toStageObject = null;
		for (StageModel existingStage : stagesList) {
			if (existingStage.getID().equals(fromStage)) {
				fromStageObject = existingStage;
			}
			if (existingStage.getID().equals(toStage)) {
				toStageObject = existingStage;
			}
		}
		if (toStageObject == null) {
			// TODO: Log to stage non-existence
			return 3;
		}
		if (fromStageObject == null) {
			// TODO: Log from stage non-existence
			return 2;
		}
		TaskModel taskObject = null;
		if (!fromStageObject.containsTaskID(task)) {
			// TODO: Log task non-existence
			return 1;
		}
		toStageObject.addTask(taskObject);
		fromStageObject.removeTaskByID(task);
		return 0;
	}

	public int moveTaskByName(String task, String fromStage, String toStage) {
		// TODO: Implement. Tasks may be duplicate.
		return -1;
	}

	public int moveTask(TaskModel task, StageModel from, StageModel to) {
		if (!stagesList.contains(to)) {
			// TODO: Log to stage non-existence
			addStage(to);
		}
		if (!stagesList.contains(from)) {
			// TODO: Log from stage non-existence
			return 2;
		}
		if (!from.containsTask(task)) {
			// TODO: Log task non-existence
			return 1;
		}
		to.addTask(task); // TODO: Check for duplicate on addition?
		from.removeTask(task);
		return 0;
	}

	/**
	 * Gets a list of the stages in this workflow.
	 *
	 * @return the list of stages
	 */
	public List<StageModel> getStages() {
		return stagesList;
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
		Gson gson = new GsonBuilder().registerTypeAdapter(WorkflowModel.class,
				new UserSerializer()).create();
		return gson.toJson(this, User.class);
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
			return ((WorkflowModel) o).name == name;
		}
		return false;
	}
}
