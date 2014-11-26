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
import javax.swing.ImageIcon;
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
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;
import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.activityModelType;

/**
 *  Edit panel for a new task
 */

/**
 * @author Tyler Jaskoviak
 *
 */

public class EditTaskView extends JPanel implements LocaleChangeListener {

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
	private JButton save = new JButton();
	private JButton cancel = new JButton();
	private JButton addUser = new JButton();
	private JButton removeUser = new JButton();
	private JButton delete = new JButton();
	private JButton addReq = new JButton();
	private JButton submitComment = new JButton();
	private JButton refreshActivities = new JButton();

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

	private ScrollList usersList = new ScrollList(null);
	private ScrollList projectUsersList = new ScrollList(null);

	private JLabel titleError = new JLabel();
	private JLabel descriptionError = new JLabel();
	private JLabel estimatedEffortError = new JLabel();
	private JLabel actualEffortError = new JLabel();

	private JComboBox<String> stages;
	private JComboBox<String> requirements;

	private EditTaskController controller;
	private ActivityView activityPane;

	private List<ActivityModel> activities;
	private List<ActivityModel> newActivities;

	private JLabel titleLabel = new JLabel();
	private JLabel descriptionLabel = new JLabel();
	private JLabel dueDateLabel = new JLabel();
	private JLabel stageLabel = new JLabel();
	private JLabel usersLabel = new JLabel();
	private JLabel estimatedEffortLabel = new JLabel();
	private JLabel actualEffortLabel = new JLabel();
	private JLabel commentsLabel = new JLabel();
	private JLabel requirementLabel = new JLabel();

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

		titleError.setVisible(false);
		titleError.setForeground(Color.RED);
		descriptionError.setVisible(false);
		descriptionError.setForeground(Color.RED);
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

		// Icon is from:
		// http://www.iconarchive.com/show/oxygen-icons-by-oxygen-icons.org/Actions-view-calendar-day-icon.html
		// Snippet is from:
		// http://stackoverflow.com/questions/8406200/swingx-personalize-jxdatepicker
		((JButton) dateField.getComponent(1)).setIcon(new ImageIcon(
				((new ImageIcon(getClass().getResource("calendar-icon.png")))
						.getImage()).getScaledInstance(20, 20,
						java.awt.Image.SCALE_SMOOTH)));

		// Comment Pane
		activityPane = new ActivityView();

		// Requirement Pane
		requirements = new JComboBox<String>();
		requirements.setName(REQUIREMENTS);

		// JButtons
		// Delete Task and close the window
		delete.setName(DELETE);
		// Add user to list
		addUser.setName(ADD_USER);
		this.setAddUserEnabled(false);
		// remove user from list
		removeUser.setName(REMOVE_USER);
		this.setRemoveUserEnabled(false);

		// Add comment to comments
		submitComment.setName(SUBMIT_COMMENT);
		// add requirement
		addReq.setName(VIEW_REQ);
		// saves all the data and closes the window
		save.setName(SAVE);
		this.setSaveEnabled(false);
		// closes the window without saving
		cancel.setName(CANCEL);
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
		window.add(titleLabel, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		window.add(descriptionLabel, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		window.add(dueDateLabel, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		window.add(stageLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		window.add(usersLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 5;
		window.add(estimatedEffortLabel, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		window.add(actualEffortLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.10;
		newTaskGridBag.gridy = 7;
		window.add(commentsLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 9;
		window.add(requirementLabel, newTaskGridBag);

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
		window.add(refreshActivities, newTaskGridBag);

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

		Localizer.addListener(this);
		onLocaleChange();
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
		usersList.setController(controller);
		projectUsersList.setController(controller);
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
	 * set the add user button enabled or disabled
	 * 
	 * @param e
	 */
	public void setAddUserEnabled(boolean e) {
		this.addUser.setEnabled(e);
	}

	/**
	 * sets the remove user button enabled or disabled
	 * 
	 * @param e
	 */
	public void setRemoveUserEnabled(boolean e) {
		this.removeUser.setEnabled(e);
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
	 * enables or disables the save button
	 * 
	 * @param e
	 *            true is enabled, false is disabled
	 */
	public void setSaveEnabled(boolean e) {
		this.save.setEnabled(e);
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

	@Override
	public void onLocaleChange() {
		titleLabel.setText(Localizer.getString("Title "));
		descriptionLabel.setText(Localizer.getString("Description "));
		dueDateLabel.setText(Localizer.getString("Due Date "));
		stageLabel.setText(Localizer.getString("Stage "));
		usersLabel.setText(Localizer.getString("Users "));
		estimatedEffortLabel.setText(Localizer.getString("Estimated Effort "));
		actualEffortLabel.setText(Localizer.getString("Actual Effort "));
		commentsLabel.setText(Localizer.getString("Comments "));
		requirementLabel.setText(Localizer.getString("Requirement "));
		titleError.setText(Localizer.getString("This is a required field"));
		descriptionError.setText(Localizer
				.getString("This is a required field"));
		estimatedEffortError.setText(Localizer
				.getString("This is a required field"));
		delete.setText(Localizer.getString("Delete"));
		addUser.setText(Localizer.getString("Add User"));
		removeUser.setText(Localizer.getString("Remove User"));
		submitComment.setText(Localizer.getString("Submit Comment"));
		addReq.setText(Localizer.getString("View Requirement"));
		save.setText(Localizer.getString("Save"));
		cancel.setText(Localizer.getString("Cancel"));
		refreshActivities.setText(Localizer.getString("Refresh"));
		usersList.setTitle(Localizer.getString("Assigned Users"));
		projectUsersList.setTitle(Localizer.getString("Project Users"));
	}
}
