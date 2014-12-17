/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class TaskInputController implements KeyListener, MouseListener,
		ActionListener, PopupMenuListener, ListSelectionListener,
		PropertyChangeListener, ItemListener {

	private final EditTaskView etv;
	private boolean addUsersSelected = false;
	private boolean removeUsersSelected = false;

	private boolean titleValid;
	private boolean descriptionValid;
	private boolean dateValid;
	private boolean actEffortValid;
	private boolean estEffortValid;

	private String fieldWithFocus = null;

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

		titleValid = true;
		descriptionValid = true;
		dateValid = true;
		estEffortValid = true;
		actEffortValid = true;
		// checks each required field and determines if it meets the
		// requirements for that field

		// Title
		if (etv.getTitleText().trim().isEmpty()) {
			titleValid = false;
		}
		// Description
		if (etv.getDescription().trim().isEmpty()) {
			descriptionValid = false;
		}
		// Date
		if (etv.getJXDatePicker().getEditor().getText().isEmpty()
				|| !etv.getJXDatePicker().isEditValid()) {
			dateValid = false;
		}
		// Estimated Effort
		if (!etv.getEstEffort().isEmpty()) {
			try {
				if (Integer.parseInt(etv.getEstEffort().trim()) <= 0
						|| Integer.parseInt(etv.getEstEffort().trim()) > 9999) {
					estEffortValid = false;
				}
			} catch (NumberFormatException e) {
				estEffortValid = false;
			}
		}

		if (!etv.getActEffort().isEmpty()) {
			// Actual Effort
			try {
				if (Integer.parseInt(etv.getActEffort().trim()) <= 0
						|| Integer.parseInt(etv.getActEffort().trim()) > 9999) {
					actEffortValid = false;
				}
			} catch (NumberFormatException e) {
				actEffortValid = false;
			}
		}

		etv.setTitleFieldRed(!titleValid);
		etv.setDateFieldRed(!dateValid);
		etv.setDescriptionFieldRed(!descriptionValid);
		etv.setEstEffortFieldRed(!estEffortValid);
		etv.setActEffortFieldRed(!actEffortValid);

		return titleValid && descriptionValid && dateValid && estEffortValid
				&& actEffortValid;
	}

	/**
	 * sets all 4 error bubbles to invisible
	 */
	private void setAllErrorsInvisible() {
		etv.setActualEffortErrorVisible(false);
		etv.setEstEffortErrorVisible(false);
		etv.setDescriptionErrorVisible(false);
		etv.setTitleErrorVisible(false);
	}

	/**
	 * figures out which field the cursor is in
	 */
	public void checkFocus() {

		if (etv.titleHasFocus()) {
			fieldWithFocus = EditTaskView.TITLE;
		} else if (etv.descriptionHasFocus()) {
			fieldWithFocus = EditTaskView.DESCRIP;
		} else if (etv.estEffortHasFocus()) {
			fieldWithFocus = EditTaskView.EST_EFFORT;
		} else if (etv.actEffortHasFocus()) {
			fieldWithFocus = EditTaskView.ACT_EFFORT;
		} else {
			fieldWithFocus = null;
		}
	}

	/**
	 * checks if comments are valid
	 * 
	 * @param commentValid
	 *            true if comment is valid
	 * @return commentValid
	 *
	 * @return true if the comment box has a valid comment in it.
	 */
	public boolean checkCommentBox() {
		if (etv.getCommentsFieldText().trim().isEmpty()) {
			return false;
		} else if (etv.getOrigCommentText().equals(etv.getCommentsFieldText())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * validate the inputs
	 */
	public void validate() {
		checkFocus();
		setAllErrorsInvisible();
		if (fieldWithFocus != null && !checkEditFields()) {
			switch (fieldWithFocus) {
			case EditTaskView.TITLE:
				etv.setTitleErrorVisible(!titleValid);
				break;
			case EditTaskView.DESCRIP:
				etv.setDescriptionErrorVisible(!descriptionValid);
				break;
			case EditTaskView.ACT_EFFORT:
				etv.setActualEffortErrorVisible(!actEffortValid);
				break;
			case EditTaskView.EST_EFFORT:
				etv.setEstEffortErrorVisible(!estEffortValid);
				break;
			}
		}
		etv.setSaveEnabled(this.checkEditFields() && isEdited());
		etv.setAddUserEnabled(addUsersSelected);
		etv.setRemoveUserEnabled(removeUsersSelected);

		etv.setSubmitCommentEnabled(this.checkCommentBox());
		etv.setCancelCommentEnabled(this.checkCommentBox());
		etv.setViewRequirementEnabled(etv.getSelectedRequirement() != null);
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
		// do nothing
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
	public void actionPerformed(ActionEvent e) {
		validate();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		validate();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
