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

import javax.swing.JScrollPane;

import taskManager.JanewayModule;
import taskManager.view.EditTaskView;
import taskManager.view.EditTaskView.Mode;
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
		etv.setFieldController(new TaskInputValidator(etv));
		// Set the dropdown menu to first stage and disable the menu.
		etv.setStageDropdown(0);

		etv.setRefreshEnabled(false);
		// Disable save button when creating a task.
		etv.setSaveEnabled(false);

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

		addTab("Create Task", etv, true);
		// Focuses on the new tab
		int index = this.tabPaneV.getTabCount() - 1;
		this.tabPaneV.setSelectedIndex(index);
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
			if (c instanceof JScrollPane
					&& ((JScrollPane) c).getViewport().getView() instanceof EditTaskView) {
				c = ((JScrollPane) c).getViewport().getView();
				etv2 = (EditTaskView) c;

				if (etv2.getTitle().getName() != null
						&& etv2.getTitle().getName()
								.equals(etv.getTitle().getName())) {
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
		tabPaneV.addTab(title, new JScrollPane(component));
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
