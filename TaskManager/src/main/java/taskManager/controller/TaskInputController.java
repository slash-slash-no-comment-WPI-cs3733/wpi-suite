/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import taskManager.view.EditTaskView;

/**
 * The controller for validating the inputs while editing or creating a task
 * 
 * @author Beth Martino
 * @author Stefan Alexander
 *
 */
public class TaskInputController implements KeyListener, FocusListener,
		PopupMenuListener {

	private final EditTaskView etv;

	public TaskInputController(EditTaskView etv) {
		this.etv = etv;
		checkFields();
	}

	/**
	 * checks to see if the edit task fields aren't empty and meet the
	 * requirements. If a field doesn't meet the requirements, display an error
	 */
	public boolean checkFields() {
		boolean titleValid = true;
		boolean descriptionValid = true;
		boolean estEffortValid = true;
		boolean actEffortValid = true;

		// checks each required field and determines if it meets the
		// requirements for that field

		// Title
		if (etv.getTitle().getText().isEmpty()) {
			titleValid = false;
		}
		// Description
		if (etv.getDescription().getText().isEmpty()) {
			descriptionValid = false;
		}
		// Estimated Effort
		if (!etv.getEstEffort().getText().isEmpty()) {
			try {

				if (Integer.parseInt(etv.getEstEffort().getText()) <= 0) {
					estEffortValid = false;
					etv.setEstEffortErrorText("Must be greater than 0");
				} else if (Integer.parseInt(etv.getEstEffort().getText()) > 9999) {
					estEffortValid = false;
					etv.setEstEffortErrorText("Must be less than 9999");
				}
			} catch (NumberFormatException e) {
				estEffortValid = false;
				etv.setEstEffortErrorText("Must be a positive integer");
			}
		}
		if (!etv.getActEffort().getText().isEmpty()) {
			// Actual Effort
			try {
				if (Integer.parseInt(etv.getActEffort().getText()) < 0) {
					actEffortValid = false;
					etv.setActualEffortErrorText("Can not be less than 0");
				} else if (Integer.parseInt(etv.getActEffort().getText()) > 9999) {
					actEffortValid = false;
					etv.setActualEffortErrorText("Must be less than 9999");
				}
			} catch (NumberFormatException e) {
				actEffortValid = false;
				etv.setActualEffortErrorText("Must be an integer");
			}
		}

		// display the errors
		etv.setTitleErrorVisible(!titleValid);
		etv.setDescriptionErrorVisible(!descriptionValid);
		etv.setEstEffortErrorVisible(!estEffortValid);
		etv.setActualEffortErrorVisible(!actEffortValid);

		return titleValid && descriptionValid && estEffortValid
				&& actEffortValid;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (this.checkFields()) {
			etv.enableSave();
		} else {
			etv.disableSave();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.checkFields()) {
			etv.enableSave();
		} else {
			etv.disableSave();
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.checkFields()) {
			etv.enableSave();
		} else {
			etv.disableSave();
		}
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		if (this.checkFields()) {
			etv.enableSave();
		} else {
			etv.disableSave();
		}
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		if (this.checkFields()) {
			etv.enableSave();
		} else {
			etv.disableSave();
		}
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		// TODO Auto-generated method stub
	}

}
