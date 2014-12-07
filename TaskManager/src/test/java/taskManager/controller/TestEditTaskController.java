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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.ScreenshotOnFail;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * Tests for the edit task controller
 *
 * @author Jon Sorrells
 */
public class TestEditTaskController extends ScreenshotOnFail {

	private static EditTaskView etv = null;
	private static final WorkflowModel wfm = WorkflowModel.getInstance();

	private final String[] stageNames = { "New", "Scheduled", "In Progress",
			"Complete" };
	private final User testUser = new User("testUser", "testUser", "password",
			1234);

	private FrameFixture fixture;
	private JFrame frame;

	@Before
	public void setup() {
		// create a new workflow model
		wfm.makeIdenticalTo(new WorkflowModel());

		// create the edit task controller
		etv = new EditTaskView(EditTaskView.Mode.CREATE);
		etv.setController(new EditTaskController(etv));
		etv.setFieldController(new TaskInputValidator(etv));

		// give it some stages
		for (String name : stageNames) {
			new StageModel(name, true);
		}

		etv.resetFields();
		etv.setVisible(true);

		frame = new JFrame();
		// frame.setLayout(new FlowLayout());
		frame.add(JanewayModule.tabPaneC.getTabView());
		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testAddTask() {

		// create a new edit task tab
		JanewayModule.tabPaneC.addEditTaskTab(etv);
		frame.pack();

		// enter information for a new task
		getTitleBoxFixture().enterText("name");
		getDescriptionBoxFixture().enterText("Desc");
		etv.getDateField().setDate(Calendar.getInstance().getTime());
		fixture.textBox(EditTaskView.EST_EFFORT).enterText("3");

		// save the task
		fixture.button(EditTaskView.SAVE).click();

		// verify the task got saved
		StageModel stage = wfm.findStageByName("New");
		assertEquals(stage.findTaskByName("name").size(), 1);
	}

	@Test
	public void testInvalidTask() {

		// create a new edit task tab
		JanewayModule.tabPaneC.addEditTaskTab(etv);
		frame.pack();

		getTitleBoxFixture().enterText("name");
		getDescriptionBoxFixture().enterText("desc");
		etv.getDateField().setDate(Calendar.getInstance().getTime());
		fixture.button(EditTaskView.SAVE).requireEnabled();

		getTitleBoxFixture().deleteText();
		fixture.button(EditTaskView.SAVE).requireDisabled();

		getTitleBoxFixture().enterText("name");
		getDescriptionBoxFixture().deleteText();
		fixture.button(EditTaskView.SAVE).requireDisabled();
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
		getTitleBoxFixture().deleteText().enterText("newT"); // renamed title
		getDescriptionBoxFixture().deleteText().enterText("newD"); // renamed
																	// description
		Date d = new Date(5 * 60 * 60 * 1000);
		etv.setDate(d);
		fixture.textBox(EditTaskView.EST_EFFORT).deleteText().enterText("4");

		// save the task
		fixture.button(EditTaskView.SAVE).click();

		StageModel stage = wfm.findStageByName(task.getStage().getName());

		assertEquals(stage.findTaskByName("New Task").size(), 0);
		assertEquals(stage.findTaskByName("newT").size(), 1);

		// verify the fields of the task got saved correctly
		TaskModel newTask = stage.findTaskByName("newT").get(0);
		assertEquals(newTask.getDescription(), "newD");
		assertEquals(newTask.getDueDate(), d);
		assertEquals(newTask.getEstimatedEffort(), 4);
	}

	@Test
	public void testMoveTask() {
		TaskModel task = createAndLoadTask();

		// move the task to a different stage
		fixture.comboBox(EditTaskView.STAGES).selectItem(0);
		fixture.button(EditTaskView.SAVE).click();

		// make sure the task got moved
		assertEquals(task.getStage().getName(), stageNames[0]);

	}

	@Test
	public void testSetActualEffort() {

		TaskModel task = createAndLoadTask();

		fixture.textBox(EditTaskView.ACT_EFFORT).deleteText().enterText("4");
		fixture.button(EditTaskView.SAVE).click();

		assertEquals(task.getActualEffort(), 4);

	}

	@Test
	public void testAddRequirement() {
		// create a requirement
		Requirement req = new Requirement();
		req.setName("test requirement");
		RequirementModel.getInstance().addRequirement(req);

		// create a task
		TaskModel task = createAndLoadTask();

		// make sure it has no requirement yet
		fixture.comboBox(EditTaskView.REQUIREMENTS).requireSelection(
				EditTaskView.NO_REQ);

		// add a requirement to the task
		fixture.comboBox(EditTaskView.REQUIREMENTS).selectItem(req.getName());
		fixture.button(EditTaskView.SAVE).click();

		// make sure the task got the requirement
		assertEquals(task.getReq().getName(), req.getName());
	}

	@Test
	public void testLoadRequirement() {
		// create a requirement
		Requirement req = new Requirement();
		req.setName("test requirement");
		RequirementModel.getInstance().addRequirement(req);

		// add a task with a requirement
		StageModel stage = wfm.getStages().get(2);
		TaskModel task = new TaskModel("New Task", stage);
		task.setDescription("test description");
		task.setDueDate(Calendar.getInstance().getTime());
		task.setEstimatedEffort(5);
		task.setActualEffort(7);
		task.setReq(req);

		// load the edit view
		TaskController tc = new TaskController(task);
		tc.editTask();

		// make sure the requirement displays properly
		fixture.comboBox(EditTaskView.REQUIREMENTS).requireSelection(
				req.getName());

	}

	@Test
	public void testAddUsers() {
		// create users
		JanewayModule.users = new User[] { testUser,
				new User("name1", "name1", "password", 4321),
				new User("name2", "name2", "password", 5678),
				new User("name3", "name3", "password", 9876) };
		TaskModel task = createAndLoadTask();

		// select users to add
		etv.getProjectUsersList().setSelected(new int[] { 0, 1 });
		fixture.button(EditTaskView.ADD_USER).click();
		fixture.button(EditTaskView.SAVE).click();

		// check to make sure users were added to model
		ArrayList<String> users = new ArrayList<String>();
		for (String user : task.getAssigned()) {
			users.add(user);
		}
		ArrayList<String> result = new ArrayList<String>();
		result.add("testUser");
		result.add("name2");
		result.add("name1");
		assertEquals(result, users);
	}

	@Test
	public void testRemoveUsers() {
		// create users
		JanewayModule.users = new User[] { testUser,
				new User("name2", "name2", "password", 5678),
				new User("name3", "name3", "password", 9876) };

		TaskModel task = createAndLoadTask();

		// select users to remove
		etv.getUsersList().setSelected(new int[] { 0 });
		fixture.button(EditTaskView.REMOVE_USER).click();
		fixture.button(EditTaskView.SAVE).click();

		// check that the user has been removed from the task model
		ArrayList<String> users = new ArrayList<String>();
		for (String user : task.getAssigned()) {
			users.add(user);
		}
		ArrayList<String> result = new ArrayList<String>();
		assertEquals(users, result);
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
		etv = null;
		// remove all tabs
		for (Component c : JanewayModule.tabPaneC.getTabView().getComponents()) {
			JanewayModule.tabPaneC.removeTabByComponent(c);
		}
	}

	/**
	 * Creates a new task, then opens the EditTaskView to edit that task
	 *
	 * @return the created task
	 */
	private TaskModel createAndLoadTask() {

		// add a task
		StageModel stage = wfm.getStages().get(3);
		TaskModel task = new TaskModel("New Task", stage);
		task.setDescription("test description");
		task.setDueDate(Calendar.getInstance().getTime());
		task.setEstimatedEffort(5);
		task.setActualEffort(7);
		task.addAssigned(testUser);

		// load the edit view
		TaskController tc = new TaskController(task);
		tc.editTask();
		Component c = JanewayModule.tabPaneC.getTabView()
				.getSelectedComponent();
		if (c instanceof JScrollPane
				&& ((JScrollPane) c).getViewport().getView() instanceof EditTaskView) {
			etv = (EditTaskView) ((JScrollPane) c).getViewport().getView();
		} else {
			fail("oh god what's going on");
		}
		frame.pack();
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
