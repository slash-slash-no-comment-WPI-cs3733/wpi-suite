/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import taskManager.controller.WorkflowController;
import taskManager.model.WorkflowModel;

/**
 * 
 * The singleton TabPaneView creates the workflow tab and deals with adding and
 * removing tabs
 *
 * @author Samee Swartz
 * @version Nov 17, 2014
 */
public class TabPaneView extends JTabbedPane {

	private static final long serialVersionUID = -4912871689110151496L;
	// Because the workflow is a permanent tab, tabview should keep track of it
	private WorkflowController wfc;
	private final WorkflowModel wfm;

	public TabPaneView() {
		setTabPlacement(TOP);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setBorder(BorderFactory.createEmptyBorder(5, 3, 3, 3));
		this.setSize(new Dimension(500, 500));

		// Create a workflow view, controller, and model
		WorkflowView wfv = new WorkflowView();
		wfm = WorkflowModel.getInstance();
		wfc = new WorkflowController(wfv);
		wfv.setController(wfc);

		// Make workflow scrollable
		JScrollPane scroll = new JScrollPane(wfv);
		scroll.setBorder(BorderFactory.createLineBorder(Color.black));

		this.addTab("Workflow", new ImageIcon(), scroll, "Workflow");
	}

	public void refreshWorkflow() {
		wfc.fetch();
	}

	public WorkflowModel getWorkflowModel() {
		return wfm;
	}

	public void reloadWorkflow() {
		wfc.reloadData();
	}

	public WorkflowController getWorkflowController() {
		return wfc;
	}
}
