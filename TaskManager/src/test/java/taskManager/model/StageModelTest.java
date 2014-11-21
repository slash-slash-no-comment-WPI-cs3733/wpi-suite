package taskManager.model;

import static org.junit.Assert.assertEquals;
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
		wf = WorkflowModel.getInstance();
		wf.makeIdenticalTo(new WorkflowModel("Workflow"));
		stage = new StageModel("Stage");
	}

	@Test
	public void testTasklistSynchronizationAddition() {
		TaskModel task1 = new TaskModel("Task", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		wf.rebuildAllRefs();
		stage = wf.findStageByName("Stage");
		TaskModel task2 = new TaskModel("Task2", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		assertNotNull(wf.findStageByName("Stage").findTaskByID("Task2"));
	}

	@Test
	public void testTasklistSynchronizationDeletion() {
		TaskModel task = new TaskModel("Task", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		stage = wf.findStageByName("Stage");
		task = wf.findTaskByID("Task");
		stage.removeTask(task);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		assertNull(wf.findStageByName("Stage").findTaskByID("Task"));
	}

	@Test
	public void testTasklistSynchronizationMove() {
		StageModel stage2 = new StageModel("Stage2");
		TaskModel task = new TaskModel("Task", stage);
		task.setEstimatedEffort(5);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		stage = wf.findStageByName("Stage");
		stage2 = wf.findStageByName("Stage2");
		task = wf.findTaskByID("Task");
		assertEquals(task.getEstimatedEffort(), 5);
		task.setEstimatedEffort(10);
		wf.moveTask(task, stage, stage2);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.save();
		stage = wf.findStageByName("Stage");
		stage2 = wf.findStageByName("Stage2");
		task = wf.findTaskByID("Task");
		assertNull(wf.findStageByName("Stage").findTaskByID("Task"));
		assertNotNull(wf.findStageByName("Stage2").findTaskByID("Task"));
		assertEquals(task.getEstimatedEffort(), 10);
	}
}
