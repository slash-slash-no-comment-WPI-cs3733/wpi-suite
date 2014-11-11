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
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	public void addStageView(String name) {
		this.add(new StageView(name));
	}

	/**
	 * Adds a new StageView as a new scroll panel to house the StageView objects
	 * and sets the size and border.
	 * 
	 * @param stv
	 *            the StageView to be added.
	 * 
	 * @param name
	 *            the name of the StageView to be added.
	 */
	public void addStageView(StageView stv, String name) {

		// creates the container for both the label and the scroll
		JPanel block = new JPanel();
		block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));

		// creates the label for the name of the stage and adds it to the block
		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(175, 25));
		JLabel stageName = new JLabel(name);
		stageName.setName(name);

		label.add(stageName);
		block.add(label);

		// creates the scroll containing the stage view and adds it to the block
		final JScrollPane stage = new JScrollPane(stv);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setPreferredSize(new Dimension(175, 350));
		block.add(stage);

		// adds the combined label and scroll to the workflow view
		this.add(block);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setController(WorkflowController controller) {
		this.controller = controller;
	}

}
