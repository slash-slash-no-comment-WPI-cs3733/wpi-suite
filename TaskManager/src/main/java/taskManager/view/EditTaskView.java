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
import java.awt.Font;
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
import javax.swing.ScrollPaneLayout;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.BalloonTip.AttachLocation;
import net.java.balloontip.BalloonTip.Orientation;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
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

public class EditTaskView extends JScrollPane {

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
	public static final String TITLE = "title";
	public static final String DESCRIP = "description";

	private static final String TITLE_ERROR = "Title cannot be empty";
	private static final String DESCRIPTION_ERROR = "Description cannot be empty";
	private static final String EFFORT_ERROR = "Must be an integer between 0 and 9999";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton save;
	private JButton cancel;
	private JButton addUser;
	private JButton removeUser;
	private JButton delete;
	private JButton viewReq;
	private JButton submitComment;
	private JCheckBox archive;

	private final JTextArea commentsField;
	private final JTextField titleField;
	private final JTextArea descripArea;
	private final JXDatePicker dateField;
	private final JTextField estEffortField;
	private final JTextField actEffortField;
	private final JPanel window;

	private BalloonTip titleError;
	private BalloonTip descripError;
	private BalloonTip actEffortError;
	private BalloonTip estEffortError;

	private final Mode mode;

	public enum Mode {
		CREATE, EDIT;
	}

	private final ScrollList usersList;
	private final ScrollList projectUsersList;

	private final JComboBox<String> stages;
	private final JComboBox<String> requirements;

	private EditTaskController controller;
	private final ActivityView activityPane;

	private List<ActivityModel> activities;

	private TaskInputController fieldC;

	// create new Font
	Font bigFont = new Font("Default", Font.BOLD, 14);

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
		JPanel center = new JPanel();
		center.setLayout(new MigLayout("center"));
		window = new JPanel(new MigLayout());
		this.setViewportView(center);
		this.setLayout(new ScrollPaneLayout());
		final Dimension panelSize = getPreferredSize();
		panelSize.width = 1300; // TODO
		panelSize.height = 650; // Decide size
		center.setPreferredSize(panelSize);
		window.setPreferredSize(panelSize);

		activities = new ArrayList<ActivityModel>();

		// JLabels
		JLabel titleLabel = new JLabel("Title");
		titleLabel.setFont(bigFont);
		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setFont(bigFont);
		JLabel dueDateLabel = new JLabel("Due Date");
		dueDateLabel.setFont(bigFont);
		JLabel stageLabel = new JLabel("Stage");
		stageLabel.setFont(bigFont);
		JLabel estimatedEffortLabel = new JLabel("Estimated Effort");
		estimatedEffortLabel.setFont(bigFont);
		JLabel actualEffortLabel = new JLabel("Actual Effort");
		actualEffortLabel.setFont(bigFont);
		JLabel requirementLabel = new JLabel("Select Requirement");
		requirementLabel.setFont(bigFont);
		JLabel assignedUsersLabel = new JLabel("Assigned Users");
		assignedUsersLabel.setFont(bigFont);
		JLabel projectUsersLabel = new JLabel("Project Users");
		projectUsersLabel.setFont(bigFont);
		JLabel activitiesLabel = new JLabel("Activities");
		activitiesLabel.setFont(bigFont);
		JLabel commentsLabel = new JLabel("Comment");
		commentsLabel.setFont(bigFont);

		// JTextFields
		// sets all text fields editable and adds them to global variables
		titleField = new JTextField(26);
		titleField.setEditable(true);
		titleField.setName(TITLE);

		descripArea = new JTextArea(14, 26);
		descripArea.setName(DESCRIP);
		descripArea.setEditable(true);
		descripArea.setLineWrap(true);
		descripArea.setWrapStyleWord(true);
		final JScrollPane descriptionScrollPane = new JScrollPane(descripArea);

		descriptionScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		commentsField = new JTextArea(6, 24);
		JScrollPane commentScrollPane = new JScrollPane(commentsField);
		commentScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		commentScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

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
		dateField.setName(DUE_DATE);
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
		usersList = new ScrollList("");
		usersList.setBackground(this.getBackground());
		projectUsersList = new ScrollList("");
		projectUsersList.setBackground(this.getBackground());

		// Comment Pane
		activityPane = new ActivityView();
		// Requirement Pane
		requirements = new JComboBox<String>();
		requirements.setName(REQUIREMENTS);
		requirements.setPrototypeDisplayValue("Select a requirement");
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

		viewReq = new JButton("View Requirement");
		viewReq.setName(VIEW_REQ);

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

		// This is where the 9 primary panels are defined
		JPanel Spacer = new JPanel(new MigLayout());
		JPanel BasicInfo = new JPanel(new MigLayout());
		JPanel Users = new JPanel(new MigLayout());
		JPanel Activities = new JPanel(new MigLayout("fill"));
		JPanel Effort = new JPanel(new MigLayout());
		JPanel Requirements = new JPanel(new MigLayout());
		JPanel EditSaveCancel = new JPanel(new MigLayout("align center"));
		JPanel dateAndStage = new JPanel(new MigLayout());
		JPanel EffortDateStage = new JPanel(new MigLayout());

		// Effort Panel internal content
		Effort.add(estimatedEffortLabel, "wrap");
		Effort.add(estEffortField, "wrap");
		Effort.add(actualEffortLabel, "wrap, gaptop 10px");
		Effort.add(actEffortField);

		// dateAndStage internal content
		dateAndStage.add(dueDateLabel, "wrap");
		dateAndStage.add(dateField, "wrap");
		dateAndStage.add(stageLabel, "gaptop 10px, wrap");
		dateAndStage.add(stages);

		// EffortDateStage internal content
		EffortDateStage.add(dateAndStage);
		EffortDateStage.add(Effort);

		// BasicInfo Panel internal content
		BasicInfo.setBorder(BorderFactory.createTitledBorder(""));
		BasicInfo.add(titleLabel, "gapleft 5px, wrap");
		BasicInfo.add(titleField, "gapleft 5px, wrap");

		BasicInfo.add(descriptionLabel, "gapleft 5px, wrap");
		BasicInfo.add(descriptionScrollPane,
				"gapbottom 20px, gapleft 5px, wrap");
		BasicInfo.add(EffortDateStage, "h 25%, gapleft 5px, gaptop 20px");

		// Requirements Panel internal content
		Requirements.add(requirementLabel, "wrap");
		Requirements.add(requirements, "gapright 10px");
		Requirements.add(viewReq);

		// Users Panel internal content

		Users.setBorder(BorderFactory.createTitledBorder(""));
		JPanel UserPanel = new JPanel(new MigLayout());
		JPanel usersListPanel = new JPanel(new MigLayout());
		JPanel projectUsersListPanel = new JPanel(new MigLayout());
		JPanel addRemoveButtons = new JPanel(new MigLayout());
		usersListPanel.add(assignedUsersLabel, "wrap");

		usersListPanel.add(usersList);
		projectUsersListPanel.add(projectUsersLabel, "wrap");
		projectUsersListPanel.add(projectUsersList);

		addRemoveButtons.add(addUser, "wrap");
		addRemoveButtons.add(removeUser);

		UserPanel.add(projectUsersListPanel);
		UserPanel.add(addRemoveButtons);
		UserPanel.add(usersListPanel);

		Users.add(UserPanel, "h 60%, wrap, gapbottom 15px");
		Users.add(Requirements, "h 40%, gaptop 20px, gapleft 35px");

		// Activities Panel internal content
		Activities.setBorder(BorderFactory.createTitledBorder(""));
		Activities.add(activitiesLabel, "wrap, gaptop 10px, gapleft 25px");
		Activities.add(activityPane, "wrap, gapbottom 50px, gapleft 25px");
		Activities.add(commentsLabel, "gapleft 25px, wrap");
		Activities.add(commentScrollPane, "wrap, gapbottom 10px, gapleft 25px");
		Activities.add(submitComment,
				"dock south, gapleft 30px, gapright 30px, gapbottom 20px");

		// EditSaveCancel Panel internal content

		EditSaveCancel.add(save);
		EditSaveCancel.add(cancel);

		if (this.mode == Mode.EDIT) {
			EditSaveCancel.add(delete);
			EditSaveCancel.add(archive);
		}

		// The finished panels are added to the main window panel

		window.add(Spacer, "dock north");
		window.add(BasicInfo, "h 80%, w 30%");
		window.add(Users, "h 80%, w 30%, gapleft 10px");
		window.add(Activities, "h 80%, w 25%, gapleft 10px");
		window.add(EditSaveCancel, "dock south, h 10%");

		// Add the window to EditTaskView
		center.add(window);

		BalloonTipStyle errorStyle = new RoundedBalloonStyle(5, 5,
				Colors.INPUT_ERROR, Color.red);
		titleError = new BalloonTip(titleField, new JLabel(TITLE_ERROR),
				errorStyle, Orientation.LEFT_ABOVE, AttachLocation.NORTHEAST,
				5, 15, false);
		descripError = new BalloonTip(descripArea,
				new JLabel(DESCRIPTION_ERROR), errorStyle,
				Orientation.LEFT_ABOVE, AttachLocation.NORTHEAST, 5, 15, false);
		actEffortError = new BalloonTip(actEffortField,
				new JLabel(EFFORT_ERROR), errorStyle, Orientation.LEFT_ABOVE,
				AttachLocation.NORTHEAST, 5, 15, false);
		estEffortError = new BalloonTip(estEffortField,
				new JLabel(EFFORT_ERROR), errorStyle, Orientation.LEFT_ABOVE,
				AttachLocation.NORTHEAST, 5, 15, false);

		setTitleErrorVisible(false);
		setDescriptionErrorVisible(false);
		setActualEffortErrorVisible(false);
		setEstEffortErrorVisible(false);
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
		viewReq.addActionListener(controller);
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
		fieldC.validate();
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
	public boolean isArchived() {
		return archive.isSelected();
	}

	/**
	 * Gets the text in the title field
	 * 
	 * @return the title field's text
	 */
	public String getTitleText() {
		return titleField.getText();
	}

	/**
	 * Gets the description field
	 * 
	 * @return the description field
	 */
	public String getDescription() {
		return descripArea.getText();
	}

	/**
	 * Gets the date field's value
	 * 
	 * @return the date entered.
	 */
	public Date getDate() {
		return dateField.getDate();
	}

	/**
	 * Gets the estimated effort
	 * 
	 * @return the estimated effort text
	 */
	public String getEstEffort() {
		return estEffortField.getText();
	}

	/**
	 * Gets the actual effort field
	 * 
	 * @return the actual effort field
	 */
	public String getActEffort() {
		return actEffortField.getText();
	}

	/**
	 * 
	 * Sets the stage dropdown menu's available options.
	 *
	 * @param stageNames
	 *            The list of stageNames to set as options.
	 */
	public void setStages(List<String> stageNames) {
		final String selectedStage = getSelectedStage();

		stages.removeAllItems();
		for (String stageName : stageNames) {
			stages.addItem(stageName);
		}

		// Select the 1st item if the old selected item doesn't exist
		stages.setSelectedItem(0);
		if (!(selectedStage == null)) {
			stages.setSelectedItem(selectedStage);
		}
	}

	/**
	 * 
	 * Sets the requirements dropdown menu's available options.
	 *
	 * @param reqNames
	 *            The list of requirement names to set as options.
	 */
	public void setRequirements(List<String> reqNames) {
		final String selectedReq = getSelectedRequirement();

		requirements.removeAllItems();
		requirements.addItem(NO_REQ);
		for (String name : reqNames) {
			requirements.addItem(name);
		}

		// Select NO_REQ if the old selected item doesn't exist
		requirements.setSelectedItem(NO_REQ);
		if (!(selectedReq == null)) {
			requirements.setSelectedItem(selectedReq);
		}
	}

	/**
	 * Gets the selected requirement. If no requirement is selected, returns
	 * null.
	 *
	 * @return The selected requirement's name
	 */
	public String getSelectedRequirement() {
		if (NO_REQ.equals(requirements.getSelectedItem())) {
			return null;
		}
		return (String) requirements.getSelectedItem();
	}

	/**
	 * Sets the selected requirement. Use null to select no requirement.
	 *
	 * @param requirementName
	 *            The requirement's name we're selecting.
	 */
	public void setSelectedRequirement(String requirementName) {
		if (requirementName == null) {
			requirements.setSelectedItem(NO_REQ);
		}
		requirements.setSelectedItem(requirementName);

		if (fieldC != null) {
			fieldC.validate();
		}
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
	 * 
	 * Set stage dropdown box to select a stage
	 *
	 * @param stageName
	 *            The name of the stage to be selected.
	 */
	public void setSelectedStage(String stageName) {
		stages.setSelectedItem(stageName);
	}

	/**
	 * 
	 * Returns the selected stage name. If it cannot be found, returns null.
	 *
	 * @return the selected stage as a String.
	 */
	public String getSelectedStage() {
		return (String) stages.getSelectedItem();
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
	 * Sets the title field border red
	 * 
	 * @param red
	 *            turns the red border on and off
	 */

	public void setTitleFieldRed(boolean red) {
		if (red) {
			titleField.setBorder(BorderFactory.createLineBorder(Color.red));
		} else {
			titleField.setBorder(BorderFactory.createLineBorder(Color.black));
		}
	}

	/**
	 * Sets the title field border red
	 * 
	 * @param boolean turns the red border on and off
	 */

	/**
	 * Sets the description error visible or invisible
	 * 
	 * @param v
	 *            true will make the description error visible, false will make
	 *            the description error invisible
	 */

	public void setDescriptionErrorVisible(boolean v) {
		descripError.setVisible(v);
	}

	/**
	 * Sets the description field border red
	 * 
	 * @param red
	 *            turns the red border on and off
	 */

	public void setDescriptionFieldRed(boolean red) {
		if (red) {
			descripArea.setBorder(BorderFactory.createLineBorder(Color.red));
		} else {
			descripArea
					.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		}
	}

	/**
	 * Sets the description field border red
	 * 
	 * @param boolean turns the red border on and off
	 */

	/**
	 * Sets the estimated effort error visible or invisible
	 * 
	 * @param v
	 *            true will make the estimated effort error visible, false will
	 *            make the estimated effort error invisible
	 */
	public void setEstEffortErrorVisible(boolean v) {
		estEffortError.setVisible(v);
	}

	/**
	 * Sets the estimated effort field border red
	 * 
	 * @param red
	 *            turns the red border on and off
	 */

	public void setEstEffortFieldRed(boolean red) {
		if (red) {
			estEffortField.setBorder(BorderFactory.createLineBorder(Color.red));
		} else {
			estEffortField
					.setBorder(BorderFactory.createLineBorder(Color.gray));
		}
	}

	/**
	 * Sets the actual effort field border red
	 * 
	 * @red boolean turns the red border on and off
	 */

	public void setActEffortFieldRed(boolean red) {
		if (red) {
			actEffortField.setBorder(BorderFactory.createLineBorder(Color.red));
		} else {
			actEffortField
					.setBorder(BorderFactory.createLineBorder(Color.gray));
		}
	}

	/**
	 * Sets the actual effort error visible or invisible
	 * 
	 * @param v
	 *            true will make the actual effort error visible, false will
	 *            make the actual effort error invisible
	 */
	public void setActualEffortErrorVisible(boolean v) {
		actEffortError.setVisible(v);
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
		submitComment.setEnabled(e);
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
		final DateFormat dateF = new SimpleDateFormat("MM/dd/yyyy kk:mm");
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
	 * Adds an activity.
	 *
	 * @param act
	 *            the activity.
	 */
	public void addActivity(ActivityModel act) {
		activities.add(act);
	}

	/**
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
	 * Sets if the view requirement button is enabled/disabled
	 *
	 * @param bool
	 *            should the button be enabled?
	 */
	public void setViewRequirementEnabled(boolean bool) {
		viewReq.setEnabled(bool);
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
