/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;

/**
 * Tests for the edit task controller
 *
 * @author Jon Sorrells
 */
public class TestEditTaskController {

	private static EditTaskView etv = JanewayModule.etv;
	private static WorkflowModel wfm;

	private final String[] stageNames = { "New", "second", "third", "fourth" };

	private FrameFixture fixture;

	@BeforeClass
	public static void setupOnce() {
		// create the edit task controller
		wfm = new WorkflowModel();
		etv.setController(new EditTaskController(wfm));
	}

	@Before
	public void setup() {
		// create a new workflow model
		wfm.makeIdenticalTo(new WorkflowModel());
		// give it some stages
		for (String name : stageNames) {
			new StageModel(wfm, name, true);
		}

		etv.resetFields();

		JFrame frame = new JFrame();
		frame.add(etv);

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testAddTask() {
		// enter information for a new task
		getTitleBoxFixture().enterText("New Task");
		getDescriptionBoxFixture().enterText("a sample task used for testing");
		fixture.textBox("due_date").enterText("11/11/2011");
		fixture.textBox("est_effort").enterText("3");

		// save the task
		fixture.button("save").click();

		// verify the task got saved
		StageModel stage = wfm.findStageByName("New");
		assertEquals(stage.findTaskByName("New Task").size(), 1);
	}

	private JTextComponentFixture getDescriptionBoxFixture() {
		return new JTextComponentFixture(fixture.robot, etv.getDescription());
	}

	private JTextComponentFixture getTitleBoxFixture() {
		return new JTextComponentFixture(fixture.robot, etv.getTitle());
	}

	@Test
	public void testInvalidTask() {
		// TODO: attempt to create a task with invalid/missing values
		// then verify save button is disabled
	}

	@Test
	public void testLoadTask() {
		// create a task, and load the edit view with it
		TaskModel task = createAndLoadTask();

		// make sure the fields match up
		verifyTask(task);
	}

	private void verifyTask(TaskModel task) {
		getTitleBoxFixture().requireText(task.getName());
		getDescriptionBoxFixture().requireText(task.getDescription());
		fixture.textBox("due_date").requireText(
				new SimpleDateFormat("MM/dd/yyyy").format(task.getDueDate()));
		fixture.textBox("est_effort").requireText(
				Integer.toString(task.getEstimatedEffort()));
		fixture.textBox("act_effort").requireText(
				Integer.toString(task.getActualEffort()));
	}

	@Test
	public void testEditTask() {
		// create a task and load it
		TaskModel task = createAndLoadTask();

		// edit the task
		getTitleBoxFixture().deleteText().enterText("renamed task");
		getDescriptionBoxFixture().deleteText().enterText("new description");
		fixture.textBox("due_date").deleteText().enterText("11/11/2011");
		fixture.textBox("est_effort").deleteText().enterText("4");
		fixture.textBox("act_effort").deleteText().enterText("8");

		// save the task
		fixture.button("save").click();

		// verify the task got saved (and not duplicated)
		StageModel stage = wfm.findStageByName(task.getStage().getName());
		assertEquals(stage.findTaskByName("New Task").size(), 0);
		assertEquals(stage.findTaskByName("renamed task").size(), 1);

		// verify the fields of the task got saved correctly
		TaskModel newTask = stage.findTaskByName("renamed task").get(0);
		assertEquals(newTask.getDescription(), "new description");
		assertEquals(
				new SimpleDateFormat("MM/dd/yyyy").format(newTask.getDueDate()),
				"11/11/2011");
		assertEquals(newTask.getEstimatedEffort(), 4);
		assertEquals(newTask.getActualEffort(), 8);
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}

	private TaskModel createAndLoadTask() {
		// add a task
		StageModel stage = wfm.getStages().get(2);
		TaskModel task = new TaskModel("New Task", stage);
		task.setDescription("test description");
		task.setDueDate(new Date(12345));
		task.setEstimatedEffort(5);
		task.setActualEffort(7);

		// load the edit view
		TaskController tc = new TaskController(null, task);
		tc.actionPerformed(null);

		return task;
	}

}
