/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.ImageIcon;

import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ToolbarView;
import taskManager.view.WorkflowView;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

/**
 * The JanewayModule for the task manager
 *
 * @author Beth Martino
 * @author Samee Swartz
 * @version November 9, 2014
 */
public class JanewayModule implements IJanewayModule {

	// The tabs used by this module
	private final ArrayList<JanewayTabModel> tabs;

	/**
	 * Construct a blank tab
	 */
	public JanewayModule() {
		// creates the workflow view
		final WorkflowView wfv = new WorkflowView();
		// create a new workflow model
		final WorkflowModel wfm = new WorkflowModel();
		// give it the default stages
		new StageModel(wfm, "Backlog", false);
		new StageModel(wfm, "In Progress", false);
		new StageModel(wfm, "Review", false);
		new StageModel(wfm, "To Merge", false);
		new StageModel(wfm, "Merged", false);

		// create and add sample tasks to the workflow model.
		TaskModel task1 = new TaskModel("Some task", "Backlog", wfm);
		// an example of setting a due date to the task.
		Calendar sampleDate = Calendar.getInstance();
		// note: the 11 here is equivalent to December since months in a
		// Calendar object are between 0-11.
		sampleDate.set(2014, 11, 25);
		task1.setDueDate(sampleDate.getTime());
		task1.setEstimatedEffort(3);
		TaskModel task2 = new TaskModel("Working on this task", "In Progress",
				wfm);
		task2.setDueDate(Calendar.getInstance().getTime());
		task2.setEstimatedEffort(1);
		TaskModel task3 = new TaskModel("Plz review", "Review", wfm);
		task3.setDueDate(Calendar.getInstance().getTime());
		task3.setEstimatedEffort(5);
		TaskModel task4 = new TaskModel("blah", "Review", wfm);
		task4.setDueDate(Calendar.getInstance().getTime());
		task4.setEstimatedEffort(2);
		TaskModel task5 = new TaskModel("This is merged", "Merged", wfm);
		task5.setDueDate(Calendar.getInstance().getTime());
		task5.setEstimatedEffort(2);

		// create the controller for the view
		wfv.setController(new WorkflowController(wfv, wfm));

		// Create the toolbar view
		ToolbarView tv = new ToolbarView();
		// toolbarPanel.setController(ToolbarController);

		// this adds the menu and the main panel to the pre-configured janeway
		// module view.
		// It uses the spring layout
		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Task Manager",
				new ImageIcon(), tv, wfv);

		tabs.add(tab);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Task Manager";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}
}
