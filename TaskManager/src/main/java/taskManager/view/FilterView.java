/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import taskManager.controller.FilterController;
import taskManager.model.TaskModel.TaskCategory;

/**
 * the view containing the elements used for filtering tasks
 * 
 * @author Beth Martino
 *
 */
public class FilterView extends JPanel {

	public static final Color[] CATEGORIES = new Color[] { Color.red,
			Color.green, Color.blue, Color.yellow };

	private FilterController filterC;

	private JCheckBox archiveCheckBox;
	private JCheckBox myTasksCheckBox;
	private JLabel catLabel;
	private JPopupMenu categories;

	public FilterView() {
		filterC = new FilterController(this);

		// adds a panel for this
		this.setLayout(new GridLayout(2, 1));
		this.setMaximumSize(new Dimension(180, 60));
		this.setMinimumSize(new Dimension(180, 60));
		this.setOpaque(false);

		// adds a panel for the checks and the category picker
		JPanel lower = new JPanel();
		lower.setLayout(new GridLayout(1, 2));

		// adds a panel for my tasks and archived check boxes
		JPanel checks = new JPanel();
		checks.setLayout(new GridLayout(2, 1));
		checks.setMaximumSize(new Dimension(90, 30));
		// Checkbox for toggling showing archived tasks.
		archiveCheckBox = new JCheckBox("<html>Show archived tasks</html>");
		archiveCheckBox.setOpaque(false);
		archiveCheckBox.addItemListener(filterC);
		myTasksCheckBox = new JCheckBox("<html>Show only my tasks</html>");
		myTasksCheckBox.setOpaque(false);
		myTasksCheckBox.addItemListener(filterC);
		checks.add(archiveCheckBox);
		checks.add(myTasksCheckBox);

		// adds the category dropdown
		JPanel catBox = new JPanel();
		catLabel = new JLabel("Filter categories");
		catLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		catLabel.addMouseListener(filterC);
		categories = new JPopupMenu("Categories");
		for (int i = 0; i < CATEGORIES.length; i++) {
			JCheckBoxMenuItem c = new JCheckBoxMenuItem();
			c.setName(TaskCategory.values()[i].toString());
			c.setBackground(CATEGORIES[i]);
			c.addItemListener(filterC);
			categories.add(c);
		}
		categories.setSize(90, 120);
		categories.setVisible(false);
		catBox.add(catLabel);
		catBox.add(categories);

		lower.add(checks);
		lower.add(catBox);
		this.add(lower);
	}

	public boolean isArchiveShown() {
		return archiveCheckBox.isSelected();
	}

	public boolean isMyTasksShown() {
		return myTasksCheckBox.isSelected();
	}

	public JPopupMenu getCategories() {
		return this.categories;
	}

	public JLabel getCategoryLabel() {
		return this.catLabel;
	}

	public JCheckBox getArchiveCheckBox() {
		return this.archiveCheckBox;
	}

	public JCheckBox getMyTasksCheckBox() {
		return this.myTasksCheckBox;
	}

}
