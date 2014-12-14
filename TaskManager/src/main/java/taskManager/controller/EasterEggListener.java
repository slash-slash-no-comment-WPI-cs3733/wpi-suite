/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/**
 * Listens for certain key combinations
 *
 * @author Jon Sorrells
 */
public class EasterEggListener implements KeyEventDispatcher {

	private static final int[] code = { KeyEvent.VK_UP, KeyEvent.VK_UP,
			KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
			KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_B, KeyEvent.VK_A, KeyEvent.VK_ENTER };
	private int location = 0;

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED
				&& WorkflowController.getInstance().getView().isShowing()) {
			int currentKey = e.getKeyCode();
			if (currentKey == code[location]) {
				location++;
				if (location == code.length) {
					location = 0;
					ToolbarController.getInstance().getView().setFunMode(true);
				}
			} else {
				location = 0;
			}
		}
		return false;
	}

}
