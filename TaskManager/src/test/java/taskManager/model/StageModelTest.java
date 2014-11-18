package taskManager.model;

import static org.junit.Assert.assertEquals;

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
	public void testTasklistSynchronization() {
		TaskModel task1 = new TaskModel("Task", stage);
		task1.setEstimatedEffort(5);
		TaskModel task2 = new TaskModel("Task", stage);
		task2.setEstimatedEffort(6);
		TaskModel task3 = new TaskModel("Task", stage);
		TaskModel task4 = new TaskModel("Task", stage);
		TaskModel task5 = new TaskModel("Task", stage);
		wf.save();
		stage.removeTask(task1);
		task1.setEstimatedEffort(10);
		stage.addTask(4, task1);
		stage = AbstractJsonableModel
				.fromJson(stage.toJson(), StageModel.class);
		wf.save();
		assertEquals(10, wf.findStageByName("Stage")
				.findTaskByID(task1.getID()).getEstimatedEffort());
		assertEquals(6, wf.findStageByName("Stage").findTaskByID(task2.getID())
				.getEstimatedEffort());
	}
}
