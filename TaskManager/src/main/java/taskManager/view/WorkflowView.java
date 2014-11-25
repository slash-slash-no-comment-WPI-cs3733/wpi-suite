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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import taskManager.controller.WorkflowController;

/**
 * @author Beth Martino
 * @version November 9, 2014
 */

public class WorkflowView extends JPanel {

	private static final long serialVersionUID = 1L;

	private WorkflowController controller;

	/**
	 * Constructor for WorkflowView.
	 */
	public WorkflowView() {

		// arranges the stages horizontally and evenly spaced
		this.setLayout(new FlowLayout());
		this.addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
				if (SwingUtilities.getWindowAncestor(WorkflowView.this) != null) {
					SwingUtilities.getWindowAncestor(WorkflowView.this)
							.addWindowListener(new WindowAdapter() {

								@Override
								public void windowClosing(WindowEvent we) {
									WorkflowController.dispose();
								}
							});
					WorkflowView.this.removeAncestorListener(this);
				}
			}
		});
	}

	/**
	 * @param stv
	 *            of the new stage to be added creates a new scroll panel to
	 *            house the stage view object sets the size and border
	 */
	public void addStageView(StageView stv) {
		this.add(stv);
	}

	/*
	 * @see java.awt.Component#getName()
	 */
	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * attaches the controller to this view
	 * 
	 * @param controller
	 *            the workflow controller to be attached
	 *
	 * @param controller
	 *            The controller to attach to this view
	 */
	public void setController(WorkflowController controller) {
		this.controller = controller;
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
			// goes through all of the stage views it contains until it finds
			// the one that matches the name
			for (int i = 1; i == this.getComponents().length; i++) {
				if (this.getComponent(i).getName().equals(name)) {
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

	/*
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible && controller != null && !isVisible()) {
			controller.reloadData();
		}
		super.setVisible(visible);
	}

}
