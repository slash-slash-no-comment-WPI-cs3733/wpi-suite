/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import taskManager.view.StageView;

/**
 * Description
 *
 * @author Samee Swartz
 * @version Dec 2, 2014
 */
public class StageTitleController implements KeyListener {

	private StageView stageV;

	/**
	 * Constructor for the controller to change a stages title
	 *
	 * @param sv
	 *            the state view whose title is being changes
	 */
	public StageTitleController(StageView sv) {
		stageV = sv;
	}

	private Boolean checkTitle() {
		String newTitle = stageV.getLabelText();
		if (newTitle.equals("") || newTitle.equals(stageV.getName())) {
			return false;
		}
		return true;
	}

	private void validate() {
		stageV.enableChangeTitleCheckmark(checkTitle());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Do nothing

	}

	@Override
	public void keyReleased(KeyEvent e) {
		validate();
	}

}
