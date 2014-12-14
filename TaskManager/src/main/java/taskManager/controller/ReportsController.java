/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.TableOrder;

import taskManager.TaskManager;
import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.ActivityModelType;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ReportsView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Code extended from BarChartDemo1.java at www.jfree.org/jfreechart
 *
 * @author Joseph Blackman
 * @author Clark Jacobsohn
 * @version Dec 12, 2014
 */
public class ReportsController implements ActionListener, ChangeListener,
		ListSelectionListener, PropertyChangeListener {

	private ReportsView rtv;

	private enum DistributionType {
		USER, STAGE;
	}

	/**
	 * An internal data structure for building the bar chart. Sortable and
	 * small.
	 *
	 * @author Joseph Blackman
	 */
	public class ReportDatum implements Comparable<ReportDatum> {
		private ReportDatum(String category, ZonedDateTime timeStamp,
				Double effort) {
			this.category = category;
			this.timeStamp = timeStamp;
			this.effort = effort;
		}

		private String category;
		private ZonedDateTime timeStamp;
		private Double effort;

		/**
		 * Method compareTo.
		 *
		 * @param o
		 *            comparison object
		 * 
		 * @return int compare value
		 */
		@Override
		public int compareTo(ReportDatum o) {
			return timeStamp.compareTo(o.timeStamp);
		}
	}

	private ZonedDateTime start;
	private ZonedDateTime end;
	private DefaultCategoryDataset dataset;
	// private List<UserData> data;
	private final WorkflowModel workflow;

	/**
	 * Constructor for ReportsManager.
	 */
	public ReportsController(ReportsView rtv) {
		workflow = WorkflowModel.getInstance();
		this.rtv = rtv;
		// Populate the fields.
		reloadStages();
		reloadUsers();
		// Default to last stage.
		rtv.setStageDropdown(workflow.getStages().size() - 1);
	}

	/**
	 * Constructor for ReportsManager.
	 */
	public ReportsController(ZonedDateTime start, ZonedDateTime end) {
		this.start = start;
		this.end = end;
		workflow = WorkflowModel.getInstance();
	}

	/**
	 * See findVelocityData() below.
	 *
	 * @param users
	 * @param start
	 * @param end
	 * @param averageCredit
	 * @return The list of ReportDatum that matches the given criteria
	 */
	public List<ReportDatum> findVelocityData(Set<String> users,
			ZonedDateTime start, ZonedDateTime end, boolean averageCredit) {
		// Assume the completion stage is the final stage
		this.start = start;
		this.end = end;
		final List<StageModel> stageList = workflow.getStages();
		final StageModel finalStage = stageList.get(stageList.size() - 1);
		return findVelocityData(users, start, end, averageCredit, finalStage);
	}

	/**
	 * Return data to allow the calculation of "Velocity", i.e. amount of effort
	 * completed/time. Will get data as a list of data points, unsorted. The
	 * user should call generateVelocityDataset() next.
	 *
	 * @param users
	 *            The set of usernames to get data about
	 * @param start
	 *            The time before which we do not care about
	 * @param end
	 *            The time after which we do not care about
	 * @param averageCredit
	 *            If true, average credit for the task across all assigned
	 *            users.
	 *
	 * @param stage
	 *            The stage to consider as the completion stage
	 * 
	 * @return The list of ReportDatum that matches the given criteria
	 */
	public List<ReportDatum> findVelocityData(Set<String> users,
			ZonedDateTime _start, ZonedDateTime _end, boolean averageCredit,
			StageModel stage) {

		start = _start;
		end = _end;

		if (!workflow.getStages().contains(stage)) {
			throw new IllegalArgumentException("Invalid stage");
		}
		List<ReportDatum> data = new ArrayList<ReportDatum>();
		// Iterate through all tasks for the specified stage.
		for (TaskModel task : stage.getTasks()) {
			ZonedDateTime completed = null;
			boolean foundMoveEvent = false;
			// We are going to iterate backward through the activities, and take
			// the final MOVE event. This event must have been to the current
			// stage (it hasn't been moved after), and since we're in the final
			// stage, this MOVE event must actually be a completion event.
			for (int i = task.getActivities().size() - 1; i >= 0; i--) {
				ActivityModel activity = task.getActivities().get(i);
				if (activity.getType() == ActivityModelType.MOVE) {
					foundMoveEvent = true;

					completed = ZonedDateTime.ofInstant(activity
							.getDateCreated().toInstant(), TimeZone
							.getDefault().toZoneId());
				}
				if (foundMoveEvent) {
					break;
				}
			}
			if (!foundMoveEvent) {
				// If the task has no move events, then it was created in the
				// completion stage. This is likely a retroactive completion, so
				// we'll assume that the task was completed on its due date.
				completed = ZonedDateTime.ofInstant(task.getDueDate()
						.toInstant(), TimeZone.getDefault().toZoneId());
			}

			// If the completion date is retrieved and the date is between the
			// specified dates.
			if (completed != null
					&& completed.toInstant().isAfter(start.toInstant())
					&& completed.toInstant().isBefore(end.toInstant())) {
				// Iterate through all of the users assigned to the current
				// task, if the user is one of the specified users, get the
				// actual effort value from that task and add to the dataset.
				for (String username : task.getAssigned()) {
					if (users.contains(username)) {
						Double effort = (double) task.getActualEffort();
						if (averageCredit) {
							effort /= task.getAssigned().size();
						}
						data.add(new ReportDatum(username, completed, effort));
					}
				}
			} // End if (inDateRange)
		} // End if (TaskModel)

		return data;
	}

	/**
	 * This method will format the data into separate categories, such as days,
	 * weeks, or months. It merges them down into a dataset object, which it
	 * saves. The user should then call createChart().
	 *
	 * @param teamData
	 *            Whether or not the data should be grouped as a team.
	 * @param start
	 *            The starting instant for the data
	 * @param interval
	 *            The interval to group the data by.
	 */
	public void generateVelocityDataset(List<ReportDatum> data,
			boolean teamData, Period interval) {
		if (data == null) {
			throw new IllegalStateException(
					"Tried to generate a dataset without getting any data first!");
		}
		dataset = new DefaultCategoryDataset();
		String intervalName = "Interval";

		// Set the label name according to the specified interval.
		if (interval.equals(Period.ofDays(1))) {
			intervalName = "Day ";
		} else if (interval.equals(Period.ofWeeks(1))) {
			intervalName = "Week ";
		} else if (interval.equals(Period.ofMonths(1))) {
			intervalName = "Month ";
		}
		int seriesNum = 0;
		// We get this user to add some 0s to, rather than creating a "" user.
		String dummyUsername = "User";
		if (!data.isEmpty()) {
			dummyUsername = data.iterator().next().category;
		}
		// Populate the buckets with names before-hand.
		for (ZonedDateTime i = start; i.compareTo(end) < 0; i = i
				.plus(interval)) {
			dataset.addValue(0, dummyUsername, intervalName + (seriesNum + 1));
			seriesNum++;
		}

		// Iterate through each userdata.
		for (ReportDatum userData : data) {

			// Boundary is the temporary enddate to use to calculate the total
			// effort for the current completion date (e.g. if completion date
			// is January 1, boundary is January 2).
			ZonedDateTime boundary = ZonedDateTime.ofInstant(start.toInstant()
					.plus(interval), TimeZone.getDefault().toZoneId());

			// seriesnum is only used for the labels, e.g. the int part of Day
			// 1, Day 2, ...
			seriesNum = 0;

			// continue until we find the seriesNum and boundary.
			// while the difference is less than a full day.
			// TODO: change the while condition to be relative to the interval
			// rather than just 86400 seconds for 1 day).
			do {
				boundary = boundary.plus(interval); // increment by interval
													// (day/week/month).
				seriesNum++;
			} while ((boundary.toInstant().getEpochSecond() - userData.timeStamp
					.toInstant().getEpochSecond()) < 86400);

			// set the name depending on the teamData value.
			String keyname = "Team";
			if (!teamData) {
				keyname = userData.category;
			}

			// If the dataset contains the keyname, increment the effort value.
			// Else, add the value as a new object in the dataset.
			if (dataset.getRowKeys().contains(keyname)) {
				dataset.incrementValue(userData.effort, keyname, intervalName
						+ (seriesNum));
			} else {
				dataset.addValue(userData.effort, keyname, intervalName
						+ (seriesNum));
			}
		}
	}

	/**
	 * Return data to allow the calculation of "Distribution", i.e. amount of
	 * tasks/effort per stage/user. Will get data as a list of data points,
	 * unsorted. The user should call generateDistributionDataSet() next.
	 * 
	 * @param type
	 *            The type of distribution to generate data for, either STAGE or
	 *            USER
	 * @param users
	 *            The set of users used to select tasks. Only data for tasks
	 *            with the selected users assigned will be calculated. If the
	 *            distribution type is USER, multiple data points will be made
	 *            for tasks with more than one matching assigned user.
	 * @param stages
	 *            The set of stages used to select tasks. Only data for tasks
	 *            from the selected stages will be calculated.
	 * @param _start
	 *            The start time to use as a boundary for data. This date will
	 *            be compared with the tasks' due date.
	 * @param _end
	 *            The end time to use as a boundary for data. This date will be
	 *            compared with the tasks' due date.
	 * @return The list of ReportDatum that matches the given criteria
	 */
	public List<ReportDatum> findDistributionData(DistributionType type,
			Set<String> users, Set<StageModel> stages, ZonedDateTime _start,
			ZonedDateTime _end) {
		List<ReportDatum> data = new ArrayList<ReportDatum>();

		start = _start;
		end = _end;

		// Iterate through the given stages
		for (StageModel stage : stages) {
			// Iterate through the tasks
			for (TaskModel task : stage.getTasks()) {
				// If we're making a stage distribution, check that at least one
				// of the set of users we're looking at is assigned to it.
				if (type == DistributionType.STAGE) {
					boolean taskAdded = false;
					for (String user : task.getAssigned()) {
						if (!taskAdded && users.contains(user)) {
							// Use the due date for the timestamp
							ZonedDateTime dueDate = ZonedDateTime.ofInstant(
									task.getDueDate().toInstant(), TimeZone
											.getDefault().toZoneId());
							// Check if it is in the time span
							if (dueDate.compareTo(start) >= 0
									&& dueDate.compareTo(end) <= 0) {
								data.add(new ReportDatum(stage.getName(),
										dueDate, (double) task
												.getActualEffort()));
							}
						}
					}
				}
				// If we're making a user distribution, then add a data point
				// for each user assigned to the task if they're part of the set
				// we're looking at.
				else if (type == DistributionType.USER) {
					for (String user : task.getAssigned()) {
						if (users.contains(user)) {
							// Use the due date for the timestamp
							ZonedDateTime dueDate = ZonedDateTime.ofInstant(
									task.getDueDate().toInstant(), TimeZone
											.getDefault().toZoneId());
							// Check if it is in the time span
							if (dueDate.compareTo(start) >= 0
									&& dueDate.compareTo(end) <= 0) {
								data.add(new ReportDatum(user, dueDate,
										(double) task.getActualEffort()));
							}
						}
					}
				}
			}
		}

		return data;
	}

	/**
	 * This method will format the data into separate categories, such as days,
	 * weeks, or months. It merges them down into a dataset object, which it
	 * saves. The user should then call createBarChart() or createPieChart().
	 *
	 * @param data
	 *            The list of ReportDatum with which to construct the dataset
	 * @param useEffort
	 *            Whether or not to use actual effort for value. True will use
	 *            effort to calculate quantity, false will use number of tasks
	 *            to calculate quantity.
	 * 
	 */
	public void generateDistributionDataset(List<ReportDatum> data,
			boolean useEffort) {
		if (data == null) {
			throw new IllegalStateException(
					"Tried to generate a dataset without getting any data first!");
		}
		dataset = new DefaultCategoryDataset();

		// Iterate through each userdata.
		for (ReportDatum datum : data) {
			if (useEffort) {
				dataset.addValue(datum.effort, "Team", datum.category);
			} else {
				dataset.addValue(1, "Team", datum.category);
			}
		}
	}

	/**
	 * Creates a bar chart from the given input
	 *
	 * @param title
	 *            The title of the chart
	 * @param xlabel
	 *            The label for the x axis
	 * @param ylabel
	 *            The label for the y axis
	 * 
	 * @return a JPanel containing the bar chart
	 */
	public JPanel createBarChart(String title, String xlabel, String ylabel) {
		if (dataset == null) {
			throw new IllegalStateException(
					"Tried to generate a chart without creating a dataset first!");
		}

		final JFreeChart chart = ChartFactory.createBarChart(title, // chart
																	// title
				xlabel, // domain axis label
				ylabel, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		final ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setMaximumDrawHeight(2880);
		chartPanel.setMaximumDrawWidth(5120);

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();

		// set the range axis to display integers only...
		((NumberAxis) plot.getRangeAxis()).setStandardTickUnits(NumberAxis
				.createIntegerTickUnits());

		// put bars next to each other
		plot.getDomainAxis().setCategoryMargin(0);

		// disable bar outlines...
		((BarRenderer) plot.getRenderer()).setDrawBarOutline(false);

		// disable gradients
		((BarRenderer) plot.getRenderer())
				.setBarPainter(new StandardBarPainter());

		return chartPanel;
	}

	/**
	 * Creates a line graph from the given input
	 *
	 * @param title
	 *            The title of the graph
	 * @param xlabel
	 *            The label for the x axis
	 * @param ylabel
	 *            The label for the y axis
	 * 
	 * @return a JPanel containing the graph
	 */
	public JPanel createLineGraph(String title, String xlabel, String ylabel) {
		if (dataset == null) {
			throw new IllegalStateException(
					"Tried to generate a chart without creating a dataset first!");
		}

		final JFreeChart chart = ChartFactory.createLineChart(title, // chart
																		// title
				xlabel, // domain axis label
				ylabel, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		final ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setMaximumDrawHeight(2880);
		chartPanel.setMaximumDrawWidth(5120);

		return chartPanel;
	}

	/**
	 * Creates a pie chart from the given input
	 *
	 * @param title
	 *            The title of the chart
	 * 
	 * @return a JPanel containing the chart
	 */
	public JPanel createPieChart(String title) {
		if (dataset == null) {
			throw new IllegalStateException(
					"Tried to generate a chart without creating a dataset first!");
		}

		final JFreeChart chart = ChartFactory.createPieChart(title, // chart
																	// title
				new CategoryToPieDataset(dataset, TableOrder.BY_COLUMN, 0), // data
				true, // include legend
				true, // tooltips
				false // urls
				);

		final ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setMaximumDrawHeight(2880);
		chartPanel.setMaximumDrawWidth(5120);

		return chartPanel;
	}

	/**
	 * 
	 * Update the stages dropdown for the view.
	 *
	 */
	private void reloadStages() {
		JComboBox<String> stages = rtv.getStages();
		stages.removeAllItems();
		for (StageModel stage : WorkflowModel.getInstance().getStages()) {
			stages.addItem(stage.getName());
		}
		// Select the 1st item if the old selected item doesn't exist
		stages.setSelectedItem(0);
	}

	/**
	 * 
	 * Update the users view.
	 *
	 */
	private void reloadUsers() {
		ArrayList<String> projectUserNames = new ArrayList<String>();
		for (User u : TaskManager.users) {
			String name = u.getUsername();
			if (!projectUserNames.contains(name)) {
				projectUserNames.add(name);
			}
		}
		// Default to have all users selected.
		rtv.getCurrUsersList().addAllToList(projectUserNames);
	}

	/**
	 * 
	 * copied from edittaskcontroller.
	 * 
	 * adds selected usernames to the assigned users list and removes them from
	 * the project user list.
	 */
	public void addUsersToList() {
		int[] toAdd = rtv.getProjectUsersList().getSelectedIndices();
		ArrayList<String> namesToAdd = new ArrayList<String>();

		for (int ind : toAdd) {
			namesToAdd.add(rtv.getProjectUsersList().getValueAtIndex(ind));
		}

		for (String n1 : namesToAdd) {
			rtv.getProjectUsersList().removeFromList(n1);
			if (!rtv.getUsersList().contains(n1)) {
				// add the new username to the assigned user list
				rtv.getUsersList().addToList(n1);
				rtv.getProjectUsersList().removeFromList(n1);
			}
		}
	}

	/**
	 * 
	 * copied from edittaskcontroller.
	 * 
	 * removes selected usernames from the assigned users list and adds them to
	 * the project user list. marks selected usernames to be removed from the
	 * model
	 */
	public void removeUsersFromList() {
		// grab all of the indices of the usernames selected in
		// assigned users and grab the associated strings
		int[] usersToRemove = rtv.getUsersList().getSelectedIndices();
		ArrayList<String> namesToRemove = new ArrayList<String>();
		for (int ind : usersToRemove) {
			namesToRemove.add(rtv.getUsersList().getValueAtIndex(ind));
		}

		// for every name that is selected, remove it from assigned
		// users and add it to project users
		for (String n2 : namesToRemove) {
			rtv.getUsersList().removeFromList(n2);
			if (!rtv.getProjectUsersList().contains(n2)) {
				rtv.getProjectUsersList().addToList(n2);
				rtv.getUsersList().removeFromList(n2);
			}
		}
	}

	/**
	 * Creates a velocity report and makes it visible.
	 */
	private void createVelocityReport() {
		// Get the starting/ending dates from the view.
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getDefault());

		Date startdate = rtv.getStartDate().getDate();
		cal.setTime(startdate);
		startdate = cal.getTime();

		Date enddate = rtv.getEndDate().getDate();
		cal.setTime(enddate);
		cal.add(Calendar.DATE, 1); // Set end date to +1 assuming user
									// will choose same start/end date
									// for reports for that
									// day/month/year
		enddate = cal.getTime();

		// TODO: Do validation for the calendars rather than showing
		// JOptionPanes.
		Date now = new Date();
		if (startdate.after(now)) {
			JOptionPane.showMessageDialog(rtv,
					"Start Date cannot be in the future.");
			return;
		}
		if (enddate.before(startdate)) {
			JOptionPane.showMessageDialog(rtv,
					"End Date must be after the Start Date.");
			return;
		}

		// Get all of the selected users from the view and store to a
		// set.
		Set<String> users = new HashSet<String>();
		for (String u : rtv.getCurrUsersList().getAllValues()) {
			users.add(u);
		}

		// Get the selected stage from the view and store as a
		// StageModel.
		String stageStr = rtv.getSelectedStage();
		StageModel stage = WorkflowModel.getInstance()
				.findStageByName(stageStr);

		// Set the dates as ZonedDateTimes that have timezone
		// information.
		ZonedDateTime startZone = ZonedDateTime.ofInstant(
				startdate.toInstant(), TimeZone.getDefault().toZoneId());
		ZonedDateTime endZone = ZonedDateTime.ofInstant(enddate.toInstant(),
				TimeZone.getDefault().toZoneId());

		// Compute velocity (amount of effort/time) and store to data,
		// unsorted.
		List<ReportDatum> data = findVelocityData(users, startZone, endZone,
				false, stage);

		// Generate the dataset required to draw the graph, with a given
		// interval.
		generateVelocityDataset(data, false, Period.ofDays(1));

		// Create the chart with the Title, Label names.
		JPanel chart = createBarChart("Effort per Day", "Time", "Effort");

		// Open a new tab with the given chart.
		TabPaneController.getInstance().addTab("Graph", chart, true);
		TabPaneController.getInstance().getView().setSelectedComponent(chart);
	}

	/**
	 * Creates a distribution report and makes it visible.
	 */
	private void createDistributionReport() {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String buttonName = ((JButton) button).getName();

			// Add user to Users Selected for Reports
			if (buttonName.equals(ReportsView.ADD_USER)) {
				addUsersToList();
				// Remove user
			} else if (buttonName.equals(ReportsView.REMOVE_USER)) {
				removeUsersFromList();
				// All users checkbox checked
			} else if (buttonName.equals(ReportsView.ALL_USERS)) {
				// TODO: Add functionality for adding all users to selected.
				// Generate Reports pressed.
			} else {
				if (rtv.getMode() == ReportsView.Mode.VELOCITY) {
					createVelocityReport();
				} else if (rtv.getMode() == ReportsView.Mode.DISTRIBUTION) {
					createDistributionReport();
				}
			}

		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO: Add all users to assigned when All is checked.
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// Used for selecting/unselecting users in the view.
		Boolean addUsersSelected = !rtv.getProjectUsersList()
				.isSelectionEmpty();
		Boolean removeUsersSelected = !rtv.getUsersList().isSelectionEmpty();
		rtv.setAddUserEnabled(addUsersSelected);
		rtv.setRemoveUserEnabled(removeUsersSelected);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}
}