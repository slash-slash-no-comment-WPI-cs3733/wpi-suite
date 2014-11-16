package taskManager.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import taskManager.JanewayModule;

public class TabView extends JPanel implements ActionListener {

	private Component component;
	private boolean closeable;

	public TabView(String title, Component component, boolean closeable) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));

		this.component = component;
		this.closeable = closeable;

		setOpaque(false);
		
		final JLabel label = new JLabel(title);
		label.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 7));
		add(label);

		if (closeable) {
			final JButton closeButton = new JButton("\u2716");
			closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
			closeButton.setMargin(new Insets(0, 0, 0, 0));
			closeButton.addActionListener(this);
			add(closeButton);
		}
	}

	public TabView(String title, Component component) {
		this(title, component, true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (closeable) {
			JanewayModule.tabPaneC.removeTabByComponent(component);
		}
	}
	
	public Component getComponent(){
		return component;
	}

}
