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
	private TaskModel archivedTask;

	@Before
	public void setup() {

		// creates a workflow view
		wfv = new WorkflowView();

		// create task models
		task = new TaskModel("Test task", wfm.findStageByName("New"));
		task.setName("Test task");
		task.setDueDate(new Date());

		archivedTask = new TaskModel("Archived task",
				wfm.findStageByName("New"));
		archivedTask.setName("Archived task");
		archivedTask.setDueDate(new Date());
		archivedTask.setArchived(true);

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
	public void testArchiveEnabled() {
		// make sure it is visible
		fixture.requireVisible();

		// make sure stage is visible
		fixture.panel("New").requireVisible();

		// The fixture of the task.
		JPanelFixture taskFixture = fixture.panel("Test task");

		// make sure task is visible
		taskFixture.requireVisible();

		// set the timing.
		taskFixture.robot.settings().delayBetweenEvents(100);
		taskFixture.robot.settings().idleTimeout(100);

		// get the dragon drop targets
		Component task = fixture.panel("Test task").target;
		Component archive = fixture.label(ToolbarView.ARCHIVE).target;

		// need to set to true to avoid network errors
		DDTransferHandler.dragSaved = true;

		// archive icon should be disabled at first.
		fixture.label(ToolbarView.ARCHIVE).requireDisabled();

		// move mouse to task and click
		fixture.robot.moveMouse(task);
		fixture.robot.pressMouse(MouseButton.LEFT_BUTTON);

		// move mouse to archive and release
		fixture.robot.moveMouse(archive);

		// archive icon should be enabled when dragging
		fixture.label(ToolbarView.ARCHIVE).requireEnabled();

		// release mouse
		fixture.robot.releaseMouse(MouseButton.LEFT_BUTTON);

		// archive icon should go back to disabled
		fixture.label(ToolbarView.ARCHIVE).requireDisabled();
		fixture.robot.waitForIdle();
	}

	@Test
	public void testUnarchiveEnabled() {
		// make sure it is visible
		fixture.requireVisible();

		// make sure stage is visible
		fixture.panel("New").requireVisible();

		// check the checkbox
		fixture.checkBox("Show Archived").check();

		// The fixture of the archived task.
		JPanelFixture archivedTaskFixture = fixture.panel("Archived task");
		archivedTaskFixture.requireVisible();

		// set the timing.
		archivedTaskFixture.robot.settings().delayBetweenEvents(100);
		archivedTaskFixture.robot.settings().idleTimeout(100);

		// get the dragon drop targets
		Component task = fixture.panel("Archived task").target;
		Component archive = fixture.label(ToolbarView.ARCHIVE).target;

		// need to set to true to avoid network errors
		DDTransferHandler.dragSaved = true;

		// unarchive icon should not be displayed.
		fixture.label(ToolbarView.UNARCHIVE).requireNotVisible();

		// move mouse to task and click
		fixture.robot.moveMouse(task);
		fixture.robot.pressMouse(MouseButton.LEFT_BUTTON);

		// move mouse to archive and release
		fixture.robot.moveMouse(archive);

		// archive icon should be enabled when dragging
		fixture.label(ToolbarView.UNARCHIVE).requireEnabled();

		// release mouse
		fixture.robot.releaseMouse(MouseButton.LEFT_BUTTON);

		// archive icon should go back to hidden
		fixture.label(ToolbarView.UNARCHIVE).requireNotVisible();
		fixture.robot.waitForIdle();
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
