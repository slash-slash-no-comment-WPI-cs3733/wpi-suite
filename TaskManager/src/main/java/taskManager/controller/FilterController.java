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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import taskManager.TaskManager;
import taskManager.controller.TaskFilter.ArchiveState;
import taskManager.model.TaskModel.TaskCategory;

public class FilterController implements ItemListener, PopupMenuListener {

	public FilterController() {

	}

	/**
	 * returns true if any of the filters are activated
	 * 
	 * @return
	 */
	private boolean hasFilter() {
		return archiveChecked() || myTasksChecked()
				|| !categoriesSelected().isEmpty() || hasSearch();
	}

	/**
	 * returns true if there is something in the search bar
	 * 
	 * @return
	 */
	private boolean hasSearch() {
		return false;
	}

	/**
	 * returns the names of the categories selected on the toolbar
	 * 
	 * @return a list of the selected categories
	 */
	private Set<TaskCategory> categoriesSelected() {
		Component[] cats = ToolbarController.getInstance().getView()
				.getCategories().getComponents();
		Set<TaskCategory> selected = new HashSet<TaskCategory>();
		for (int i = 0; i < cats.length; i++) {
			if (cats[1] instanceof JCheckBox) {
				JCheckBox item = (JCheckBox) cats[i];
				if (item.isSelected()) {
					selected.add(TaskCategory.values()[i]);
				}
			}

		}
		return selected;
	}

	/**
	 * returns true if the show my tasks box is checked
	 * 
	 * @return true if the show my tasks box is checked
	 */
	private boolean myTasksChecked() {
		return ToolbarController.getInstance().getView().isMyTasksShown();
	}

	/**
	 * returns true if the show archived tasks box is checked
	 * 
	 * @return true if the show archived tasks box is checked
	 */
	private boolean archiveChecked() {
		return ToolbarController.getInstance().getView().isArchiveShown();
	}

	/**
	 * reloads the workflow with the appropriate filter applied
	 */
	private void filter() {
		if (hasFilter()) {
			TaskFilter filter = new TaskFilter();
			WorkflowController.getInstance().setCurrentFilter(filter);
			if (archiveChecked()) {
				filter.setArchive(ArchiveState.ARCHIVED,
						ArchiveState.NOT_ARCHIVED);
			} else {
				filter.setArchive(ArchiveState.NOT_ARCHIVED);
			}
			if (myTasksChecked()) {
				filter.setUser(TaskManager.currentUser);
			} else {
				filter.setUsers(null);
			}
			if (!categoriesSelected().isEmpty()) {
				filter.setCategories(categoriesSelected());
			}
		} else {
			WorkflowController.getInstance().setCurrentFilter(new TaskFilter());
		}
		WorkflowController.getInstance().reloadData();
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		filter();

	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		filter();

	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		filter();

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		filter();
	}
}
