/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.prompt.PromptSupport;

import taskManager.controller.FilterController;
import taskManager.localization.Localizer;
import taskManager.model.TaskModel.TaskCategory;

/**
 * the view containing the elements used for filtering tasks
 * 
 * @author Beth Martino
 *
 */
public class FilterView extends JPanel {

	private static final long serialVersionUID = 1L;

	private final String CHECK = "\u2713";
	public static final String SHOW_ARCHIVE = "showArchive";
	public static final String MY_TASKS = "myTasks";
	public static final String SEARCH = "search";

	private FilterController filterC;

	private JCheckBox archiveCheckBox;
	private JCheckBox myTasksCheckBox;
	private JPanel categories;
	private ArrayList<JLabel> labels;
	private JTextField search;

	private static final Dimension SIZE = new Dimension(150, 90);

	/**
	 * creates a new filter view to be added to the toolbar
	 * 
	 * @param filter
	 *            the controller for the new filter view
	 */
	public FilterView(FilterController filter) {
		filterC = filter;

		Dimension size = new Dimension(170, 30);
		// Construct Search bar
		search = new JTextField();
		search.setMaximumSize(size);
		search.addKeyListener(filterC);
		search.setName(SEARCH);
		PromptSupport.setPrompt(Localizer.getString("Search"), search);
		PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT,
				search);

		// adds a panel for this
		this.setLayout(new GridLayout(3, 1));
		this.setMaximumSize(SIZE);
		this.setPreferredSize(SIZE);
		this.setOpaque(false);

		// adds a panel for my tasks and archived check boxes
		JPanel checks = new JPanel();
		checks.setLayout(new GridLayout(2, 1));
		checks.setMinimumSize(size);
		// Checkbox for toggling showing archived tasks.
		archiveCheckBox = new JCheckBox("<html>Show archived tasks</html>");
		archiveCheckBox.setOpaque(false);
		archiveCheckBox.addItemListener(filterC);
		archiveCheckBox.setMinimumSize(checks.getMinimumSize());
		archiveCheckBox.setName(SHOW_ARCHIVE);
		myTasksCheckBox = new JCheckBox("<html>Show only my tasks</html>");
		myTasksCheckBox.setOpaque(false);
		myTasksCheckBox.addItemListener(filterC);
		myTasksCheckBox.setMinimumSize(checks.getMinimumSize());
		myTasksCheckBox.setName(MY_TASKS);
		checks.add(archiveCheckBox);
		checks.add(myTasksCheckBox);
		checks.setOpaque(false);

		// adds the category dropdown
		categories = new JPanel();
		categories.setLayout(new FlowLayout());
		categories.setOpaque(false);
		labels = new ArrayList<JLabel>();
		// make all of the category boxes, don't include the "no category"
		Dimension catBoxSize = new Dimension(20, 20);
		for (int i = 1; i < TaskCategory.values().length; i++) {
			JPanel catBox = new JPanel();
			catBox.setSize(catBoxSize);
			catBox.setPreferredSize(catBoxSize);
			catBox.setMinimumSize(catBoxSize);
			catBox.setMaximumSize(catBoxSize);
			catBox.setLayout(new BoxLayout(catBox, BoxLayout.Y_AXIS));
			JLabel c = new JLabel("");
			c.setFont(new Font("Default", Font.BOLD, 14));
			c.setMinimumSize(catBox.getPreferredSize());
			c.setAlignmentX(CENTER_ALIGNMENT);
			c.setAlignmentY(CENTER_ALIGNMENT);
			catBox.add(c);
			labels.add(c);
			catBox.setName(Colors.CATEGORY_NAMES[i]);
			catBox.setBackground(Colors.CAT_COLORS[i]);
			catBox.addMouseListener(filterC);
			categories.add(catBox);
		}
		categories.setMaximumSize(size);

		this.add(search);
		this.add(categories);
		this.add(checks);

	}

	/**
	 * return whether or not the "archive" checkbox is checked
	 * 
	 * @return true if the box is checked, false if it is not
	 */
	public boolean isArchiveShown() {
		return archiveCheckBox.isSelected();
	}

	/**
	 * return whether or not the "show my tasks" checkbox is checked
	 * 
	 * @return true if the box is checked, false if it is not
	 */
	public boolean isMyTasksShown() {
		return myTasksCheckBox.isSelected();
	}

	/**
	 * get the category menu component
	 * 
	 * @return the category menu component
	 */
	public JPanel getCategories() {
		return this.categories;
	}

	/**
	 * returns the category color box of the given color name
	 * 
	 * @param name
	 *            the name of the color
	 * @return the category box of the given color name
	 */
	public JPanel getCatBox(String name) {
		JPanel label = new JPanel();
		for (int i = 0; i < this.getCategories().getComponents().length; i++) {
			if (name.equals(getCategories().getComponents()[i])) {
				label = (JPanel) getCategories().getComponents()[i];
			}
		}
		return label;
	}

	/**
	 * returns the category color box of the given color name
	 * 
	 * @param name
	 *            the name of the color
	 * @return the category box of the given color name
	 */
	public boolean catBoxIsChecked(String name) {
		for (int i = 0; i < this.getCategories().getComponents().length; i++) {
			if (name.equals(getCategories().getComponents()[i].getName())) {
				JPanel catBox = (JPanel) getCategories().getComponents()[i];
				JLabel label = (JLabel) catBox.getComponent(0);
				if (label.getText().equals(CHECK)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * set the given color box to either have a check or not
	 * 
	 * @param checked
	 *            whether or not it should be checked
	 * @param name
	 *            the name of color of the desired color box
	 */
	public void checkCatBox(boolean checked, String name) {
		JLabel label = new JLabel();
		for (int i = 0; i < TaskCategory.values().length; i++) {
			// don't check for the "no category" name
			if (name.equals(Colors.CATEGORY_NAMES[i])) {
				label = labels.get(i - 1);
			}
		}
		if (checked) {
			label.setText(CHECK);
		} else {
			label.setText("");
		}
		categories.repaint();
	}

	/**
	 * get the "show archive tasks" checkbox component
	 * 
	 * @return the "show archive tasks" checkbox component
	 */
	public JCheckBox getArchiveCheckBox() {
		return this.archiveCheckBox;
	}

	/**
	 * get the "show my tasks" checkbox component
	 * 
	 * @return the "show my tasks" checkbox component
	 */
	public JCheckBox getMyTasksCheckBox() {
		return this.myTasksCheckBox;
	}

	/**
	 * returns the controller attached to this view
	 * 
	 * @return the controller attached to this view
	 */
	public FilterController getController() {
		return this.filterC;
	}

	/**
	 * returns the text in the search field
	 *
	 * @ return searchString
	 */
	public String getSearchString() {
		return search.getText();
	}

}
