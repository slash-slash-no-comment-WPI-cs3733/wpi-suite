/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.JXDatePicker;

import taskManager.controller.EditTaskController;
import taskManager.controller.TaskInputController;

/**
 *  Edit panel for a new task
 */

/**
 * @author Tyler Jaskoviak
 *
 */

public class EditTaskView extends JPanel {

	public static final String STAGES = "stages";
	public static final String REQUIREMENTS = "requirements";
	public static final String CANCEL = "cancel";
	public static final String SAVE = "save";
	public static final String VIEW_REQ = "viewReq";
	public static final String SUBMIT_COMMENT = "submitComment";
	public static final String ADD_USER = "addUser";
	public static final String REMOVE_USER = "removeUser";
	public static final String DELETE = "delete";
	public static final String COMMENTS = "comments";
	public static final String ACT_EFFORT = "act_effort";
	public static final String EST_EFFORT = "est_effort";
	public static final String DUE_DATE = "due_date";
	public static final String NO_REQ = "[None]";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton save;
	private JButton cancel;
	private JButton addUser;
	private JButton removeUser;
	private JButton delete;
	private JButton addReq;
	private JButton submitComment;

	private JTextField titleField;
	private JTextArea descripArea;
	private JXDatePicker dateField;
	private JTextField estEffortField;
	private JTextField actEffortField;
	private JTextField commentsField;
	private JPanel window;

	private Mode mode;

	public enum Mode {
		CREATE, EDIT;
	}

	private ScrollList usersList;
	private ScrollList projectUsersList;

	private JLabel titleError;
	private JLabel descriptionError;
	private JLabel estimatedEffortError;
	private JLabel actualEffortError;

	private JComboBox<String> stages;
	private JComboBox<String> requirements;

	public EditTaskController controller;

	/**
	 * Creates a Edit Task Panel so that you can change all of the values of a
	 * task: Title Description Due Date Estimated Effort Actual Effort Adding
	 * Comments
	 */
	public EditTaskView(Mode mode) {
		// TODO: User Mode to switch between create and edit views
		// When Task added make EditTask take in a Task called currTask
		this.mode = mode;

		window = new JPanel();
		this.setLayout(new FlowLayout());

		Dimension nt_panelSize = getPreferredSize();
		nt_panelSize.width = 1000; // TODO
		nt_panelSize.height = 500; // Decide size
		window.setPreferredSize(nt_panelSize);
		this.setPreferredSize(nt_panelSize);
		this.setMinimumSize(nt_panelSize);

		// window.setBorder(BorderFactory.createTitledBorder(""));

		// JLabels
		JLabel nt_titleLabel = new JLabel("Title ");
		JLabel nt_descriptionLabel = new JLabel("Description ");
		JLabel nt_dueDateLabel = new JLabel("Due Date ");
		JLabel nt_stageLabel = new JLabel("Stage ");
		JLabel nt_usersLabel = new JLabel("Users ");
		JLabel nt_estimatedEffortLabel = new JLabel("Estimated Effort ");
		JLabel nt_actualEffortLabel = new JLabel("Actual Effort ");
		JLabel nt_commentsLabel = new JLabel("Comments ");
		JLabel nt_requirementLabel = new JLabel("Requirement ");

		JLabel nt_titleLabel_error = new JLabel("This a required field");
		nt_titleLabel_error.setVisible(false);
		titleError = nt_titleLabel_error;
		JLabel nt_descriptionLabel_error = new JLabel(
				"This is a required field");
		nt_descriptionLabel_error.setVisible(false);
		descriptionError = nt_descriptionLabel_error;
		JLabel nt_estimatedEffortLabel_error = new JLabel(
				"Estimated Effort is required");
		nt_estimatedEffortLabel_error.setVisible(false);
		estimatedEffortError = nt_estimatedEffortLabel_error;
		JLabel nt_actualEffortLabel_error = new JLabel("");
		nt_actualEffortLabel_error.setVisible(false);
		actualEffortError = nt_actualEffortLabel_error;

		// JTextFields
		// sets all text fields editable and adds them to global variables
		JTextField nt_titleField = new JTextField(25);
		nt_titleField.setEditable(true);
		titleField = nt_titleField;
		JTextArea nt_descriptionArea = new JTextArea(2, 25);
		nt_descriptionArea.setEditable(true);
		descripArea = nt_descriptionArea;
		nt_descriptionArea.setLineWrap(true);
		JScrollPane nt_descriptionScrollPane = new JScrollPane(
				nt_descriptionArea);
		nt_descriptionScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		nt_descriptionScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JTextField nt_estimatedEffortField = new JTextField(10);
		nt_estimatedEffortField.setEditable(true);
		nt_estimatedEffortField.setName(EST_EFFORT);
		estEffortField = nt_estimatedEffortField;
		JTextField nt_actualEffortField = new JTextField(10);
		nt_actualEffortField.setEditable(true);
		nt_actualEffortField.setName(ACT_EFFORT);
		actEffortField = nt_actualEffortField;
		JTextField nt_commentsField = new JTextField(25);
		nt_commentsField.setEditable(true);
		nt_commentsField.setName(COMMENTS);
		commentsField = nt_estimatedEffortField;

		// adds calendar
		JXDatePicker nt_dueDateField = new JXDatePicker();
		nt_dueDateField.setName("due_date");
		this.dateField = nt_dueDateField;
		dateField.setDate(Calendar.getInstance().getTime());

		// JTextArea
		// TODO
		// Get to add users
		usersList = new ScrollList();
		projectUsersList = new ScrollList();

		// TODO
		// Comment Pane

		// Requirement Pane
		requirements = new JComboBox<String>();
		requirements.setName(REQUIREMENTS);

		// JButtons
		// Delete Task and close the window
		delete = new JButton("Delete");
		delete.setName(DELETE);
		// Add user to list
		addUser = new JButton("Add User");
		addUser.setName(ADD_USER);
		// remove user from list

		removeUser = new JButton("Remove User");
		removeUser.setName(REMOVE_USER);

		// Add comment to comments
		submitComment = new JButton("Submit Comment");
		submitComment.setName(SUBMIT_COMMENT);
		// add requirement
		addReq = new JButton("View Requirement");
		addReq.setName(VIEW_REQ);
		// saves all the data and closes the window
		save = new JButton("Save");
		save.setName(SAVE);
		this.disableSave();
		// closes the window without saving
		cancel = new JButton("Cancel");
		cancel.setName(CANCEL);

		// Combo Box for Stage
		stages = new JComboBox<String>();
		stages.setName(STAGES);

		window.setLayout(new GridBagLayout());

		GridBagConstraints newTaskGridBag = new GridBagConstraints();

		// First Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;

		newTaskGridBag.weightx = 0.6;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 0;

		newTaskGridBag.gridy = 0;
		window.add(nt_titleLabel, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		window.add(nt_descriptionLabel, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		window.add(nt_dueDateLabel, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		window.add(nt_stageLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		window.add(nt_usersLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 5;
		window.add(nt_estimatedEffortLabel, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		window.add(nt_actualEffortLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.10;
		newTaskGridBag.gridy = 7;
		window.add(nt_commentsLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 9;
		window.add(nt_requirementLabel, newTaskGridBag);

		// Second Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = 0.4;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 1;

		newTaskGridBag.gridy = 0;
		window.add(nt_titleField, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		window.add(nt_descriptionScrollPane, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		window.add(nt_dueDateField, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		window.add(stages, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		window.add(usersList, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 5;
		window.add(nt_estimatedEffortField, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		window.add(nt_actualEffortField, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		window.add(nt_commentsField, newTaskGridBag);

		// TODO
		// List of Comments
		// newTaskGridBag.gridy = 8;

		// List of Requirements
		newTaskGridBag.gridy = 9;
		window.add(requirements, newTaskGridBag);

		// Third Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = .5;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 2;

		newTaskGridBag.gridy = 0;
		window.add(nt_titleLabel_error, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		window.add(nt_descriptionLabel_error, newTaskGridBag);

		JPanel userButtons = new JPanel();
		userButtons.setLayout(new BoxLayout(userButtons, BoxLayout.Y_AXIS));
		userButtons.add(addUser);
		userButtons.add(removeUser);
		newTaskGridBag.gridy = 4;
		window.add(userButtons, newTaskGridBag);

		newTaskGridBag.gridy = 5;
		window.add(nt_estimatedEffortLabel_error, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		window.add(nt_actualEffortLabel_error, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		window.add(submitComment, newTaskGridBag);

		newTaskGridBag.gridy = 9;
		window.add(requirements, newTaskGridBag);

		JPanel bottomBtns = new JPanel();
		bottomBtns.add(save);
		bottomBtns.add(cancel);
		if (this.mode == Mode.EDIT) {
			bottomBtns.add(delete);
		}
		newTaskGridBag.gridy = 11;
		window.add(bottomBtns, newTaskGridBag);

		// Fourth Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = .5;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 3;

		newTaskGridBag.gridy = 4;
		window.add(projectUsersList, newTaskGridBag);

		this.add(window);
	}

	/**
	 * Adds the action listener (controller) to this view
	 * 
	 * @param controller
	 *            the controller to be attached to this view
	 */
	public void setController(EditTaskController controller) {
		this.controller = controller;
		cancel.addActionListener(controller);
		save.addActionListener(controller);
		addUser.addActionListener(controller);
		addReq.addActionListener(controller);
		submitComment.addActionListener(controller);
		delete.addActionListener(controller);
	}

	/**
	 * Adds the action listener (controller) to this view
	 * 
	 * @param controller
	 *            the controller to be attached to this view
	 */
	public void setFieldController(TaskInputController controller) {
		titleField.addKeyListener(controller);
		descripArea.addKeyListener(controller);
		estEffortField.addKeyListener(controller);
		actEffortField.addKeyListener(controller);
		stages.addPopupMenuListener(controller);
	}

	/**
	 * gets the save button object
	 * 
	 * @return the save button object
	 */
	public JButton getSaveButton() {
		return this.save;
	}

	/**
	 * gets the delete button object
	 * 
	 * @return the delete button object
	 */
	public JButton getDeleteButton() {
		return this.delete;
	}

	/**
	 * Gets the text in the title field
	 * 
	 * @return the title field
	 */
	public JTextField getTitle() {
		return titleField;
	}

	/**
	 * Gets the description field
	 * 
	 * @return the description field
	 */
	public JTextArea getDescription() {
		return descripArea;
	}

	/**
	 * Gets the date field
	 * 
	 * @return the date field
	 */
	public JXDatePicker getDateField() {
		return dateField;
	}

	/**
	 * Gets the estimated effort field
	 * 
	 * @return the estimated effort field
	 */
	public JTextField getEstEffort() {
		return estEffortField;
	}

	/**
	 * Gets the actual effort field
	 * 
	 * @return the actual effort field
	 */
	public JTextField getActEffort() {
		return actEffortField;
	}

	/**
	 * gets the dropdown box in the view that contains all the stage names
	 * 
	 * @return the stages dropdown box
	 */
	public JComboBox<String> getStages() {
		return stages;
	}

	public JComboBox<String> getRequirements() {
		return requirements;
	}

	/**
	 * return the JList containing the assigned user names
	 * 
	 * @return the JList of assigned usernames
	 */
	public ScrollList getUsersList() {
		return this.usersList;
	}

	/**
	 * return the JList containing the project user names
	 * 
	 * @return the JLst of project user names
	 */
	public ScrollList getProjectUsersList() {
		return this.projectUsersList;
	}

	/**
	 * sets the text in the title field
	 * 
	 * @param d
	 *            the text in the title field
	 */
	public void setTitle(String d) {
		titleField.setText(d);
	}

	/**
	 * sets the text in the description field
	 * 
	 * @param d
	 *            the text in the description field
	 */
	public void setDescription(String d) {
		descripArea.setText(d);
	}

	/**
	 * Sets the text in the date field
	 * 
	 * @param d
	 *            the text in the date field
	 */
	public void setDate(Date d) {
		dateField.setDate(d);
	}

	/**
	 * Sets the estimated effort to the value i
	 * 
	 * @param i
	 *            the value to set the estimated effort field to
	 */
	public void setEstEffort(Integer i) {
		estEffortField.setText(i.toString());
	}

	/**
	 * Set the the actual effort field to the value of i
	 * 
	 * @param i
	 *            the value to set the actual effort field to
	 */
	public void setActEffort(Integer i) {
		actEffortField.setText(i.toString());
	}

	/**
	 * set stage dropdown box to the stage associated with the task
	 * 
	 * @param n
	 *            the index of the stage in the workflow
	 */
	public void setStageDropdown(int n) {
		String p = stages.getItemAt(n);
		stages.setSelectedItem(p);
	}

	/**
	 * 
	 * Returns the selected stage name. If the selected item cannot be retrieved
	 * returns an empty string.
	 *
	 * @return the selected stage as a String.
	 */
	public String getSelectedStage() {
		if (stages.getSelectedItem() != null) {
			return stages.getSelectedItem().toString();
		}
		return "";
	}

	/**
	 * 
	 * Sets the title error visible or invisible
	 * 
	 * @param v
	 *            true will make the title error visible, false will make the
	 *            title error invisible
	 */
	public void setTitleErrorVisible(boolean v) {
		titleError.setVisible(v);
	}

	/**
	 * Sets the description error visible or invisible
	 * 
	 * @param v
	 *            true will make the description error visible, false will make
	 *            the description error invisible
	 */
	public void setDescriptionErrorVisible(boolean v) {
		descriptionError.setVisible(v);
	}

	/**
	 * Sets the estimated effort error visible or invisible
	 * 
	 * @param v
	 *            true will make the estimated effort error visible, false will
	 *            make the estimated effort error invisible
	 */
	public void setEstEffortErrorVisible(boolean v) {
		estimatedEffortError.setVisible(v);
	}

	/**
	 * Sets the estimated effort error text
	 * 
	 * @param text
	 *            the text to set the error
	 */
	public void setEstEffortErrorText(String text) {
		estimatedEffortError.setText(text);
	}

	/**
	 * Sets the actual effort error visible or invisible
	 * 
	 * @param v
	 *            true will make the actual effort error visible, false will
	 *            make the actual effort error invisible
	 */
	public void setActualEffortErrorVisible(boolean v) {
		actualEffortError.setVisible(v);
	}

	/**
	 * Sets the actual effort error text
	 * 
	 * @param text
	 *            the text to set the error
	 */
	public void setActualEffortErrorText(String text) {
		actualEffortError.setText(text);
	}

	/**
	 * Sets the stage selector enabled or disabled
	 * 
	 * @param v
	 *            true will make the stage selector enabled, false will make the
	 *            stage selector disabled
	 */
	public void setStageSelectorEnabled(boolean v) {
		stages.setEnabled(v);
	}

	/**
	 * disables the delete button
	 */
	public void disableDelete() {
		this.delete.setEnabled(false);
	}

	/**
	 * enables the delete button
	 */
	public void enableDelete() {
		this.delete.setEnabled(true);
	}

	/**
	 * makes all of the text fields blank
	 */
	public void resetFields() {

		titleField.setText("");
		descripArea.setText("");
		estEffortField.setText("");
		actEffortField.setText("");
		dateField.setDate(Calendar.getInstance().getTime());
	}

	/**
	 * enables the ability to click the save button
	 */
	public void enableSave() {
		this.save.setEnabled(true);
	}

	/**
	 * disables the ability to click the save button
	 */
	public void disableSave() {
		this.save.setEnabled(false);
	}

	/*
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible && titleField.getKeyListeners().length > 0) {
			TaskInputController tic = (TaskInputController) titleField
					.getKeyListeners()[0];
			tic.checkFields();
		}
		if (visible && controller != null) {
			controller.reloadData();
		}
		super.setVisible(visible);
	}

	// Used for tests
	public JPanel getWindow() {
		return this.window;
	}
}
