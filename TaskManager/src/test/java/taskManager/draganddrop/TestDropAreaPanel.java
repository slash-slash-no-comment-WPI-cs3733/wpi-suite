/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.draganddrop;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.controller.WorkflowController;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.WorkflowView;

/**
 * Tests for Drop Area Panel
 *
 * @author Jon Sorrells
 */
public class TestDropAreaPanel {

	private WorkflowView wfv;
	private final WorkflowModel wfm = WorkflowModel.getInstance();
	private FrameFixture fixture;
	private TaskModel task;

	@Before
	public void setup() {
		// creates a workflow view
		wfv = new WorkflowView();

		// add a task to the workflow
		task = new TaskModel("test", wfm.getStages().get(0));
		task.setDueDate(new Date(0));

		// create controller for view
		wfv.setController(new WorkflowController(wfv));

		JFrame frame = new JFrame();
		frame.add(wfv);

		Dimension size = new Dimension(1000, 500);
		frame.setSize(size);
		frame.setPreferredSize(size);

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void dragOnePixel() {
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
		fixture.robot.waitForIdle();
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
