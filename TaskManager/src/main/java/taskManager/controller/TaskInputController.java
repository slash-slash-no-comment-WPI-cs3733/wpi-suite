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
		checkFields();
	}

	/**
	 * checks to see if the edit task fields aren't empty and meet the
	 * requirements. If a field doesn't meet the requirements, display an error
	 * 
	 * @return true if all the fields are valid
	 */
	public boolean checkFields() {

		addUsersSelected = !etv.getProjectUsersList().isSelectionEmpty();
		removeUsersSelected = !etv.getUsersList().isSelectionEmpty();

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
					etv.setEstEffortErrorText("Must be a positive integer");
				} else if (Integer.parseInt(etv.getEstEffort().getText()) > 9999) {
					estEffortValid = false;
					etv.setEstEffortErrorText("Must be between 1 and 9999");
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
					etv.setActualEffortErrorText("Can not be less than zero");
				} else if (Integer.parseInt(etv.getActEffort().getText()) > 9999) {
					actEffortValid = false;
					etv.setActualEffortErrorText("Must be between 0 and 9999");
				}
			} catch (NumberFormatException e) {
				actEffortValid = false;
				etv.setActualEffortErrorText("Must be a non negative integer");
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

	/**
	 * checks if comments are valid
	 * 
	 * @param commentValid
	 *            true if comment is valid
	 * @return commentValid
	 *
	 */
	public boolean checkSaveComment() {
		boolean commentValid = true;
		if (etv.getCommentsFieldText().trim().isEmpty()) {
			// Comments Pane
			commentValid = false;
		}
		return commentValid;
	}

	/**
	 * validate the inputs
	 */
	public void validate() {
		etv.setSaveEnabled(this.checkFields() && isEdited());
		etv.setAddUserEnabled(addUsersSelected);
		etv.setRemoveUserEnabled(removeUsersSelected);
		etv.setCommentSubmitEnabled(this.checkSaveComment());
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
		validate();
	}

	@Override
	public void focusLost(FocusEvent e) {
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
		validate();

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		validate();
	}

}
