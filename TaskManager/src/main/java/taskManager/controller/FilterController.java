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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import taskManager.TaskManager;
import taskManager.controller.TaskFilter.ArchiveState;
import taskManager.model.TaskModel.TaskCategory;
import taskManager.view.FilterView;

/**
 * A controller for the filter view in toolbar. When you click filter options,
 * this controller adds a new filter to the workflow according to the optons
 * selected
 * 
 * @author Beth Martino
 *
 */
public class FilterController implements ItemListener, MouseListener {

	private final FilterView view;

	public FilterController(FilterView view) {
		this.view = view;
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
		Component[] cats = view.getCategories().getComponents();
		Set<TaskCategory> selected = new HashSet<TaskCategory>();
		for (int i = 0; i < cats.length; i++) {
			if (cats[1] instanceof JPanel) {
				String name = cats[i].getName();
				if (view.catBoxIsChecked(name)) {
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
		return view.isMyTasksShown();
	}

	/**
	 * returns true if the show archived tasks box is checked
	 * 
	 * @return true if the show archived tasks box is checked
	 */
	private boolean archiveChecked() {
		return view.isArchiveShown();
	}

	/**
	 * reloads the workflow with the appropriate filter applied
	 */
	private void filter() {
		TaskFilter filter = new TaskFilter();
		WorkflowController.getInstance().setCurrentFilter(filter);
		if (archiveChecked()) {
			filter.setArchive(ArchiveState.ARCHIVED, ArchiveState.NOT_ARCHIVED);
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

		WorkflowController.getInstance().reloadData();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		filter();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (String name : FilterView.CATEGORY_NAMES) {
			if (e.getComponent().getName().equals(name)) {
				view.checkCatBox(!view.catBoxIsChecked(name), name);
				System.out.println("Cat box " + name + " checked is "
						+ view.catBoxIsChecked(name));
			}
		}
		filter();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// do nothing
	}
}
