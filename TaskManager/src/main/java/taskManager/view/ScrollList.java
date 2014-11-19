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
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ScrollList extends JPanel {

	/**
	 * A component that has a scroll pane containing a list of strings
	 * 
	 * @author Jack R
	 * @author Beth Martino
	 */
	private static final long serialVersionUID = -1862712982356137942L;

	private DefaultListModel<String> lm;
	private JScrollPane listScroller;
	private JList<String> jl;

	public ScrollList(String t) {
		setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(200, 75));
		this.setMaximumSize(new Dimension(200, 75));
		this.setPreferredSize(new Dimension(200, 75));
		this.setSize(new Dimension(200, 75));
		lm = new DefaultListModel<String>();
		jl = new JList<String>(lm);
		jl.setVisibleRowCount(3);
		listScroller = new JScrollPane(jl);
		listScroller.setMinimumSize(new Dimension(200, 75));
		listScroller.setMaximumSize(new Dimension(200, 75));
		listScroller.setPreferredSize(new Dimension(200, 75));
		listScroller.setSize(new Dimension(200, 75));
		listScroller
				.setVerticalScrollBarPolicy(listScroller.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroller
				.setHorizontalScrollBarPolicy(listScroller.HORIZONTAL_SCROLLBAR_NEVER);

		JLabel title = new JLabel(t);
		this.add(title, BorderLayout.NORTH);
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
	public void addAllToList(ArrayList<String> elements) {
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
	 * @return
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
	public LinkedList<String> getAllValues() {
		LinkedList<String> result = new LinkedList<String>();
		for (int i = 0; i < this.lm.getSize(); i++) {
			result.add(this.lm.get(i));
		}
		return result;
	}

	/**
	 * get the list of strings
	 * 
	 * @return the lit of strings
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

}
