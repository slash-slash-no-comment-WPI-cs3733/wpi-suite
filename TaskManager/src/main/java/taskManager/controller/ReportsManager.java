/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.jfree.data.category.DefaultCategoryDataset;

import taskManager.JanewayModule;
import taskManager.model.ActivityModel;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ReportsView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Code extended from BarChartDemo1.java at www.jfree.org/jfreechart
 *
 * @author Joseph Blackman
 * @version Nov 29, 2014
 */
public class ReportsManager implements ActionListener, ChangeListener,
		ListSelectionListener, PropertyChangeListener {

	private ReportsView rtv;

	/**
	 * An internal data structure for building the bar chart. Sortable and
	 * small.
	 *
	 * @author Joseph Blackman
	 */
	private class UserData implements Comparable<UserData> {
		private UserData(String username, Instant completion, Double effort) {
			this.username = username;
			this.completion = completion;
			this.effort = effort;
		}

		private String username;
		private Instant completion;
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
		public int compareTo(UserData o) {
			return completion.compareTo(o.completion);
		}
	}

	private Instant start;
	private Instant end;
	private DefaultCategoryDataset dataset;
	private List<UserData> data;
	private final WorkflowModel workflow;

	/**
	 * Constructor for ReportsManager.
	 */
	public ReportsManager(ReportsView rtv) {
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
	public ReportsManager(Instant start, Instant end) {
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
	 */
	public void findVelocityData(Set<String> users, Instant start, Instant end,
			boolean averageCredit) {
		// Assume the completion stage is the final stage
		this.start = start;
		this.end = end;
		final List<StageModel> stageList = workflow.getStages();
		final StageModel finalStage = stageList.get(stageList.size() - 1);
		findVelocityData(users, start, end, averageCredit, finalStage);
	}

	/**
	 * Return data to allow the calculation of "Velocity", i.e. amount of effort
	 * completed/time. Will get data as a list of data points, unsorted. The
	 * user should call generateDataSet() next.
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
	 */
	public void findVelocityData(Set<String> users, Instant _start,
			Instant _end, boolean averageCredit, StageModel stage) {
		// adjust for EST.
		start = _start.minusSeconds(18000);
		end = _end.minusSeconds(18000);

		if (!workflow.getStages().contains(stage)) {
			throw new IllegalArgumentException("Invalid stage");
		}
		data = new ArrayList<UserData>();
		for (TaskModel task : stage.getTasks()) {
			Instant completed = null;
			boolean foundMoveEvent = false;
			// We are going to iterate backward through the activities, and take
			// the final MOVE event. This event must have been to the current
			// stage (it hasn't been moved after), and since we're in the final
			// stage, this MOVE event must actually be a completion event.
			for (int i = task.getActivities().size() - 1; i >= 0; i--) {
				ActivityModel activity = task.getActivities().get(i);
				if (activity.getType() == ActivityModel.activityModelType.MOVE) {
					foundMoveEvent = true;

					completed = Instant.ofEpochMilli(setToEST(
							activity.getDateCreated()).getTime());
				}
				if (foundMoveEvent) {
					break;
				}
			}
			if (!foundMoveEvent) {
				// If the task has no move events, then it was created in the
				// completion stage. This is likely a retroactive completion, so
				// we'll assume that the task was completed on its due date.
				completed = Instant.ofEpochMilli(setToEST(task.getDueDate())
						.getTime());
			}
			if (completed != null && completed.isAfter(start)
					&& completed.isBefore(end)) {
				for (String username : task.getAssigned()) {
					if (users.contains(username)) {
						Double effort = (double) task.getActualEffort();
						if (averageCredit) {
							effort /= task.getAssigned().size();
						}
						data.add(new UserData(username, completed, effort));
						System.out.println("name: " + username + " completed: "
								+ completed + " effort: " + effort);
					}
				}
			} // End if (inDateRange)
		} // End if (TaskModel)
	}

	/**
	 * 
	 * Convert the given date to EST assuming we are 5 hours behind.
	 *
	 * @param original
	 *            the original Date object.
	 * @return the converted Date object.
	 */
	public Date setToEST(Date original) {
		Calendar cal = Calendar.getInstance();
		Date created = original;
		cal.setTime(created);
		cal.add(Calendar.HOUR_OF_DAY, -5);
		return cal.getTime();
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
	public void generateDataset(boolean teamData, Period interval) {
		if (data == null) {
			throw new IllegalStateException(
					"Tried to generate a dataset without getting any data first!");
		}
		dataset = new DefaultCategoryDataset();
		String intervalName = "Interval";
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
			dummyUsername = data.iterator().next().username;
		}
		// Populate the buckets with names before-hand.
		for (Instant i = start; i.compareTo(end) < 0; i = i.plus(interval)) {
			dataset.addValue(0, dummyUsername, intervalName + (seriesNum + 1));
			seriesNum++;
		}

		System.out.println("Data size: " + data.size());

		// Iterate through each userdata.
		for (UserData userData : data) {
			Instant boundary = start.plus(interval);
			seriesNum = 0;

			// continue until we find the seriesNum and boundary.
			do {
				boundary = boundary.plus(interval);
				seriesNum++;
			} while ((boundary.getEpochSecond() - userData.completion
					.getEpochSecond()) < 86400); // while the difference is less
													// than a full day.

			// set the name depending on the teamData value.
			String keyname = "Team";
			if (!teamData) {
				keyname = userData.username;
			}

			// If the dataset contains the keyname, increment the effort value.
			// Else, add the value as a new object in the dataset.
			if (dataset.getRowKeys().contains(keyname)) {
				dataset.incrementValue(userData.effort, keyname, intervalName
						+ (seriesNum));
				System.out.println("Increment: +" + userData.effort + " name: "
						+ keyname + " interval: " + seriesNum);
				System.out.println("Boundary: " + boundary);
			} else {
				dataset.addValue(userData.effort, keyname, intervalName
						+ (seriesNum));
				System.out.println("Add: +" + userData.effort + " name: "
						+ keyname + " interval: " + seriesNum);
				System.out.println("Boundary: " + boundary);
			}
		}
		if (dataset == null) {
			System.out.println(dataset);
		}
	}

	/**
	 * Creates a chart from the given input
	 *
	 * @param title
	 *            The title of the chart
	 * @param xlabel
	 *            The label for the x axis
	 * @param ylabel
	 *            The label for the y axis
	 * 
	 * @return a JPanel containing the chart
	 */
	public JPanel createChart(String title, String xlabel, String ylabel) {
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
		chartPanel.setPreferredSize(new Dimension(500, 270));

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();

		// set the range axis to display integers only...
		((NumberAxis) plot.getRangeAxis()).setStandardTickUnits(NumberAxis
				.createIntegerTickUnits());

		// put bars next to each other
		plot.getDomainAxis().setCategoryMargin(0);

		// disable bar outlines...
		((BarRenderer) plot.getRenderer()).setDrawBarOutline(false);

		return new ChartPanel(chart, false);
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
		for (User u : JanewayModule.users) {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String buttonName = ((JButton) button).getName();
			if (buttonName.equals(ReportsView.ADD_USER)) {
				addUsersToList();
			} else if (buttonName.equals(ReportsView.REMOVE_USER)) {
				removeUsersFromList();
			} else if (buttonName.equals(ReportsView.ALL_USERS)) {

			} else {
				Date startdate = rtv.getStartDate().getDate();
				Date enddate = rtv.getEndDate().getDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(enddate);
				cal.add(Calendar.DATE, 1);

				// TODO: Do validation for the calendars rather than showing
				// JOptionPanes.
				Date now = new Date();
				if (startdate.after(now)) {
					JOptionPane.showMessageDialog(rtv,
							"Start Date cannot be in the future.");
					return;
				}
				if (cal.getTime().before(startdate)) {
					JOptionPane.showMessageDialog(rtv,
							"End Date must be after the Start Date.");
					return;
				}

				Instant startCal = Instant.ofEpochMilli(startdate.getTime());
				Instant endCal = Instant.ofEpochMilli(cal.getTime().getTime());

				Set<String> users = new HashSet<String>();
				for (String u : rtv.getCurrUsersList().getAllValues()) {
					users.add(u);
				}

				String stageStr = rtv.getSelectedStage();
				StageModel stage = WorkflowModel.getInstance().findStageByName(
						stageStr);
				findVelocityData(users, startCal, endCal, false, stage);
				generateDataset(false, Period.ofDays(1));
				JPanel chart = createChart("Effort per Day", "Time", "Effort");
				TabPaneController.getInstance().addTab("Graph", chart, true);
				TabPaneController.getInstance().getView()
						.setSelectedComponent(chart);
			}

		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO: Add all users to assigned when All is checked.
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
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
