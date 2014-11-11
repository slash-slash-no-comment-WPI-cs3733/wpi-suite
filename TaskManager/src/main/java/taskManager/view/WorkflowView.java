/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import taskManager.controller.WorkflowController;

/**
 * @author Beth Martino
 * @version November 9, 2014
 */

public class WorkflowView extends JPanel implements IWorkflowView {

	private static final long serialVersionUID = 1L;

	private WorkflowController controller;

	// used to generate unique names for the stage labels
	private int i = 0;

	/**
	 * Constructor for WorkflowView.
	 */
	public WorkflowView() {

		// arranges the stages horizontally and evenly spaced
		this.setLayout(new FlowLayout());

	}

	/**
	 * @param name
	 *            of the new stage to be added creates a new scroll panel to
	 *            house the stage view object sets the size and border
	 */

	public void addStageView(StageView stv) {
		this.add(stv);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setController(WorkflowController controller) {
		this.controller = controller;
	}

}
