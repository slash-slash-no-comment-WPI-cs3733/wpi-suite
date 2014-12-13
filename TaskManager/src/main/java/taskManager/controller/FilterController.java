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

import javax.swing.JCheckBox;

import taskManager.TaskManager;
import taskManager.controller.TaskFilter.ArchiveState;
import taskManager.model.TaskModel.TaskCategory;
import taskManager.view.FilterView;

public class FilterController implements ItemListener, MouseListener {

	private boolean popupVisible;
	private final FilterView view;

	public FilterController(FilterView view) {
		this.view = view;
		popupVisible = false;
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
		view.getCategories().setVisible(!view.getCategories().isVisible());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
