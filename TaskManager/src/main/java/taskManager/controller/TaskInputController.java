/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
		PopupMenuListener, ListSelectionListener {

	private final EditTaskView etv;
	private boolean addUsersSelected = false;
	private boolean removeUsersSelected = false;
	private Component fieldWithFocus;

	boolean titleValid = true;
	boolean descriptionValid = true;
	boolean estEffortValid = true;
	boolean actEffortValid = true;

	public TaskInputController(EditTaskView etv) {
		this.etv = etv;
		fieldWithFocus = null;
		checkFields();
	}

	/**
	 * checks to see if the edit task fields aren't empty and meet the
	 * requirements. If a field doesn't meet the requirements, display an error
	 */
	public boolean checkFields() {

		addUsersSelected = !etv.getProjectUsersList().isSelectionEmpty();
		removeUsersSelected = !etv.getUsersList().isSelectionEmpty();

		titleValid = true;
		descriptionValid = true;
		estEffortValid = true;
		actEffortValid = true;
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

				if (Integer.parseInt(etv.getEstEffort().getText()) <= 0
						|| Integer.parseInt(etv.getEstEffort().getText()) > 9999) {
					estEffortValid = false;
					etv.setEstEffortFieldRed(true);
				}
			} catch (NumberFormatException e) {
				estEffortValid = false;
				etv.setEstEffortFieldRed(true);
			}
		}
		if (!etv.getActEffort().getText().isEmpty()) {
			// Actual Effort
			try {
				if (Integer.parseInt(etv.getActEffort().getText()) < 0
						|| Integer.parseInt(etv.getActEffort().getText()) > 9999) {
					actEffortValid = false;
					etv.setActEffortFieldRed(true);
				}
			} catch (NumberFormatException e) {
				actEffortValid = false;
				etv.setActEffortFieldRed(true);
			}
		}
		etv.setEstEffortFieldRed(!estEffortValid);
		etv.setActEffortFieldRed(!actEffortValid); // set the borders back to
													// black

		// display the errors
		etv.setTitleErrorVisible(!titleValid);
		etv.setDescriptionErrorVisible(!descriptionValid);
		etv.setEstEffortErrorVisible(!estEffortValid);
		etv.setActualEffortErrorVisible(!actEffortValid);

		return titleValid && descriptionValid && estEffortValid
				&& actEffortValid;
	}

	/**
	 * validate the inputs
	 */
	public void validate() {

		if (fieldWithFocus != null && !checkFields()) {
			switch (fieldWithFocus.getName()) {
			case EditTaskView.TITLE:
				if (!titleValid) {
					etv.showTitleError(true);
				}
				break;
			case EditTaskView.DESCRIP:
				if (!descriptionValid) {
					etv.showDescripError(true);
				}
				break;
			case EditTaskView.EST_EFFORT:
				if (!estEffortValid) {
					etv.showEstEffortError(true);
				}
				break;

			case EditTaskView.ACT_EFFORT:
				if (!actEffortValid) {
					etv.showActEffortError(true);
				}
				break;

			}
		} else {
			etv.showActEffortError(false);
			etv.showDescripError(false);
			etv.showEstEffortError(false);
			etv.showTitleError(false);
		}
		// enable or disable the appropriate buttons
		etv.setSaveEnabled(this.checkFields());
		etv.setAddUserEnabled(addUsersSelected);
		etv.setRemoveUserEnabled(removeUsersSelected);
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
		validate();
	}

	@Override
	public void focusGained(FocusEvent e) {
		fieldWithFocus = e.getComponent();
		validate();
	}

	@Override
	public void focusLost(FocusEvent e) {
		fieldWithFocus = null;
		validate();
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		validate();
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		validate();
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		validate();

	}

}
