package taskManager.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;

public class TestObjectDuplication {

	private WorkflowModel clientWF;
	private WorkflowModel serverWF;
	StageModel clientSM1;
	StageModel clientSM2;
	StageModel serverSM1;
	StageModel serverSM2;
	TaskModel clientTM1;
	TaskModel clientTM2;

	@Before
	public void initialize() {
		clientWF = WorkflowModel.getInstance();
		serverWF = new WorkflowModel("Server");
		clientSM1 = new StageModel("Stage 1");
		clientSM2 = new StageModel("Stage 2");
		clientTM1 = new TaskModel("Task 1", clientSM1);
		clientTM2 = new TaskModel("Task 2", clientSM1);
		WorkflowModel clientWFtoServer = WorkflowModel.fromJson(
				clientWF.toJson(), WorkflowModel.class);
		serverWF.makeIdenticalTo(clientWFtoServer);
		serverSM1 = serverWF.findStageByName("Stage 1");
		serverSM2 = serverWF.findStageByName("Stage 2");
	}

	@After
	public void wipe() {
		clientWF.removeStage(clientSM1);
		clientWF.removeStage(clientSM2);
	}

	@Test
	public void stageDuplication() {
		clientWF.removeStage(clientSM1);
		// Stage 2 is "changed"
		new StageModel("Stage 3");
		StageModel serverSM2 = serverWF.findStageByName("Stage 2");
		WorkflowModel clientWFtoServer = WorkflowModel.fromJson(
				clientWF.toJson(), WorkflowModel.class);
		try {
			// Equivalent to .save()
			serverWF.makeIdenticalTo(clientWFtoServer);
		} catch (NullPointerException e) {
			// Thrown from .delete()
			assertEquals(e.getMessage(),
					"The networkConfiguration must not be null.");
		}
		StageModel newServerSM1 = serverWF.findStageByName("Stage 1");
		StageModel newServerSM2 = serverWF.findStageByName("Stage 2");
		StageModel newServerSM3 = serverWF.findStageByName("Stage 3");
		assertNull("Failed to delete stage", newServerSM1);
		assertTrue("Failed to modify stage", serverSM2 == newServerSM2);
		assertNotNull("Failed to create stage", newServerSM3);
	}

	@Test
	public void TaskDuplication() {
		clientSM1.removeTask(clientTM1);
		clientSM2.addTask(clientTM2);
		new TaskModel("Task 3", clientSM2);
		TaskModel serverTM2 = serverWF.findTaskByID("Task 2");
		StageModel clientSM1toServer = StageModel.fromJson(clientSM1.toJson(),
				StageModel.class);
		try {
			// Equivalent to .save()
			serverSM1.makeIdenticalTo(clientSM1toServer);
		} catch (NullPointerException e) {
			// Thrown from .delete()
			assertEquals(e.getMessage(),
					"The networkConfiguration must not be null.");
		}
		StageModel clientSM2toServer = StageModel.fromJson(clientSM2.toJson(),
				StageModel.class);
		try {
			// Equivalent to .save()
			serverSM2.makeIdenticalTo(clientSM2toServer);
		} catch (NullPointerException e) {
			// Thrown from .delete()
			assertEquals(e.getMessage(),
					"The networkConfiguration must not be null.");
		}

		TaskModel newServerTM1 = serverWF.findTaskByID("Task 1");
		TaskModel newServerTM2 = serverWF.findTaskByID("Task 2");
		TaskModel newServerTM3 = serverWF.findTaskByID("Task 3");
		assertNull("Failed to delete task", newServerTM1);
		// When moving tasks, we delete and add. Therefore, the two objects'
		// pointers are DIFFERENT.
		assertFalse("Failed to move task", serverTM2 == newServerTM2);
		assertNotNull("Failed to create task", newServerTM3);
	}

}
