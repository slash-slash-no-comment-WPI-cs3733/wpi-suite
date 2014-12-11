/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.draganddrop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.core.MouseButton;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.MockNetwork;
import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;

import com.db4o.ext.DatabaseClosedException;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;

/**
 * Tests for Drop Area Panel
 *
 * @author Jon Sorrells
 */
public class TestDropAreaPanel {

	private FrameFixture fixture;
	private TaskModel task;
	private StageModel sm;
	WorkflowController wfc;
	private boolean shouldFail = false;

	@BeforeClass
	public static void netSetup() {
		Network.setInstance(new MockNetwork());
	}

	@Before
	public void setup() {

		JanewayModule.reset();

		wfc = WorkflowController.getInstance();

		sm = new StageModel("TestStage");

		// add a task to the workflow
		task = new TaskModel("test", sm);
		task.setDueDate(new Date(0));

		wfc.reloadData();

		JFrame frame = new JFrame();
		frame.add(wfc.getView());

		Dimension size = new Dimension(1000, 500);
		frame.setSize(size);
		frame.setPreferredSize(size);

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void dragTask() {
		StageModel sm2 = new StageModel("TestStage2");

		TaskModel tm1 = new TaskModel("task21", sm2);
		tm1.setDueDate(new Date(0));
		TaskModel tm2 = new TaskModel("task22", sm2);
		tm2.setDueDate(new Date(0));

		wfc.reloadData();

		assertTrue(sm.containsTask(task));

		// JPanelFixture taskFixture = fixture.panel("test");

		fixture.robot.settings().delayBetweenEvents(10);

		Point location = fixture.panel("test").target.getLocationOnScreen();
		location.x += 10;
		location.y += 10;

		Point goal = fixture.panel("task22").target.getLocationOnScreen();
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
		assertTrue(sm2.containsTask(task));

	}

	@Test
	public void dragOnePixel() {
		// catch exceptions from EDT
		UncaughtExceptionHandler old = java.lang.Thread
				.getDefaultUncaughtExceptionHandler();
		Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread th, Throwable ex) {
				// ignore networking stuff
				StackTraceElement[] x = ex.getStackTrace();
				if (!x[0].getClassName().equals(Request.class.getName())
						&& !(ex instanceof DatabaseClosedException)) {

					shouldFail = true;
				}
			}
		};
		java.lang.Thread.setDefaultUncaughtExceptionHandler(h);

		// get the fixture for the task panel
		JPanelFixture taskFixture = fixture.panel("test");

		// try dragging really fast
		taskFixture.robot.settings().delayBetweenEvents(0);
		taskFixture.robot.settings().idleTimeout(0);

		// actually drag now
		Point location = taskFixture.target.getLocation();
		location.x += 5;
		location.y += 5;
		taskFixture.robot.pressMouse(taskFixture.target.getParent(), location);
		location.x += 1;
		taskFixture.robot.moveMouse(taskFixture.target.getParent(), location);
		taskFixture.robot.releaseMouseButtons();

		// let it process things
		taskFixture.robot.settings().idleTimeout(100);
		fixture.robot.waitForIdle();

		// check if anything failed in other threads
		java.lang.Thread.setDefaultUncaughtExceptionHandler(old);
		assertFalse(shouldFail);
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
