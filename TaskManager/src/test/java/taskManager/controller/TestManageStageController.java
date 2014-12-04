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

import java.util.List;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ManageStageView;

/**
 * Tests for the manage stage controller
 *
 * @author Jon Sorrells
 */
public class TestManageStageController {

	private final String[] stageNames = { "first", "second", "third", "fourth" };
	private ManageStageView msv;
	private final WorkflowModel wfm = WorkflowModel.getInstance();
	private ManageStageController msc;
	private FrameFixture fixture;

	@Before
	public void setup() {
		System.out.println("starting setup");
		// creates a manage stage view
		msv = new ManageStageView();

		// create a new workflow model
		wfm.makeIdenticalTo(new WorkflowModel());
		// give it the stages
		for (String name : stageNames) {
			new StageModel(name, true);
		}

		// create controller for view
		msc = new ManageStageController(msv);
		msv.setController(msc);

		JFrame frame = new JFrame();
		frame.add(msv);
		frame.pack();

		fixture = new FrameFixture(frame);

		fixture.show();
		System.out.println("done with setup");
	}

	@Test
	public void testAddStage() {
		System.out.println("starting add stage");
		// the stages we should end up with
		final String[] result = { "first", "second", "third", "fourth", "NS" };

		// add a new stage named NS
		fixture.textBox(ManageStageView.NEW_STAGE_NAME).deleteText()
				.enterText("NS");
		fixture.button(ManageStageView.ADD_NEW_STAGE).click();

		checkStages(result);
		System.out.println("done with add stage");
	}

	@Test
	public void testRemoveStage() {
		System.out.println("starting remove stage");
		// the stages we should end up with
		final String[] result = { "first", "second", "fourth" };

		// remove the stage named third
		fixture.panel("third").button(ManageStageView.DELETE).click();

		checkStages(result);

		// remove two more stages to end up with one stage
		fixture.panel("second").button(ManageStageView.DELETE).click();
		fixture.panel("fourth").button(ManageStageView.DELETE).click();

		// make sure the delete button is disabled
		fixture.panel("first").button(ManageStageView.DELETE).requireDisabled();
		System.out.println("done with remove stage");
	}

	@Test
	public void testMoveStage() {
		System.out.println("starting move stage");
		// move stage third up twice
		fixture.panel("third").button(ManageStageView.MOVE_UP).click();
		fixture.panel("third").button(ManageStageView.MOVE_UP).click();
		String[] result = { "third", "first", "second", "fourth" };
		checkStages(result);

		// attempt to move fourth down (shouldn't do anything)
		fixture.panel("fourth").button(ManageStageView.MOVE_DOWN).click();
		checkStages(result);

		// move stage third down once
		fixture.panel("second").button(ManageStageView.MOVE_DOWN).click();
		String[] result2 = { "third", "first", "fourth", "second" };
		checkStages(result2);
		System.out.println("done with move stage");
	}

	@Test
	public void testAddInvalidStage() {
		// the stages we should end up with
		final String[] result = { "first", "second", "third", "fourth" };

		// add a new stage named NS
		fixture.textBox(ManageStageView.NEW_STAGE_NAME).deleteText()
				.enterText("first");
		fixture.button(ManageStageView.ADD_NEW_STAGE).click();

		checkStages(result);
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
			fixture.panel(stage.getName()).label().requireText(stage.getName());
		}
	}

	@After
	public void cleanup() {
		System.out.println("starting cleanup");
		fixture.cleanUp();
		System.out.println("done with cleanup");
	}

}
