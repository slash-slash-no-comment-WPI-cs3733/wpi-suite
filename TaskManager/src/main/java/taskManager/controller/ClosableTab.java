package taskManager.controller;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import taskManager.view.TabView;
import taskManager.view.ManageStageView;
import taskManager.view.ManageUsersView;

public class ClosableTab extends JPanel implements ActionListener {
	private final JTabbedPane tabbedPane;
	private TabView tv;
	private Component content;

	/**
	 * Create a closable tab component belonging to the given tabbedPane. The
	 * title is extracted with {@link JTabbedPane#getTitleAt(int)}.
	 * 
	 * @param tabbedPane
	 *            The JTabbedPane this tab component belongs to
	 */
	public ClosableTab(JTabbedPane tabbedPane, Component content) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.tabbedPane = tabbedPane;
		this.content = content;
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
		if (this.content instanceof ManageUsersView) {
			tv.setManageUsersTabOut(false);
		}
		if (this.content instanceof ManageStageView) {
			tv.setManageStagesTabOut(false);
		}
		
		if (index > -1) {
			tabbedPane.remove(index);
		}
	}
	
	public void setTabView(TabView tv) {
		this.tv = tv;
	}
	
	public void close() {
		this.actionPerformed(null);
	}

}
