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
import java.util.logging.Level;
import java.util.logging.Logger;

import taskManager.view.EditTaskView;
import taskManager.view.TabPaneView;
import taskManager.view.TabView;
import taskManager.view.WorkflowView;

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
	private static final Logger logger = Logger
			.getLogger(TabPaneController.class.getName());

	/**
	 * Constructor for a controller for the tab view
	 *
	 * @param tabPaneV
	 *            the tab view to be controlled
	 */
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
		EditTaskController etc = new EditTaskController();

		// Focuses on the new tab
		int index = tabPaneV.getTabCount() - 1;
		tabPaneV.setSelectedIndex(index);

		etc.getView().focusOnTitleField();
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
			logger.log(Level.WARNING, "tried to switch to non-existant tab");
		}
	}

	public TabPaneView getTabView() {
		return tabPaneV;
	}

}
