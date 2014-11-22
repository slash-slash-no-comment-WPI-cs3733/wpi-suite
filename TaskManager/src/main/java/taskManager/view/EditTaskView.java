/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	public static final String REMOVE_USER = "removeUser";
	public static final String DELETE = "delete";
	public static final String COMMENTS = "comments";
	public static final String ACT_EFFORT = "act_effort";
	public static final String EST_EFFORT = "est_effort";
	public static final String DUE_DATE = "due_date";
	public static final String NO_REQ = "[None]";
	public static final String REFRESH = "refresh";
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
	private JButton refreshActivities;

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

	private EditTaskController controller;
	private ActivityView activityPane;

	private List<ActivityModel> activities;
	private List<ActivityModel> newActivities;

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

		titleError = new JLabel("This a required field");
		titleError.setVisible(false);
		titleError.setForeground(Color.RED);
		descriptionError = new JLabel("This is a required field");
		descriptionError.setVisible(false);
		descriptionError.setForeground(Color.RED);
		estimatedEffortError = new JLabel("This is a required field");
		estimatedEffortError.setVisible(false);
		estimatedEffortError.setForeground(Color.RED);
		actualEffortError = new JLabel("");
		actualEffortError.setVisible(false);
		actualEffortError.setForeground(Color.RED);

		// JTextFields
		// sets all text fields editable and adds them to global variables
		titleField = new JTextField(25);
		titleField.setEditable(true);
		descripArea = new JTextArea(2, 25);
		descripArea.setEditable(true);
		descripArea.setLineWrap(true);
		JScrollPane nt_descriptionScrollPane = new JScrollPane(descripArea);
		nt_descriptionScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		nt_descriptionScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		estEffortField = new JTextField(10);
		estEffortField.setEditable(true);
		estEffortField.setName(EST_EFFORT);
		actEffortField = new JTextField(10);
		actEffortField.setEditable(true);
		actEffortField.setName(ACT_EFFORT);
		commentsField = new JTextField(25);
		commentsField.setEditable(true);
		commentsField.setName(COMMENTS);

		// adds calendar
		dateField = new JXDatePicker();
		dateField.setName("due_date");
		dateField.setDate(Calendar.getInstance().getTime());

		// JTextArea
		// TODO
		// Get to add users
		usersList = new ScrollList("Assigned Users");
		projectUsersList = new ScrollList("Project Users");

		// Comment Pane
		activityPane = new ActivityView();

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
		JButton nt_refreshBtn = new JButton("Refresh");
		refreshActivities = nt_refreshBtn;
		refreshActivities.setName(REFRESH);

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
		window.add(titleField, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		window.add(nt_descriptionScrollPane, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		window.add(dateField, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		window.add(stages, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		window.add(usersList, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 5;
		window.add(estEffortField, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		window.add(actEffortField, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		window.add(commentsField, newTaskGridBag);

		newTaskGridBag.gridy = 8;
		window.add(activityPane, newTaskGridBag);

		// List of Requirements
		newTaskGridBag.gridy = 9;
		window.add(requirements, newTaskGridBag);

		// Third Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = .5;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 2;

		newTaskGridBag.gridy = 0;
		window.add(titleError, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		window.add(descriptionError, newTaskGridBag);

		JPanel userButtons = new JPanel();
		userButtons.setLayout(new BoxLayout(userButtons, BoxLayout.Y_AXIS));
		userButtons.add(addUser);
		userButtons.add(removeUser);
		newTaskGridBag.gridy = 4;
		window.add(userButtons, newTaskGridBag);

		newTaskGridBag.gridy = 5;
		window.add(estimatedEffortError, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		window.add(actualEffortError, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		window.add(submitComment, newTaskGridBag);

		newTaskGridBag.gridy = 8;
		window.add(nt_refreshBtn, newTaskGridBag);

		newTaskGridBag.gridy = 9;
		window.add(addReq, newTaskGridBag);

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
		removeUser.addActionListener(controller);
		addReq.addActionListener(controller);
		submitComment.addActionListener(controller);
		delete.addActionListener(controller);
		refreshActivities.addActionListener(controller);
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
	 * 
	 * Sets the refreshActivities to enabled/disabled.
	 *
	 * @param boolean for whether or not to enable.
	 */
	public void setRefreshEnabled(Boolean b) {
		refreshActivities.setEnabled(b);
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
		usersList.removeAllValues();
		projectUsersList.removeAllValues();
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
		reloadActivitiesPanel();
	}

	/**
	 * 
	 * Sets the activies panel according to the activities list.
	 *
	 * @param activities
	 */
	public void setActivitiesPanel(List<ActivityModel> activities) {
		List<ActivityModel> tskActivitiesCopy = new ArrayList<ActivityModel>(
				activities);
		activityPane.setMessage("");
		for (ActivityModel act : tskActivitiesCopy) {
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
		List<ActivityModel> tskActivitiesCopy = new ArrayList<ActivityModel>(
				act);
		activityPane.setMessage("");
		activities = tskActivitiesCopy;
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

	/**
	 * 
	 * Clears the activities.
	 *
	 */
	public void clearActivities() {
		activities.clear();
		newActivities.clear();
	}

	/**
	 * 
	 * Adds an activity.
	 *
	 * @param the
	 *            activity.
	 */
	public void addActivity(ActivityModel act) {
		activities.add(act);
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

	// Used for tests
	public JPanel getWindow() {
		return this.window;
	}

	/**
	 * 
	 * Returns the EditTaskController.
	 *
	 * @return the controller.
	 */
	public EditTaskController getController() {
		return controller;
	}
}
