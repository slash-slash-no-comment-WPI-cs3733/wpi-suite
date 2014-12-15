/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fest.swing.core.MouseButton;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskManager.MockNetwork;
import taskManager.ScreenshotOnFail;
import taskManager.TaskManager;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.RotationView;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.ToolbarView;
import edu.wpi.cs.wpisuitetng.network.Network;

/**
 * Tests for the rotation controller
 *
 * @author Jon Sorrells
 */
public class TestRotationController extends ScreenshotOnFail {

	private FrameFixture fixture;
	private JFrame frame;
	private TaskModel t = null;

	@BeforeClass
	public static void netSetup() {
		Network.setInstance(new MockNetwork());
	}

	@Before
	public void setup() {
		TaskManager.reset();
		new TaskManager();
		TaskManager.reset();

		// give it a stage
		StageModel s = new StageModel("stage", false);

		// and a task
		t = new TaskModel("test", s);
		t.setDueDate(new Date(0));

		frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(ToolbarController.getInstance().getView());
		panel.add(WorkflowController.getInstance().getView());
		frame.add(panel);
		Dimension size = new Dimension(1000, 500);
		frame.setSize(size);
		frame.setPreferredSize(size);
		WorkflowController.getInstance().reloadData();

		fixture = new FrameFixture(frame);

		fixture.show();

		// enter fun mode
		ToolbarController.getInstance().getView().setFunMode(true);
		fixture.robot.waitForIdle();
	}

	@Test
	public void testFunButtonVibility() {
		// turn off fun mode
		fixture.checkBox(ToolbarView.FUN_MODE).uncheck();
		fixture.robot.waitForIdle();

		// make sure fun buttons are not visible at first
		try {
			fixture.button(ToolbarView.TASK_ANGLES);
			fail("found Randomize Task Angles button, which should have been invisible");
		} catch (ComponentLookupException e) {
			// component was not shown, continue with test
		}

		// enter fun mode
		ToolbarController.getInstance().getView().setFunMode(true);
		fixture.robot.waitForIdle();

		// make sure fun buttons are visible now
		fixture.button(ToolbarView.TASK_ANGLES).requireVisible();
	}

	@Test
	public void testRotatedTaskClicking() {

		JPanelFixture rotationFixture = fixture.panel("rotation - test");

		// set angle to 90 deg
		((RotationView) rotationFixture.target).setAngle(Math.PI / 2);
		WorkflowController.getInstance().reloadData();
		fixture.robot.waitForIdle();

		RotationView rotationView = (RotationView) fixture
				.panel("rotation - test").target;
		int x = rotationFixture.target.getWidth();
		int y = rotationFixture.target.getHeight();

		// make sure only clicks on the task do stuff
		fixture.robot.click(rotationView, new Point(10, 10)); // not on task
		try {
			fixture.panel(TaskInfoPreviewView.NAME);
			fail("task info preview view opened from click not on task");
		} catch (ComponentLookupException e) {
			// component was not shown, continue with test
		}

		fixture.robot.click(rotationView, new Point(x - 10, y - 10)); // not on
																		// task
		try {
			fixture.panel(TaskInfoPreviewView.NAME);
			fail("task info preview view opened from click not on task");
		} catch (ComponentLookupException e) {
			// component was not shown, continue with test
		}

		fixture.robot.click(rotationView, new Point(x / 2, y / 2)); // on task
		fixture.panel(TaskInfoPreviewView.NAME).requireVisible();
	}

	@Test
	public void testFunModeDragons() {

		// ensure the task exists
		assertNotNull(WorkflowModel.getInstance().findTaskByID("test"));

		// set angle to 90 deg
		JPanelFixture rotationFixture = fixture.panel("rotation - test");
		((RotationView) rotationFixture.target).setAngle(Math.PI / 2);
		WorkflowController.getInstance().reloadData();
		fixture.robot.waitForIdle();

		// click at the center of the rotatioin view
		rotationFixture = fixture.panel("rotation - test");
		Component c = rotationFixture.target;
		Point location = c.getLocationOnScreen();
		location.x += c.getWidth() / 2;
		location.y += c.getHeight() / 2;
		fixture.robot.pressMouse(location, MouseButton.LEFT_BUTTON);

		// move the mouse to the trash
		Point goal = fixture.label(ToolbarView.ARCHIVE).target
				.getLocationOnScreen();
		goal.x += 10;
		goal.y += 10;
		fixture.robot.settings().delayBetweenEvents(10);
		while (!location.equals(goal)) {
			int movex = Integer.min(Integer.max(goal.x - location.x, -3), 3);
			int movey = Integer.min(Integer.max(goal.y - location.y, -3), 3);
			location.x += movex;
			location.y += movey;
			fixture.robot.moveMouse(location);
		}
		fixture.robot.settings().delayBetweenEvents(60);

		// end the drag
		fixture.robot.releaseMouseButtons();
		fixture.robot.waitForIdle();

		// make sure the task was archived
		assertTrue(WorkflowModel.getInstance().findTaskByID("test")
				.isArchived());
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
