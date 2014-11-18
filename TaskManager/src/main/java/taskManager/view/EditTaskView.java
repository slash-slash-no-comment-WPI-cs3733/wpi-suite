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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.JXDatePicker;

import taskManager.controller.EditTaskController;

/**
 *  Edit panel for a new task
 */

/**
 * @author Tyler Jaskoviak
 *
 */

public class EditTaskView extends JPanel {

	public static final String STAGES = "stages";
	public static final String CANCEL = "cancel";
	public static final String SAVE = "save";
	public static final String ADD_REQ = "addReq";
	public static final String SUBMIT_COMMENT = "submitComment";
	public static final String ADD_USER = "addUser";
	public static final String DELETE = "delete";
	public static final String COMMENTS = "comments";
	public static final String ACT_EFFORT = "act_effort";
	public static final String EST_EFFORT = "est_effort";
	public static final String DUE_DATE = "due_date";

	private static final long serialVersionUID = 1L;
	private JButton save;
	private JButton cancel;
	private JButton addUser;
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

	private JComboBox<String> stages;

	public EditTaskController controller;

	public EditTaskView(Mode mode) {
		// TODO: User Mode to switch between create and edit views
		// When Task added make EditTask take in a Task called currTask
		this.mode = mode;

		window = new JPanel();
		this.setLayout(new FlowLayout());

		Dimension nt_panelSize = getPreferredSize();
		nt_panelSize.width = 675; // TODO
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
		JLabel nt_requirementLabel = new JLabel("Requirements ");

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
		JList<String> nt_usersList = new JList<String>();
		nt_usersList.setVisibleRowCount(3);
		nt_usersList.setFixedCellWidth(this.getWidth() * 2 / 5);
		// usersField placed within scrollPan to maintain size
		JScrollPane nt_usersScrollPane = new JScrollPane(nt_usersList);
		nt_usersScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// TODO
		// Comment Pane

		// TODO
		// Requirement Pane

		// JButtons
		// Delete Task and close the window
		JButton nt_deleteBtn = new JButton("Delete");
		delete = nt_deleteBtn;
		delete.setName(DELETE);
		// Add user to list
		JButton nt_addUsersBtn = new JButton("Add Users");
		addUser = nt_addUsersBtn;
		addUser.setName(ADD_USER);
		// Add comment to comments
		JButton nt_submitCommentBtn = new JButton("Submit Comment");
		submitComment = nt_submitCommentBtn;
		submitComment.setName(SUBMIT_COMMENT);
		// add requirement
		JButton nt_addRequirementBtn = new JButton("Add Requirement");
		addReq = nt_addRequirementBtn;
		addReq.setName(ADD_REQ);
		// saves all the data and closes the window
		JButton nt_saveBtn = new JButton("Save");
		save = nt_saveBtn;
		save.setName(SAVE);
		// closes the window without saving
		JButton nt_cancelBtn = new JButton("Cancel");
		cancel = nt_cancelBtn;
		cancel.setName(CANCEL);

		// Combo Box for Stage
		JComboBox<String> nt_stagesBoxes = new JComboBox<String>();
		nt_stagesBoxes.setName(STAGES);
		stages = nt_stagesBoxes;

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
		newTaskGridBag.weightx = 0.15;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 1;

		newTaskGridBag.gridy = 0;
		window.add(nt_titleField, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		window.add(nt_descriptionScrollPane, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		window.add(nt_dueDateField, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		window.add(nt_stagesBoxes, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		window.add(nt_usersScrollPane, newTaskGridBag);

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

		// TODO
		// List of Requirements
		// newTaskGridBag.gridy = 9;

		// Third Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = .5;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 2;

		newTaskGridBag.gridy = 4;
		window.add(nt_addUsersBtn, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		window.add(nt_submitCommentBtn, newTaskGridBag);

		newTaskGridBag.gridy = 9;
		window.add(nt_addRequirementBtn, newTaskGridBag);

		JPanel bottomBtns = new JPanel();
		bottomBtns.add(nt_saveBtn);
		bottomBtns.add(nt_cancelBtn);
		if (this.mode == Mode.EDIT) {
			bottomBtns.add(nt_deleteBtn);
		}
		newTaskGridBag.gridy = 11;
		window.add(bottomBtns, newTaskGridBag);

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
	 * Gets the text in the title field
	 * 
	 * @return the text in the title field
	 */
	public JTextField getTitle() {
		return titleField;
	}

	/**
	 * Gets the text in the description field
	 * 
	 * @return the text in the description field
	 */
	public JTextArea getDescription() {
		return descripArea;
	}

	/**
	 * Gets the text in the date field
	 * 
	 * @return the text in the date field
	 */
	public JXDatePicker getDateField() {
		return dateField;
	}

	/**
	 * Gets the number in the estimated effort field
	 * 
	 * @return the number in the estimated effort field
	 */
	public JTextField getEstEffort() {
		return estEffortField;
	}

	/**
	 * Gets the number in the actual effort field
	 * 
	 * @return the number in the actual effort field
	 */
	public JTextField getActEffort() {
		return actEffortField;
	}

	public JComboBox<String> getStages() {
		return stages;
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
	public void setStageDropdown(String s) {
		stages.setSelectedItem(s);
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

	/*
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
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
