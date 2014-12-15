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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import taskManager.TaskManager;
import taskManager.controller.TaskFilter.ArchiveState;
import taskManager.model.TaskModel.TaskCategory;
import taskManager.view.Colors;
import taskManager.view.FilterView;

/**
 * A controller for the filters. Includes filter by category,
 * "Show only my tasks", "Show archived tasks" and filter by search. Every time
 * a filter element is clicked, it checks which filters are selected and creates
 * a new TaskFilter object and applies it to the workflow
 * 
 * @author Beth Martino
 *
 */
public class FilterController implements ItemListener, MouseListener,
		KeyListener {

	private final FilterView view;

	public FilterController() {
		this.view = new FilterView(this);
	}

	/**
	 * gets the filter view
	 * 
	 * @return the filter view
	 */
	public FilterView getView() {
		return this.view;
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
			if (cats[i] instanceof JPanel) {
				String name = cats[i].getName();
				if (view.catBoxIsChecked(name)) {
					// ignore the "no category option"
					selected.add(TaskCategory.values()[i + 1]);
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
		WorkflowController.getInstance().removeTaskInfos(true);
		TaskFilter filter = new TaskFilter();

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
		if (!view.getSearchString().equals("")) {
			filter.setString(view.getSearchString());
		}
		WorkflowController.getInstance().reloadData(filter);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		filter();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (String name : Colors.CATEGORY_NAMES) {
			if (e.getComponent().getName().equals(name)) {
				view.checkCatBox(!view.catBoxIsChecked(name), name);
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

	@Override
	public void keyTyped(KeyEvent e) {
		filter();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		filter();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		filter();
	}
}
