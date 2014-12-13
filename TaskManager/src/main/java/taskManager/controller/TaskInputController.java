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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
		PopupMenuListener, ListSelectionListener, PropertyChangeListener,
		ItemListener {

	private final EditTaskView etv;
	private boolean addUsersSelected = false;
	private boolean removeUsersSelected = false;

	/**
	 * The controller to validate input when editing a task
	 *
	 * @param etv
	 *            The edit task view being edited
	 */
	public TaskInputController(EditTaskView etv) {
		this.etv = etv;
		checkEditFields();
	}

	/**
	 * checks to see if the edit task fields aren't empty and meet the
	 * requirements. If a field doesn't meet the requirements, display an error
	 * 
	 * @return true if all the fields are valid
	 */
	public boolean checkEditFields() {

		addUsersSelected = !etv.getProjectUsersList().isSelectionEmpty();
		removeUsersSelected = !etv.getUsersList().isSelectionEmpty();

		boolean titleValid = true;
		boolean descriptionValid = true;
		boolean estEffortValid = true;
		boolean actEffortValid = true;
		// checks each required field and determines if it meets the
		// requirements for that field

		// Title
		if (etv.getTitle().getText().trim().isEmpty()) {
			titleValid = false;
		}
		// Description
		if (etv.getDescription().getText().trim().isEmpty()) {
			descriptionValid = false;
		}
		// Estimated Effort
		if (!etv.getEstEffort().getText().isEmpty()) {
			try {
				if (Integer.parseInt(etv.getEstEffort().getText().trim()) <= 0
						|| Integer
								.parseInt(etv.getEstEffort().getText().trim()) > 9999) {
					estEffortValid = false;
				}
			} catch (NumberFormatException e) {
				estEffortValid = false;
			}
		}
		if (!etv.getActEffort().getText().trim().isEmpty()) {
			// Actual Effort
			try {
				if (Integer.parseInt(etv.getActEffort().getText().trim()) < 0
						|| Integer
								.parseInt(etv.getActEffort().getText().trim()) > 9999) {
					actEffortValid = false;
				}
			} catch (NumberFormatException e) {
				actEffortValid = false;
			}
		}

		etv.setTitleErrorVisible(!titleValid);
		etv.setTitleFieldRed(!titleValid);
		etv.setDescriptionErrorVisible(!descriptionValid);
		etv.setDescriptionFieldRed(!descriptionValid);
		etv.setEstEffortErrorVisible(!estEffortValid);
		etv.setEstEffortFieldRed(!estEffortValid);
		etv.setActualEffortErrorVisible(!actEffortValid);
		etv.setActEffortFieldRed(!actEffortValid);

		return titleValid && descriptionValid && estEffortValid
				&& actEffortValid;
	}

	/**
	 *
	 * @return true if the comment box has a valid comment in it.
	 */
	public boolean checkCommentBox() {
		if (etv.getCommentsFieldText().trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * validate the inputs
	 */
	public void validate() {
		etv.setSaveEnabled(this.checkEditFields() && isEdited());
		etv.setAddUserEnabled(addUsersSelected);
		etv.setRemoveUserEnabled(removeUsersSelected);

		etv.setSubmitCommentEnabled(this.checkCommentBox());
	}

	/**
	 * Whether the view has been changed since the last save.
	 *
	 * @return true if the user has edited the form
	 */
	private boolean isEdited() {
		return etv.getController().isEdited();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		validate();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		validate();
	}

	@Override
	public void keyReleased(KeyEvent e) {
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("date")) {
			validate();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		validate();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}

}
