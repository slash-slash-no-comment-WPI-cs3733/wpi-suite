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
import java.util.Date;
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

// Code extended from BarChartDemo1.java
public class ReportsManager {

	final long HOUR = 3600000;
	final long DAY = HOUR * 24;
	final long WEEK = DAY * 7;

	private class UserData {
		private UserData(String username, Date date, Double effort) {
			this.username = username;
			this.date = date;
			this.effort = effort;
		}

		String username;
		Date date;
		Double effort;
	}

	private CategoryDataset dataset;
	private final WorkflowModel workflow;

	public ReportsManager() {
		workflow = WorkflowModel.getInstance();
	}

	private List<UserData> getVelocity(Set<String> users, Date start, Date end,
			boolean averageCredit) {
		// Assume the completion stage is the final stage
		final List<StageModel> stageList = workflow.getStages();
		final StageModel finalStage = stageList.get(stageList.size() - 1);
		return getVelocity(users, start, end, averageCredit, finalStage);
	}

	/**
	 * Return data to allow the calculation of "Velocity", i.e. amount of effort
	 * completed/time. Will return data in a nested map, the outer one is a map
	 * of users, the inner one is a map of completed task times to effort
	 *
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
	 * @return HashMap<Username : TreeMap<Task completion date : Effort >>
	 */
	public List<UserData> getVelocity(Set<String> users, Date start, Date end,
			boolean averageCredit, StageModel stage) {
		if (!workflow.getStages().contains(stage)) {
			throw new IllegalArgumentException("Invalid stage");
		}

		// We use a linked list here to have o91) insertion. Since we only ever
		// build this dataset and then read from it in order, this is efficient.
		final List<UserData> data = new LinkedList<UserData>();
		for (TaskModel task : stage.getTasks()) {
			Date completed = null;
			boolean foundMoveEvent = false;
			// We are going to iterate backward through the activities, and take
			// the final MOVE event. This event must have been to the current
			// stage (it hasn't been moved after), and since we're in the final
			// stage, this MOVE event must actually be a completion event.
			for (int i = task.getActivities().size(); i >= 0; i--) {
				ActivityModel activity = task.getActivities().get(i);
				if (activity.getType() == ActivityModel.activityModelType.MOVE) {
					foundMoveEvent = true;
					completed = activity.getDateCreated();
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
				completed = task.getDueDate();
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
		return data;
	}

	// This method will format the data into separate categories, e.g. weeks.
	// Category names and exact ranges should be calculated here.
	public void getDataSet(Set<String> users, Date start, Date end,
			boolean teamData, Date interval) {
		final long startTime = start.getTime();
		final long endTime = end.getTime();
		final long intervalTime = interval.getTime();
		String categories[] = new String[(int) Math
				.ceil((double) (endTime - startTime) / intervalTime)];
		String intervalName = "Interval";
		if (intervalTime == HOUR) {
			intervalName = "Hour";
		} else if (intervalTime == DAY) {
			intervalName = "Day";
		} else if (intervalTime == WEEK) {
			intervalName = "Week";
		}
		for (int i = 0; i < categories.length; i++) {
			categories[i] = intervalName + " " + (i + 1);
		}
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<UserData> data = getVelocity(users, start, end, false);
		for (UserData userData : data) {
			int category = (int) ((userData.date.getTime() - startTime) / intervalTime);
			if (!teamData) {
				dataset.addValue(userData.effort, userData.username,
						categories[category]);
			} else {
				dataset.addValue(userData.effort, "Team", categories[category]);
			}
		}
		this.dataset = dataset;
	}

	// This method generates the chart from the dataset above. How it gets
	// integrated with JPanels is another question...
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