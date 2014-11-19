package taskManager.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ScrollList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1862712982356137942L;

	private DefaultListModel<String> lm;
	private JScrollPane listScroller;
	private JList<String> jl;

	public ScrollList() {
		setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(250, 100));
		this.setMaximumSize(new Dimension(250, 100));
		this.setPreferredSize(new Dimension(250, 100));
		this.setSize(new Dimension(250, 100));
		lm = new DefaultListModel<String>();
		jl = new JList<String>(lm);
		jl.setVisibleRowCount(3);
		listScroller = new JScrollPane(jl);
		listScroller.setMinimumSize(new Dimension(250, 100));
		listScroller.setMaximumSize(new Dimension(250, 100));
		listScroller.setPreferredSize(new Dimension(250, 100));
		listScroller.setSize(new Dimension(250, 100));
		listScroller
				.setVerticalScrollBarPolicy(listScroller.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroller
				.setHorizontalScrollBarPolicy(listScroller.HORIZONTAL_SCROLLBAR_NEVER);
		add(listScroller, BorderLayout.CENTER);
	}

	private void refresh() {
		add(listScroller, BorderLayout.CENTER);
		validate();
	}

	public void addToList(String element) {
		lm.addElement(element);
		refresh();
	}

	public void addAllToList(String[] elements) {
		int i = 0;
		while (i < elements.length) {
			lm.addElement(elements[i]);
			i++;
		}
		refresh();
	}

	public void removeFromList(int i) {
		lm.removeElementAt(i);
		refresh();
	}

	public void removeFromList(String s) {
		lm.removeElement(s);
		refresh();
	}

	public String getSelectedValue() {
		return jl.getSelectedValue();
	}

	public int getSelectedIndex() {
		return jl.getSelectedIndex();
	}

	public int[] getSelectedIndcies() {
		return jl.getSelectedIndices();
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
}
