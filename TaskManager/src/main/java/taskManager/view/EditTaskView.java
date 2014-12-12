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
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

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

	private static final long serialVersionUID = 1L;
	private JButton save;
	private JButton cancel;
	private JButton addUser;
	private JButton removeUser;
	private JButton delete;
	private JButton addReq;

	private JCheckBox archive;

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

		// TODO: User Mode to switch between create and edit views
		// When Task added make EditTask take in a Task called currTask
		this.mode = mode;
		this.setOpaque(false);
		this.setLayout(new MigLayout("wrap 1", "[grow, fill]"));
		this.activityC = activityC;

		window = new JPanel(new MigLayout());

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

		// JTextArea
		// TODO
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

		// This is where the 9 primary panels are defined
		JPanel Spacer = new JPanel(new MigLayout());
		JPanel BasicInfo = new JPanel(new MigLayout());
		JPanel Users = new JPanel(new MigLayout());
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
		Requirements.add(addReq);

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

		// EditSaveCancel Panel internal content

		EditSaveCancel.add(save);
		EditSaveCancel.add(cancel);

		if (this.mode == Mode.EDIT) {
			EditSaveCancel.add(delete);
			EditSaveCancel.add(archive);
		}

		window.add(Spacer, "dock north");
		window.add(BasicInfo, "h 80%, w 30%");
		window.add(Users, "h 80%, w 30%, gapleft 10px");
		window.add(EditSaveCancel, "dock south, h 10%");

		BalloonTipStyle errorStyle = new RoundedBalloonStyle(5, 5,
				Colors.INPUT_ERROR, Color.red);
		titleError = new BalloonTip(getTitle(), new JLabel(TITLE_ERROR),
				errorStyle, Orientation.LEFT_ABOVE, AttachLocation.NORTHEAST,
				5, 15, false);
		descripError = new BalloonTip(getDescription(), new JLabel(
				DESCRIPTION_ERROR), errorStyle, Orientation.LEFT_ABOVE,
				AttachLocation.NORTHEAST, 5, 15, false);
		actEffortError = new BalloonTip(getActEffort(),
				new JLabel(EFFORT_ERROR), errorStyle, Orientation.LEFT_ABOVE,
				AttachLocation.NORTHEAST, 5, 15, false);
		estEffortError = new BalloonTip(getEstEffort(),
				new JLabel(EFFORT_ERROR), errorStyle, Orientation.LEFT_ABOVE,
				AttachLocation.NORTHEAST, 5, 15, false);

		setTitleErrorVisible(false);
		setDescriptionErrorVisible(false);
		setActualEffortErrorVisible(false);
		setEstEffortErrorVisible(false);

		// Make the activities panel
		JTabbedPane activitiesTabs = new JTabbedPane();
		activitiesTabs.addTab("Comments", activityC.getCommentsPanel());
		activitiesTabs.addTab("All Activities", activityC.getActivitiesPanel());

		// The finished panels are added to the main window panel
		Dimension panelSize = window.getPreferredSize();
		panelSize.height = 500; // Decide size
		window.setPreferredSize(panelSize);

		JScrollPane fieldsScroll = new JScrollPane(window);
		JPanel tabs = new JPanel();
		tabs.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
		tabs.add(activitiesTabs);
		tabs.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				fieldsScroll, tabs);
		splitPane.setDividerLocation(900);
		splitPane.setDividerSize(15);
		splitPane.setUI(new BasicSplitPaneUI() {
			@Override
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {

					private static final long serialVersionUID = 6473604947769495646L;

					@Override
					public void paint(Graphics g) {
					}
				};
			}
		});

		BasicSplitPaneDivider divider = (BasicSplitPaneDivider) splitPane
				.getComponent(2);
		// divider.setBackground(Color.BLUE);
		// divider.setForeground(Color.BLUE);

		// JLabel slider = new JLabel();
		// slider.setBackground(Color.BLUE);
		// divider.add(slider);
		// divider.setLayout(new MigLayout("", "[grow, fill]"));
		divider.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));

		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(.5);
		this.add(splitPane);
		this.add(EditSaveCancel);
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
	 * Sets the title field border red
	 * 
	 * @param boolean turns the red border on and off
	 */

	public void setTitleFieldRed(boolean red) {
		if (red) {
			this.titleField
					.setBorder(BorderFactory.createLineBorder(Color.red));
		} else {
			this.titleField.setBorder(BorderFactory
					.createLineBorder(Color.black));
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
	 * @param boolean turns the red border on and off
	 */

	public void setDescriptionFieldRed(boolean red) {
		if (red) {
			this.descripArea.setBorder(BorderFactory
					.createLineBorder(Color.red));
		} else {
			this.descripArea.setBorder(BorderFactory.createLineBorder(
					Color.gray, 1));
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
	 * @param boolean turns the red border on and off
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
	 * @param boolean turns the red border on and off
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
			tic.checkFields();
		}
		if (visible && controller != null) {
			controller.reloadData();
		}
		if (visible) {
			activityC.reloadActivitiesPanel();
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
}
