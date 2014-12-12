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
import static org.junit.Assert.assertNull;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fest.swing.core.MouseButton;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.TaskManager;
import taskManager.ScreenshotOnFail;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.ToolbarView;

/**
 * Tests for the stage controller
 *
 * @author Jon Sorrells
 */
public class TestStageController extends ScreenshotOnFail {

	private FrameFixture fixture;
	private JFrame frame;
	private WorkflowModel wfm = WorkflowModel.getInstance();

	@Before
	public void setup() {
		TaskManager.reset();

		// give it a stage
		new StageModel("blah", false);

		frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(ToolbarController.getInstance().getView());
		panel.add(WorkflowController.getInstance().getView());
		frame.add(panel);
		Dimension size = new Dimension(1500, 500);
		frame.setSize(size);
		frame.setPreferredSize(size);
		WorkflowController.getInstance().reloadData();

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testDragOnEdge() {
		// create a new stage
		fixture.button(ToolbarView.CREATE_STAGE).click();
		fixture.textBox(StageView.TEXT_LABEL).enterText("test");
		fixture.button(StageView.CHECK).click();

		// make sure the stage got added
		assertNotNull(WorkflowModel.getInstance().findStageByName("test"));

		// find the edge of the stageview
		JLabelFixture stageFixture = fixture.label("test");
		Component c = stageFixture.target;
		while (!(c instanceof StageView) && c != null) {
			c = c.getParent();
		}
		Point location = c.getLocationOnScreen();

		// click right on the edge
		stageFixture.robot.pressMouse(location, MouseButton.LEFT_BUTTON);

		// move the mouse to the trash
		Point goal = fixture.label(ToolbarView.DELETE).target
				.getLocationOnScreen();
		stageFixture.robot.settings().delayBetweenEvents(10);
		while (!location.equals(goal)) {
			int movex = Integer.min(Integer.max(goal.x - location.x, -3), 3);
			int movey = Integer.min(Integer.max(goal.y - location.y, -3), 3);
			location.x += movex;
			location.y += movey;
			stageFixture.robot.moveMouse(location);
		}
		stageFixture.robot.settings().delayBetweenEvents(60);

		// end the drag
		stageFixture.robot.releaseMouseButtons();
		fixture.robot.waitForIdle();

		// make sure the stage got deleted
		assertNull(WorkflowModel.getInstance().findStageByName("test"));
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
