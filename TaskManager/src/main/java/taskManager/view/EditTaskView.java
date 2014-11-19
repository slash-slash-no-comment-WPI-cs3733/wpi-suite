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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
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
import taskManager.controller.TaskInputController;
import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.activityModelType;

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
	private JButton delete;
	private JButton addReq;
	private JButton submitComment;

	private JTextField titleField;
	private JTextArea descripArea;
	private JXDatePicker dateField;
	private JTextField estEffortField;
	private JTextField actEffortField;
	private JTextField commentsField;

	private JLabel titleError;
	private JLabel descriptionError;
	private JLabel estimatedEffortError;
	private JLabel actualEffortError;

	private JComboBox<String> stages;
	private JComboBox<String> requirements;

	private EditTaskController controller;
	private ActivityView activityPane;

	private List<ActivityModel> activities;
	private List<ActivityModel> newActivities;

	/**
	 * Creates a Edit Task Panel so that you can change all of the values of a
	 * task: Title Description Due Date Estimated Effort Actual Effort Adding
	 * Comments
	 */
	public EditTaskView() {

		Dimension nt_panelSize = getPreferredSize();
		nt_panelSize.width = 625; // TODO
		nt_panelSize.height = 500; // Decide size
		setPreferredSize(nt_panelSize);

		setBorder(BorderFactory.createTitledBorder("Edit Task"));

		activities = new ArrayList<ActivityModel>();
		newActivities = new ArrayList<ActivityModel>();

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
		commentsField = nt_commentsField;

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

		// Comment Pane
		activityPane = new ActivityView();

		// Requirement Pane
		JComboBox<String> nt_requirementBoxes = new JComboBox<String>();
		requirements = nt_requirementBoxes;
		requirements.setName(REQUIREMENTS);

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
		JButton nt_addRequirementBtn = new JButton("View Requirement");
		addReq = nt_addRequirementBtn;
		addReq.setName(VIEW_REQ);
		// saves all the data and closes the window
		JButton nt_saveBtn = new JButton("Save");
		save = nt_saveBtn;
		save.setName(SAVE);
		this.disableSave();
		// closes the window without saving
		JButton nt_cancelBtn = new JButton("Cancel");
		cancel = nt_cancelBtn;
		cancel.setName(CANCEL);

		// Combo Box for Stage
		JComboBox<String> nt_stagesBoxes = new JComboBox<String>();

		nt_stagesBoxes.setName(STAGES);
		stages = nt_stagesBoxes;

		setLayout(new GridBagLayout());

		GridBagConstraints newTaskGridBag = new GridBagConstraints();

		// First Column ////

		newTaskGridBag.anchor = GridBagConstraints.FIRST_LINE_START;

		newTaskGridBag.weightx = 0.15;
		newTaskGridBag.weighty = 0.077;

		newTaskGridBag.gridx = 0;

		newTaskGridBag.gridy = 0;
		add(nt_titleLabel, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		add(nt_descriptionLabel, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		add(nt_dueDateLabel, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		add(nt_stageLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		add(nt_usersLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 5;
		add(nt_estimatedEffortLabel, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		add(nt_actualEffortLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.10;
		newTaskGridBag.gridy = 7;
		add(nt_commentsLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 9;
		add(nt_requirementLabel, newTaskGridBag);

		// Second Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = 0.3;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 1;

		newTaskGridBag.gridy = 0;
		add(nt_titleField, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		add(nt_descriptionScrollPane, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		add(nt_dueDateField, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		add(nt_stagesBoxes, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		add(nt_usersScrollPane, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 5;
		add(nt_estimatedEffortField, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		add(nt_actualEffortField, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		add(nt_commentsField, newTaskGridBag);

		newTaskGridBag.gridy = 8;
		add(activityPane, newTaskGridBag);

		// List of Requirements
		newTaskGridBag.gridy = 9;
		add(nt_requirementBoxes, newTaskGridBag);

		// Third Column ////

		newTaskGridBag.anchor = GridBagConstraints.CENTER;
		newTaskGridBag.weightx = 0.18;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 2;

		newTaskGridBag.gridy = 0;
		add(nt_titleLabel_error, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		add(nt_descriptionLabel_error, newTaskGridBag);

		newTaskGridBag.gridy = 4;
		add(nt_addUsersBtn, newTaskGridBag);

		newTaskGridBag.gridy = 5;
		add(nt_estimatedEffortLabel_error, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		add(nt_actualEffortLabel_error, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		add(nt_submitCommentBtn, newTaskGridBag);

		newTaskGridBag.gridy = 9;
		add(nt_addRequirementBtn, newTaskGridBag);

		newTaskGridBag.gridy = 10;
		add(nt_saveBtn, newTaskGridBag);

		// Fourth Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = 0.15;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 3;

		newTaskGridBag.gridy = 0;
		add(nt_deleteBtn, newTaskGridBag);

		newTaskGridBag.gridy = 10;
		add(nt_cancelBtn, newTaskGridBag);
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
		activityPane.setMessage("");
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

	/**
	 * 
	 * Adds comment to the activities list and refreshes the activities panel.
	 *
	 */
	public void addComment() {
		ActivityModel act = new ActivityModel(commentsField.getText(),
				activityModelType.COMMENT);
		activities.add(act);
		newActivities.add(act);
		commentsField.setText("");
		setActivitiesPanel(activities);
	}

	/**
	 * 
	 * Sets the activies panel according to the activities list.
	 *
	 * @param activities
	 */
	public void setActivitiesPanel(List<ActivityModel> activities) {
		activityPane.setMessage("");
		for (ActivityModel act : activities) {
			String current = activityPane.getMessage().getText();
			switch (act.getType()) {
			case CREATION:
				activityPane.setMessage(current + act.getDescription() + "\n");
				break;
			case MOVE:
				activityPane.setMessage(current + act.getDescription() + "\n");
				break;
			case COMPLETION:
				activityPane.setMessage(current + act.getDescription() + "\n");
				break;
			case USER_ADD:
				activityPane.setMessage(current + act.getDescription() + "\n");
				break;
			case USER_REMOVE:
				activityPane.setMessage(current + act.getDescription() + "\n");
				break;
			case COMMENT:
				activityPane.setMessage(current + "User: "
						+ act.getDescription() + "\n");
				break;
			}
		}
	}

	/**
	 * 
	 * Reloads the activities panel.
	 *
	 */
	public void reloadActivitiesPanel() {
		setActivitiesPanel(activities);
	}

	/**
	 * 
	 * Sets activities.
	 *
	 * @param act
	 */
	public void setActivities(List<ActivityModel> act) {
		activityPane.setMessage("");
		activities = act;
	}

	/**
	 * 
	 * Returns the new activities.
	 *
	 * @return
	 */
	public List<ActivityModel> getNewActivities() {
		return newActivities;
	}

	public void clearActivities() {
		activities.clear();
		newActivities.clear();
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
			reloadActivitiesPanel();
		}
		if (visible && controller != null) {
			controller.reloadData();
		}
		super.setVisible(visible);
	}
}
