/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	@Test
	public void testGetStageViewByName() {
		StageView view = new StageView("test");
		StageView view2 = wfv.getStageViewByName("test");

		// it should have created a new view with the same name
		assertNotEquals(view, view2);
		assertEquals(view2.getName(), "test");

		wfv.addStageView(view);
		view2 = wfv.getStageViewByName("test");

		// now it should get the one from the tests
		assertEquals(view, view2);
	}

	@Test(expected = NullPointerException.class)
	public void testGetStageViewByNameNull() {
		wfv.getStageViewByName(null);
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}

}
