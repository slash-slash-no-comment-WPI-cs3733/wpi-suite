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

import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.ScreenshotOnFail;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.Colors;

/**
 * Tests for the workflow controller
 *
 * @author Jon Sorrells
 */
public class TestWorkflowController extends ScreenshotOnFail {

	private final String[] stageNames = { "first", "second", "not-a-duplicate",
			"last" };
	private FrameFixture fixture;

	@Before
	public void setup() {
		JanewayModule.reset();

		// give it the stages
		for (String name : stageNames) {
			new StageModel(name, false);
		}
		WorkflowController.getInstance().reloadData();

		JFrame frame = new JFrame();
		frame.add(WorkflowController.getInstance().getView());

		Dimension size = new Dimension(1000, 500);
		frame.setSize(size);
		frame.setPreferredSize(size);

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testStageNames() {

		// make sure it is visible
		fixture.requireVisible();

		// make sure the correct stages exist and were added in the correct
		// order
		for (int i = 0; i < 4; i++) {
			fixture.label(stageNames[i]).requireText(stageNames[i]);
		}
	}

	@Test
	public void testTaskHover() {

		// make a new task
		TaskModel t = new TaskModel("test", WorkflowModel.getInstance()
				.getStages().get(0));
		t.setDueDate(new Date());
		WorkflowController.getInstance().reloadData();

		fixture.robot.waitForIdle();
		JPanelFixture taskFixture = fixture.panel("test");

		// make sure the task is visible
		taskFixture.requireVisible();

		// make sure it highlights
		taskFixture.robot.moveMouse(taskFixture.target);
		taskFixture.robot.waitForIdle();
		assertEquals(taskFixture.target.getBackground(), Colors.TASK_HOVER);

		// make sure it re-highlights after a reload
		WorkflowController.getInstance().reloadData();
		fixture.robot.waitForIdle();
		taskFixture = fixture.panel("test");
		assertEquals(taskFixture.target.getBackground(), Colors.TASK_HOVER);
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}

}
