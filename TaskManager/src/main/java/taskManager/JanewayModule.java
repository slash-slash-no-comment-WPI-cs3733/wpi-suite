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
import javax.swing.JPanel;

import taskManager.controller.EditTaskController;
import taskManager.controller.ManageStageController;
import taskManager.controller.ToolbarController;
import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.ManageStageView;
import taskManager.view.ManageUsersView;
import taskManager.view.ToolbarView;
import taskManager.view.WorkflowView;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

/**
 * The JanewayModule for the task manager
 *
 * @author Beth Martino
 * @author Samee Swartz
 * @author Stefan Alexander
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

		// TODO change JPanels to ManageUsersView, NewTaskView, StatisticsView
		// TODO move setVisible(false) into view constructors?

		// create workflow view
		WorkflowView wfv = new WorkflowView();
		wfv.setVisible(true);

		// create manage stages view
		ManageStageView msv = new ManageStageView();
		msv.setVisible(false);

		// ManageUsers window
		ManageUsersView muv = new ManageUsersView();
		muv.setVisible(false);

		// create new task view
		EditTaskView ntv = new EditTaskView();
		ntv.setVisible(false);

		// create statistics view
		JPanel sv = new JPanel();
		sv.setVisible(false);

		// create a new workflow model
		final WorkflowModel wfm = new WorkflowModel();

		// give it the default stages
		StageModel newStage = new StageModel(wfm, "New", false);
		StageModel startedStage = new StageModel(wfm, "Started", false);
		StageModel progressStage = new StageModel(wfm, "In Progress", false);
		StageModel completeStage = new StageModel(wfm, "Complete", false);

		// create and add sample tasks to the workflow model.
		TaskModel task1 = new TaskModel("Some task", newStage);
		// an example of setting a due date to the task.
		Calendar sampleDate = Calendar.getInstance();
		// note: the 11 here is equivalent to December since months in a
		// Calendar object are between 0-11.
		sampleDate.set(2014, 11, 25);
		// TODO put all of the dates back in
		// task1.setDueDate(sampleDate.getTime());
		task1.setEstimatedEffort(3);
		task1.setDueDate("01/14/14");
		TaskModel task2 = new TaskModel("Working on this task", startedStage);
		// task2.setDueDate(Calendar.getInstance().getTime());
		task2.setEstimatedEffort(1);
		task2.setDueDate("01/14/14");
		TaskModel task3 = new TaskModel("Plz review", progressStage);
		// task3.setDueDate(Calendar.getInstance().getTime());
		task3.setEstimatedEffort(5);
		task3.setDueDate("01/14/14");
		TaskModel task4 = new TaskModel("blah", progressStage);
		// task4.setDueDate(Calendar.getInstance().getTime());
		task4.setEstimatedEffort(2);
		task4.setDueDate("01/14/14");
		TaskModel task5 = new TaskModel("This is merged", completeStage);
		// task5.setDueDate(Calendar.getInstance().getTime());
		task5.setEstimatedEffort(2);
		task5.setDueDate("01/14/14");

		for (int i = 0; i < 10; ++i) {
			TaskModel tsk = new TaskModel("test " + i, progressStage);
			// tsk.setDueDate(Calendar.getInstance().getTime());
			tsk.setDueDate("01/14/15");
			tsk.setEstimatedEffort(1);
		}

		// create the controller for the view
		wfv.setController(new WorkflowController(wfv, wfm));
		msv.setController(new ManageStageController(msv, wfm));
		ntv.setController(new EditTaskController(ntv, wfm, wfv));

		// adds all views to one panel
		JPanel allPanels = new JPanel();
		allPanels.add(wfv);
		allPanels.add(msv);
		allPanels.add(muv);
		allPanels.add(ntv);
		allPanels.add(sv);

		// Create the toolbar view
		ToolbarView tv = new ToolbarView();
		ToolbarController tc = new ToolbarController(tv, wfv, msv, muv, ntv, sv);
		tv.setController(tc);

		// this adds the menu and the main panel to the pre-configured janeway
		// module view.
		// It uses the spring layout
		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Task Manager",

		new ImageIcon(), tv, allPanels);
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
