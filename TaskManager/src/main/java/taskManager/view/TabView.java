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
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import taskManager.controller.EditTaskController;
import taskManager.controller.TabPaneController;
import taskManager.controller.WorkflowController;
import taskManager.model.FetchWorkflowObserver;

/**
 * 
 * TabView deals with the tab for each window
 *
 * @author Samee Swartz
 * @version Nov 21, 2014
 */
public class TabView extends JPanel implements ActionListener, MouseListener {

	public static final String X = "X";

	private static final long serialVersionUID = -5461050356588592448L;
	private Component component;
	private boolean closeable;
	private TabPaneController tabPaneC;
	private final JButton closeButton;

	/**
	 * 
	 * Creates a new tab to contain a window/pane of information
	 *
	 * @param title
	 *            The title to put in the tab itself
	 * @param component
	 *            The component to display in the tab's window/pane
	 * @param closeable
	 *            Whether to make the tab closeable - aka put an 'x' in the tab
	 */
	public TabView(String title, Component component, boolean closeable) {
		super(new MigLayout("", "0[][]0", "0[]0"));

		this.component = component;
		this.closeable = closeable;

		tabPaneC = TabPaneController.getInstance();

		setOpaque(false);

		final JLabel label = new JLabel(title);
		// This makes the tab's a set width and adds the ... if a task name is
		// too long for the tab
		final JLabel temp = new JLabel();
		temp.setText("Tabs Name Length");
		final Dimension size = temp.getPreferredSize();
		label.setMaximumSize(size);
		add(label);

		closeButton = new JButton("\u2716");
		if (closeable) {
			closeButton.setFont(closeButton.getFont().deriveFont((float) 10));
			closeButton.setMargin(new Insets(0, 2, 0, 2));
			closeButton.setFocusPainted(false);
			closeButton.setContentAreaFilled(false);
			closeButton.setBorderPainted(false);
			closeButton.setOpaque(false);
			closeButton.addActionListener(this);
			closeButton.addMouseListener(this);
			closeButton.setName(X);
			add(closeButton);
		}
	}

	/**
	 * 
	 * Constructs a closeable tab
	 *
	 * @param title
	 *            The title to put in the tab itself
	 * @param component
	 *            The component to display in the tab's window/pane
	 */
	public TabView(String title, Component component) {
		this(title, component, true);
	}

	/**
	 * When the 'x' button is pressed
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (closeable) {
			if (component instanceof EditTaskView) {

				final EditTaskController etc = ((EditTaskView) component)
						.getController();
				// If there are edits, show confirmation dialog.
				if (etc.isEdited()) {
					final Integer choice = JOptionPane
							.showConfirmDialog(
									tabPaneC.getView(),
									"You still have unsaved edits. Are you sure you want to delete this tab?",
									"Warning - Deleting a tab with edits",
									JOptionPane.YES_NO_OPTION);
					if (choice.equals(JOptionPane.YES_OPTION)) {
						tabPaneC.removeTabByComponent(component);
						tabPaneC.getView().setSelectedIndex(0);
					}
				} else {
					tabPaneC.removeTabByComponent(component);
					tabPaneC.getView().setSelectedIndex(0);
				}
			} else {
				tabPaneC.removeTabByComponent(component);
				tabPaneC.getView().setSelectedIndex(0);
			}
		}
		FetchWorkflowObserver.ignoreAllResponses = false;

		WorkflowController.getInstance().removeTaskInfos(false);
		WorkflowController.getInstance().reloadData();
	}

	public Component getComponent() {
		return component;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		closeButton.setBorderPainted(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		closeButton.setBorderPainted(false);
	}

}
