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

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.TabPaneView;
import taskManager.view.ToolbarView;

/**
 * Tests for the stage controller
 *
 * @author Jon Sorrells
 */
public class TestStageController {

	private FrameFixture fixture;

	@Before
	public void setup() {
		TabPaneView tpv = new TabPaneView();
		JanewayModule.toolV.setController(new ToolbarController(tpv));

		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(JanewayModule.toolV);
		panel.add(tpv);
		frame.add(panel);

		Dimension size = new Dimension(1500, 500);
		frame.setSize(size);
		frame.setPreferredSize(size);

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testNewStage() throws InterruptedException {
		fixture.button(ToolbarView.CREATE_STAGE).click();
		fixture.textBox(StageView.TEXT_LABEL).enterText("test");
		fixture.button(StageView.CHECK).click();

		// make sure the stage got added
		assertNotNull(WorkflowModel.getInstance().findStageByName("test"));
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
