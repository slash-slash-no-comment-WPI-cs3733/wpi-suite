package taskManager.controller;

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
	TaskModel tm1;
	TaskModel tm2;

	@Before
	public void initialize() {
		clientWF = WorkflowModel.getInstance();
		serverWF = new WorkflowModel("Server");
		clientSM1 = new StageModel("Stage 1");
		clientSM2 = new StageModel("Stage 2");
		tm1 = new TaskModel("Task 1", clientSM1);
		tm2 = new TaskModel("Task 2", clientSM1);
		WorkflowModel clientWFtoServer = WorkflowModel.fromJson(
				clientWF.toJson(), WorkflowModel.class);
		serverWF.makeIdenticalTo(clientWFtoServer);
	}

	@After
	public void wipe() {
		clientWF.deleteStage(clientSM1);
		clientWF.deleteStage(clientSM2);
	}

	@Test
	public void stageDuplication() {
		clientWF.deleteStage(clientSM1);
		// Stage 2 is "changed"
		new StageModel("Stage 3");
		WorkflowModel clientWFtoServer = WorkflowModel.fromJson(
				clientWF.toJson(), WorkflowModel.class);
		StageModel serverSM2 = serverWF.findStageByName("Stage 2");
		System.out.println(serverSM2);
		serverWF.makeIdenticalTo(clientWFtoServer); // Equivalent to .save()
		StageModel newServerSM1 = serverWF.findStageByName("Stage 1");
		StageModel newServerSM2 = serverWF.findStageByName("Stage 2");
		System.out.println(serverSM2);
		System.out.println(newServerSM2);
		StageModel newServerSM3 = serverWF.findStageByName("Stage 3");
		assertNull(newServerSM1);
		assertTrue(serverSM2 == newServerSM2);
		assertNotNull(newServerSM3);
	}

	@Test
	public void TaskDuplication() {
		clientSM1.deleteTask(tm1);
		tm2.setActualEffort(5);
		new TaskModel("Task 3", clientSM2);
		StageModel clientSM1toServer = StageModel.fromJson(clientSM1.toJson(),
				StageModel.class);
		StageModel serverSM1 = serverWF.findStageByName("Stage 1");
		serverSM1.makeIdenticalTo(clientSM1toServer); // Equivalent to .save()
		StageModel clientSM2toServer = StageModel.fromJson(clientSM1.toJson(),
				StageModel.class);
		StageModel serverSM2 = serverWF.findStageByName("Stage 2");
		serverSM2.makeIdenticalTo(clientSM2toServer); // Equivalent to .save()

	}

}
