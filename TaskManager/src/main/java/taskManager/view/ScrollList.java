/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import taskManager.controller.ReportsManager;
import taskManager.controller.TaskInputController;

/**
 * A component that has a scroll pane containing a list of strings
 * 
 * @author Jack R
 * @author Beth Martino
 */
public class ScrollList extends JPanel {

	private static final long serialVersionUID = -1862712982356137942L;

	private final DefaultListModel<String> lm;
	private final JScrollPane listScroller;
	private final JList<String> jl;
	private final JLabel title;

	/**
	 * Constructor for a scroll list
	 *
	 * @param t
	 *            The title of the scroll list
	 */
	public ScrollList(String t) {
		setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(140, 290));
		this.setMaximumSize(new Dimension(140, 290));
		this.setPreferredSize(new Dimension(140, 290));
		this.setSize(new Dimension(140, 290));
		lm = new DefaultListModel<String>();
		jl = new JList<String>(lm);
		jl.setVisibleRowCount(3);
		listScroller = new JScrollPane(jl);
		listScroller.setMinimumSize(new Dimension(200, 175));
		listScroller.setMaximumSize(new Dimension(200, 175));
		listScroller.setPreferredSize(new Dimension(200, 175));
		listScroller.setSize(new Dimension(200, 175));
		listScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		title = new JLabel(t);
		this.add(title, BorderLayout.NORTH);
		this.setBackground(Colors.TASK);
		this.add(listScroller, BorderLayout.CENTER);
	}

	/**
	 * refresh the list
	 */
	private void refresh() {
		add(listScroller, BorderLayout.CENTER);
		validate();
	}

	/**
	 * add the given string to the list
	 * 
	 * @param element
	 */
	public void addToList(String element) {
		lm.addElement(element);
		refresh();
	}

	/**
	 * add all strings in the given list to the list
	 * 
	 * @param elements
	 *            the list to be added
	 */
	public void addAllToList(List<String> elements) {
		int i = 0;
		while (i < elements.size()) {
			lm.addElement(elements.get(i));
			i++;
		}
		refresh();
	}

	/**
	 * remove the string at the given index from the list
	 * 
	 * @param i
	 *            the string to be removed
	 */
	public void removeFromList(int i) {
		lm.removeElementAt(i);
		refresh();
	}

	/**
	 * remove the given string from the list
	 * 
	 * @param s
	 *            the string to be removed
	 */
	public void removeFromList(String s) {
		lm.removeElement(s);
		refresh();
	}

	/**
	 * returns true if the list contains the given string
	 * 
	 * @param s
	 *            the string to search for in the list
	 * @return if the scroll list contains the given string
	 */
	public boolean contains(String s) {
		return lm.contains(s);
	}

	public String getSelectedValue() {
		return jl.getSelectedValue();
	}

	public int getSelectedIndex() {
		return jl.getSelectedIndex();
	}

	public int[] getSelectedIndices() {
		return jl.getSelectedIndices();
	}

	/**
	 * returns true if there is no selection made
	 * 
	 * @return true if there is no selection, false if there is a selection
	 */
	public boolean isSelectionEmpty() {
		return jl.isSelectionEmpty();
	}

	/**
	 * Remove the given items from the list
	 *
	 * @param index
	 *            an array of indices to be removed
	 */
	public void removeSelectedIndices(int[] index) {
		int i = 0;
		while (i < index.length) {
			jl.remove(index[i]);
			i++;
		}
	}

	/**
	 * get the list of strings
	 * 
	 * @return the lit of strings
	 */
	public List<String> getAllValues() {
		final List<String> result = new LinkedList<String>();
		for (int i = 0; i < lm.getSize(); i++) {
			result.add(lm.get(i));
		}
		return result;
	}

	/**
	 * get the list of strings
	 * 
	 */
	public void removeAllValues() {
		lm.removeAllElements();
		refresh();
	}

	/**
	 * returns true if the list of users is empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return lm.isEmpty();
	}

	/**
	 * Sets the selected value to the given indices
	 * 
	 * @param i
	 *            array of indices to set selected
	 */
	public void setSelected(int[] i) {
		jl.setSelectedIndices(i);
	}

	/**
	 * gets the value at the given index
	 * 
	 * @param i
	 *            the index
	 * @return the value at index i
	 */
	public String getValueAtIndex(int i) {
		return lm.get(i);
	}

	/**
	 * sets the listener for the JList
	 * 
	 * @param listener
	 *            the listener to be added to the list
	 */
	public void setController(TaskInputController listener) {
		jl.addListSelectionListener(listener);
		jl.addPropertyChangeListener(listener);
	}

	/**
	 * sets the listener for the JList
	 * 
	 * @param listener
	 *            the listener to be added to the list
	 */
	public void setController(ReportsManager listener) {
		jl.addListSelectionListener(listener);
		jl.addPropertyChangeListener(listener);
	}

	public void setTitle(String s) {
		title.setText(s);
	}

}
