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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.core.MouseButton;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.TaskManager;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.ToolbarView;

public class TestTabPaneController {

	private static final WorkflowModel wfm = WorkflowModel.getInstance();
	private static final WorkflowController wfc = WorkflowController
			.getInstance();
	private static ToolbarView toolV;

	private final String[] stageNames = { "New", "Scheduled", "In Progress",
			"Complete" };
	private FrameFixture fixture;
	private JFrame frame;

	private TaskModel t1;
	private TaskModel t2;
	private TaskModel t3;

	private Dimension size = new Dimension(1000, 500);

	@Before
	public void setup() {
		// create a new workflow model
		TaskManager.reset();
		toolV = ToolbarController.getInstance().getView();

		// give it some stages
		for (String name : stageNames) {
			new StageModel(name, false);
		}

		wfc.reloadData();
		wfc.repaintView();

		wfc.reloadData();

		createDummyTasks();

		frame = new JFrame();
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setLayout(new BorderLayout());
		frame.add(toolV, BorderLayout.NORTH);
		frame.add(TabPaneController.getInstance().getView());
		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testOpenEditTaskTab() {
		// click the task
		fixture.panel("Task 1").click();

		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// check that the tab is open
		assertEquals(TabPaneController.getInstance().getView().getTitleAt(1),
				"Task 1");
	}

	@Test
	public void testArchiveTaskWithOpenTab() {
		// click the task
		JPanelFixture taskFixture = fixture.panel("Task 1");
		taskFixture.click();
		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// go back to the workflow
		TabPaneController.getInstance().getView().setSelectedIndex(0);

		fixture.robot.settings().delayBetweenEvents(10);

		Point location = fixture.panel("Task 1").target.getLocationOnScreen();
		location.x += 10;
		location.y += 10;

		Point goal = fixture.label(ToolbarView.ARCHIVE).target
				.getLocationOnScreen();
		goal.x += 10;
		goal.y += 10;

		fixture.robot.pressMouse(location, MouseButton.LEFT_BUTTON);
		while (!location.equals(goal)) {
			int movex = Integer.min(Integer.max(goal.x - location.x, -3), 3);
			int movey = Integer.min(Integer.max(goal.y - location.y, -3), 3);
			location.x += movex;
			location.y += movey;
			fixture.robot.moveMouse(location);
		}
		fixture.robot.settings().delayBetweenEvents(60);
		fixture.robot.settings().idleTimeout(100);

		fixture.robot.releaseMouseButtons();

		fixture.robot.waitForIdle();

		// switch to edit task tab
		TabPaneController.getInstance().getView().setSelectedIndex(1);

		boolean result = fixture.checkBox(EditTaskView.ARCHIVE).target
				.isSelected();

		assertEquals(true, result);
	}

	@Test
	public void testDeleteTaskWithOpenTab() {
		// click the task
		JPanelFixture taskFixture = fixture.panel("Task 1");
		taskFixture.click();
		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// go back to the workflow
		TabPaneController.getInstance().getView().setSelectedIndex(0);

		// archive the task
		fixture.robot.settings().delayBetweenEvents(10);

		Point location = fixture.panel("Task 1").target.getLocationOnScreen();
		location.x += 10;
		location.y += 10;

		Point goal = fixture.label(ToolbarView.ARCHIVE).target
				.getLocationOnScreen();
		goal.x += 10;
		goal.y += 10;

		fixture.robot.pressMouse(location, MouseButton.LEFT_BUTTON);
		while (!location.equals(goal)) {
			int movex = Integer.min(Integer.max(goal.x - location.x, -3), 3);
			int movey = Integer.min(Integer.max(goal.y - location.y, -3), 3);
			location.x += movex;
			location.y += movey;
			fixture.robot.moveMouse(location);
		}
		fixture.robot.settings().delayBetweenEvents(60);
		fixture.robot.settings().idleTimeout(100);
		fixture.robot.releaseMouseButtons();
		fixture.robot.waitForIdle();

		// show archived tasks
		fixture.checkBox(ToolbarView.SHOW_ARCHIVE).click();

		// delete the task
		fixture.robot.settings().delayBetweenEvents(10);

		location = fixture.panel("Task 1").target.getLocationOnScreen();
		location.x += 10;
		location.y += 10;

		goal = fixture.label(ToolbarView.DELETE).target.getLocationOnScreen();
		goal.x += 10;
		goal.y += 10;

		fixture.robot.pressMouse(location, MouseButton.LEFT_BUTTON);
		while (!location.equals(goal)) {
			int movex = Integer.min(Integer.max(goal.x - location.x, -3), 3);
			int movey = Integer.min(Integer.max(goal.y - location.y, -3), 3);
			location.x += movex;
			location.y += movey;
			fixture.robot.moveMouse(location);
		}
		fixture.robot.settings().delayBetweenEvents(60);
		fixture.robot.settings().idleTimeout(100);

		fixture.robot.releaseMouseButtons();

		fixture.robot.waitForIdle();

		new JOptionPaneFixture(fixture.robot).yesButton().click();

		assertEquals(TabPaneController.getInstance().getView().getTabCount(), 1);
		assertEquals(TabPaneController.getInstance().getView().getTitleAt(0),
				"Workflow");

	}

	@After
	public void cleanup() {
		fixture.cleanUp();

	}

	public void createDummyTasks() {

		t1 = new TaskModel("Task 1", wfm.getStages().get(0));
		t2 = new TaskModel("Task 2", wfm.getStages().get(1));
		t3 = new TaskModel("Task 3", wfm.getStages().get(2));

		t1.setDueDate(new Date());
		t2.setDueDate(new Date());
		t3.setDueDate(new Date());

		t1.setDescription("description");
		t2.setDescription("description");
		t3.setDescription("description");

		wfc.reloadData();
		wfc.repaintView();

	}

}
