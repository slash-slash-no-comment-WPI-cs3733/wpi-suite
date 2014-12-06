/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.ScreenshotOnFail;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.WorkflowView;

/**
 * Tests for the workflow controller
 *
 * @author Jon Sorrells
 */
public class TestWorkflowController extends ScreenshotOnFail {

	private final String[] stageNames = { "first", "second", "not-a-duplicate",
			"last" };
	private WorkflowView wfv;
	private final WorkflowModel wfm = WorkflowModel.getInstance();
	private FrameFixture fixture;

	@Before
	public void setup() {
		// creates a workflow view
		wfv = new WorkflowView();

		// create a new workflow model
		wfm.makeIdenticalTo(new WorkflowModel());
		// give it the stages
		for (String name : stageNames) {
			new StageModel(name, false);
			wfv.addStageView(new StageView(name));
		}

		// create controller for view
		wfv.setController(new WorkflowController(wfv));

		JFrame frame = new JFrame();
		frame.add(wfv);

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

	@After
	public void cleanup() {
		fixture.cleanUp();
	}

}
