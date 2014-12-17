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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import taskManager.controller.ReportsController;
import taskManager.controller.ReportsController.DistributionType;
import taskManager.controller.TaskInputController;
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;

/**
 * @author Tyler Jaskoviak
 * @author Thane Hunt
 */
public class ReportsView extends JPanel implements ActionListener,
		LocaleChangeListener {

	private static final long serialVersionUID = 9104742041224722960L;

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

	public String names[] = { Localizer.getString("Velocity"),
			Localizer.getString("Distribution") };
	public String slices[] = { Localizer.getString("Days"),
			Localizer.getString("Weeks") };

	private JPanel window;
	private JPanel generator;

	public enum Mode {
		VELOCITY, DISTRIBUTION
	}

	// create new Font
	private Font bigFont = new Font("Default", Font.BOLD, 14);

	private Mode mode;

	private JLabel reportTypeLabel;

	// Variable to Insert Images
	private Image img;
	// Effort or number of tasks panel
	private JPanel effortOrNumberofTasks;

	// Stage picker
	private JPanel stagePanel;
	private JLabel stagePanelLabel;
	private JComboBox<String> stages;
	private JPanel stagePanel2;
	private JComboBox<String> stages2;
	private JComboBox<String> timeSliceList;

	private final JPanel graph;
	JPanel options;
	private JSplitPane splitPane;

	private JPanel TaskDistribution = new JPanel();
	private JPanel WorkVelocity = new JPanel(new MigLayout());
	private JPanel usersHolder = new JPanel();
	private JPanel usersHolderDistro = new JPanel();
	private JPanel SelectStages = new JPanel();

	// Card-changing panel
	private JPanel cards;
	private JRadioButton workvel = new JRadioButton(names[0]);
	private JRadioButton taskdistro = new JRadioButton(names[1]);
	private JRadioButton effort = new JRadioButton("Effort");
	private JRadioButton numberoftasks = new JRadioButton("Number of Tasks");
	private JCheckBox select_stages = new JCheckBox("All Stages");

	// Date Picker
	private JPanel timePanel;
	private JPanel datePanel;
	private JPanel timeSlice;
	private JLabel timeSliceLabel;
	private JLabel startDateLabel;
	private JLabel endDateLabel;
	private JXDatePicker startDate;
	private JXDatePicker endDate;

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

	public ReportsView(Mode mode) {

		this.mode = mode;

		// Contains the splitPane and button panel
		this.setLayout(new MigLayout("wrap 1, align center", "0[grow, fill]0",
				"0[grow, fill][]0"));

		graph = new JPanel();
		JPanel generateButton = new JPanel(new FlowLayout());
		generateButton.setOpaque(false);
		options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		window = new JPanel(new FlowLayout());
		window.setMinimumSize(new Dimension(160, 325));
		generator = new JPanel(new MigLayout());
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Stages or Users
		JPanel SelectStages = new JPanel(new MigLayout());

		// Report Type Pane
		JPanel reportType = new JPanel(new MigLayout());
		reportTypeLabel = new JLabel(Localizer.getString("ChooseReportType"));
		reportTypeLabel.setFont(bigFont);
		workvel.addActionListener(this);
		workvel.setSelected(true);
		taskdistro.addActionListener(this);
		ButtonGroup reportTypeButtons = new ButtonGroup();
		reportTypeButtons.add(workvel);
		reportTypeButtons.add(taskdistro);
		effort.addActionListener(this);
		effort.setOpaque(false);
		numberoftasks.addActionListener(this);
		numberoftasks.setOpaque(false);
		effort.setSelected(true);

		ButtonGroup effortOrTasksButtons = new ButtonGroup();
		effortOrTasksButtons.add(effort);
		effortOrTasksButtons.add(numberoftasks);
		select_stages.addActionListener(this);
		select_stages.setFont(bigFont);
		effortOrNumberofTasks = new JPanel(new MigLayout());
		reportType.add(reportTypeLabel, "align center, wrap");
		reportType.add(workvel);
		reportType.add(taskdistro);
		effortOrNumberofTasks.add(effort);
		effortOrNumberofTasks.add(numberoftasks);
		effortOrNumberofTasks.setOpaque(false);

		JPanel WorkVelocity = new JPanel(new MigLayout());
		JPanel TaskDistribution = new JPanel(new MigLayout());

		// Create two different stage views for each view
		stagePanel = new JPanel();
		stagePanel2 = new JPanel();
		stagePanel.setLayout(new MigLayout());
		stagePanel2.setLayout(new MigLayout());
		stages = new JComboBox<String>();
		stages.setName(STAGE_NAME);

		stages2 = new JComboBox<String>();
		stages2.setName(STAGE_NAME2);

		timeSliceList = new JComboBox<String>(slices);
		timeSliceList.setPrototypeDisplayValue(Localizer.getString("Units"));
		stagePanelLabel = new JLabel(Localizer.getString("Stage"));
		stagePanel.add(stagePanelLabel, "align left");
		stagePanel.add(stages, "align left");
		stagePanel2.add(stages2);

		startDateLabel = new JLabel(Localizer.getString("StartDate"));
		endDateLabel = new JLabel(Localizer.getString("EndDate"));

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

		datePanel.add(startDateLabel, "gapbottom 14px, gaptop 4px");
		datePanel.add(startDate, "wrap, gapbottom 14px, gaptop 4px");
		datePanel.add(endDateLabel);
		datePanel.add(endDate);

		timeSliceLabel = new JLabel(Localizer.getString("Units"));

		// Time
		timePanel = new JPanel(new MigLayout());
		timeSlice = new JPanel(new MigLayout());
		timeSlice.add(timeSliceLabel);
		timeSlice.add(timeSliceList);
		JPanel sliceAndStage = new JPanel(new MigLayout());
		sliceAndStage.add(timeSlice, "wrap");
		sliceAndStage.add(stagePanel, "gapright 20px");

		timePanel.add(sliceAndStage, "h 100%, w 50%");
		timePanel.add(datePanel, "h 100%, w 50%");

		// Users for Work Velocity
		usersPanel = new JPanel();
		allUsers = new JCheckBox(Localizer.getString("AllUsers"));
		allUsers.setFont(bigFont);
		allUsers.setName(ALL_USERS);
		currUsersList = new ScrollList("");
		currUsersList.setBackground(this.getBackground());
		currUsersList.setScrollListSize(new Dimension(100, 120));
		projectUsersList = new ScrollList("");
		projectUsersList.setBackground(this.getBackground());
		projectUsersList.setScrollListSize(new Dimension(100, 120));
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
		generateGraph = new JButton(Localizer.getString("Generate"));
		generateGraph.setName(GENERATE);

		try {
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"reports-icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		generateGraph.setIcon(new ImageIcon(img));

		// Panel for reports generating options
		JLabel selectStageLabel = new JLabel("Select Stage");
		selectStageLabel.setFont(bigFont);
		SelectStages.add(selectStageLabel, "wrap");
		SelectStages.add(stagePanel2, "wrap, gapright 10px");
		SelectStages.add(select_stages, "gaptop 15px, gapleft 5px");

		JPanel Distro = new JPanel(new MigLayout());

		Distro.add(SelectStages, "align center");
		TaskDistribution.add(Distro, "gapleft 140");
		WorkVelocity.add(allUsers, "wrap, gapleft 20px");
		WorkVelocity.add(usersPanel, "wrap, w 100%");
		WorkVelocity.add(timePanel, "w 100%");

		cards = new JPanel(new CardLayout());
		cards.add(WorkVelocity, names[0]);
		cards.add(TaskDistribution, names[1]);
		generator.add(reportType, "align center, wrap");
		generator.add(cards, "wrap");
		CardLayout cl = (CardLayout) (cards.getLayout());

		Dimension CardSize = cards.getSize();
		cards.setPreferredSize(CardSize);

		cl.show(cards, names[0]);
		window.add(generator);
		JScrollPane windowScroll = new JScrollPane(window,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		windowScroll.getVerticalScrollBar().setUnitIncrement(12);
		windowScroll.getHorizontalScrollBar().setUnitIncrement(12);

		// adds the scroll and the generate button
		generateButton.add(effortOrNumberofTasks);
		generateButton.add(generateGraph);
		options.add(windowScroll);
		options.add(generateButton);
		options.setBackground(Colors.STAGE);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, options,
				graph);
		splitPane.setDividerLocation(500);
		splitPane.setDividerSize(10);

		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(.5);
		this.add(splitPane);

		Localizer.addListener(this);
		onLocaleChange();
	}

	/**
	 * sets the panel containing the graph
	 */
	public void setGraphPanel(JPanel graph) {
		this.removeAll();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, options,
				graph);
		this.add(splitPane);
		this.repaint();
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

		onLocaleChange();
	}

	public void setController(ReportsController controller) {
		addUser.addActionListener(controller);
		removeUser.addActionListener(controller);
		allUsers.addChangeListener(controller);
		currUsersList.setController(controller);
		projectUsersList.setController(controller);
		generateGraph.addActionListener(controller);
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

	/**
	 * Returns the selected distribution type
	 * 
	 * @return The selected distribution type
	 */
	public DistributionType getDistributionType() {
		if (select_stages.isSelected()) {
			return DistributionType.STAGE;
		} else {
			return DistributionType.USER;
		}
	}

	/**
	 * Returns the selected time unit
	 * 
	 * @return The selected time unit
	 */
	public Period getTimeUnit() {
		String selected = (String) timeSliceList.getSelectedItem();
		if (selected.equals(Localizer.getString("Days"))) {
			return Period.ofDays(1);
		} else if (selected.equals(Localizer.getString("Weeks"))) {
			return Period.ofWeeks(1);
		} else {
			return Period.ofDays(1);
		}
	}

	/**
	 * Returns whether to use effort or number of tasks for calculation
	 * 
	 * @return whether to use effort or number of tasks for calculation
	 */
	public boolean getUseEffort() {
		return effort.isSelected();
	}

	/**
	 * Returns whether to calculate for all stages or just one
	 * 
	 * @return whether to calculate for all stages or just one
	 */
	public boolean getUseAllStages() {
		return select_stages.isSelected();
	}

	@Override
	public void onLocaleChange() {
		allUsers.setText(Localizer.getString("AllUsers"));
		currUsersList.setTitle(Localizer.getString("UsersReport"));
		projectUsersList.setTitle(Localizer.getString("UsersNotReport"));
		generateGraph.setText(Localizer.getString("Generate"));
		names[0] = Localizer.getString("Velocity");
		names[1] = Localizer.getString("Distribution");
		slices[0] = Localizer.getString("Days");
		slices[1] = Localizer.getString("Weeks");
		timeSliceLabel.setText(Localizer.getString("Units"));
		stagePanelLabel.setText(Localizer.getString("Stage"));
		timeSliceList.setPrototypeDisplayValue(Localizer.getString("Units"));
		startDateLabel.setText(Localizer.getString("StartDate"));
		endDateLabel.setText(Localizer.getString("EndDate"));
		reportTypeLabel.setText(Localizer.getString("ChooseReportType"));
	}
}
