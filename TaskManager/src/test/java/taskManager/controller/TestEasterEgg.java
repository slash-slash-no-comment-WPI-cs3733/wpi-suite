/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.TaskManager;
import taskManager.view.ToolbarView;

/**
 * Tests for the rotation controller
 *
 * @author Jon Sorrells
 */
public class TestEasterEgg {

	private static final int[] code = { KeyEvent.VK_UP, KeyEvent.VK_UP,
			KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
			KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_B, KeyEvent.VK_A, KeyEvent.VK_ENTER };
	private ToolbarView tv;
	private JFrame frame;
	private FrameFixture fixture;

	@Before
	public void setup() {
		TaskManager.reset();
		tv = ToolbarController.getInstance().getView();

		frame = new JFrame();
		frame.add(WorkflowController.getInstance().getView());
		fixture = new FrameFixture(frame);
		fixture.show();
	}

	@Test
	public void testEnterFunMode() throws AWTException {
		// make sure we aren't in fun mode yet
		assertFalse(tv.isFunMode());

		EasterEggListener egg = new EasterEggListener();
		for (int c : code) {
			egg.dispatchKeyEvent(new KeyEvent(new JPanel(),
					KeyEvent.KEY_PRESSED, 0, 0, c, (char) 0));
		}

		fixture.robot.waitForIdle();

		// make sure fun mode was entered
		assertTrue(tv.isFunMode());
	}

	@Test
	public void testDontEnterFunMode() throws AWTException {
		// make sure we aren't in fun mode already
		assertFalse(tv.isFunMode());

		EasterEggListener egg = new EasterEggListener();

		WorkflowController.getInstance().getView().setVisible(false);

		for (int c : code) {
			egg.dispatchKeyEvent(new KeyEvent(new JPanel(),
					KeyEvent.KEY_PRESSED, 0, 0, c, (char) 0));
		}
		fixture.robot.waitForIdle();
		assertFalse("Shouldn't enter fun mode when workflow is not shown",
				tv.isFunMode());

		WorkflowController.getInstance().getView().setVisible(true);
		for (int c : code) {
			egg.dispatchKeyEvent(new KeyEvent(new JPanel(),
					KeyEvent.KEY_PRESSED, 0, 0, c, (char) 0));
			if (c == KeyEvent.VK_A) {
				// send an incorrect key
				egg.dispatchKeyEvent(new KeyEvent(new JPanel(),
						KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_Q, (char) 0));
			}
		}
		fixture.robot.waitForIdle();
		assertFalse(
				"Shouldn't enter fun mode when incorrect sequence is entered",
				tv.isFunMode());
	}

	@After
	public void cleanup() {
		fixture.cleanUp();
	}
}
