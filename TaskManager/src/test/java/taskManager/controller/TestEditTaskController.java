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

import java.util.Calendar;
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

	private static EditTaskView etv_create;
	private static EditTaskView etv_edit;
	private static WorkflowModel wfm_create;
	private static WorkflowModel wfm_edit;

	private final String[] stageNames = { "New", "second", "third", "fourth" };

	private FrameFixture fixture_create;
	private FrameFixture fixture_edit;

	@BeforeClass
	public static void setupOnce() {
		// create the edit task controller
		etv_create = new EditTaskView(EditTaskView.Mode.CREATE);
		etv_edit = new EditTaskView(EditTaskView.Mode.EDIT);
		wfm_create = new WorkflowModel();
		wfm_edit = new WorkflowModel();
		etv_create.setController(new EditTaskController(wfm_create, etv_create));
		etv_edit.setController(new EditTaskController(wfm_edit, etv_edit));
	}

	@Before
	public void setup() {
		// Create
		// create a new workflow model
		wfm_create.makeIdenticalTo(new WorkflowModel());
		// give it some stages
		for (String name : stageNames) {
			new StageModel(wfm_create, name, true);
		}

		etv_create.resetFields();
		etv_create.setVisible(true);
		JFrame frame_create = new JFrame();
		frame_create.add(etv_create);
		fixture_create = new FrameFixture(frame_create);
		fixture_create.show();
		
		// Edits
		// create a new workflow model
		wfm_edit.makeIdenticalTo(new WorkflowModel());
		// give it some stages
		for (String name : stageNames) {
			new StageModel(wfm_edit, name, true);
		}

		etv_edit.resetFields();
		etv_edit.setVisible(true);

		JFrame frame_edit = new JFrame();
		frame_edit.add(etv_edit);

		fixture_edit = new FrameFixture(frame_edit);

		fixture_edit.show();
		
		frame_create.pack();
		frame_edit.pack();
	}

	@Test
	public void testAddTask() {
		// enter information for a new task
		getTitleBoxFixture().enterText("New Task");
		getDescriptionBoxFixture().enterText("a sample task used for testing");
		etv_create.getDateField().setDate(Calendar.getInstance().getTime());
		fixture_create.textBox(EditTaskView.EST_EFFORT).enterText("3");

		// save the task
		fixture_create.button(EditTaskView.SAVE).click();

		// verify the task got saved
		StageModel stage = wfm_create.findStageByName("New");
		assertEquals(stage.findTaskByName("New Task").size(), 1);
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

	@Test
	public void testEditTask() {
		// create a task and load it
		TaskModel task = createAndLoadTask();

		// edit the task
		getTitleBoxFixture().deleteText().enterText("renamed task");
		getDescriptionBoxFixture().deleteText().enterText("new description");
		Date d = new Date(5 * 60 * 60 * 1000);
		etv_create.setDate(d);
		fixture_create.textBox(EditTaskView.EST_EFFORT).deleteText().enterText("4");
		fixture_create.textBox(EditTaskView.ACT_EFFORT).deleteText().enterText("8");

		// save the task
		fixture_create.button(EditTaskView.SAVE).click();

		// verify the task got saved (and not duplicated)
		StageModel stage = wfm_create.findStageByName(task.getStage().getName());
		assertEquals(stage.findTaskByName("New Task").size(), 0);
		assertEquals(stage.findTaskByName("renamed task").size(), 1);

		// verify the fields of the task got saved correctly
		TaskModel newTask = stage.findTaskByName("renamed task").get(0);
		assertEquals(newTask.getDescription(), "new description");
		assertEquals(newTask.getDueDate(), d);
		assertEquals(newTask.getEstimatedEffort(), 4);
		assertEquals(newTask.getActualEffort(), 8);
	}

	@Test
	public void testMoveTask() {
		TaskModel task = createAndLoadTask();

		fixture_create.comboBox(EditTaskView.STAGES).selectItem(0);
		fixture_create.button(EditTaskView.SAVE).click();

		assertEquals(task.getStage().getName(), stageNames[0]);
	}

	@After
	public void cleanup() {
		fixture_edit.cleanUp();
		fixture_create.cleanUp();
	}

	/**
	 * Creates a new task, then opens the EditTaskView to edit that task
	 *
	 * @return the created task
	 */
	private TaskModel createAndLoadTask() {
		// add a task
		StageModel stage = wfm_create.getStages().get(2);
		TaskModel task = new TaskModel("New Task", stage);
		task.setDescription("test description");
		task.setDueDate(Calendar.getInstance().getTime());
		task.setEstimatedEffort(5);
		task.setActualEffort(7);

		// load the edit view
		TaskController tc = new TaskController(null, task);
		tc.actionPerformed(null);

		return task;
	}

	/**
	 * Checks to make sure the the given task matches what is displayed on the
	 * view
	 *
	 * @param task
	 *            The task that should be shown on the view
	 */
	private void verifyTask(TaskModel task) {
		getTitleBoxFixture().requireText(task.getName());
		getDescriptionBoxFixture().requireText(task.getDescription());
		fixture_create.textBox(EditTaskView.EST_EFFORT).requireText(
				Integer.toString(task.getEstimatedEffort()));
		fixture_create.textBox(EditTaskView.ACT_EFFORT).requireText(
				Integer.toString(task.getActualEffort()));
		fixture_create.comboBox(EditTaskView.STAGES).requireSelection(
				task.getStage().getName());
	}

	/**
	 * Create a fixture for the Description text field on the EditTaskView
	 *
	 * @return A fixture with the description text box as the target
	 */
	private JTextComponentFixture getDescriptionBoxFixture() {
		return new JTextComponentFixture(fixture_create.robot, etv_create.getDescription());
	}

	/**
	 * Create a fixture for the Title text field on the EditTaskView
	 *
	 * @return A fixture with the title text box as the target
	 */
	private JTextComponentFixture getTitleBoxFixture() {
		return new JTextComponentFixture(fixture_create.robot, etv_create.getTitle());
	}

}
