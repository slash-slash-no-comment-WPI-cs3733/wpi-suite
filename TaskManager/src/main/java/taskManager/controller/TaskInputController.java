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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

		etv.setTitleFieldRed(!titleValid);
		etv.setDescriptionFieldRed(!descriptionValid);
		etv.setEstEffortFieldRed(!estEffortValid);
		etv.setActEffortFieldRed(!actEffortValid);

		return titleValid && descriptionValid && estEffortValid
				&& actEffortValid;
	}

	/**
	 * determines if a field that can be validated has been clicked If it has,
	 * set the fieldWithFocus variable to that field If something else has been
	 * clicked, set the fieldWithFocus variable to null
	 * 
	 * @param e
	 *            the mouse event
	 */
	private void checkFocus(MouseEvent e) {
		setAllErrorsInvisible();
		if (e.getComponent() != null && e.getComponent().getName() != null) {
			System.out.println(e.getComponent().getName() + " was clicked");
			if (e.getComponent().getName().equals(EditTaskView.TITLE)
					|| e.getComponent().getName().equals(EditTaskView.DESCRIP)
					|| e.getComponent().getName()
							.equals(EditTaskView.ACT_EFFORT)
					|| e.getComponent().getName()
							.equals(EditTaskView.EST_EFFORT)) {
				fieldWithFocus = e.getComponent();
			} else {
				fieldWithFocus = null;
			}
		} else {
			fieldWithFocus = null;
		}
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

		if (!this.checkFields() && fieldWithFocus != null) {
			switch (fieldWithFocus.getName()) {
			case EditTaskView.TITLE:
				etv.setTitleErrorVisible(!titleValid);
				break;
			case EditTaskView.DESCRIP:
				etv.setDescriptionErrorVisible(!descriptionValid);
				break;
			case EditTaskView.EST_EFFORT:
				etv.setEstEffortErrorVisible(!estEffortValid);
				break;
			case EditTaskView.ACT_EFFORT:
				etv.setActualEffortErrorVisible(!actEffortValid);
				break;
			}
		} else {
			setAllErrorsInvisible();
		}

		etv.repaint();
		// enable or disable the appropriate buttons
		etv.setSaveEnabled(this.checkFields());
		etv.setAddUserEnabled(addUsersSelected);
		etv.setRemoveUserEnabled(removeUsersSelected);
		etv.setCommentSubmitEnabled(this.checkSaveComment());
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
	public void mouseClicked(MouseEvent e) {
		checkFocus(e);
		validate();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		checkFocus(e);
		validate();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
