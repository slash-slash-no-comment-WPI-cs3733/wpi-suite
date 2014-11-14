package taskManager.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestWorkflowModel {
	private WorkflowModel wf;

	@Before
	public void setUp() {
		wf = new WorkflowModel("wf1");
		StageModel stage = new StageModel(wf, "stage1");
		TaskModel task1 = new TaskModel("task1", stage);
		TaskModel task2 = new TaskModel("task 2", stage);
		TaskModel task3 = new TaskModel("duplicateNamedTask", stage);
		TaskModel task4 = new TaskModel("duplicateNamedTask", stage);

		wf.addStage(stage);

		stage.addTask(task1);
		stage.addTask(task2);
		stage.addTask(task3);
		stage.addTask(task4);

	}

	@Test
	public void testToJson() {
		assertTrue(wf.toJson().matches("task1|task 2|duplicateNamedTask"));
		assertTrue(wf.toJson().contains("stage1"));
	}

	@Test
	public void testWorkflowModelString() {
		assertNotNull(new WorkflowModel("name"));
	}

	@Test
	public void testWorkflowModel() {
		assertNotNull(new WorkflowModel());
	}

	@Test
	public void testGetName() {
		assertEquals("wf1", wf.getName());
	}

	@Test
	public void testSetName() {
		wf.setName("New name");
		assertEquals("New name", wf.getName());
	}

	@Test
	public void testMoveStage() {
		StageModel newStage = new StageModel(wf, "new stage");
		wf.addStage(newStage);
		List<StageModel> unmovedStageList = wf.getStages();
		wf.moveStage(0, newStage);

		assertTrue(wf.getStages().get(0).equals(unmovedStageList.get(1)));
		assertTrue(wf.getStages().get(1).equals(unmovedStageList.get(0)));

	}

	@Test
	public void testAddStageStageModel() {
		StageModel newStage = new StageModel(wf, "new stage");
		wf.addStage(newStage);
		assertTrue(wf.getStages().contains(newStage));
	}

	@Test
	public void testAddStageStageModelInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasStage() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindStageByName() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUniqueTaskID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStages() {
		assertEquals("stage1", wf.getStages().get(0).getName());
	}

	@Test
	public void testMoveTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeIdenticalToWorkflowModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testRebuildAllRefs() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromJsonString() {
		WorkflowModel wfCopy = WorkflowModel.fromJson(wf.toJson());
		assertTrue(wfCopy.toJson().equals(wf.toJson()));
	}

	@Test
	public void testIdentify() {
		fail("Not yet implemented");
	}

}
