/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskManager.ClientDataStore;
import taskManager.TaskManager;
import taskManager.MockNetwork;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.network.Network;

/**
 * @author Joseph Blackman
 * @version Nov 12, 2014
 */
public class WorkflowModelTest {
	private WorkflowModel wm;
	private StageModel sm1;
	private StageModel sm2;

	@BeforeClass
	public static void netSetup() {
		Network.setInstance(new MockNetwork(true));
	}

	@Before
	public void setup() {
		TaskManager.reset();

		wm = WorkflowModel.getInstance();
		wm.reset();

		sm1 = new StageModel("Stage1");
		sm2 = new StageModel("Stage2");
	}

	public void verifyWorkflow(WorkflowModel a, WorkflowModel b) {
		assertTrue("Workflows have different names", a.getID()
				.equals(b.getID()));
		List<StageModel> aStages = a.getStages();
		List<StageModel> bStages = b.getStages();
		if (aStages.size() != bStages.size()) {
			fail("Stage lists not equal size.");
		}
		for (int i = 0; i < aStages.size(); i++) {
			try {
				verifyStage(aStages.get(i), bStages.get(i));
			} catch (AssertionError e) {
				fail("Stage #" + i + " does not match: " + e.getMessage());
			}
		}
	}

	public void verifyStage(StageModel a, StageModel b) {
		assertTrue("Stages have different names",
				a.getName().equals(b.getName()));
		List<TaskModel> aTasks = a.getTasks();
		List<TaskModel> bTasks = b.getTasks();
		if (aTasks.size() != bTasks.size()) {
			fail("Task lists not equal size.");
		}
		for (int i = 0; i < aTasks.size(); i++) {
			try {
				verifyTask(aTasks.get(i), bTasks.get(i));
			} catch (AssertionError e) {
				fail("Task #" + i + " does not match: " + e.getMessage());
			}
		}
	}

	public void verifyTask(TaskModel a, TaskModel b) {
		assertTrue("Tasks have different names", a.getName()
				.equals(b.getName()));
		assertTrue("Tasks have different IDs", a.getID().equals(b.getID()));
		assertTrue(a.getDescription().equals(b.getDescription()));
		// Could verify more, but this should be sufficient for test purposes.
	}

	@Test
	public void basicStageOperations() {
		StageModel sm3 = new StageModel("Stage3");
		StageModel sm4 = new StageModel("Stage4");
		String inorder = "";
		for (StageModel s : wm.getStages()) {
			assertTrue("Workflow missing included stage", wm.hasStage(s));
			inorder += s.toJson();
		}
		wm.moveStage(0, sm4);
		wm.moveStage(1, sm3);
		wm.moveStage(2, sm2);
		wm.moveStage(3, sm1);
		String revorder = "";
		for (StageModel s : wm.getStages()) {
			revorder = s.toJson() + revorder;
		}
		assertEquals("Stages did not move properly", inorder, revorder);
		WorkflowModel wmcpy = AbstractTaskManagerModel.fromJson(wm.toJson(),
				WorkflowModel.class);
		verifyWorkflow(wm, wmcpy);
		StageModel sm4cpy = AbstractTaskManagerModel.fromJson(sm4.toJson(),
				StageModel.class);
		verifyStage(sm4, sm4cpy);
	}

	@Test
	public void testRemoveStage() {
		wm.removeStage(sm1);
		assertFalse(wm.getStages().contains(sm1));
		assertTrue(wm.getStages().contains(sm2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveInvalidStage() {
		wm.removeStage(new StageModel());
	}

	@Test
	public void testSetProject() {
		Project p = new Project("Name", "ID");
		TaskModel tm1 = new TaskModel("T1", sm1);
		TaskModel tm2 = new TaskModel("T1", sm2);
		TaskModel tm3 = new TaskModel("T1", sm2);

		wm.setProject(p);

		assertSame(p, wm.getProject());
		assertSame(p, sm1.getProject());
		assertSame(p, sm2.getProject());
		assertSame(p, tm3.getProject());
		assertSame(p, tm2.getProject());
		assertSame(p, tm3.getProject());
	}

	@Test
	public void basicTaskOperations() {
		TaskModel t1 = new TaskModel("Task1", sm1);
		TaskModel t2 = new TaskModel("Task1", sm1);
		TaskModel t3 = new TaskModel("Task1", sm1);
		TaskModel t4 = new TaskModel("Task1", sm1);
		assertFalse("Duplicate IDs 1 & 2", t1.getID().equals(t2.getID()));
		assertFalse("Duplicate IDs 1 & 3", t1.getID().equals(t3.getID()));
		assertFalse("Duplicate IDs 1 & 4", t1.getID().equals(t4.getID()));
		assertFalse("Duplicate IDs 2 & 3", t2.getID().equals(t3.getID()));
		assertFalse("Duplicate IDs 2 & 4", t2.getID().equals(t4.getID()));
		assertFalse("Duplicate IDs 3 & 4", t3.getID().equals(t4.getID()));
		sm2.addTask(t1);
		sm2.addTask(t2);
		sm2.addTask(t3);
		sm2.addTask(t4);

		assertTrue(sm2.getTasks().size() == 4);
	}

	@Test
	public void testWorkflow() throws WPISuiteException {

		ClientDataStore db = ClientDataStore.getDataStore();

		assertTrue(db.retrieve(WorkflowModel.class, "id", wm.getID()).isEmpty());
		wm.save();
		assertFalse(db.retrieve(WorkflowModel.class, "id", wm.getID())
				.isEmpty());
		wm.delete();
		assertTrue(db.retrieveAll(new WorkflowModel()).isEmpty());

	}

	@AfterClass
	public static void netTeardown() {
		ClientDataStore.deleteDataStore();
	}
}
