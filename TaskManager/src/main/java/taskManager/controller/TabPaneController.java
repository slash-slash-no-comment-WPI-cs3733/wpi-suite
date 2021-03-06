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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import taskManager.view.EditTaskView;
import taskManager.view.ReportsView;
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
public class TabPaneController implements ChangeListener {

	// singleton TabPaneView
	private TabPaneView view;
	private static TabPaneController instance = null;

	/**
	 * Hide Singleton constructor
	 */
	private TabPaneController() {
		reset();
	}

	/**
	 * Resets instance
	 */
	public void reset() {
		view = new TabPaneView();
	}

	/**
	 * 
	 * Creates and adds a new tab to create a task.
	 *
	 */
	public void addCreateTaskTab() {
		// Each press of create a new tab should launch a new createTaskTab
		final EditTaskController etc = new EditTaskController();

		// Focuses on the new tab
		final int index = view.getTabCount() - 1;
		view.setSelectedIndex(index);

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
		for (Component c : view.getComponents()) {
			if (c instanceof EditTaskView) {
				etv2 = (EditTaskView) c;

				if (etv2.getController().isDuplicate(etv.getController())) {
					exists = true;
					break;
				}
			}
		}
		if (exists) {
			view.setSelectedComponent(etv2);
		} else {
			addTab(etv.getTitleText(), etv, true, false);
			view.setSelectedComponent(etv);
		}
	}

	/**
	 * 
	 * Adds the reports view tab.
	 *
	 * @param rtv
	 *            the reports view to add.
	 */
	public void addReportsTab(ReportsView rtv) {
		addTab("Reports", rtv, true, true);
		view.setSelectedComponent(rtv);
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
	 * @param localizable
	 *            If this title should be localized
	 */
	public void addTab(String title, Component component, boolean closeable,
			boolean localizable) {
		view.addTab(title, component);
		view.setTabComponentAt(view.indexOfComponent(component), new TabView(
				title, component, closeable, localizable));
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
			view.remove(component);
		}
	}

	/**
	 * remove a tab of the given id, if it's open
	 * 
	 * @param id
	 *            the id of the tab you want to remove
	 */
	public void removeEditTaskTabByID(String id) {
		for (Component t : view.getComponents()) {
			if (t instanceof EditTaskView) {
				EditTaskView tab = (EditTaskView) t;
				if (tab.getViewID().equals(id)) {
					TabPaneController.getInstance().removeTabByComponent(tab);
				}
			}
		}
	}

	/**
	 * Returns the associated TabPaneView
	 * 
	 * @return The associated TabPaneView
	 */
	public TabPaneView getView() {
		return view;
	}

	/**
	 * Returns the singleton instance of TabPaneController
	 * 
	 * @return The singleton instance of TabPaneController
	 */
	public static TabPaneController getInstance() {
		if (instance == null) {
			instance = new TabPaneController();
		}
		return instance;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (view.getSelectedComponent() instanceof EditTaskView) {
			final EditTaskView etv = (EditTaskView) view.getSelectedComponent();
			etv.getController().reloadData();
		}
	}

}
