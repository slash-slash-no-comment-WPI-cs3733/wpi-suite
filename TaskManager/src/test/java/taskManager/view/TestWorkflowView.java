/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;

import taskManager.ScreenshotOnFail;
import taskManager.controller.WorkflowController;

/**
 * Tests for the workflow view
 *
 * @author Jon Sorrells
 */
public class TestWorkflowView extends ScreenshotOnFail {

	private FrameFixture fixture;
	private WorkflowView wfv;
	private JFrame frame;

	@Before
	public void setup() {
		wfv = WorkflowController.getInstance().getView();

		frame = new JFrame();
		frame.add(wfv);

		fixture = new FrameFixture(frame);
		fixture.show();
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}

}
