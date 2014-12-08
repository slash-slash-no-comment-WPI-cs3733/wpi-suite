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
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * @author Sam Khalandovsky
 * @version Dec 3, 2014
 */
public class TestTaskModel {

	TaskModel task;

	@Before
	public void setUp() {
		WorkflowModel wf = WorkflowModel.getInstance();
		wf.makeIdenticalTo(new WorkflowModel("Workflow"));
		StageModel stage = new StageModel("Stage");
		task = new TaskModel("Task", stage);
	}

	@Test
	public void testMakeIdentical() {
		StageModel stage2 = new StageModel("Stage2");
		TaskModel task2 = new TaskModel("Task2", stage2);
		User u = new User("Name", "username", "pass", 5);
		Date d = new Date();
		ActivityModel a = new ActivityModel("desc",
				ActivityModel.activityModelType.COMMENT);
		Requirement r = new Requirement();
		r.setId(99);
		RequirementModel.getInstance().addRequirement(r);

		task2.setDescription("Desc");
		task2.setArchived(true);
		task2.addAssigned(u);
		task2.setDueDate(d);
		task2.setEstimatedEffort(5);
		task2.setActualEffort(3);
		task2.addActivity(a);
		task2.setReq(r);

		task.makeIdenticalTo(task2);
		assertEquals("Desc", task.getDescription());
		assertTrue(task2.isArchived());
		assertTrue(task2.getAssigned().contains(u.getUsername()));
		assertEquals(d, task2.getDueDate());
		assertEquals(5, task2.getEstimatedEffort());
		assertEquals(3, task2.getActualEffort());
		assertTrue(task2.getActivities().contains(a));
		assertEquals(r, task2.getReq());
		assertEquals(stage2, task2.getStage());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidEstimatedEffort() {
		task.setEstimatedEffort(-15);
	}

	@Test
	public void testValidEstimatedEffort() {
		assertFalse(task.isEstimatedEffortSet());
		task.setEstimatedEffort(20);
		assertTrue(task.isEstimatedEffortSet());
		assertEquals(20, task.getEstimatedEffort());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidActualEffort() {
		task.setActualEffort(-15);
	}

	@Test
	public void testValidActualEffort() {
		assertFalse(task.isActualEffortSet());
		task.setActualEffort(20);
		assertTrue(task.isActualEffortSet());
		assertEquals(20, task.getActualEffort());
	}
}
