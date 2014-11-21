/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import taskManager.JanewayModule;

/**
 * 
 * TabView deals with the actual tab at the top of a panel.
 *
 * @author Samee Swartz
 * @version Nov 21, 2014
 */
public class TabView extends JPanel implements ActionListener {

	private static final long serialVersionUID = -5461050356588592448L;
	private Component component;
	private boolean closeable;

	/**
	 * 
	 * Creates a new tab to hold a window of information
	 *
	 * @param title
	 *            The title to be displayed on the tab
	 * @param component
	 *            The component to be displayed in this tab's window/pane
	 * @param closeable
	 *            Whether the tab should be closeable - aka have an 'x' on the
	 *            tab itself
	 */
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

	/**
	 * 
	 * Calls the constructor to make a closeable tab
	 *
	 * @param title
	 * @param component
	 */
	public TabView(String title, Component component) {
		this(title, component, true);
	}

	/**
	 * When the 'x' in the tab is pressed
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (closeable) {
			JanewayModule.tabPaneC.removeTabByComponent(component);
		}
	}

	/**
	 * 
	 * Returns the component displayed in this tab
	 *
	 * @return the Component displayed
	 */
	public Component getComponent() {
		return component;
	}

}
