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

import taskManager.JanewayModule;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.ToolbarView;

/**
 * Tests for the stage controller
 *
 * @author Jon Sorrells
 */
public class TestStageController {

	private FrameFixture fixture;
	private JFrame frame;

	@Before
	public void setup() {
		JanewayModule.toolV.setController(new ToolbarController(
				JanewayModule.tabPaneC.getTabView()));

		frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(JanewayModule.toolV);
		panel.add(JanewayModule.tabPaneC.getTabView());
		frame.add(panel);

		Dimension size = new Dimension(1500, 500);
		frame.setSize(size);
		frame.setPreferredSize(size);

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testNewStage() {
		// create a new stage
		fixture.button(ToolbarView.CREATE_STAGE).click();
		fixture.textBox(StageView.TEXT_LABEL).enterText("test");
		fixture.button(StageView.CHECK).click();

		// make sure the stage got added
		assertNotNull(WorkflowModel.getInstance().findStageByName("test"));

		// drag it somewhere invalid
		// start dragging
		JLabelFixture stageFixture = fixture.label("test");
		Point location = stageFixture.target.getLocationOnScreen();
		location.x += 5;
		location.y += 5;
		stageFixture.robot.pressMouse(location, MouseButton.LEFT_BUTTON);

		// move the mouse all the way to the edge of the screen
		stageFixture.robot.settings().delayBetweenEvents(10);
		while (location.y > 0) {
			location.y -= 1;
			stageFixture.robot.moveMouse(location);

		}
		stageFixture.robot.settings().delayBetweenEvents(60);

		// end the drag
		stageFixture.robot.releaseMouseButtons();
		fixture.robot.waitForIdle();

		// make sure the new stage is displayed
		fixture.label("test").requireVisible();
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
