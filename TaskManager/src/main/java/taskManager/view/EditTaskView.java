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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import taskManager.controller.EditTaskController;
import taskManager.controller.TaskInputController;
import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.activityModelType;

/**
 *  Edit panel for a new task
 */

/**
 * 
 * @author Thane Hunt
 * @author Tyler Jaskoviak
 */

public class EditTaskView extends JPanel {

	public static final String STAGES = "stages";
	public static final String REQUIREMENTS = "requirements";
	public static final String CANCEL = "cancel";
	public static final String ARCHIVE = "archive";
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
	private JCheckBox archive;

	private final JTextArea commentsField;
	private final JTextField titleField;
	private final JTextArea descripArea;
	private final JXDatePicker dateField;
	private final JTextField estEffortField;
	private final JTextField actEffortField;
	private final JPanel window;

	private final Mode mode;

	public enum Mode {
		CREATE, EDIT;
	}

	private final ScrollList usersList;
	private final ScrollList projectUsersList;

	private final JLabel titleError;
	private final JLabel descriptionError;
	private final JLabel estimatedEffortError;
	private final JLabel actualEffortError;

	private final JComboBox<String> stages;
	private final JComboBox<String> requirements;

	private EditTaskController controller;
	private final ActivityView activityPane;

	private List<ActivityModel> activities;

	private TaskInputController fieldC;

	/**
	 * Creates a Edit Task Panel so that you can change all of the values of a
	 * task: Title Description Due Date Estimated Effort Actual Effort Adding
	 * Comments
	 * 
	 * @param mode
	 *            Which mode this view should be created in
	 */
	public EditTaskView(Mode mode) {
		// TODO: User Mode to switch between create and edit views
		// When Task added make EditTask take in a Task called currTask
		this.mode = mode;

		window = new JPanel(new MigLayout());

		this.setLayout(new FlowLayout());
		final Dimension panelSize = getPreferredSize();
		panelSize.width = 1100; // TODO
		panelSize.height = 500; // Decide size
		window.setPreferredSize(panelSize);
		this.setPreferredSize(panelSize);
		this.setMinimumSize(panelSize);

		activities = new ArrayList<ActivityModel>();

		// JLabels
		final JLabel titleLabel = new JLabel("Title ");
		final JLabel descriptionLabel = new JLabel("Description ");
		final JLabel dueDateLabel = new JLabel("Due Date ");
		final JLabel stageLabel = new JLabel("Stage ");
		final JLabel estimatedEffortLabel = new JLabel("Estimated Effort ");
		final JLabel actualEffortLabel = new JLabel("Actual Effort ");
		final JLabel requirementLabel = new JLabel("Select Requirement ");

		titleError = new JLabel("Cannot be empty");
		titleError.setVisible(false);
		titleError.setForeground(Color.RED);
		descriptionError = new JLabel("Cannot be empty");
		descriptionError.setVisible(false);
		descriptionError.setForeground(Color.RED);
		estimatedEffortError = new JLabel("*");
		estimatedEffortError.setVisible(false);
		estimatedEffortError.setForeground(Color.RED);
		actualEffortError = new JLabel("*");
		actualEffortError.setVisible(false);
		actualEffortError.setForeground(Color.RED);

		// JTextFields
		// sets all text fields editable and adds them to global variables

		titleField = new JTextField(25);
		titleField.setEditable(true);
		descripArea = new JTextArea(4, 25);
		descripArea.setEditable(true);
		descripArea.setLineWrap(true);
		descripArea.setWrapStyleWord(true);
		final JScrollPane descriptionScrollPane = new JScrollPane(descripArea);
		descriptionScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		commentsField = new JTextArea(2, 22);
		final JScrollPane commentScrollPane = new JScrollPane(commentsField);
		commentScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		commentScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		estEffortField = new JTextField(4);
		estEffortField.setEditable(true);
		estEffortField.setName(EST_EFFORT);
		actEffortField = new JTextField(4);
		actEffortField.setEditable(true);
		actEffortField.setName(ACT_EFFORT);
		commentsField.setEditable(true);
		commentsField.setLineWrap(true);
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

		// JTextArea
		// TODO
		// Get to add users
		usersList = new ScrollList("Assigned Users");
		usersList.setBackground(this.getBackground());
		projectUsersList = new ScrollList("Project Users");
		projectUsersList.setBackground(this.getBackground());

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
		addUser = new JButton(">>");
		addUser.setName(ADD_USER);
		this.setAddUserEnabled(false);
		// remove user from list

		removeUser = new JButton("<<");
		removeUser.setName(REMOVE_USER);
		this.setRemoveUserEnabled(false);

		// Add comment to comments
		submitComment = new JButton("Submit Comment");
		submitComment.setName(SUBMIT_COMMENT);
		this.setCommentSubmitEnabled(false);
		// add requirement
		addReq = new JButton("View Requirement");
		addReq.setName(VIEW_REQ);
		// saves all the data and closes the window
		save = new JButton("Save");
		save.setName(SAVE);
		this.setSaveEnabled(false);
		// closes the window without saving
		cancel = new JButton("Cancel");
		cancel.setName(CANCEL);
		archive = new JCheckBox("Archived");
		archive.setName(ARCHIVE);

		// Combo Box for Stage
		stages = new JComboBox<String>();
		stages.setName(STAGES);

		window.setLayout(new MigLayout());

		window.add(titleLabel);

		// This is where the 6 primary panels are defined
		final JPanel Spacer = new JPanel(new MigLayout());
		final JPanel BasicInfo = new JPanel(new MigLayout());
		final JPanel Users = new JPanel(new MigLayout());
		final JPanel Activities = new JPanel(new MigLayout());
		final JPanel Effort = new JPanel(new MigLayout("fill"));
		final JPanel Requirements = new JPanel(new MigLayout());
		final JPanel EditSaveCancel = new JPanel(new MigLayout());

		// ready to go
		// BasicInfo Panel internal content
		BasicInfo.setBorder(BorderFactory.createTitledBorder("Basic Info"));
		BasicInfo.add(titleLabel, "wrap");
		BasicInfo.add(titleField);
		BasicInfo.add(titleError, "wrap");
		BasicInfo.add(descriptionLabel, "wrap");
		BasicInfo.add(descriptionScrollPane, "gapbottom 20px");
		BasicInfo.add(descriptionError, "wrap");
		BasicInfo.add(dueDateLabel);
		BasicInfo.add(stageLabel, "wrap");
		BasicInfo.add(dateField);
		BasicInfo.add(stages);

		// Users Panel internal content
		Users.setBorder(BorderFactory.createTitledBorder("Users"));
		final JPanel usersListPanel = new JPanel(new MigLayout());
		final JPanel projectUsersListPanel = new JPanel(new MigLayout());
		final JPanel addRemoveButtons = new JPanel(new MigLayout());
		usersListPanel.add(usersList);
		projectUsersListPanel.add(projectUsersList);

		addRemoveButtons.add(addUser, "wrap");
		addRemoveButtons.add(removeUser);

		Users.add(projectUsersListPanel, "w 100!, gapleft 15px");
		Users.add(addRemoveButtons);
		Users.add(usersListPanel, "w 100!");

		// Activities Panel internal content
		Activities.setBorder(BorderFactory.createTitledBorder("Activities"));
		Activities.add(activityPane, "wrap, gapbottom 20px");
		Activities.add(commentScrollPane, "center, wrap, gapbottom 10px");
		Activities
				.add(submitComment, "dock south, gapleft 30px, gapright 30px");

		// Effort Panel internal content

		Effort.setBorder(BorderFactory.createTitledBorder("Effort"));
		Effort.add(estimatedEffortLabel);
		Effort.add(actualEffortLabel, "wrap");
		Effort.add(estEffortField);
		Effort.add(actEffortField, "wrap");
		final JPanel Errors = new JPanel(new MigLayout());
		Errors.add(estimatedEffortError, "wrap");
		Errors.add(actualEffortError);

		// Requirements Panel internal content
		Requirements
				.setBorder(BorderFactory.createTitledBorder("Requirements"));
		Requirements.add(requirementLabel, "wrap");
		Requirements.add(requirements);
		Requirements.add(addReq);

		// EditSaveCancel Panel internal content

		EditSaveCancel.add(save);
		EditSaveCancel.add(cancel);
		if (this.mode == Mode.EDIT) {
			EditSaveCancel.add(delete);
			EditSaveCancel.add(archive);
		}
		EditSaveCancel.add(Errors);

		// The finished panels are added to the main window panel

		window.add(Spacer, "dock north");
		window.add(BasicInfo, "w 30%, h 50%, gapbottom 20px");
		window.add(Users, "w 30%, h 50%, gapbottom 20px, wrap");
		window.add(Effort, "w 30%, h 20%");
		window.add(Requirements, "w 30%, h 20%");
		window.add(EditSaveCancel, "dock south, h 10%");
		window.add(Activities, "w 25%, dock east, gapleft 5px");
		this.add(window);
	}

	/**
	 * 
	 * Sets the focus to the title field.
	 *
	 */
	public void focusOnTitleField() {
		titleField.requestFocus();
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
		archive.addActionListener(controller);
		save.addActionListener(controller);
		addUser.addActionListener(controller);
		removeUser.addActionListener(controller);
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
		fieldC = controller;
		titleField.addKeyListener(fieldC);
		descripArea.addKeyListener(fieldC);
		estEffortField.addKeyListener(fieldC);
		actEffortField.addKeyListener(fieldC);
		stages.addPopupMenuListener(fieldC);
		usersList.setController(fieldC);
		projectUsersList.setController(fieldC);
		commentsField.addKeyListener(fieldC);
		requirements.addPopupMenuListener(fieldC);
		dateField.addPropertyChangeListener(fieldC);
		archive.addItemListener(fieldC);
	}

	/**
	 * returns the task input controller
	 * 
	 * @return the task input controller
	 */
	public TaskInputController getFieldController() {
		return fieldC;
	}

	/**
	 * 
	 * Sets the archive button's text
	 *
	 * @param text
	 *            The text to set it to
	 */
	public void setArchiveButtonText(String text) {
		archive.setText(text);
	}

	/**
	 * 
	 * Sets the archive checkbox to selected/unselected.
	 *
	 * @param selected
	 *            when true, sets the checkbox to selected
	 */
	public void checkArchive(Boolean selected) {
		archive.setSelected(selected);
	}

	/**
	 * 
	 * Returns the state of the archive checkbox.
	 *
	 * @return true if selected.
	 */
	public Boolean isArchived() {
		return archive.isSelected();
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
		return usersList;
	}

	/**
	 * return the JList containing the project user names
	 * 
	 * @return the JLst of project user names
	 */
	public ScrollList getProjectUsersList() {
		return projectUsersList;
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
		final String p = stages.getItemAt(n);
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
	 * disables the archive button
	 */
	public void disableArchive() {
		archive.setEnabled(false);
	}

	/**
	 * enables the archive button
	 */
	public void enableArchive() {
		archive.setEnabled(true);
	}

	/**
	 * set the add user button enabled or disabled
	 * 
	 * @param e
	 */
	public void setAddUserEnabled(boolean e) {
		addUser.setEnabled(e);
	}

	/**
	 * sets the remove user button enabled or disabled
	 * 
	 * @param e
	 */
	public void setRemoveUserEnabled(boolean e) {
		removeUser.setEnabled(e);
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
		save.setEnabled(e);
	}

	/**
	 * enables or disables the comment submit button
	 * 
	 * @param e
	 *            true is enabled false is disabled
	 */
	public void setCommentSubmitEnabled(boolean e) {
		this.submitComment.setEnabled(e);
	}

	/**
	 * 
	 * Adds comment to the activities list and refreshes the activities panel.
	 *
	 * @return the resulting ActivityModel added.
	 */
	public ActivityModel addComment() {
		final ActivityModel act = new ActivityModel(commentsField.getText(),
				activityModelType.COMMENT);
		activities.add(act);
		commentsField.setText("");
		reloadActivitiesPanel();
		fieldC.validate();
		return act;
	}

	/**
	 * 
	 * Sets the activies panel according to the activities list.
	 *
	 * @param activities
	 */
	public void setActivitiesPanel(List<ActivityModel> activities) {
		final List<ActivityModel> tskActivitiesCopy = new ArrayList<ActivityModel>(
				activities);
		activityPane.setMessage("");
		DateFormat dateF = new SimpleDateFormat("MM/dd/yyyy kk:mm");
		for (ActivityModel act : tskActivitiesCopy) {
			String current = activityPane.getMessage().getText();
			activityPane.setMessage(current + act.getActor() + " ["
					+ dateF.format(act.getDateCreated()) + "]: "
					+ act.getDescription() + "\n");

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
		final List<ActivityModel> tskActivitiesCopy = new ArrayList<ActivityModel>(
				act);
		activityPane.setMessage("");
		activities = tskActivitiesCopy;
	}

	/**
	 * 
	 * Clears the activities.
	 *
	 */
	public void clearActivities() {
		activities.clear();
	}

	/**
	 * 
	 * Adds an activity.
	 *
	 * @param act
	 *            the activity.
	 */
	public void addActivity(ActivityModel act) {
		activities.add(act);
	}

	/**
	 * 
	 * Set the delete button to enabled/disabled.
	 *
	 * @param bool
	 *            boolean to set the button to.
	 */
	public void setDeleteEnabled(boolean bool) {
		delete.setEnabled(bool);
	}

	/*
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible && titleField.getKeyListeners().length > 0) {
			final TaskInputController tic = (TaskInputController) titleField
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
		return window;
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

	/**
	 * 
	 * Whether this editTaskView is creating a new task, or editing one.
	 *
	 * @return Mode.CREATE or Mode.EDIT
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * Returns the comments field's text
	 * 
	 * @return The text the user wants to say
	 */
	public String getCommentsFieldText() {
		return commentsField.getText();
	}
}
