/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.controller;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.EditTaskView.Mode;
import taskManager.view.ManageStageView;
import taskManager.view.ManageUsersView;
import taskManager.view.TabPaneView;
import taskManager.view.TabView;
import taskManager.view.WorkflowView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * This is the singleton controller for the singleton TabPaneView.
 *
 * @author Samee Swartz
 * @author Clark Jacobsohn
 * @version Nov 17, 2014
 */
public class TabPaneController {

	// singleton TabPaneView
	private TabPaneView tabPaneV;
	// booleans to make sure there is only one tab for manageStages and
	// manageUsers out at a time
	boolean manageStagesTabOpen = false;
	boolean manageUsersTabOpen = false;

	public TabPaneController(TabPaneView tabPaneV) {
		this.tabPaneV = tabPaneV;
	}

	/**
	 * 
	 * Creates and adds a new tab to create a task.
	 *
	 */
	public void addCreateTaskTab() {

		// Each press of create a new tab should launch a new createTaskTab
		EditTaskView etv = new EditTaskView(Mode.CREATE);
		etv.setController(new EditTaskController(etv));
		etv.setFieldController(new TaskInputController(etv));
		// Set the dropdown menu to New stage and disable the menu.
		StageModel newStage = WorkflowModel.getInstance()
				.findStageByName("New");
		List<StageModel> stages = WorkflowModel.getInstance().getStages();
		for (int i = 0; i < stages.size(); i++) {
			if (stages.get(i) == newStage) {
				etv.setStageDropdown(i);
				break;
			}
		}

		etv.getActEffort().setEnabled(false);
		etv.setStageSelectorEnabled(false);
		etv.setRefreshEnabled(false);
		// Disable save button when creating a task.
		etv.disableSave();

		addTab("Create Task", etv, true);
		// Focuses on the new tab
		int index = this.tabPaneV.getTabCount() - 1;
		this.tabPaneV.setSelectedIndex(index);

		// Set actual effort field enabled only if the selected stage is
		// "Complete"
		if (etv.getSelectedStage().equals("Complete")) {
			etv.getActEffort().setEnabled(true);
		} else {
			etv.getActEffort().setEnabled(false);
		}

		// Clear all activities, reset fields.
		etv.clearActivities();
		etv.resetFields();

		// fills the user lists
		ArrayList<String> projectUserNames = new ArrayList<String>();
		for (User u : JanewayModule.users) {
			String name = u.getUsername();
			if (!projectUserNames.contains(name)) {
				projectUserNames.add(name);
			}
		}
		etv.getProjectUsersList().addAllToList(projectUserNames);
	}

	/**
	 * 
	 * Creates and adds a new tab to edit a task. If the edit tab is already out
	 * for the given task if focuses on that tab instead of creating a new one
	 *
	 * @param etv
	 */
	public void addEditTaskTab(EditTaskView etv) {
		boolean exists = false;
		EditTaskView etv2 = null;
		for (Component c : tabPaneV.getComponents()) {
			if (c instanceof EditTaskView) {
				etv2 = (EditTaskView) c;
				if (etv2.getTitle().getName().equals(etv.getTitle().getName())) {
					exists = true;
					break;
				}
			}
		}
		if (exists) {
			tabPaneV.setSelectedComponent(etv2);
		} else {
			addTab(etv.getTitle().getText(), etv, true);
			tabPaneV.setSelectedComponent(etv);
		}
	}

	/**
	 * 
	 * Creates a new manageStages tab if there is not one out or focuses on one
	 * that is already out
	 *
	 */
	public void addManageStagesTab() {
		// There should only be one tab for manageStages up at a time
		if (!manageStagesTabOpen) {
			ManageStageView view = new ManageStageView();
			ManageStageController msc = new ManageStageController(view);
			view.setController(msc);
			addTab("Manage Stages", view, true);
			manageStagesTabOpen = true;
		}
		// Focuses on the new or old manageStagesTab
		tabPaneV.setSelectedIndex(tabPaneV.indexOfTab("Manage Stages"));
	}

	/**
	 * 
	 * Creates a new manageUsers tab if there is not one out or focuses on one
	 * that is already out
	 *
	 */
	public void addManageUsersTab() {
		// There should only be one tab for manageUsers up at a time
		if (!manageUsersTabOpen) {
			ManageUsersView muv = new ManageUsersView();
			// ManageUsersController muc = new
			// ManageUsersController(tabPaneV.getWorkflowModel(), muv);
			addTab("Manage Users", muv, true);
			manageUsersTabOpen = true;
		}
		// Focuses on the new or old manageStagesTab
		tabPaneV.setSelectedIndex(tabPaneV.indexOfTab("Manage Users"));
	}

	/**
	 * 
	 * Adds a new tab with the given information
	 *
	 * @param title
	 *            The title to show up on the tab
	 * @param component
	 *            The Component to be displayed when the tab is clicked on
	 * @param closeable
	 *            Whether or not a tab can be closed. True adds an 'x' button to
	 *            the tab
	 */
	public void addTab(String title, Component component, boolean closeable) {
		tabPaneV.addTab(title, component);
		tabPaneV.setTabComponentAt(tabPaneV.indexOfComponent(component),
				new TabView(title, component, closeable));
	}

	/**
	 * 
	 * Removes a tab based on the the instance of the component being displayed.
	 *
	 * @param component
	 *            Instance of the component being displayed
	 */
	public void removeTabByComponent(Component component) {
		// resets the manageStagesTab boolean so a new manageStages can be
		// opened later
		if (component instanceof ManageStageView) {
			manageStagesTabOpen = false;
		}
		// resets the manageUsersTab boolean so a new manageUsers can be
		// opened later
		if (component instanceof ManageUsersView) {
			manageUsersTabOpen = false;
		}
		if (!(component instanceof WorkflowView)) {
			tabPaneV.remove(component);
		}
	}

	/**
	 * Changes the selected tab to the tab with the given index
	 * 
	 * @param tabIndex
	 *            the index of the tab to select
	 */
	private void switchToTab(int tabIndex) {
		try {
			tabPaneV.setSelectedIndex(tabIndex);
		} catch (IndexOutOfBoundsException e) {
			// an invalid tab was requested, do nothing
		}
	}

	public TabPaneView getTabView() {
		return tabPaneV;
	}

	public void reloadWorkflow() {
		tabPaneV.reloadWorkflow();
	}

}
