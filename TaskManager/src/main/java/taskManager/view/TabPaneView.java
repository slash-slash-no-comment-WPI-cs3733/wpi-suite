/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import taskManager.controller.WorkflowController;

/**
 * 
 * The singleton TabPaneView creates the workflow tab and deals with adding and
 * removing tabs
 *
 * @author Samee Swartz
 * @author Clark Jacobsohn
 * @version Nov 17, 2014
 */
public class TabPaneView extends JTabbedPane {

	private static final long serialVersionUID = -4912871689110151496L;

	/**
	 * Constructs the TabPaneView and adds the WorkflowView to a scrollable pane
	 * in a permanent tab.
	 */
	public TabPaneView() {
		setTabPlacement(TOP);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setBorder(BorderFactory.createEmptyBorder(5, 3, 3, 3));
		this.setSize(new Dimension(500, 500));

		// Add the scrollable workflow
		final JScrollPane scroll = new JScrollPane(WorkflowController
				.getInstance().getView());

		this.addTab("Workflow", new ImageIcon(), scroll, "Workflow");
	}
}
