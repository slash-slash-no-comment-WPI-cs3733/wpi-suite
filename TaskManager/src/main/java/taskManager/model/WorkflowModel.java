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

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
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

	private static WorkflowModel instance = null;

	public static boolean alive = true;
	public static final int timeout = 60000; // 1 minute

	// Generic logger
	private static final Logger logger = Logger.getLogger(WorkflowModel.class
			.getName());

	/**
	 * Required to create dummy instance; necessary for passing TaskModel type
	 * into DataStore. Should not be called manually.
	 *
	 */
	@Deprecated
	public WorkflowModel() {
	}

	/**
	 * Private for WorkflowModel.
	 */
	private WorkflowModel(String ID) {
		// set ID
		super(ID);
		reset();
	}

	/**
	 * Reset instance
	 */
	public void reset() {
		stageList = new ArrayList<StageModel>();

		alive = true;
	}

	/**
	 * Returns singleton instance WorkflowModel
	 *
	 * @return singleton instance
	 */
	public static WorkflowModel getInstance() {
		if (instance == null) {
			instance = new WorkflowModel("defaultWorkflow");
		}
		return instance;
	}

	/**
	 * Tells the other threads to die
	 *
	 */
	public static void dispose() {
		alive = false;
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
	 * @param stage
	 *            Stage to be added.
	 * @return whether the workflow changed as a result
	 */
	public boolean addStage(StageModel stage) {
		if (stageList.contains(stage)) {
			return false;
		}
		return addStage(stage, -1);
	}

	/**
	 * Adds a stage to index items from the beginning of the WorkFlowModel
	 *
	 * @param stage
	 *            Stage to be added.
	 * @param index
	 *            Index in the list of stages where we are adding the new stage.
	 * @return whether the workflow changed as a result
	 */

	public boolean addStage(StageModel stage, int index) {
		if (index == -1 || index > stageList.size()) {// add to end of list
			index = stageList.size();
		}

		// if nothing changed
		if (index != stageList.size() && stageList.get(index).equals(stage)) {
			return false;
		}

		if (!stageList.contains(stage)) {
			logger.log(Level.FINER, "Stage " + stage.getName() + " added.");
		} else {
			stageList.remove(stage);
		}

		stageList.add(index, stage);
		return true;
	}

	/**
	 * Remove a stage by object. This does not need to do any additional
	 * processing.
	 *
	 * @param stage
	 *            The stage to remove
	 * 
	 * @return The removed stage
	 */
	public StageModel removeStage(StageModel stage) {
		if (!stageList.contains(stage)) {
			logger.log(Level.WARNING,
					"Tried to remove a stage that did not exist.");
			throw new IllegalArgumentException("No such stage.");
		}
		stageList.remove(stage);
		return stage;
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
		while (findTaskByID(newID) != null) {
			// Append '#' when conflicts appear
			newID += '#';
		}
		return newID;
	}

	/**
	 * Searches the workflow for a task with the given id
	 *
	 * @param id
	 *            the id of the task to find
	 * @return the TaskModel with the given id, or null if no task is found
	 */
	public TaskModel findTaskByID(String id) {
		for (StageModel stage : stageList) {
			TaskModel task = stage.findTaskByID(id);
			if (task != null) {
				return task;
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
	 * Changes this workflowmodel to be identical to the inputed workflow model,
	 * while maintaining the pointer
	 *
	 * @param workflow
	 *            The workflow to copy
	 */
	public void makeIdenticalTo(WorkflowModel incomingWorkflow) {
		setID(incomingWorkflow.getID());
		final List<StageModel> localStages = stageList;
		final List<StageModel> incomingStages = incomingWorkflow.getStages();
		boolean stageWasUsed[] = new boolean[localStages.size()];
		List<StageModel> toSaveStages = new ArrayList<StageModel>();
		for (StageModel incomingStage : incomingStages) {
			StageModel toSaveStage = this.findStageByName(incomingStage
					.getName());
			if (toSaveStage != null) {
				toSaveStages.add(toSaveStage);
				stageWasUsed[localStages.indexOf(toSaveStage)] = true;
			} else {
				toSaveStages.add(incomingStage);
			}
		}
		stageList = toSaveStages; // This does not change localStages
		for (int i = localStages.size() - 1; i >= 0; i--) {
			if (!stageWasUsed[i]) {
				// Delete any stages that are no longer in the stage list.
				try {
					localStages.get(i).delete();
				} catch (NullPointerException e) {
					// TODO: This gets thrown during tests fairly often.
					if (!e.getMessage().equals(
							"The networkConfiguration must not be null.")) {
						throw new NullPointerException(e.getMessage());
					}
				}
			}
		}
		System.out.println(stageList.size());
	}

	/**
	 * Rebuild the stage->workflow and task->stage references for all objects in
	 * workflow
	 *
	 */
	public void rebuildAllRefs() {
		for (StageModel stage : stageList) {
			stage.rebuildTaskRefs();
		}
	}

	@Override
	public void save() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/workflow/" + getID(), HttpMethod.POST);
		request.setBody(toJson());
		System.out.println("Saving " + getClass() + ": " + toJson());
		request.addObserver(getObserver());
		request.send();
	}

	@Override
	public void delete() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/workflow/" + getID(), HttpMethod.DELETE);
		request.setBody(toJson());
		System.out.println("Deleting " + getClass() + ": " + toJson());
		request.addObserver(getObserver());
		request.send();
	}

	/**
	 * Opens a connection to the server asking for workflows. The server will
	 * respond when there is a change or the timeout happens
	 *
	 */
	public static void update() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/workflow", HttpMethod.GET);
		request.addObserver(new FetchWorkflowObserver());
		request.addHeader("long-polling",
				Integer.toString(WorkflowModel.timeout));
		// wait timeout + 5 sec (to allow for round trip time + database
		// interaction)
		request.setReadTimeout(WorkflowModel.timeout + 5 * 1000);
		request.send();
	}

	/**
	 * Asks the server to immediately give us all the workflows
	 *
	 */
	public static void updateNow() {
		final Request request = Network.getInstance().makeRequest(
				"taskmanager/workflow", HttpMethod.GET);
		request.addObserver(new FetchWorkflowObserver(false));
		request.send();
	}

	/**
	 * Request a list of users from the server
	 *
	 */
	public static void updateUsers() {
		final Request request = Network.getInstance().makeRequest("core/user",
				HttpMethod.GET);
		request.addObserver(new GetUsersObserver());
		request.addHeader("long-polling",
				Integer.toString(WorkflowModel.timeout));
		// wait timeout + 5 sec (to allow for round trip time + database
		// interaction)
		request.setReadTimeout(WorkflowModel.timeout + 5 * 1000);
		request.send();
	}

	@Override
	public void setProject(Project p) {
		super.setProject(p);
		System.out.println("setting workflow project");
		for (StageModel s : getStages()) {
			s.setProject(p);
		}
	}
}
