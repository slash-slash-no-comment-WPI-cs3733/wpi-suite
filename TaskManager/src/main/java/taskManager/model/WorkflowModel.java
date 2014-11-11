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

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * An entire program workflow. Contains a number of {@link StageModel Stages}.
 *
 * @author Joseph Blackman
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 6, 2014
 */
public class WorkflowModel extends AbstractJsonableModel<WorkflowModel> {
	// List of stages in the workflow.
	List<StageModel> stageList;
	String name;

	private static final Logger logger = Logger.getLogger(WorkflowModel.class
			.getName());

	/**
	 * Constructor for WorkflowModel.
	 */
	public WorkflowModel(String name) {
		// set ID
		super(name);
		stageList = new ArrayList<StageModel>();
		// TODO Add default stages
	}

	public WorkflowModel() {
		this(null);
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
			logger.log(Level.WARNING, "Stage not in workflow");
			throw new IllegalArgumentException(
					"Stage being moved must already be in the workflow.");
		}

		if (stageList.size() <= index) {
			index = stageList.size() - 1;

		}
		if (index < 0) {
			index = 0;
		}
		stageList.remove(s);
		stageList.add(index, s);
		logger.log(Level.FINER, "Stage " + s.getName() + " moved.");
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
		logger.log(Level.FINER, "Stage " + newStage.getName() + " added.");
	}

	/**
	 * Check if the workflow has a given stage
	 *
	 * @param stage
	 *            the stage to look for
	 *
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
	 * 
	 * @return a unique ID
	 */
	public String findUniqueTaskID(String newID) {
		for (StageModel stage : stageList) {
			for (TaskModel task : stage.getTasks()) {
				if (task.getID().equals(newID)) {
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
	 */
	public void moveTask(TaskModel task, StageModel fromStage,
			StageModel toStage) {
		if (!stageList.contains(toStage)) {
			logger.log(Level.FINE,
					"Tried to move a task to a non-existant stage. Stage was created.");
			addStage(toStage);
		}
		if (!stageList.contains(fromStage)) {
			logger.log(Level.WARNING,
					"Tried to move a task from a non-existant stage.");
			throw new IndexOutOfBoundsException("No such stage.");
		}
		if (!fromStage.containsTask(task)) {
			logger.log(Level.WARNING,
					"Tried to move a task that does not exist.");
			throw new IndexOutOfBoundsException("No such task.");
		}
		final ActivityModel movedTask = new ActivityModel("Moved task",
				ActivityModel.activityModelType.MOVE);
		toStage.addTask(task);
		fromStage.removeTask(task);
		task.addActivity(movedTask);
		logger.log(Level.FINER, "Moved task successfully.");
	}

	/**
	 * Changes this workflowmodel to be identical to the inputted stage model,
	 * while maintaining the pointer
	 *
	 * @param workflow
	 *            The workflow to copy
	 */
	public void makeIdenticalTo(WorkflowModel workflow) {
		setID(workflow.getID());
		stageList = workflow.getStages();
		name = workflow.getName();
	}

	@Override
	public void save() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/workflow", HttpMethod.POST);
		request.setBody(toJson());
		request.addObserver(getObserver());
		request.send();
	}

	@Override
	public void delete() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/workflow", HttpMethod.DELETE);
		request.setBody(toJson());
		request.addObserver(getObserver());
		request.send();
	}

	@Override
	public String toJson() {
		final Gson gson = new Gson();
		return gson.toJson(this);
	}

	/**
	 * Static method for deserializing object from JSON
	 *
	 * @param serialized
	 *            JSON string
	 * 
	 * @return the deserialized TaskModel
	 */
	public static WorkflowModel fromJson(String serialized) {
		final Gson gson = new Gson();
		return gson.fromJson(serialized, WorkflowModel.class);
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
