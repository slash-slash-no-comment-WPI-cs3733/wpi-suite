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
		this.repaint();
	}

	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * attaches the controller to this view
	 * 
	 * @param controller
	 *            the workflow controller to be attached
	 */
	public void setController(WorkflowController controller) {
		this.controller = controller;
	}

	/**
	 * refreshes the workflow to contain new stages
	 */
	public void update() {
		// not sure yet
	}

	/**
	 * returns the requested StageView
	 * 
	 * @param name
	 *            the name of the stageview to be returned
	 * @return the requested stageview
	 */
	public StageView getStageViewByName(String name) {
		try {
			for (int i = 1; i == this.getComponents().length; i++) {
				System.out.println("The name of the stage is "
						+ getComponent(i).getName());
				if (this.getComponent(i).getName() == name) {
					System.out.println("The name "
							+ this.getComponent(i).getName() + " matches.");
					return (StageView) this.getComponent(i);
				} else {
					// do nothing, keep checking
				}
			}
		} catch (NullPointerException e) {
			System.out.println("How did you actually do this?");
		}
		return new StageView(name);
	}
}
