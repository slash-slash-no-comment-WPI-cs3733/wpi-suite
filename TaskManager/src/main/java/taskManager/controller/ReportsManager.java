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
import java.time.Instant;
import java.time.Period;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import taskManager.model.ActivityModel;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;

/**
 * Code extended from BarChartDemo1.java
 *
 * @author Joseph Blackman
 * @version Nov 29, 2014
 */
public class ReportsManager {

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

		String username;
		Instant completion;
		Double effort;

		/**
		 * Method compareTo.
		 * 
		 * @param o
		 *            comparison object
		 * @return int compare value
		 */
		@Override
		public int compareTo(UserData o) {
			return completion.compareTo(o.completion);
		}
	}

	private CategoryDataset dataset;
	private List<UserData> data;
	private final WorkflowModel workflow;

	/**
	 * Constructor for ReportsManager.
	 */
	public ReportsManager() {
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
	public void findVelocityData(Set<String> users, Instant start, Instant end,
			boolean averageCredit, StageModel stage) {
		if (!workflow.getStages().contains(stage)) {
			throw new IllegalArgumentException("Invalid stage");
		}

		// We use a linked list here to have o91) insertion. Since we only ever
		// build this dataset and then read from it in order, this is efficient.
		data = new LinkedList<UserData>();
		for (TaskModel task : stage.getTasks()) {
			Instant completed = null;
			boolean foundMoveEvent = false;
			// We are going to iterate backward through the activities, and take
			// the final MOVE event. This event must have been to the current
			// stage (it hasn't been moved after), and since we're in the final
			// stage, this MOVE event must actually be a completion event.
			for (int i = task.getActivities().size(); i >= 0; i--) {
				ActivityModel activity = task.getActivities().get(i);
				if (activity.getType() == ActivityModel.activityModelType.MOVE) {
					foundMoveEvent = true;
					completed = Instant.ofEpochMilli(activity.getDateCreated()
							.getTime());
					if (completed.compareTo(start) < 0
							|| completed.compareTo(end) > 0) {
						completed = null;
						// Pretend as if we didn't find the completion event.
					}
				}
				if (foundMoveEvent) {
					break;
				}
			}
			if (!foundMoveEvent) {
				// If the task has no move events, then it was created in the
				// completion stage. This is likely a retroactive completion, so
				// we'll assume that the task was completed on its due date.
				completed = Instant.ofEpochMilli(task.getDueDate().getTime());
			}
			if (completed != null) {
				for (String username : task.getAssigned()) {
					if (users.contains(username)) {
						Double effort = (double) task.getEstimatedEffort();
						if (averageCredit) {
							effort /= task.getAssigned().size();
						}
						data.add(new UserData(username, completed, effort));
					}
				}
			} // End if inDateRange
		} // End for TaskModel
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
	public void generateDataset(boolean teamData, Instant start, Period interval) {
		if (data == null) {
			throw new IllegalStateException(
					"Tried to generate a dataset without getting any data first!");
		}
		String intervalName = "Interval";
		if (interval.equals(Period.ofDays(1))) {
			intervalName = "Day ";
		} else if (interval.equals(Period.ofWeeks(1))) {
			intervalName = "Week ";
		} else if (interval.equals(Period.ofMonths(1))) {
			intervalName = "Month ";
		}
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Collections.sort(data); // Sort while inserting?
		int seriesNum = 0;
		final Instant boundary = start.plus(interval);
		for (UserData userData : data) {
			if (userData.completion.compareTo(boundary) > 0) {
				seriesNum++;
				boundary.plus(interval);
				continue;
			}
			if (!teamData) {
				dataset.addValue(userData.effort, userData.username,
						intervalName + seriesNum);
			} else {
				dataset.addValue(userData.effort, "Team", intervalName
						+ seriesNum);
			}
		}
	}

	// This method generates the chart from the dataset above. How it gets
	// integrated with JPanels is another question...
	/**
	 * Method createChart.
	 * 
	 * @return JFreeChart
	 */
	public JFreeChart createChart() {

		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart("Bar Chart Demo", // chart
																				// title
				"Time interval", // domain axis label
				"Effort completed", // range axis label
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

		// disable bar outlines...
		((BarRenderer) plot.getRenderer()).setDrawBarOutline(false);

		return chart;
	}
}