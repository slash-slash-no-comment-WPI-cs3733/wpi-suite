/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.draganddrop;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import org.fest.swing.core.MouseButton;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.controller.WorkflowController;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ToolbarView;
import taskManager.view.WorkflowView;

/**
 * Tests for dragging and dropping tasks/stages to archive/delete.
 *
 * @author Stefan Alexander
 * @version December 4, 2014
 */
public class TestDragonDropArchiveDelete {

	private WorkflowView wfv;
	private final WorkflowModel wfm = WorkflowModel.getInstance();
	private FrameFixture fixture;
	private TaskModel task;

	@Before
	public void setup() {

		// creates a workflow view
		wfv = new WorkflowView();

		// create a task model
		TaskModel task = new TaskModel("Test task", wfm.findStageByName("New"));
		task.setName("Test task");
		task.setDueDate(new Date());

		// create controller for view
		wfv.setController(new WorkflowController(wfv));

		// set up frame
		JFrame frame = new JFrame();
		Dimension size = new Dimension(1000, 500);
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.add(JanewayModule.toolV);
		frame.add(wfv);

		// create fixture and show
		fixture = new FrameFixture(frame);
		fixture.show();
	}

	@Test
	public void testArchiveEnabled() throws InterruptedException {

		// make sure it is visible
		fixture.requireVisible();

		// make sure stage is visible
		fixture.panel("New").requireVisible();

		JPanelFixture taskFixture = fixture.panel("Test task");

		// make sure task is visible
		taskFixture.requireVisible();

		taskFixture.robot.settings().delayBetweenEvents(100);
		taskFixture.robot.settings().idleTimeout(100);

		// get the dragon drop targets
		Component task = fixture.panel("Test task").target;
		Component archive = fixture.label(ToolbarView.ARCHIVE).target;

		fixture.label(ToolbarView.ARCHIVE).requireDisabled();
		// move mouse to task and click
		fixture.robot.moveMouse(task);
		fixture.robot.pressMouse(MouseButton.LEFT_BUTTON);
		// move mouse to archive and release
		fixture.robot.moveMouse(archive);
		fixture.label(ToolbarView.ARCHIVE).requireEnabled();
		fixture.robot.releaseMouse(MouseButton.LEFT_BUTTON);
		fixture.robot.moveMouse(task);
		fixture.robot.waitForIdle();
		// Thread.sleep(1000);
		// fixture.label(ToolbarView.ARCHIVE).requireDisabled();

	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
