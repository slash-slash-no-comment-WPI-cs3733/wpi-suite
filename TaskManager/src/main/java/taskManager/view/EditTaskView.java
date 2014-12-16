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
import java.awt.KeyboardFocusManager;
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
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.BalloonTip.AttachLocation;
import net.java.balloontip.BalloonTip.Orientation;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import taskManager.controller.ActivityController;
import taskManager.controller.EditTaskController;
import taskManager.controller.TaskInputController;
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;

/**
 *  Edit panel for a task
 */

/**
 * 
 * @author Thane Hunt
 * @author Tyler Jaskoviak
 * @author Samee Swartz
 * @author Clark Jacobsohn
 */

public class EditTaskView extends JPanel implements LocaleChangeListener {

	public static final String STAGES = "stages";
	public static final String REQUIREMENTS = "requirements";
	public static final String CANCEL = "cancel";
	public static final String ARCHIVE = "archive";
	public static final String SAVE = "save";
	public static final String VIEW_REQ = "viewReq";
	public static final String SUBMIT_COMMENT = "submitComment";
	public static final String CANCEL_COMMENT = "cancelComment";
	public static final String ADD_USER = "addUser";
	public static final String REMOVE_USER = "removeUser";
	public static final String DELETE = "delete";
	public static final String ACT_EFFORT = "act_effort";
	public static final String EST_EFFORT = "est_effort";
	public static final String DUE_DATE = "due_date";
	public static final String NO_REQ = "None";
	public static final String REFRESH = "refresh";
	public static final String TITLE = "title";
	public static final String DESCRIP = "description";
	private static final String TITLE_ERROR = "TitleEmpty";
	private static final String DESCRIPTION_ERROR = "DescriptionEmpty";
	private static final String EFFORT_ERROR = "EffortNotInt";

	private static final long serialVersionUID = 1L;
	private JButton save;
	private JButton cancel;
	private JButton addUser;
	private JButton removeUser;
	private JButton delete;
	private JButton viewReq;
	private JButton submitComment;
	private JButton cancelComment;

	private JCheckBox archive;

	private final JTextField titleField;
	private final JTextArea descripArea;
	private final JXDatePicker dateField;
	private final JTextField estEffortField;
	private final JTextField actEffortField;
	private JTextArea commentBox;
	private final JPanel window;

	private final JLabel titleLabel;
	private final JLabel descriptionLabel;
	private final JLabel dueDateLabel;
	private final JLabel stageLabel;
	private final JLabel estimatedEffortLabel;
	private final JLabel actualEffortLabel;
	private final JLabel requirementLabel;
	private final JLabel assignedUsersLabel;
	private final JLabel projectUsersLabel;
	private final JLabel activitiesLabel;
	private final JLabel commentsLabel;

	private BalloonTip titleError;
	private BalloonTip descripError;
	private BalloonTip actEffortError;
	private BalloonTip estEffortError;

	private JSplitPane splitPane;

	private final Mode mode;

	public enum Mode {
		CREATE, EDIT;
	}

	private final ScrollList usersList;
	private final ScrollList projectUsersList;

	private final JComboBox<String> stages;
	private final JComboBox<String> requirements;

	private EditTaskController controller;
	private ActivityController activityC;
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
	 * 
	 * @param activityC
	 *            The ActivityController for this EditTaskView's task
	 */
	public EditTaskView(Mode mode, ActivityController activityC) {

		// When Task added make EditTask take in a Task called currTask
		this.mode = mode;
		this.activityC = activityC;
		this.setOpaque(false);
		// Contains the splitPane and button panel
		this.setLayout(new MigLayout("wrap 1, align center", "[grow, fill]",
				"[grow, fill][]"));

		// the Panel holding all task editing (not activity) stuff
		window = new JPanel(new MigLayout("center align", "[][][]",
				"[grow, fill]"));

		// JLabels
		titleLabel = new JLabel();
		titleLabel.setFont(bigFont);
		descriptionLabel = new JLabel();
		descriptionLabel.setFont(bigFont);
		dueDateLabel = new JLabel();
		dueDateLabel.setFont(bigFont);
		stageLabel = new JLabel();
		stageLabel.setFont(bigFont);
		estimatedEffortLabel = new JLabel();
		estimatedEffortLabel.setFont(bigFont);
		actualEffortLabel = new JLabel();
		actualEffortLabel.setFont(bigFont);
		requirementLabel = new JLabel();
		requirementLabel.setFont(bigFont);
		assignedUsersLabel = new JLabel();
		assignedUsersLabel.setFont(bigFont);
		projectUsersLabel = new JLabel();
		projectUsersLabel.setFont(bigFont);
		activitiesLabel = new JLabel();
		activitiesLabel.setFont(bigFont);
		commentsLabel = new JLabel();
		commentsLabel.setFont(bigFont);

		// JTextFields
		// sets all text fields editable and adds them to global variables
		titleField = new JTextField(26);
		titleField.setEditable(true);
		titleField.setName(TITLE);

		descripArea = new JTextArea(14, 26);
		descripArea.setMinimumSize(new Dimension(20, 100));
		descripArea.setName(DESCRIP);
		descripArea.setEditable(true);
		descripArea.setLineWrap(true);
		descripArea.setWrapStyleWord(true);
		// Sets the traversal keys to null so that it inherits parent's
		// behavior.
		// Reference: http://stackoverflow.com/a/5043957
		descripArea.setFocusTraversalKeys(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		descripArea.setFocusTraversalKeys(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		final JScrollPane descriptionScrollPane = new JScrollPane(descripArea);

		descriptionScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		estEffortField = new JTextField(4);
		estEffortField.setEditable(true);
		estEffortField.setName(EST_EFFORT);
		actEffortField = new JTextField(4);
		actEffortField.setEditable(true);
		actEffortField.setName(ACT_EFFORT);

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

		// Get to add users
		usersList = new ScrollList("");
		usersList.setBackground(this.getBackground());
		projectUsersList = new ScrollList("");
		projectUsersList.setBackground(this.getBackground());

		// Requirement Pane
		requirements = new JComboBox<String>();
		requirements.setName(REQUIREMENTS);
		requirements.setPrototypeDisplayValue("Select a requirement");
		// JButtons
		// Delete Task and close the window
		delete = new JButton();
		delete.setName(DELETE);

		// Add user to list
		addUser = new JButton(">>");
		addUser.setName(ADD_USER);
		this.setAddUserEnabled(false);

		// remove user from list
		removeUser = new JButton("<<");
		removeUser.setName(REMOVE_USER);
		this.setRemoveUserEnabled(false);

		// add requirement
		viewReq = new JButton();
		viewReq.setName(VIEW_REQ);

		// saves all the data and closes the window
		save = new JButton();
		save.setName(SAVE);
		this.setSaveEnabled(false);

		// closes the window without saving
		cancel = new JButton();
		cancel.setName(CANCEL);
		archive = new JCheckBox();
		archive.setName(ARCHIVE);
		archive.setOpaque(false);

		// Combo Box for Stage
		stages = new JComboBox<String>();
		stages.setName(STAGES);

		// This is where the 8 primary panels are defined
		JPanel SpacerTop = new JPanel(new MigLayout());
		JPanel SpacerBtm = new JPanel(new MigLayout());
		JPanel BasicInfo = new JPanel(new MigLayout());
		JPanel Users = new JPanel(new MigLayout("align center, wrap 1",
				"[grow, fill]"));
		JPanel Effort = new JPanel(new MigLayout());
		JPanel Requirements = new JPanel(new MigLayout("center"));
		JPanel EditSaveCancel = new JPanel(new MigLayout("center"));
		EditSaveCancel.setOpaque(false);
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
		BasicInfo.add(titleLabel, "gapleft 15px, wrap");
		BasicInfo.add(titleField, "gapleft 15px, wrap");

		BasicInfo.add(descriptionLabel, "gapleft 15px, wrap");
		BasicInfo.add(descriptionScrollPane,
				"gapbottom 20px, gapleft 15px, wrap");
		BasicInfo.add(EffortDateStage, "h 25%, gapleft 5px, gaptop 20px");

		// Requirements Panel internal content
		Requirements.add(requirementLabel, "wrap");
		Requirements.add(requirements, "gapright 10px");
		Requirements.add(viewReq);

		// Users Panel internal content

		Users.setBorder(BorderFactory.createTitledBorder(""));
		JPanel UserPanel = new JPanel(new MigLayout("align center"));
		JPanel usersListPanel = new JPanel(new MigLayout("align center"));
		JPanel projectUsersListPanel = new JPanel(new MigLayout("align center"));
		JPanel addRemoveButtons = new JPanel(new MigLayout("align center"));
		usersListPanel.add(assignedUsersLabel, "wrap");

		usersListPanel.add(usersList);
		projectUsersListPanel.add(projectUsersLabel, "wrap");
		projectUsersListPanel.add(projectUsersList);

		addRemoveButtons.add(addUser, "wrap");
		addRemoveButtons.add(removeUser);

		UserPanel.add(projectUsersListPanel);
		UserPanel.add(addRemoveButtons);
		UserPanel.add(usersListPanel);

		Users.add(UserPanel, "h 60%");
		Users.add(Requirements, "h 40%");

		// EditSaveCancel Panel internal content

		EditSaveCancel.add(save);
		EditSaveCancel.add(cancel);

		if (this.mode == Mode.EDIT) {
			EditSaveCancel.add(delete);
			EditSaveCancel.add(archive);
		}

		window.add(SpacerTop, "dock north");
		window.add(BasicInfo, "h 80%, w 30%");
		window.add(Users, "h 80%, w 30%, gapleft 10px");
		window.add(SpacerBtm, "dock south");

		BalloonTipStyle errorStyle = new RoundedBalloonStyle(5, 5,
				Colors.INPUT_ERROR, Color.red);
		titleError = new BalloonTip(titleField, new JLabel(), errorStyle,
				Orientation.LEFT_ABOVE, AttachLocation.NORTHEAST, 5, 15, false);
		descripError = new BalloonTip(descripArea, new JLabel(), errorStyle,
				Orientation.LEFT_ABOVE, AttachLocation.NORTHEAST, 5, 15, false);
		actEffortError = new BalloonTip(actEffortField, new JLabel(),
				errorStyle, Orientation.LEFT_ABOVE, AttachLocation.NORTHEAST,
				5, 15, false);
		estEffortError = new BalloonTip(estEffortField, new JLabel(),
				errorStyle, Orientation.LEFT_ABOVE, AttachLocation.NORTHEAST,
				5, 15, false);

		setTitleErrorVisible(false);
		setDescriptionErrorVisible(false);
		setActualEffortErrorVisible(false);
		setEstEffortErrorVisible(false);

		// The finished panels are added to the main window panel
		Dimension panelSize = window.getPreferredSize();
		panelSize.height = 500; // Decide size
		window.setPreferredSize(panelSize);

		JScrollPane windowScroll = new JScrollPane(window);
		windowScroll.getVerticalScrollBar().setUnitIncrement(12);
		windowScroll.getHorizontalScrollBar().setUnitIncrement(12);

		// The activities and comments tabs
		JPanel tabs = new JPanel(new MigLayout("wrap 1", "[grow, fill]",
				"[grow, fill][]"));
		tabs.add(activityC.getActivitiesPanel());
		tabs.add(initCommentBoxandBtns());
		tabs.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				windowScroll, tabs);
		splitPane.setDividerLocation(900);
		splitPane.setDividerSize(10);

		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(.5);
		this.add(splitPane);
		this.add(EditSaveCancel);
	}

	private JPanel initCommentBoxandBtns() {
		JPanel commentAndBtns = new JPanel(new MigLayout("wrap 1",
				"[grow, fill]", "[]"));

		commentBox = new JTextArea();
		commentBox.setRows(5);
		commentBox.setWrapStyleWord(true);
		commentBox.setLineWrap(true);

		commentBox.getInputMap()
				.put(KeyStroke.getKeyStroke("TAB"), "doNothing");
		commentBox.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),
				"doNothing");

		JScrollPane commentScroll = new JScrollPane(commentBox,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		commentScroll.setMinimumSize(new Dimension(20, 100));

		// Buttons
		JPanel buttons = new JPanel();
		buttons.setOpaque(false);
		submitComment = new JButton("Save Comment");
		submitComment.setName(EditTaskView.SUBMIT_COMMENT);
		submitComment.setEnabled(false);
		cancelComment = new JButton("Cancel");
		cancelComment.setName(EditTaskView.CANCEL_COMMENT);
		cancelComment.setEnabled(false);
		buttons.add(submitComment);
		buttons.add(cancelComment);
		buttons.setMaximumSize(new Dimension(10000, 40));

		commentAndBtns.add(commentScroll);
		commentAndBtns.add(buttons);

		// load strings the first time
		onLocaleChange();
		Localizer.addListener(this);

		return commentAndBtns;
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
		delete.addActionListener(controller);
		submitComment.addActionListener(controller);
		cancelComment.addActionListener(controller);
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
		requirements.addPopupMenuListener(fieldC);
		dateField.addPropertyChangeListener(fieldC);
		archive.addItemListener(fieldC);
		commentBox.addKeyListener(fieldC);
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
		requirements.addItem(Localizer.getString(NO_REQ));
		for (String name : reqNames) {
			requirements.addItem(name);
		}

		// Select NO_REQ if the old selected item doesn't exist
		requirements.setSelectedItem(Localizer.getString(NO_REQ));
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
		if (Localizer.getString(NO_REQ).equals(requirements.getSelectedItem())) {
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
			requirements.setSelectedItem(Localizer.getString(NO_REQ));
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
			tic.checkEditFields();
		}
		if (visible && controller != null) {
			controller.reloadData();
		}
		if (visible) {
			activityC.reloadActivitiesPanel();
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
	 * Set whether the submit and cancel buttons for the activity view are
	 * enabled or not
	 * 
	 * @param e
	 *            true to make the submit button enabled, false to disable it
	 */
	public void setSubmitCancelCommentEnabled(boolean e) {
		submitComment.setEnabled(e);
		cancelComment.setEnabled(e);
	}

	/**
	 * Returns the text in the comments field.
	 * 
	 * @return The text in the comments field
	 */
	public String getCommentsFieldText() {
		return commentBox.getText();
	}

	/**
	 * Sets the text in the comment JTextArea.
	 * 
	 */
	public void setCommentsFieldText(String text) {
		commentBox.setText(text);
		cancelComment.setEnabled(true);
	}

	/**
	 * Clears the text in the comments field.
	 */
	public void clearText() {
		commentBox.setText("");
		submitComment.setEnabled(false);
		cancelComment.setEnabled(false);
	}

	@Override
	public void onLocaleChange() {
		titleLabel.setText(Localizer.getString("Title"));
		descriptionLabel.setText(Localizer.getString("Description"));
		dueDateLabel.setText(Localizer.getString("DueDate"));
		stageLabel.setText(Localizer.getString("Stage"));
		estimatedEffortLabel.setText(Localizer.getString("EstimatedEffort"));
		actualEffortLabel.setText(Localizer.getString("ActualEffort"));
		requirementLabel.setText(Localizer.getString("SelectRequirement"));
		assignedUsersLabel.setText(Localizer.getString("AssignedUsers"));
		projectUsersLabel.setText(Localizer.getString("ProjectUsers"));
		activitiesLabel.setText(Localizer.getString("Activities"));
		commentsLabel.setText(Localizer.getString("Comment"));
		delete.setText(Localizer.getString("Delete"));
		submitComment.setText(Localizer.getString("SubmitComment"));
		viewReq.setText(Localizer.getString("ViewRequirement"));
		save.setText(Localizer.getString("Save"));
		cancel.setText(Localizer.getString("Cancel"));
		archive.setText(Localizer.getString("Archived"));
		((JLabel) titleError.getContents()).setText(Localizer
				.getString(TITLE_ERROR));
		((JLabel) descripError.getContents()).setText(Localizer
				.getString(DESCRIPTION_ERROR));
		((JLabel) actEffortError.getContents()).setText(Localizer
				.getString(EFFORT_ERROR));
		((JLabel) estEffortError.getContents()).setText(Localizer
				.getString(EFFORT_ERROR));

		// reload the requirements box
		if (controller != null) {
			String r = getSelectedRequirement();
			String s = getSelectedStage();
			controller.reloadData();
			setSelectedRequirement(r);
			setSelectedStage(s);
		}
	}
}
