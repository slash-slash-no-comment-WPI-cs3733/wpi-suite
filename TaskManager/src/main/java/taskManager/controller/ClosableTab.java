package taskManager.controller;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ClosableTab extends JPanel implements ActionListener {
	private final JTabbedPane tabbedPane;

	/**
	 * Create a closable tab component belonging to the given tabbedPane. The
	 * title is extracted with {@link JTabbedPane#getTitleAt(int)}.
	 * 
	 * @param tabbedPane
	 *            The JTabbedPane this tab component belongs to
	 */
	public ClosableTab(JTabbedPane tabbedPane) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.tabbedPane = tabbedPane;
		setOpaque(false);

		final JLabel label = new JLabel() {
			// display the title according to what's set on our JTabbedPane
			@Override
			public String getText() {
				final JTabbedPane tabbedPane = ClosableTab.this.tabbedPane;
				final int index = tabbedPane
						.indexOfTabComponent(ClosableTab.this);
				return index > -1 ? tabbedPane.getTitleAt(index) : "";
			}
		};
		label.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 7));
		add(label);

		final JButton closeButton = new JButton("\u2716");
		closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.addActionListener(this);
		add(closeButton);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// close this tab when close button is clicked
		final int index = tabbedPane.indexOfTabComponent(this);
		if (index > -1) {
			tabbedPane.remove(index);
		}
	}

}
