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
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import taskManager.controller.EditTaskController;
import taskManager.controller.TabPaneController;
import taskManager.controller.WorkflowController;
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;
import taskManager.model.FetchWorkflowObserver;

/**
 * 
 * TabView deals with the tab for each window
 *
 * @author Samee Swartz
 * @version Nov 21, 2014
 */
public class TabView extends JPanel implements ActionListener,
		LocaleChangeListener {

	public static final String X = "X";

	private static final long serialVersionUID = -5461050356588592448L;
	private Component component;
	private boolean closeable;
	private TabPaneController tabPaneC;
	private JButton closeButton = null;
	private boolean localizable;
	private final String title;
	private final JLabel label;

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
	 * @param localizable
	 *            If this title should be localized
	 */
	public TabView(String title, Component component, boolean closeable,
			boolean localizable) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));

		this.component = component;
		this.closeable = closeable;
		this.localizable = localizable;
		this.title = title;

		tabPaneC = TabPaneController.getInstance();

		setOpaque(false);

		label = new JLabel();
		// This makes the tab's a set width and adds the ... if a task name is
		// too long for the tab
		final JLabel temp = new JLabel();
		temp.setText("Tabs Name Length");
		final Dimension size = temp.getPreferredSize();
		label.setMaximumSize(size);
		label.setPreferredSize(size);
		label.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 7));
		add(label);

		if (closeable) {
			closeButton = new JButton();
			closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
			closeButton.setMargin(new Insets(0, 0, 0, 0));
			closeButton.addActionListener(this);
			closeButton.setName(X);
			add(closeButton);
		}

		onLocaleChange();
		Localizer.addListener(this);
	}

	/**
	 * 
	 * Constructs a closeable tab
	 *
	 * @param title
	 *            The title to put in the tab itself
	 * @param component
	 *            The component to display in the tab's window/pane
	 * @param localizable
	 *            If this title should be localized
	 */
	public TabView(String title, Component component, boolean localizable) {
		this(title, component, true, localizable);
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
					final Integer choice = JOptionPane.showConfirmDialog(
							tabPaneC.getView(),
							Localizer.getString("UnsavedWarning"),
							Localizer.getString("DeleteTabWarning"),
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
	public void onLocaleChange() {
		closeButton.setText(Localizer.getString("x"));
		if (localizable) {
			label.setText(Localizer.getString(title));
		} else {
			label.setText(title);
		}
	}
}
