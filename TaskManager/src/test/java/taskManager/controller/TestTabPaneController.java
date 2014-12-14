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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.core.MouseButton;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import taskManager.TaskManager;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.TaskView;
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

	@Ignore
	public void testArchiveTaskWithOpenTab() {
		// click the task
		JPanelFixture taskFixture = fixture.panel("Task 1");
		taskFixture.click();
		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// go back to the workflow
		TabPaneController.getInstance().getView().setSelectedIndex(0);

		Component c = taskFixture.target;
		while (!(c instanceof TaskView) && c != null) {
			c = c.getParent();

		}
		Point location = c.getLocationOnScreen();

		// click right on the edge
		taskFixture.robot.pressMouse(location, MouseButton.LEFT_BUTTON);

		// move the mouse to the trash
		Point goal = fixture.label(ToolbarView.DELETE).target
				.getLocationOnScreen();
		taskFixture.robot.settings().delayBetweenEvents(10);
		while (!location.equals(goal)) {
			int movex = Integer.min(Integer.max(goal.x - location.x, -3), 3);
			int movey = Integer.min(Integer.max(goal.y - location.y, -3), 3);
			location.x += movex;
			location.y += movey;
			taskFixture.robot.moveMouse(location);
		}
		taskFixture.robot.settings().delayBetweenEvents(60);

		// end the drag
		taskFixture.robot.releaseMouseButtons();
		fixture.robot.waitForIdle();

		// switch to edit task tab
		TabPaneController.getInstance().getView().setSelectedIndex(1);

		String result = fixture.button(EditTaskView.ARCHIVE).text();

		assertEquals("Unarchive", result);
	}

	@Ignore
	public void testDeleteTaskWithOpenTab() {
		// click the task
		JPanelFixture taskFixture = fixture.panel("test");
		taskFixture.click();
		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// go back to the workflow
		TabPaneController.getInstance().getView().setSelectedIndex(0);

		// archive the task

		// show archived tasks
		fixture.robot.click(toolV.getComponent(3));

		// delete the task

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
