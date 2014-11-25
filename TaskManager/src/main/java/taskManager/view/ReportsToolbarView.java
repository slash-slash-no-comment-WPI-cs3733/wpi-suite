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
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.JXDatePicker;

import taskManager.controller.TaskInputController;
//TODO import taskManager.controller.ReportsController;

/**
 * @author Tyler Jaskoviak
 *
 */
public class ReportsToolbarView extends JPanel {

	public static final String STAGE_NAME = "stage_name";
	public static final String START_DATE = "start_date";
	public static final String END_DATE = "end_date";
	public static final String WORK_FLOW= "work_flow";
	public static final String WORK_VELOCITY = "work_velocity";
	public static final String TOTALED_DIST = "totaled_dist";
	public static final String DIVIDED_DIST = "divided_dist";
	public static final String COLLABORATIVE = "collaborative";
	public static final String COMPARATIVE = "comparative";
	public static final String ALL_USERS = "all_users";
	public static final String USERS_LIST = "users_list";
	public static final String GENERATE = "generate";
	
	private JPanel window;

	// Stage picker
	private JLabel stageTitle;
	private JComboBox<String> stages;

	// Date Picker
	private JLabel fromLabel;
	private JLabel toLabel;
	private JXDatePicker startDate;
	private JXDatePicker endDate;
	
	// Flow vs Velocity
	private JLabel workTypeLabel;
	private JRadioButton workFlow;
	private JRadioButton workVelocity;
	private ButtonGroup workTypeGroup;

	// Work Distribution
	private JLabel distributionTitle;
	private JRadioButton totaledDist;
	private JRadioButton dividedDist;
	private ButtonGroup distributionGroup;

	// Compare/Combine
	private JRadioButton combineWork;
	private JRadioButton compareWork;
	private ButtonGroup workflowGroup;

	// Users
	private JLabel usersLabel;
	private JCheckBox allUsers;
	private JList<JCheckBox> users;
	
	// Generate Graph Button
	private JButton generateGraph;

	//TODO private ReportsController controller;

	public ReportsToolbarView() {
		
		Dimension nt_panelSize = getPreferredSize();
		nt_panelSize.width = 1000;
		nt_panelSize.height = 500;
		this.setPreferredSize(nt_panelSize);
		this.setMinimumSize(nt_panelSize);
		
		window = new JPanel();
		window.setPreferredSize(nt_panelSize);
		this.setLayout(new FlowLayout());
		
		// Stage 
		stageTitle = new JLabel("Stage");
		stages = new JComboBox<String>();
		stages.setName(STAGE_NAME);
		
		fromLabel = new JLabel("Start Date:");
		toLabel = new JLabel("End Date:");
		
		// Date
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
		
		// WorkType
		workTypeLabel = new JLabel("Work Type");
		workFlow = new JRadioButton("Flow");
		workFlow.setName(WORK_FLOW);
		workVelocity = new JRadioButton("Velocity");
		workVelocity.setName(WORK_VELOCITY);
		workTypeGroup = new ButtonGroup();
		workTypeGroup.add(workFlow);
		workTypeGroup.add(workVelocity);
		
		// Distribution
		distributionTitle = new JLabel("Ditribution Method");
		totaledDist = new JRadioButton("Totaled Distribution");
		totaledDist.setName(TOTALED_DIST);
		dividedDist = new JRadioButton("Divided Distribution");
		dividedDist.setName(DIVIDED_DIST);
		distributionGroup = new ButtonGroup();
		distributionGroup.add(totaledDist);
		distributionGroup.add(dividedDist);
		
		// Combined or Compared graph
		combineWork = new JRadioButton("Combine Work");
		combineWork.setName(COLLABORATIVE);
		compareWork = new JRadioButton("Compare Work");
		compareWork.setName(COMPARATIVE);
		workflowGroup = new ButtonGroup();
		workflowGroup.add(combineWork);
		workflowGroup.add(compareWork);
		
		// Users
		usersLabel = new JLabel("Users");
		allUsers = new JCheckBox("All Users");
		allUsers.setName(ALL_USERS);
		users = new JList<JCheckBox>();
		users.setName(USERS_LIST);
		JScrollPane usersPane = new JScrollPane(users);
		usersPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		usersPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		// Generate Graph
		generateGraph = new JButton("Generate");
		generateGraph.setName(GENERATE);
		
		window.setLayout(new GridBagLayout());

		GridBagConstraints toolbarGrid = new GridBagConstraints();
		
		// One Column
		toolbarGrid.anchor = GridBagConstraints.LINE_START;

		toolbarGrid.weightx = 0.5;
		toolbarGrid.weighty = 0.5;
		toolbarGrid.gridx = 0;

		toolbarGrid.gridy = 0;
		window.add(stageTitle, toolbarGrid);
		
		toolbarGrid.gridy = 1;
		window.add(stages, toolbarGrid);
		
		toolbarGrid.gridy = 3;
		window.add(fromLabel, toolbarGrid);
		
		toolbarGrid.gridy = 4;
		window.add(startDate, toolbarGrid);
		
		toolbarGrid.gridy = 5;
		window.add(toLabel, toolbarGrid);
		
		toolbarGrid.gridy = 6;
		window.add(endDate, toolbarGrid);
		
		toolbarGrid.gridy = 8;
		window.add(workTypeLabel, toolbarGrid);

		toolbarGrid.gridy = 9;
		window.add(workFlow, toolbarGrid);
		
		toolbarGrid.gridy = 10;
		window.add(workVelocity, toolbarGrid);
		
		toolbarGrid.gridy = 12;
		window.add(distributionTitle, toolbarGrid);

		toolbarGrid.gridy = 13;
		window.add(totaledDist, toolbarGrid);
		
		toolbarGrid.gridy = 14;
		window.add(dividedDist, toolbarGrid);
		
		toolbarGrid.gridy = 16;
		window.add(combineWork, toolbarGrid);
		
		toolbarGrid.gridy = 17;
		window.add(compareWork, toolbarGrid);
		
		toolbarGrid.gridy = 19;
		window.add(usersLabel, toolbarGrid);
		
		toolbarGrid.gridy = 20;
		window.add(allUsers, toolbarGrid);
		
		toolbarGrid.gridy = 21;
		window.add(usersPane, toolbarGrid);
		
		toolbarGrid.gridy = 23;
		window.add(generateGraph, toolbarGrid);
		
		this.add(window);
	}
	
	/**
	 * Adds the action listener (controller) to this view
	 * 
	 * @param controller
	 *            the controller to be attached to this view
	 *
	public void setController(ReportsController controller) {
		//TODO this.controller = controller;
		workFlow.addActionListener(controller);
		workVelocity.addActionListener(controller);
		totaledDist.addActionListener(controller);
		dividedDist.addActionListener(controller);
		combineWork.addActionListener(controller);
		compareWork.addActionListener(controller);
		allUsers.addActionListener(controller);
		
		//TODO help with action listeners for all users
	}*/
	
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
	public JComboBox<String> getStage(){
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
	public JXDatePicker getStartDate(){
		return startDate;
	}
	
	/**
	 * Sets the start date of the graph
	 * 
	 * @param d
	 * 				the date to set the start to
	 */
	public void setStartDate(Date d){
		startDate.setDate(d);
	}
	
	/**
	 * Returns the end date of the graph
	 * 
	 * @return the end date as a JXDatePicker
	 */
	public JXDatePicker getEndDate(){
		return endDate;
	}
	
	/**
	 * Sets the end date of the graph
	 * 
	 * @param d
	 * 				the date to set the end to
	 */
	public void setEndDate(Date d){
		endDate.setDate(d);
	}
	
	/**
	 * Returns the radio button for flow workType
	 * 
	 * @return the workFlow as a JRadioButton
	 */
	public JRadioButton getWorkFlow(){
		return workFlow;
	}
	
	/**
	 * Sets the radio button for flow workType
	 * 
	 * @param b
	 * 				the state of the button
	 */
	public void setWorkFlow(Boolean b){
		workFlow.setSelected(b);
	}
	
	/**
	 * Returns the radio button for velocity workType
	 * 
	 * @return the workVelocity as a JRadioButton
	 */
	public JRadioButton getWorkVelocity(){
		return workVelocity;
	}
	
	/**
	 * Sets the radio button for velocity workType
	 * 
	 * @param b
	 * 				the state of the button
	 */
	public void setWorkVelocity(Boolean b){
		workVelocity.setSelected(b);
	}
	
	/**
	 * Returns the radio button for workflow distribution that
	 * gives every member the total effort
	 * 
	 * @return the totaledDist as a JRadioButton
	 */
	public JRadioButton getTotaledDist(){
		return totaledDist;
	}
	
	/**
	 * Sets the radio button for workflow distribution that
	 * gives every member the total effort
	 * 
	 * @param b
	 * 				the state to set the button
	 */
	public void setTotaledDist(Boolean b){
		totaledDist.setSelected(b);
	}
	
	/**
	 * Returns the radio button for workflow distribution that
	 * divides the work evenly among the users
	 * 
	 * @return the dividedDist as a JRadioButton
	 */
	public JRadioButton getDividedDist(){
		return dividedDist;
	}
	
	/**
	 * Sets the radio button for workflow distribution that
	 * divides the work evenly among the users
	 * 
	 * @param b
	 * 				the state to set the button
	 */
	public void setDividedDist(Boolean b){
		dividedDist.setSelected(b);
	}
	
	/**
	 * Returns the radio button that shows users combined work
	 * 
	 * @return the combined work as a JRadioButton
	 */
	public JRadioButton getCombineWork(){
		return combineWork;
	}
	
	/**
	 * Sets the radio button that shows users combined work
	 * 
	 * @param b
	 * 				the state to set the button
	 */
	public void setCombineWork(Boolean b){
		combineWork.setSelected(b);
	}
	
	/**
	 * Returns the radio button that compares users' work
	 * 
	 * @return the compared work as a JRadioButton
	 */
	public JRadioButton getCompareWork(){
		return compareWork;
	}
	
	/**
	 * Sets the radio button that compares users' work
	 * 
	 * @param b
	 * 				the state of the button
	 */
	public void setCompareWork(Boolean b){
		compareWork.setSelected(b);
	}
	
	/**
	 * Returns the check box to select all users
	 * 
	 * @return all users as a JCheckBox
	 */
	public JCheckBox getAllUsers(){
		return allUsers;
	}
	
	/**
	 * Sets the check box for all users
	 * 
	 * @param b
	 * 				the state of the check box
	 */
	public void setAllUsers(Boolean b){
		allUsers.setSelected(b);
	}
	
	/**
	 * Returns the list of all of the users
	 * 
	 * @return the list of users as a JList<JCheckBox>
	 */
	public JList<JCheckBox> getUsers(){
		return users;
	}
	
	/**
	 * Sets the users of the JList
	 * 
	 * @param u
	 * 				the checkbox of a user to add
	 */
	public void addUsers(JCheckBox u){
		users.add(u);
	}
	
	/**
	 * Sets the state of a particular user in the users list
	 * 
	 * @param i
	 * 				index of the user
	 * @param b
	 * 				state to set the user
	 */
	public void setUser(int i, Boolean b){
		users.getModel().getElementAt(i).setSelected(b);
	}
}
