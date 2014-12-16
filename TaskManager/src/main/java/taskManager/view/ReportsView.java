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
import java.awt.Image;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import taskManager.controller.ReportsManager;
import taskManager.controller.TaskInputController;
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;

/**
 * @author Tyler Jaskoviak
 *
 */
public class ReportsView extends JPanel implements LocaleChangeListener {

	public static final String STAGE_NAME = "stage_name";
	public static final String START_DATE = "start_date";
	public static final String END_DATE = "end_date";
	public static final String WORK_FLOW = "work_flow";
	public static final String WORK_VELOCITY = "work_velocity";
	public static final String TOTALED_DIST = "totaled_dist";
	public static final String DIVIDED_DIST = "divided_dist";
	public static final String COLLABORATIVE = "collaborative";
	public static final String COMPARATIVE = "comparative";
	public static final String ALL_USERS = "all_users";
	public static final String ADD_USER = "add_user";
	public static final String REMOVE_USER = "remove_user";
	public static final String GENERATE = "generate";

	private JPanel window;

	// Variable to Insert Images
	Image img;

	// Stage picker
	private JPanel stagePanel;
	private JComboBox<String> stages;

	// Date Picker
	private JPanel datePanel;
	private JLabel fromLabel;
	private JLabel toLabel;
	private JXDatePicker startDate;
	private JXDatePicker endDate;

	// Flow vs Velocity
	private JPanel workTypePanel;
	private JRadioButton workFlow;
	private JRadioButton workVelocity;
	private ButtonGroup workTypeGroup;

	// Work Distribution
	private JPanel distributionPanel;
	private JRadioButton totaledDist;
	private JRadioButton dividedDist;
	private ButtonGroup distributionGroup;

	// Compare/Combine
	private JPanel workModePanel;
	private JRadioButton combineWork;
	private JRadioButton compareWork;
	private ButtonGroup workflowGroup;

	// Users
	private JPanel usersPanel;
	private JCheckBox allUsers;
	private ScrollList currUsersList;
	private ScrollList projectUsersList;
	private JButton addUser;
	private JButton removeUser;

	// Generate Graph Button
	private JButton generateGraph;

	private ReportsManager controller;

	public ReportsView() {

		Dimension nt_panelSize = getPreferredSize();
		nt_panelSize.width = 1000;
		nt_panelSize.height = 600;
		this.setPreferredSize(nt_panelSize);
		this.setMinimumSize(nt_panelSize);

		window = new JPanel();
		window.setPreferredSize(nt_panelSize);
		this.setLayout(new FlowLayout());

		// Used to organize every JPanel
		GridBagConstraints reportsGridBag = new GridBagConstraints();

		// Stage
		stagePanel = new JPanel();
		stagePanel.setLayout(new GridBagLayout());
		stages = new JComboBox<String>();
		stages.setName(STAGE_NAME);
		reportsGridBag.anchor = GridBagConstraints.FIRST_LINE_START;
		reportsGridBag.gridx = 0;
		reportsGridBag.gridy = 0;
		stagePanel.setBorder(BorderFactory.createTitledBorder(""));
		stagePanel.add(stages, reportsGridBag);
		Dimension stageDimensions = getPreferredSize();
		stageDimensions.width = 175;
		stageDimensions.height = 75;
		stagePanel.setPreferredSize(stageDimensions);
		stagePanel.setMinimumSize(stageDimensions);
		stagePanel.setMaximumSize(stageDimensions);

		fromLabel = new JLabel();
		toLabel = new JLabel();

		// Date
		datePanel = new JPanel();
		datePanel.setLayout(new GridBagLayout());
		startDate = new JXDatePicker();
		startDate.setName(START_DATE);
		startDate.setDate(Calendar.getInstance().getTime());
		((JButton) startDate.getComponent(1)).setIcon(new ImageIcon(
				((new ImageIcon(getClass().getResource("calendar-icon.png")))
						.getImage()).getScaledInstance(20, 20,
						java.awt.Image.SCALE_SMOOTH)));

		endDate = new JXDatePicker();
		endDate.setName(END_DATE);
		endDate.setDate(Calendar.getInstance().getTime());
		((JButton) endDate.getComponent(1)).setIcon(new ImageIcon(
				((new ImageIcon(getClass().getResource("calendar-icon.png")))
						.getImage()).getScaledInstance(20, 20,
						java.awt.Image.SCALE_SMOOTH)));
		reportsGridBag.anchor = GridBagConstraints.WEST;
		reportsGridBag.gridx = 0;
		reportsGridBag.gridy = 0;
		datePanel.setBorder(BorderFactory.createTitledBorder(""));
		datePanel.add(fromLabel, reportsGridBag);
		reportsGridBag.gridy = 1;
		datePanel.add(toLabel, reportsGridBag);
		reportsGridBag.gridx = 1;
		reportsGridBag.gridy = 0;
		datePanel.add(startDate, reportsGridBag);
		reportsGridBag.gridy = 1;
		datePanel.add(endDate, reportsGridBag);
		Dimension dateDimensions = getPreferredSize();
		dateDimensions.width = 250;
		dateDimensions.height = 90;
		datePanel.setPreferredSize(dateDimensions);
		datePanel.setMinimumSize(dateDimensions);
		datePanel.setMaximumSize(dateDimensions);

		// WorkType
		workTypePanel = new JPanel();
		workTypePanel.setLayout(new GridBagLayout());
		workFlow = new JRadioButton("");
		workFlow.setName(WORK_FLOW);
		workVelocity = new JRadioButton("");
		workVelocity.setName(WORK_VELOCITY);
		workTypeGroup = new ButtonGroup();
		workTypeGroup.add(workFlow);
		workTypeGroup.add(workVelocity);
		reportsGridBag.anchor = GridBagConstraints.FIRST_LINE_START;
		reportsGridBag.gridx = 0;
		reportsGridBag.gridy = 0;
		workTypePanel.setBorder(BorderFactory.createTitledBorder(""));
		workTypePanel.add(workFlow, reportsGridBag);
		reportsGridBag.gridy = 1;
		workTypePanel.add(workVelocity, reportsGridBag);
		Dimension workTypeDimension = getPreferredSize();
		workTypeDimension.width = 125;
		workTypeDimension.height = 75;
		workTypePanel.setPreferredSize(workTypeDimension);
		workTypePanel.setMinimumSize(workTypeDimension);
		workTypePanel.setMaximumSize(workTypeDimension);

		// Combined or Compared graph
		workModePanel = new JPanel();
		workModePanel.setLayout(new GridBagLayout());
		combineWork = new JRadioButton("");
		combineWork.setName(COLLABORATIVE);
		compareWork = new JRadioButton("");
		compareWork.setName(COMPARATIVE);
		workflowGroup = new ButtonGroup();
		workflowGroup.add(combineWork);
		workflowGroup.add(compareWork);
		reportsGridBag.anchor = GridBagConstraints.WEST;
		reportsGridBag.gridx = 0;
		reportsGridBag.gridy = 0;
		workModePanel.setBorder(BorderFactory.createTitledBorder(""));
		workModePanel.add(combineWork, reportsGridBag);
		reportsGridBag.gridy = 1;
		workModePanel.add(compareWork, reportsGridBag);
		Dimension workModeDimension = getPreferredSize();
		workModeDimension.width = 125;
		workModeDimension.height = 75;
		workModePanel.setPreferredSize(workModeDimension);
		workModePanel.setMinimumSize(workModeDimension);
		workModePanel.setMaximumSize(workModeDimension);

		// Users
		usersPanel = new JPanel();
		allUsers = new JCheckBox();
		allUsers.setName(ALL_USERS);
		currUsersList = new ScrollList("");
		currUsersList.setBackground(this.getBackground());
		projectUsersList = new ScrollList("");
		projectUsersList.setBackground(this.getBackground());
		// Add user to list
		addUser = new JButton(">>");
		addUser.setName(ADD_USER);
		this.setAddUserEnabled(false);
		// remove user from list
		removeUser = new JButton("<<");
		removeUser.setName(REMOVE_USER);
		this.setRemoveUserEnabled(false);
		reportsGridBag.anchor = GridBagConstraints.WEST;
		reportsGridBag.gridx = 0;
		reportsGridBag.gridy = 0;
		usersPanel.setBorder(BorderFactory.createTitledBorder(""));
		JPanel usersListPanel = new JPanel(new MigLayout());
		JPanel projectUsersListPanel = new JPanel(new MigLayout());
		JPanel addRemoveButtons = new JPanel(new MigLayout());
		// usersPanel.add(allUsers, reportsGridBag);
		usersListPanel.add(currUsersList);
		projectUsersListPanel.add(projectUsersList);
		addRemoveButtons.add(addUser, "wrap");
		addRemoveButtons.add(removeUser);
		usersPanel.add(projectUsersListPanel, "w 100!, gapleft 15px");
		usersPanel.add(addRemoveButtons);
		usersPanel.add(usersListPanel, "w 100!");
		Dimension usersPanelDimension = new Dimension();
		usersPanelDimension.width = 500;
		usersPanelDimension.height = 350;
		usersPanel.setPreferredSize(usersPanelDimension);
		usersPanel.setMinimumSize(usersPanelDimension);
		usersPanel.setMaximumSize(usersPanelDimension);

		// Generate Graph
		generateGraph = new JButton();
		generateGraph.setName(GENERATE);

		window.setLayout(new GridBagLayout());

		GridBagConstraints toolbarGrid = new GridBagConstraints();
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"reports-icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		generateGraph.setIcon(new ImageIcon(img));

		// One Column
		toolbarGrid.anchor = GridBagConstraints.LINE_START;

		toolbarGrid.weightx = 0.5;
		toolbarGrid.weighty = 0.5;
		toolbarGrid.gridx = 0;

		toolbarGrid.gridy = 0;
		window.add(stagePanel, toolbarGrid);

		toolbarGrid.gridy = 1;
		window.add(datePanel, toolbarGrid);

		toolbarGrid.gridy = 3;
		// window.add(workTypePanel, toolbarGrid);

		toolbarGrid.gridy = 4;
		// window.add(distributionPanel, toolbarGrid);

		toolbarGrid.gridy = 5;
		// window.add(workModePanel, toolbarGrid);

		toolbarGrid.gridy = 6;
		window.add(usersPanel, toolbarGrid);

		toolbarGrid.gridy = 7;
		window.add(generateGraph, toolbarGrid);

		this.add(window);

		onLocaleChange();
		Localizer.addListener(this);
	}

	public void setController(ReportsManager manager) {
		controller = manager;
		addUser.addActionListener(manager);
		removeUser.addActionListener(manager);
		allUsers.addChangeListener(manager);
		currUsersList.setController(manager);
		projectUsersList.setController(manager);
		generateGraph.addActionListener(manager);
	}

	/**
	 * Adds the action listener (controller) to this view
	 * 
	 * @param controller
	 *            the controller to be attached to this view
	 */
	public void setFieldController(TaskInputController controller) {
		stages.addPopupMenuListener(controller);
	}

	/**
	 * gets the dropdown box in the view that contains all the stage names
	 * 
	 * @return the stages dropdown box
	 */
	public JComboBox<String> getStage() {
		return stages;
	}

	/**
	 * set stage dropdown box to the stage for graphing
	 * 
	 * @param n
	 *            the index of the stage in the workflow
	 */
	public void setStageDropdown(int n) {
		String p = stages.getItemAt(n);
		stages.setSelectedItem(p);
	}

	/**
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
	 * Returns the start date for the graph
	 * 
	 * @return the start date as a JXDatePicker
	 */
	public JXDatePicker getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date of the graph
	 * 
	 * @param d
	 *            the date to set the start to
	 */
	public void setStartDate(Date d) {
		startDate.setDate(d);
	}

	/**
	 * Returns the end date of the graph
	 * 
	 * @return the end date as a JXDatePicker
	 */
	public JXDatePicker getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date of the graph
	 * 
	 * @param d
	 *            the date to set the end to
	 */
	public void setEndDate(Date d) {
		endDate.setDate(d);
	}

	/**
	 * Returns the radio button for flow workType
	 * 
	 * @return the workFlow as a JRadioButton
	 */
	public JRadioButton getWorkFlow() {
		return workFlow;
	}

	/**
	 * Sets the radio button for flow workType
	 * 
	 * @param b
	 *            the state of the button
	 */
	public void setWorkFlow(Boolean b) {
		workFlow.setSelected(b);
	}

	/**
	 * Returns the radio button for velocity workType
	 * 
	 * @return the workVelocity as a JRadioButton
	 */
	public JRadioButton getWorkVelocity() {
		return workVelocity;
	}

	/**
	 * Sets the radio button for velocity workType
	 * 
	 * @param b
	 *            the state of the button
	 */
	public void setWorkVelocity(Boolean b) {
		workVelocity.setSelected(b);
	}

	/**
	 * Returns the radio button for workflow distribution that gives every
	 * member the total effort
	 * 
	 * @return the totaledDist as a JRadioButton
	 */
	public JRadioButton getTotaledDist() {
		return totaledDist;
	}

	/**
	 * Sets the radio button for workflow distribution that gives every member
	 * the total effort
	 * 
	 * @param b
	 *            the state to set the button
	 */
	public void setTotaledDist(Boolean b) {
		totaledDist.setSelected(b);
	}

	/**
	 * Returns the radio button for workflow distribution that divides the work
	 * evenly among the users
	 * 
	 * @return the dividedDist as a JRadioButton
	 */
	public JRadioButton getDividedDist() {
		return dividedDist;
	}

	/**
	 * Sets the radio button for workflow distribution that divides the work
	 * evenly among the users
	 * 
	 * @param b
	 *            the state to set the button
	 */
	public void setDividedDist(Boolean b) {
		dividedDist.setSelected(b);
	}

	/**
	 * Returns the radio button that shows users combined work
	 * 
	 * @return the combined work as a JRadioButton
	 */
	public JRadioButton getCombineWork() {
		return combineWork;
	}

	/**
	 * Sets the radio button that shows users combined work
	 * 
	 * @param b
	 *            the state to set the button
	 */
	public void setCombineWork(Boolean b) {
		combineWork.setSelected(b);
	}

	/**
	 * Returns the radio button that compares users' work
	 * 
	 * @return the compared work as a JRadioButton
	 */
	public JRadioButton getCompareWork() {
		return compareWork;
	}

	/**
	 * Sets the radio button that compares users' work
	 * 
	 * @param b
	 *            the state of the button
	 */
	public void setCompareWork(Boolean b) {
		compareWork.setSelected(b);
	}

	/**
	 * Returns the check box to select all users
	 * 
	 * @return all users as a JCheckBox
	 */
	public JCheckBox getAllUsers() {
		return allUsers;
	}

	/**
	 * Sets the check box for all users
	 * 
	 * @param b
	 *            the state of the check box
	 */
	public void setAllUsers(Boolean b) {
		allUsers.setSelected(b);
	}

	/**
	 * 
	 * Return the stages component.
	 *
	 * @return the stages component.
	 */
	public JComboBox<String> getStages() {
		return stages;
	}

	/**
	 * return the JList containing the assigned user names
	 * 
	 * @return the JList of assigned usernames
	 */
	public ScrollList getCurrUsersList() {
		return this.currUsersList;
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
	 * return the JList containing the assigned user names
	 * 
	 * @return the JList of assigned usernames
	 */
	public ScrollList getUsersList() {
		return currUsersList;
	}

	@Override
	public void onLocaleChange() {
		((TitledBorder) stagePanel.getBorder()).setTitle(Localizer
				.getString("Stage"));
		fromLabel.setText(Localizer.getString("StartDate") + ":");
		toLabel.setText(Localizer.getString("EndDate") + ":");
		((TitledBorder) datePanel.getBorder()).setTitle(Localizer
				.getString("Timeframe"));
		workFlow.setText(Localizer.getString("Flow"));
		workVelocity.setText(Localizer.getString("Velocity"));
		((TitledBorder) workTypePanel.getBorder()).setTitle(Localizer
				.getString("WorkType"));
		combineWork.setText(Localizer.getString("CombineWork"));
		compareWork.setText(Localizer.getString("CompareWork"));
		((TitledBorder) workModePanel.getBorder()).setTitle(Localizer
				.getString("CompareCombine"));
		allUsers.setText(Localizer.getString("All"));
		currUsersList.setTitle(Localizer.getString("UsersReport"));
		projectUsersList.setTitle(Localizer.getString("UsersNotReport"));
		((TitledBorder) usersPanel.getBorder()).setTitle(Localizer
				.getString("Users"));
		generateGraph.setText(Localizer.getString("Generate"));
	}
}
