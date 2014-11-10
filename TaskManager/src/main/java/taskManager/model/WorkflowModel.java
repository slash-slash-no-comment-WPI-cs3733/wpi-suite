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
	String name;

	/**
	 * Constructor for WorkflowModel.
	 */
	public WorkflowModel() {
		stageList = new ArrayList<StageModel>();
		// TODO Add default stages
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * 
	 * @return if the workflow contains the given stage
	 */
	public boolean hasStage(StageModel stage) {
		return stageList.contains(stage);

	}

	/**
	 * Find a StageModel in the workflow via its unique name
	 *
	 * @param stage
	 *            the name of the stage
	 * 
	 * @return the StageModel, null if non-existent
	 */
	public StageModel findStageByName(String stage) {
		for (StageModel existingStage : stageList) {
			if (existingStage.getName().equals(stage)) {
				return existingStage;
			}
		}
		return null;
	}

	/**
	 * Locates unique ID for a task, by appending '#' when conflicts appear
	 *
	 * @param newID
	 *            a potential ID
	 * @return a unique ID
	 */
	public String findUniqueTaskID(String newID) {
		for (StageModel stage : stageList) {
			for (TaskModel task : stage.getTasks()) {
				if (task.getID() == newID) {
					// Append '#' when conflicts appear
					return findUniqueTaskID(newID + '#');
				}
			}
		}
		return newID;
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
	 * 
	 * @return int error code, 1: task not found, 2: From stage not found, 3: To
	 *         stage not found.
	 */
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

	public void makeIdenticalTo(WorkflowModel workflow) {
		stageList = workflow.getStages();
		name = workflow.getName();
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
