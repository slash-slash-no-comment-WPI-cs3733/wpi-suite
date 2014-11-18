package taskManager.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import taskManager.TestLogin;

public class StageModelTest {
	WorkflowModel wf;
	StageModel stage;

	@Before
	public void initializeWorkflow() {
		TestLogin.login();
		wf = new WorkflowModel("Workflow");
		stage = new StageModel(wf, "Stage");
	}

	@Test
	public void testTasklistSynchronizationAddition() {
		TaskModel task1 = new TaskModel("Task", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		TaskModel task2 = new TaskModel("Task2", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		assertNotNull(wf.findStageByName("Stage").findTaskByID("Task2"));
	}

	@Test
	public void testTasklistSynchronizationDeletion() {
		TaskModel task1 = new TaskModel("Task1", stage);
		TaskModel task2 = new TaskModel("Task2", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		stage.removeTask(task2);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		assertNull(wf.findStageByName("Stage").findTaskByID("Task2"));
	}

	@Test
	public void testTasklistSynchronizationMove() {
		StageModel stage2 = new StageModel(wf, "Stage 2");
		TaskModel task1 = new TaskModel("Task1", stage);
		TaskModel task2 = new TaskModel("Task2", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		wf.moveTask(task2, stage, stage2);
		assertNull(wf.findStageByName("Stage").findTaskByID("Task2"));
		assertNotNull(wf.findStageByName("Stage2").findTaskByID("Task2"));
	}
}
