/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.controller.ManageStageController;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ManageStageView;

/**
 * Tests for the manage stage controller
 *
 * @author Jon Sorrells
 */
public class TestManageStageController {

	private final String[] stageNames = { "first", "duplicate", "duplicate",
			"last" };
	private ManageStageView msv;
	private WorkflowModel wfm;
	private FrameFixture fixture;

	@Before
	public void setup() {
		// creates a manage stage view
		msv = new ManageStageView();

		// create a new workflow model
		wfm = new WorkflowModel();
		// give it the stages
		for (String name : stageNames) {
			new StageModel(wfm, name, false);
		}

		// create controller for view
		msv.setController(new ManageStageController(msv, wfm));

		JFrame frame = new JFrame();
		frame.add(msv);

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testAddStage() {
		// the stages we should end up with
		final String[] result = { "first", "duplicate", "duplicate", "last",
				"New Stage" };

		// add a new stage named New Stage
		fixture.textBox("newStageName").deleteText().enterText("New Stage");
		fixture.button("Add new stage").click();

		checkStages(result);
	}

	@Test
	public void testRemoveStage() {
		// the stages we should end up with
		final String[] result = { "first", "duplicate", "last" };

		// remove the stage with id duplicate#
		fixture.panel("duplicate#").button("Delete").click();

		checkStages(result);
	}

	@Test
	public void testMoveStage() {
		// move stage duplicate# up twice
		fixture.panel("duplicate#").button("Move Up").click();
		fixture.panel("duplicate#").button("Move Up").click();
		String[] result = { "duplicate", "first", "duplicate", "last" };
		checkStages(result);

		// attempt to move last down (shouldn't do anything)
		fixture.panel("last").button("Move Down").click();
		checkStages(result);

		// move stage duplicate down once
		fixture.panel("duplicate").button("Move Down").click();
		String[] result2 = { "duplicate", "first", "last", "duplicate" };
		checkStages(result2);
	}

	/**
	 * Checks to make sure that the names of the stages are what is expected
	 *
	 * @param expectedResult
	 *            What you expect the names of the stages to be
	 */
	private void checkStages(String[] expectedResult) {

		// make sure it has the correct number of stages
		List<StageModel> stages = wfm.getStages();
		assertEquals(stages.size(), expectedResult.length);

		// ensure model has proper stages
		for (int i = 0; i < expectedResult.length; i++) {
			assertEquals(stages.get(i).getName(), expectedResult[i]);
		}

		// ensure view is displaying proper stages
		for (StageModel stage : stages) {
			fixture.panel(stage.getID()).label().requireText(stage.getName());
		}
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}

}
