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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.jfree.ui.ApplicationFrame;

import taskManager.model.ActivityModel;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;

// Code extended from BarChartDemo1.java
public class ReportsManager extends ApplicationFrame {

	private class UserData {
		Date date;
		Double effort;
	}

	private final WorkflowModel workflow;

	private static final long serialVersionUID = 5324733789017141522L;

	public ReportsManager(String title) {
		super(title);
		final CategoryDataset dataset = createDataSet();
		final JFreeChart chart = createChart(dataset);
		workflow = WorkflowModel.getInstance();
		final ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);
	}

	private Map<String, List<UserData>> getVelocityData(Set<String> users,
			Date start, Date end, boolean averageCredit) {
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
	public Map<String, List<UserData>> getVelocity(Set<String> users,
			Date start, Date end, boolean averageCredit, StageModel stage) {
		if (!workflow.getStages().contains(stage)) {
			throw new IllegalArgumentException("Invalid stage");
		}

		final Map<String, List<UserData>> data = new HashMap<String, List<UserData>>();
		for (String username : users) {
			data.put(username, new ArrayList<UserData>());
		}
		for (TaskModel task : stage.getTasks()) {
			Date completed = null;
			// We are going to iterate backward through the activities, and take
			// the final MOVE event. This event must have been to the current
			// stage (it hasn't been moved after), and since we're in the final
			// stage, this MOVE event must actually be a completion event.
			for (int i = task.getActivities().size(); i >= 0; i--) {
				ActivityModel activity = task.getActivities().get(i);
				if (activity.getType() == ActivityModel.activityModelType.MOVE) {
					completed = activity.getDateCreated();
					if (completed.compareTo(start) < 0
							|| completed.compareTo(end) > 0) {
						completed = null; // Pretend as if we didn't find the
											// completion event.
					}
					break;
				}
			}
			if (completed != null) {
				for (String username : task.getAssigned()) {
					if (users.contains(username)) {
						List<UserData> userData = data.get(username);
						UserData dataPoint = new UserData();
						dataPoint.date = completed;
						dataPoint.effort = (double) task.getEstimatedEffort();
						if (averageCredit) {
							dataPoint.effort /= task.getAssigned().size();
						}
						userData.add(dataPoint);
						data.put(username, userData);
					}
				}
			} // End if inDateRange
		} // End for TaskModel
		return data;
	}

	// This method will format the data into separate categories, e.g. weeks.
	// Category names and exact ranges should be calculated here.
	private static CategoryDataset createDataSet() {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		final String series = "";
		final String[] categories = new String[] { "", "" };
		dataset.addValue(1.5, series, categories[0]);
		return dataset;
	}

	// This method generates the chart from the dataset above. How it gets
	// integrated with JPanels is another question...
	private static JFreeChart createChart(CategoryDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart("Bar Chart Demo", // chart
																				// title
				"Category", // domain axis label
				"Value", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();

		// set the range axis to display integers only...
		((NumberAxis) plot.getRangeAxis()).setStandardTickUnits(NumberAxis
				.createIntegerTickUnits());

		// disable bar outlines...
		((BarRenderer) plot.getRenderer()).setDrawBarOutline(false);

		return chart;
	}
}