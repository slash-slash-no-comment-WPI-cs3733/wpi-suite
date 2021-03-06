/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.TaskManager;
import taskManager.controller.ToolbarController;
import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * Tests for the task preview
 *
 * @author Sam Khalandovsky
 * @version Dec 7, 2014
 */

public class TestTaskPreview {

	private FrameFixture fixture;
	private JFrame frame;
	private TaskModel tm1;

	@Before
	public void setup() {
		TaskManager.reset();

		WorkflowModel wfm = WorkflowModel.getInstance();

		StageModel sm1 = new StageModel("Stage1");
		StageModel sm2 = new StageModel("Stage2");

		tm1 = new TaskModel("Task1", sm1);
		tm1.setDueDate(new Date());
		tm1.setDescription("Description");
		tm1.addAssigned("User 1");
		tm1.setReq(new Requirement());

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
	public void testPreviewDisappear() {
		assertFalse(panelShown(TaskInfoPreviewView.NAME));
		fixture.panel("Task1").click();
		assertTrue(panelShown(TaskInfoPreviewView.NAME));
		fixture.button(ToolbarView.CREATE_STAGE).click();
		assertFalse(panelShown(TaskInfoPreviewView.NAME));
	}

	@Test
	public void testPreviewDisappearArchived() {
		tm1.setArchived(true);
		assertFalse(panelShown(TaskInfoPreviewView.NAME));
		fixture.panel("Task1").click();
		assertTrue(panelShown(TaskInfoPreviewView.NAME));
		fixture.button(ToolbarView.CREATE_STAGE).click();
		assertFalse(panelShown(TaskInfoPreviewView.NAME));
	}

	/**
	 * Attempts to find a panel, returns true if it cant
	 */
	public boolean panelShown(String name) {
		try {
			fixture.panel(name);
			return true;
		} catch (ComponentLookupException e) {
			return false;
		}
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
