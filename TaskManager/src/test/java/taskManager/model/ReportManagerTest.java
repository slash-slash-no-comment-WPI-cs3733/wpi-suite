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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class ReportManagerTest {

	ReportManager rm;
	User user;
	TaskModel task1, task2, task3;
	WorkflowModel workflow;
	StageModel stage;

	@Before
	public void setUp() {
		JanewayModule.reset();

		rm = new ReportManager();
		user = new User("Name", "userName", "password", 42);

		stage = new StageModel("Stage");
		task1 = new TaskModel("task1", stage);
		task2 = new TaskModel("task2", stage);
		task3 = new TaskModel("task3", stage);
	}

	@Test
	public void testAddUser() {
		rm.addUser(user);
		assertTrue(rm.trackingUser(user));
	}

	@Test(expected = IllegalStateException.class)
	public void testAddTaskIllegalTask() {
		rm.addUser(user);
		rm.userFinishedTask(user, task1);
	}

	@Test
	public void testAddTask() {
		rm.addUser(user);
		task1.setEstimatedEffort(1);
		task1.setActualEffort(1);
		rm.userFinishedTask(user, task1);
		assertEquals(1, rm.getUserAverageEffort(user), 0.01);
	}

	@Test
	public void testGetAverageEffort() {
		rm.addUser(user);

		task3.setEstimatedEffort(10000000);
		task3.setActualEffort(5);
		task2.setEstimatedEffort(1);
		task2.setActualEffort(1);
		task1.setEstimatedEffort(2);
		task1.setActualEffort(2);
		rm.userFinishedTask(user, task1);
		rm.userFinishedTask(user, task2);
		rm.userFinishedTask(user, task3);
		assertEquals("Unexpected error with average effort", 8.0 / 3,
				rm.getUserAverageEffort(user), 0.01);
	}

	@Test
	public void testStdDevEstimateQuality() {
		rm.addUser(user);

		task3.setEstimatedEffort(20);
		task3.setActualEffort(10);
		task2.setEstimatedEffort(40);
		task2.setActualEffort(10);
		task1.setEstimatedEffort(10);
		task1.setActualEffort(20);
		rm.userFinishedTask(user, task1);
		rm.userFinishedTask(user, task2);
		rm.userFinishedTask(user, task3);
		assertEquals(
				"Unexpected error with variance in estimated effort quality",
				16.3299, rm.getStdDevEstimateQuality(user), 0.01);
	}

}
