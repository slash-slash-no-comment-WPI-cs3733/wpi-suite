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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.exception.WaitTimedOutError;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.jdesktop.swingx.JXDatePicker;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskManager.MockNetwork;
import taskManager.ScreenshotOnFail;
import taskManager.TaskManager;
import taskManager.localization.Localizer;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.TabView;
import taskManager.view.TaskView;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.Network;

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
	private final String testUser = "testUser";
	private FrameFixture fixture;
	private JFrame frame;

	@BeforeClass
	public static void netSetup() {
		Network.setInstance(new MockNetwork());
	}

	@Before
	public void setup() {
		TaskManager.reset();

		// give it some stages
		for (String name : stageNames) {
			new StageModel(name, true);
		}

		EditTaskController etc = new EditTaskController();
		etv = etc.getView();

		frame = new JFrame();
		// frame.setLayout(new FlowLayout());
		frame.add(TabPaneController.getInstance().getView());
		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testAddTask() throws NotFoundException {

		// create a new edit task tab
		TabPaneController.getInstance().addEditTaskTab(etv);
		frame.pack();

		// enter information for a new task
		getTitleBoxFixture().enterText("name");
		getDescriptionBoxFixture().enterText("Desc");
		getDateField().setDate(Calendar.getInstance().getTime());
		fixture.textBox(EditTaskView.EST_EFFORT).enterText("3");

		// save the task
		fixture.button(EditTaskView.SAVE).click();

		// verify the task got saved
		StageModel stage = wfm.findStageByName("New");
		assertEquals(1, stage.findTaskByName("name").size());
	}

	@Test
	public void testInvalidTask() throws NotFoundException {

		// create a new edit task tab
		TabPaneController.getInstance().addEditTaskTab(etv);
		frame.pack();

		getTitleBoxFixture().enterText("name");
		getDescriptionBoxFixture().enterText("desc");
		getDateField().setDate(Calendar.getInstance().getTime());
		fixture.button(EditTaskView.SAVE).requireEnabled();

		getTitleBoxFixture().deleteText();
		fixture.button(EditTaskView.SAVE).requireDisabled();

		getTitleBoxFixture().enterText("name");
		getDescriptionBoxFixture().deleteText();
		fixture.button(EditTaskView.SAVE).requireDisabled();
	}

	@Test
	public void testLoadTask() throws NotFoundException {

		// create a task, and load the edit view with it
		TaskModel task = createAndLoadTask();

		// make sure the fields match up
		verifyTask(task);
	}

	@Test
	public void testEditTask() throws NotFoundException {
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

		assertEquals(0, stage.findTaskByName("New Task").size());
		assertEquals(1, stage.findTaskByName("newT").size());

		// verify the fields of the task got saved correctly
		TaskModel newTask = stage.findTaskByName("newT").get(0);
		assertEquals(newTask.getDescription(), "newD");
		assertEquals(newTask.getDueDate(), d);
		assertEquals(newTask.getEstimatedEffort(), new Integer(4));
	}

	@Test
	public void testMoveTask() {
		TaskModel task = createAndLoadTask();

		// move the task to a different stage
		fixture.comboBox(EditTaskView.STAGES).selectItem(0);
		fixture.button(EditTaskView.SAVE).click();

		// make sure the task got moved
		assertEquals(stageNames[0], task.getStage().getName());

	}

	@Test
	public void testSetActualEffort() {

		TaskModel task = createAndLoadTask();

		fixture.textBox(EditTaskView.ACT_EFFORT).deleteText().enterText("4");
		fixture.button(EditTaskView.SAVE).click();

		assertEquals(new Integer(4), task.getActualEffort());

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
				Localizer.getString(EditTaskView.NO_REQ));

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
		TaskController tc = new TaskController(new TaskView("Task", new Date(),
				0, 0, 235), task);
		tc.editTask();

		// make sure the requirement displays properly
		fixture.comboBox(EditTaskView.REQUIREMENTS).requireSelection(
				req.getName());

	}

	@Test
	public void testAddUsers() {
		// create users
		TaskManager.users = new String[] { testUser, "name1", "name2", "name3" };
		TaskModel task = createAndLoadTask();

		// select users to add
		etv.getProjectUsersList().setSelected(new int[] { 0, 1 });
		fixture.button(EditTaskView.ADD_USER).click();
		fixture.button(EditTaskView.SAVE).click();

		// check to make sure users were added to model
		List<String> users = new ArrayList<String>();
		for (String user : task.getAssigned()) {
			users.add(user);
		}
		List<String> result = new ArrayList<String>();
		result.add("testUser");
		result.add("name2");
		result.add("name1");
		assertEquals(result, users);
	}

	@Test
	public void testRemoveUsers() {
		// create users
		TaskManager.users = new String[] { testUser, "name2", "name3" };

		TaskModel task = createAndLoadTask();

		// select users to remove
		etv.getUsersList().setSelected(new int[] { 0 });
		fixture.button(EditTaskView.REMOVE_USER).click();
		fixture.button(EditTaskView.SAVE).click();

		// check that the user has been removed from the task model
		List<String> users = new ArrayList<String>();
		for (String user : task.getAssigned()) {
			users.add(user);
		}
		List<String> result = new ArrayList<String>();
		assertEquals(result, users);
	}

	@Test
	public void testArchive() {
		TaskModel task = createAndLoadTask();

		assertFalse(task.isArchived());
		fixture.checkBox(EditTaskView.ARCHIVE).check();
		fixture.button(EditTaskView.SAVE).click();
		assertTrue(task.isArchived());

		TaskController tc = new TaskController(new TaskView("Task", new Date(),
				0, 0, 235), task);
		tc.editTask();

		fixture.checkBox(EditTaskView.ARCHIVE).uncheck();
		fixture.button(EditTaskView.SAVE).click();
		assertFalse(task.isArchived());
	}

	@Test
	public void testDelete() {
		TaskModel task = createAndLoadTask();

		fixture.button(EditTaskView.DELETE).requireDisabled();
		fixture.checkBox(EditTaskView.ARCHIVE).check();
		// Only archived tasks can be deleted, and the archive checkbox doesn't
		// take effect immediately.
		// So you have to save and reload the view.
		fixture.button(EditTaskView.SAVE).click();
		new EditTaskController(task); // reopen view

		fixture.button(EditTaskView.DELETE).requireEnabled();
		fixture.button(EditTaskView.DELETE).click();

		new JOptionPaneFixture(fixture.robot).yesButton().click();

		assertNull(wfm.findTaskByID(task.getID()));
	}

	@Test
	public void testClose() throws NotFoundException {
		fixture.button(TabView.X).click();
		// If warning comes up, hit yes
		try {
			fixture.optionPane().yesButton().click();
		} catch (ComponentLookupException | WaitTimedOutError e) {
		}

		TaskModel task = createAndLoadTask();
		fixture.button(TabView.X).click();
		try { // dialog shouldn't come up if no changes made
			fixture.optionPane();
			fail();
		} catch (ComponentLookupException | WaitTimedOutError e) {
		}

		task = createAndLoadTask();
		String name = task.getName();
		getTitleBoxFixture().deleteText();
		getTitleBoxFixture().enterText("nt2");
		fixture.button(TabView.X).click();
		fixture.optionPane().noButton().click();
		assertEquals(task.getName(), name);
		getTitleBoxFixture().requireText("nt2");
		fixture.button(TabView.X).click();
		fixture.optionPane().yesButton().click();
		assertEquals(task.getName(), name);

	}

	@Test
	public void testCloseCreateTask() {
		// Close task opened in setup
		fixture.button(TabView.X).click();
		try {
			fixture.optionPane().yesButton().click();
		} catch (ComponentLookupException | WaitTimedOutError e) {
		}
		// load the Create task view
		TabPaneController.getInstance().addCreateTaskTab();

		Component c = TabPaneController.getInstance().getView()
				.getSelectedComponent();
		if (c instanceof EditTaskView) {
			etv = (EditTaskView) c;
		} else {
			fail("oh god what's going on");
		}
		frame.pack();

		fixture.button(TabView.X).click();
		try { // dialog shouldn't come up if no changes made
			fixture.optionPane();
			fail("New task threw up popup");
		} catch (ComponentLookupException | WaitTimedOutError e) {
		}
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
		etv = null;

		// remove tab
		Component[] tabs = TabPaneController.getInstance().getView()
				.getComponents();
		TabPaneController.getInstance().removeTabByComponent(
				tabs[tabs.length - 1]);
	}

	/**
	 * Creates a new task, then opens the EditTaskView to edit that task
	 *
	 * @return the created task
	 */
	private TaskModel createAndLoadTask() {

		// add a task
		System.out.println(wfm.getStages());
		StageModel stage = wfm.getStages().get(3);
		TaskModel task = new TaskModel("New Task", stage);
		task.setDescription("test description");
		task.setDueDate(Calendar.getInstance().getTime());
		task.setEstimatedEffort(5);
		task.setActualEffort(7);
		task.addAssigned(testUser);

		// load the edit view
		TaskController tc = new TaskController(new TaskView("Task", new Date(),
				0, 0, 235), task);
		tc.editTask();
		Component c = TabPaneController.getInstance().getView()
				.getSelectedComponent();
		if (c instanceof EditTaskView) {
			etv = (EditTaskView) c;
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
	 * @throws NotFoundException
	 */
	private void verifyTask(TaskModel task) throws NotFoundException {
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
		return fixture.textBox(EditTaskView.DESCRIP);
	}

	/**
	 * Create a fixture for the Title text field on the EditTaskView
	 *
	 * @return A fixture with the title text box as the target
	 * @throws NotFoundException
	 *             if Title isn't found.
	 */
	private JTextComponentFixture getTitleBoxFixture() throws NotFoundException {
		return fixture.textBox(EditTaskView.TITLE);
	}

	/**
	 * Finds the dateField
	 *
	 * @return The due date date-picker
	 * @throws NotFoundException
	 *             if the field cannot be found.
	 */
	private JXDatePicker getDateField() throws NotFoundException {
		// Fest doesn't understand JXDatePickers, so I have to use this method
		return (JXDatePicker) findByName(EditTaskView.DUE_DATE);
	}

	/**
	 * 
	 * Searches the fixture for a component by name. Useful because Fest doesn't
	 * know how to search for odd types of components.
	 *
	 * @param name
	 *            The Component's name
	 * @return The first component found with that name.
	 * @throws NotFoundException
	 *             If no component has that name in the fixture.
	 */
	private Component findByName(String name) throws NotFoundException {
		final ComponentMatcher nameMatcher = new ComponentMatcher() {
			@Override
			public boolean matches(Component c) {
				return name.equals(c.getName()) && c.isShowing();
			}
		};
		return (Component) fixture.robot.finder().find(fixture.target,
				nameMatcher);
	}
}
