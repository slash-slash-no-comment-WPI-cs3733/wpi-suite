/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.Period;
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

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import taskManager.controller.ReportsController;
import taskManager.controller.ReportsController.DistributionType;
import taskManager.controller.TaskInputController;

//TODO import taskManager.controller.ReportsController;

/**
 * @author Tyler Jaskoviak
 * @author Thane Hunt
 */
public class ReportsView extends JPanel implements ActionListener {

	public static final String STAGE_NAME = "stage_name";
	public static final String STAGE_NAME2 = "stage_name2";
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
	public static final String USERS = "Users";
	public static final String NUMBER_OF_TASKS = "number_of_tasks";

	public String names[] = { "Work Velocity", "Task Distribution",
			"Distribution Stages" };
	public String slices[] = { "Days", "Weeks", "Months" };

	private JPanel window;
	private JPanel generator;

	public enum Mode {
		VELOCITY, DISTRIBUTION
	}

	private Mode mode;

	// Variable to Insert Images
	Image img;
	// Effort or number of tasks panel
	private JPanel effortOrNumberofTasks;

	// Stage picker
	private JPanel stagePanel;
	private JComboBox<String> stages;
	private JPanel stagePanel2;
	private JComboBox<String> stages2;
	private JComboBox<String> timeSliceList;

	JPanel TaskDistribution = new JPanel();
	JPanel WorkVelocity = new JPanel(new MigLayout());
	JPanel usersHolder = new JPanel();
	JPanel usersHolderDistro = new JPanel();
	JPanel SelectStages = new JPanel();
	// Card-changing panel
	JPanel cards;
	JRadioButton workvel = new JRadioButton(names[0]);
	JRadioButton taskdistro = new JRadioButton(names[1]);
	JRadioButton effort = new JRadioButton("Effort");
	JRadioButton numberoftasks = new JRadioButton("Number of Tasks");
	JCheckBox select_stages = new JCheckBox("All Stages");

	// Date Picker
	private JPanel timePanel;
	private JPanel datePanel;
	private JPanel timeSlice;
	private JLabel timeSliceLabel;
	private JLabel startDateLabel;
	private JLabel endDateLabel;
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

	// Set mode

	// Generate Graph Button
	private JButton generateGraph;

	private ReportsController controller;

	public ReportsView(Mode mode) {

		this.mode = mode;

		window = new JPanel(new MigLayout());
		generator = new JPanel(new MigLayout());
		this.setLayout(new FlowLayout());

		// Stages or Users
		JPanel SelectStages = new JPanel(new MigLayout());

		// Report Type Pane

		JPanel reportType = new JPanel(new MigLayout());
		JLabel reportTypeLabel = new JLabel("Choose report type");
		workvel.addActionListener(this);
		workvel.setSelected(true);
		taskdistro.addActionListener(this);
		ButtonGroup reportTypeButtons = new ButtonGroup();
		reportTypeButtons.add(workvel);
		reportTypeButtons.add(taskdistro);
		effort.addActionListener(this);
		numberoftasks.addActionListener(this);

		ButtonGroup effortOrTasksButtons = new ButtonGroup();
		effortOrTasksButtons.add(effort);
		effortOrTasksButtons.add(numberoftasks);
		select_stages.addActionListener(this);
		effortOrNumberofTasks = new JPanel(new MigLayout());
		reportType.add(reportTypeLabel, "wrap");
		reportType.add(workvel);
		reportType.add(taskdistro);
		effortOrNumberofTasks.add(effort);
		effortOrNumberofTasks.add(numberoftasks);

		JPanel WorkVelocity = new JPanel(new MigLayout());
		JPanel TaskDistribution = new JPanel(new MigLayout());

		// Create two differnent stage views for each view
		stagePanel = new JPanel();
		stagePanel2 = new JPanel();
		stagePanel.setLayout(new MigLayout());
		stagePanel2.setLayout(new MigLayout());
		stages = new JComboBox<String>();
		stages.setName(STAGE_NAME);

		stages2 = new JComboBox<String>();
		stages2.setName(STAGE_NAME2);

		timeSliceList = new JComboBox<String>(slices);
		timeSliceList.setPrototypeDisplayValue("Time slices");
		stagePanel.add(new JLabel("Stage"), "align left");
		stagePanel.add(stages, "align left");
		stagePanel2.add(stages2);

		startDateLabel = new JLabel("Start Date:");
		endDateLabel = new JLabel("End Date:");

		// Date
		datePanel = new JPanel(new MigLayout());
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

		datePanel.add(startDateLabel, "gapbottom 13px");
		datePanel.add(startDate, "wrap, gapbottom 13px");
		datePanel.add(endDateLabel);
		datePanel.add(endDate);

		timeSliceLabel = new JLabel("Units");

		// Time
		timePanel = new JPanel(new MigLayout());
		timeSlice = new JPanel(new MigLayout());
		timeSlice.add(timeSliceLabel, "gapright 5px");
		timeSlice.add(timeSliceList);
		JPanel sliceAndStage = new JPanel(new MigLayout());
		sliceAndStage.add(timeSlice, "wrap");
		sliceAndStage.add(stagePanel, "gapright 20px");

		timePanel.add(sliceAndStage, "h 100%, w 50%");
		timePanel.add(datePanel, "h 100%, w 50%");

		// WorkType
		workTypePanel = new JPanel();
		workTypePanel.setLayout(new MigLayout());
		workFlow = new JRadioButton("Flow");
		workFlow.setName(WORK_FLOW);
		workVelocity = new JRadioButton("Velocity");
		workVelocity.setName(WORK_VELOCITY);
		workTypeGroup = new ButtonGroup();
		workTypeGroup.add(workFlow);
		workTypeGroup.add(workVelocity);
		workTypePanel.setBorder(BorderFactory.createTitledBorder("WorkType"));
		workTypePanel.add(workFlow);
		workTypePanel.add(workVelocity);

		// Combined or Compared graph
		workModePanel = new JPanel();
		workModePanel.setLayout(new MigLayout());
		combineWork = new JRadioButton("Combine Work");
		combineWork.setName(COLLABORATIVE);
		compareWork = new JRadioButton("Compare Work");
		compareWork.setName(COMPARATIVE);
		workflowGroup = new ButtonGroup();
		workflowGroup.add(combineWork);
		workflowGroup.add(compareWork);
		workModePanel.setBorder(BorderFactory
				.createTitledBorder("Compare/Combine"));

		workModePanel.add(compareWork);

		// Users for Work Velocity
		usersPanel = new JPanel();
		allUsers = new JCheckBox("Add all Users to report");
		allUsers.setName(ALL_USERS);
		currUsersList = new ScrollList("Users Included in Report");
		currUsersList.setBackground(this.getBackground());
		projectUsersList = new ScrollList("Users Not Included");
		projectUsersList.setBackground(this.getBackground());
		// Add user to list
		addUser = new JButton(">>");
		addUser.setName(ADD_USER);
		this.setAddUserEnabled(false);
		// remove user from list
		removeUser = new JButton("<<");
		removeUser.setName(REMOVE_USER);
		this.setRemoveUserEnabled(false);
		JPanel usersListPanel = new JPanel(new MigLayout());
		JPanel projectUsersListPanel = new JPanel(new MigLayout());
		JPanel addRemoveButtons = new JPanel(new MigLayout());
		usersListPanel.add(currUsersList);
		projectUsersListPanel.add(projectUsersList);
		addRemoveButtons.add(addUser, "wrap");
		addRemoveButtons.add(removeUser);

		usersPanel.add(projectUsersListPanel, "w 100!, gapleft 15px");
		usersPanel.add(addRemoveButtons);
		usersPanel.add(usersListPanel, "w 100!");

		// Generate Graph
		generateGraph = new JButton("Generate");
		generateGraph.setName(GENERATE);

		try {
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"reports-icon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		generateGraph.setIcon(new ImageIcon(img));

		// One Column
		// Panel for reports generating options
		JPanel reportOptions = new JPanel(new MigLayout());

		SelectStages.add(new JLabel("Select Stage"), "wrap");
		SelectStages.add(stagePanel2, "wrap");
		SelectStages.add(select_stages, "gaptop 15px");

		JPanel Distro = new JPanel(new MigLayout());

		Distro.add(SelectStages);
		TaskDistribution.add(Distro, "gapleft 70px");
		WorkVelocity.add(allUsers, "wrap, gapleft 20px");
		WorkVelocity.add(usersPanel, "wrap, w 100%");
		WorkVelocity.add(timePanel, "w 100%");

		cards = new JPanel(new CardLayout());
		cards.add(WorkVelocity, names[0]);
		cards.add(TaskDistribution, names[1]);
		generator.add(reportType, "align center, wrap");
		generator.add(cards, "wrap");
		generator.add(effortOrNumberofTasks, "wrap");
		generator.add(generateGraph, "dock south, gapbottom 20px");
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, names[0]);
		window.add(generator, "dock west");
		this.add(window);
	}

	public void actionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout) (cards.getLayout());

		if (e.getSource() == workvel) {
			cl.show(cards, names[0]);
			mode = Mode.VELOCITY;

		}
		if (e.getSource() == taskdistro) {

			cl.show(cards, names[1]);
			mode = Mode.DISTRIBUTION;

		}

	}

	public void setController(ReportsController manager) {
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
	 * Adds the action listener (controller) to this view
	 * 
	 * @param controller
	 *            the controller to be attached to this view
	 */
	public void setFieldController2(TaskInputController controller) {
		stages2.addPopupMenuListener(controller);
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
	 * gets the dropdown box in the view that contains all the stage names
	 * 
	 * @return the stages2 dropdown box
	 */
	public JComboBox<String> getStages2() {
		return stages2;
	}

	/**
	 * set stage2 dropdown box to the stage for graphing
	 * 
	 * @param n
	 *            the index of the stage in the workflow
	 */
	public void setStageDropdown2(int n) {
		String p = stages2.getItemAt(n);
		stages2.setSelectedItem(p);
	}

	/**
	 * Returns the selected stage2 name. If the selected item cannot be
	 * retrieved returns an empty string.
	 *
	 * @return the selected stage as a String.
	 */
	public String getSelectedStage2() {
		if (stages2.getSelectedItem() != null) {
			return stages2.getSelectedItem().toString();
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

	/**
	 * Returns the current report mode.
	 * 
	 * @return the current report mode
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * Sets the report mode.
	 * 
	 * @param mode
	 *            The desired mode
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public DistributionType getDistributionType() {
		if (select_stages.isSelected()) {
			return DistributionType.STAGE;
		} else {
			return DistributionType.USER;
		}
	}

	public Period getTimeUnit() {
		switch ((String) timeSliceList.getSelectedItem()) {
		case "Days":
			return Period.ofDays(1);
		case "Weeks":
			return Period.ofWeeks(1);
		case "Months":
			return Period.ofMonths(1);
		default:
			return Period.ofDays(1);
		}
	}

	public boolean getUseEffort() {
		return effort.isSelected();
	}

	public boolean getUseAllStages() {
		return select_stages.isSelected();
	}
}
