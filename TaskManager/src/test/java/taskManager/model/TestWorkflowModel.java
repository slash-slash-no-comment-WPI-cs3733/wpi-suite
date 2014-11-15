package taskManager.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
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
		assertTrue(wf.toJson().contains("task 2"));
		assertTrue(wf.toJson().contains("task1"));
		assertTrue(wf.toJson().contains("duplicateNamedTask"));
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
	public void testMoveStage() {
		StageModel newStage = new StageModel(wf, "new stage");
		wf.addStage(newStage);
		List<StageModel> unmovedStageList = new LinkedList<StageModel>(
				wf.getStages());
		wf.moveStage(0, newStage);

		assertTrue(wf.getStages().get(0)
				.equals(unmovedStageList.get(unmovedStageList.size() - 1)));
		assertTrue(wf.getStages().get(1).equals(unmovedStageList.get(0)));
		assertEquals(unmovedStageList.size(), wf.getStages().size());
	}

	@Test
	public void testAddStageStageModel() {
		StageModel newStage = new StageModel(wf, "new stage");
		wf.addStage(newStage);
		assertTrue(wf.getStages().contains(newStage));
	}

	@Test
	public void testAddStageStageModelInt() {
		StageModel newStage = new StageModel(wf, "new stage");
		wf.addStage(newStage, 2);
		assertTrue(wf.getStages().contains(newStage));
		assertTrue(wf.getStages().get(2).equals(newStage));
	}

	@Test
	public void testHasStage() {
		StageModel newStage = new StageModel(wf, "new stage");
		wf.addStage(newStage, 2);
		assertTrue(wf.hasStage(newStage));
		assertTrue(wf.hasStage(wf.getStages().get(0)));
		assertFalse(wf.hasStage(new StageModel()));
	}

	@Test
	public void testFindStageByName() {
		StageModel newStage = new StageModel(wf, "new stage");
		wf.addStage(newStage, 2);

		assertNull(wf.findStageByName("non-existent stage"));
		assertEquals(wf.getStages().get(0), wf.findStageByName("stage1"));
		assertEquals(newStage, wf.findStageByName("new stage"));
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
		WorkflowModel newWf = new WorkflowModel();
		newWf.makeIdenticalTo(wf);
		assertEquals(wf.getID(), newWf.getID());
		assertEquals(wf.getStages(), newWf.getStages());
		assertEquals(wf.getObserver(), newWf.getObserver());
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
