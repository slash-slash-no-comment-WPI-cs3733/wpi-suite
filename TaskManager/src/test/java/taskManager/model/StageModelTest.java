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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 * @author Sam Khalandovsky
 * @version Dec 6, 2014
 */
public class StageModelTest {
	WorkflowModel wf;
	StageModel stage;
	StageModel stage2;

	@Before
	public void initializeWorkflow() {
		JanewayModule.reset();

		wf = WorkflowModel.getInstance();

		stage = new StageModel("Stage");
		stage2 = new StageModel("Stage2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWrongTask() {
		TaskModel task = new TaskModel("Task", stage);
		TaskModel task2 = new TaskModel("Task", stage2);
		stage2.removeTask(task);
	}

	@Test
	public void testRemoveRightTask() {
		TaskModel task = new TaskModel("Task", stage);
		TaskModel task2 = new TaskModel("Task", stage2);
		stage2.removeTask(task2);
		assertFalse(stage2.containsTask(task2));
		assertFalse(stage.containsTask(task2));
	}

	@Test
	public void testCreateStage() {
		StageModel st = new StageModel("Stage3");
		assertTrue(st.isRemovable());
		assertSame(st, wf.getStages().get(wf.getStages().size() - 1));

		st = new StageModel("Stage4", false);
		assertFalse(st.isRemovable());
		assertSame(st, wf.getStages().get(wf.getStages().size() - 1));

		st = new StageModel("Stage5", 2);
		assertTrue(st.isRemovable());
		assertSame(st, wf.getStages().get(2));

		st = new StageModel("Stage6", 1, false);
		assertFalse(st.isRemovable());
		assertSame(st, wf.getStages().get(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateStage() {
		new StageModel("Stage3");
		new StageModel(" Stage3  ");
	}

	@Test
	public void testSetProject() {
		Project p = new Project("Project", "ID");
		StageModel st = new StageModel("Stage3");
		TaskModel t1 = new TaskModel("T1", st);
		TaskModel t2 = new TaskModel("T2", st);
		st.setProject(p);
		assertSame(p, st.getProject());
		assertSame(p, t1.getProject());
		assertSame(p, t2.getProject());
	}

	@Test
	public void testChangeName() {
		stage2.setName("Stage2++ ");
		assertEquals("Stage2++", stage2.getName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidChangeName() {
		stage2.setName("Stage ");
	}

	@Test
	public void testTasklistSynchronizationAddition() {
		TaskModel task1 = new TaskModel("Task", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.rebuildAllRefs();
		stage = wf.findStageByName("Stage");
		TaskModel task2 = new TaskModel("Task2", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		assertNotNull(wf.findStageByName("Stage").findTaskByID("Task2"));
	}

	@Test
	public void testTasklistSynchronizationDeletion() {
		TaskModel task = new TaskModel("Task", stage);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		stage = wf.findStageByName("Stage");
		task = wf.findTaskByID("Task");
		stage.removeTask(task);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		assertNull(wf.findStageByName("Stage").findTaskByID("Task"));
	}

	@Test
	public void testTasklistSynchronizationMove() {
		TaskModel task = new TaskModel("Task", stage);
		task.setEstimatedEffort(5);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.rebuildAllRefs();
		stage = wf.findStageByName("Stage");
		stage2 = wf.findStageByName("Stage2");
		task = wf.findTaskByID("Task");
		assertEquals(task.getEstimatedEffort(), 5);
		task.setEstimatedEffort(10);
		stage2.addTask(task);
		wf = AbstractJsonableModel.fromJson(wf.toJson(), WorkflowModel.class);
		wf.rebuildAllRefs();
		stage = wf.findStageByName("Stage");
		stage2 = wf.findStageByName("Stage2");
		task = wf.findTaskByID("Task");
		assertNull(wf.findStageByName("Stage").findTaskByID("Task"));
		assertNotNull(wf.findStageByName("Stage2").findTaskByID("Task"));
		assertEquals(task.getEstimatedEffort(), 10);
	}
}
