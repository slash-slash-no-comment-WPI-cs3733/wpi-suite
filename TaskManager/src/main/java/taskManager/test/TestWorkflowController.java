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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.WorkflowView;

/**
 * Description tests for the workflow controller
 *
 * @author Jon Sorrells
 */
public class TestWorkflowController {

	private final String[] stageNames = { "first", "duplicate", "duplicate",
			"last" };
	private WorkflowView wfv;
	private WorkflowModel wfm;

	@Before
	public void setup() {
		// creates a workflow view
		wfv = new WorkflowView();

		// create a new workflow model
		wfm = new WorkflowModel();
		// give it the stages
		for (String name : stageNames) {
			new StageModel(wfm, name, false);
		}

		// create the controller for the view
		wfv.setController(new WorkflowController(wfv, wfm));
	}

	/**
	 * I need to look into other ways to test this
	 *
	 */
	@Test
	public void testStageNames() {
		int i = 0;
		Component[] stagePanels = wfv.getComponents();
		for (Component panel : stagePanels) {
			if (panel instanceof JPanel) {
				Component[] components = ((JPanel) panel).getComponents();
				for (Component thing : components) {
					if (thing instanceof JPanel) {
						Component[] stuff = ((JPanel) thing).getComponents();
						for (Component blah : stuff) {
							assertEquals(((JLabel) blah).getText(),
									stageNames[i++]);
						}
					}
				}
			}
		}
	}

}
