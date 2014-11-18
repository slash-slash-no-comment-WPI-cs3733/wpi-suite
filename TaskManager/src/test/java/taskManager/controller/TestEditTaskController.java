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
import static org.junit.Assert.fail;

import java.awt.Component;
import java.awt.Dimension;
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

	private static EditTaskView etv = new EditTaskView(EditTaskView.Mode.EDIT);
	private static final WorkflowModel wfm = WorkflowModel.getInstance();

	private final String[] stageNames = { "New", "second", "third", "fourth" };

	private FrameFixture fixture;
	private JFrame frame;

	@BeforeClass
	public static void setupOnce() {
		// create the edit task controller
		etv.setController(new EditTaskController(etv));
	}

	@Before
	public void setup() {
		// create a new workflow model
		wfm.makeIdenticalTo(new WorkflowModel());
		// give it some stages
		for (String name : stageNames) {
			new StageModel(name, true);
		}

		etv.resetFields();
		etv.setVisible(true);

		frame = new JFrame();
		frame.add(JanewayModule.tabPaneC.getTabView());
		// Need all of these to get the test frame to be the correct size
		frame.setMinimumSize(new Dimension(800, 800));
		frame.setSize(new Dimension(800, 800));
		frame.setPreferredSize(new Dimension(800, 800));
		frame.setMaximumSize(new Dimension(800, 800));

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testAddTask() {
		// enter information for a new task
		getTitleBoxFixture().enterText("New Task");
		getDescriptionBoxFixture().enterText("a sample task used for testing");
		etv.getDateField().setDate(Calendar.getInstance().getTime());
		fixture.textBox(EditTaskView.EST_EFFORT).enterText("3");

		// save the task
		fixture.button(EditTaskView.SAVE).click();

		// verify the task got saved
		StageModel stage = wfm.findStageByName("New");
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
		etv.setDate(d);
		fixture.textBox(EditTaskView.EST_EFFORT).deleteText().enterText("4");
		fixture.textBox(EditTaskView.ACT_EFFORT).deleteText().enterText("8");

		// save the task
		fixture.button(EditTaskView.SAVE).click();

		// verify the task got saved (and not duplicated)
		StageModel stage = wfm.findStageByName(task.getStage().getName());
		System.out.println(stage.findTaskByName("New Task").size());

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

		fixture.comboBox(EditTaskView.STAGES).selectItem(0);
		fixture.button(EditTaskView.SAVE).click();

		assertEquals(task.getStage().getName(), stageNames[0]);
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}

	/**
	 * Creates a new task, then opens the EditTaskView to edit that task
	 *
	 * @return the created task
	 */
	private TaskModel createAndLoadTask() {
		// add a task
		StageModel stage = wfm.getStages().get(2);
		TaskModel task = new TaskModel("New Task", stage);
		task.setDescription("test description");
		task.setDueDate(Calendar.getInstance().getTime());
		task.setEstimatedEffort(5);
		task.setActualEffort(7);

		// load the edit view
		TaskController tc = new TaskController(null, task);
		tc.actionPerformed(null);

		// EditTaskView etv2 = null;
		Component c = JanewayModule.tabPaneC.getTabView()
				.getSelectedComponent();
		if (c instanceof EditTaskView) {
			etv = (EditTaskView) c;
		} else {
			fail("oh god what's going on");
		}

		// fixture.cleanUp();
		// frame = new JFrame();
		// frame.add(etv);
		// fixture = new FrameFixture(frame);
		// fixture.show();

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
		fixture.textBox(EditTaskView.EST_EFFORT).requireText(
				Integer.toString(task.getEstimatedEffort()));
		fixture.textBox(EditTaskView.ACT_EFFORT).requireText(
				Integer.toString(task.getActualEffort()));
		fixture.comboBox(EditTaskView.STAGES).requireSelection(
				task.getStage().getName());
	}

	/**
	 * Create a fixture for the Description text field on the EditTaskView
	 *
	 * @return A fixture with the description text box as the target
	 */
	private JTextComponentFixture getDescriptionBoxFixture() {
		return new JTextComponentFixture(fixture.robot, etv.getDescription());
	}

	/**
	 * Create a fixture for the Title text field on the EditTaskView
	 *
	 * @return A fixture with the title text box as the target
	 */
	private JTextComponentFixture getTitleBoxFixture() {
		return new JTextComponentFixture(fixture.robot, etv.getTitle());
	}

}
