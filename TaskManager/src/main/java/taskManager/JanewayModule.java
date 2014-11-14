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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import taskManager.controller.EditTaskController;
import taskManager.controller.ManageStageController;
import taskManager.controller.ToolbarController;
import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
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
	private final List<JanewayTabModel> tabs;
	public static final WorkflowView wfv = new WorkflowView();
	public static final ManageStageView msv = new ManageStageView();
	public static final ManageUsersView muv = new ManageUsersView();
	public static final EditTaskView etv = new EditTaskView();
	public static final JPanel sv = new JPanel();

	/**
	 * Construct a blank tab
	 */
	public JanewayModule() {
		// creates the workflow view

		// TODO change JPanels to ManageUsersView, NewTaskView, StatisticsView
		// TODO move setVisible(false) into view constructors?

		// create workflow view
		wfv.setVisible(true);

		// create manage stages view
		msv.setVisible(false);

		// ManageUsers window
		muv.setVisible(false);

		// create new task view
		etv.setVisible(false);

		// create statistics view
		sv.setVisible(false);

		// create a new workflow model
		final WorkflowModel wfm = new WorkflowModel();

		// give it the default stages
		StageModel newStage = new StageModel(wfm, "New", false);
		StageModel startedStage = new StageModel(wfm, "Scheduled", false);
		StageModel progressStage = new StageModel(wfm, "In Progress", false);
		StageModel completeStage = new StageModel(wfm, "Complete", false);

		// create the controller for the view
		final WorkflowController wfc = new WorkflowController(wfv, wfm);
		wfv.setController(wfc);
		msv.setController(new ManageStageController(msv, wfm));
		etv.setController(new EditTaskController(wfm));

		// adds all views to one panel
		JPanel allPanels = new JPanel();
		allPanels.add(wfv);
		allPanels.add(msv);
		allPanels.add(muv);
		allPanels.add(etv);
		allPanels.add(sv);

		// Create the toolbar view
		ToolbarView tv = new ToolbarView();
		ToolbarController tc = new ToolbarController(tv, wfv, msv, muv, etv,
				sv, wfc);
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
