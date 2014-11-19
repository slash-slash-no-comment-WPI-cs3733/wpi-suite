package taskManager.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ScrollList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1862712982356137942L;

	public static void main(String[] args) {
		System.out.println("Program Start!");
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setSize(500, 500);
		jf.add(new ScrollList(), BorderLayout.CENTER);
		jf.setVisible(true);
	}

	private DefaultListModel<String> lm;
	private JScrollPane listScroller;
	private JList<String> jl;

	public ScrollList() {
		setLayout(new BorderLayout());
		lm = new DefaultListModel<String>();
		jl = new JList<String>(lm);
		listScroller = new JScrollPane(jl);
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
}
