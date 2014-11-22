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

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import taskManager.controller.WorkflowController;

/**
 * @author Beth Martino
 * @author Clark Jacobsohn
 * @version November 9, 2014
 */
public class WorkflowView extends JLayeredPane {

	private static final long serialVersionUID = 1L;

	private WorkflowController controller;
	private JPanel stages;
	private TaskInfoPreviewView taskInfoView;

	/**
	 * Constructor for WorkflowView.
	 */
	public WorkflowView() {
		stages = new JPanel();

		// ignores the task info view so the stages view fills up the screen
		this.setLayout(new WorkflowLayout());

		// arranges the stages horizontally and evenly spaced
		stages.setLayout(new FlowLayout());

		this.add(stages);
		this.setLayer(stages, JLayeredPane.DEFAULT_LAYER);
	}

	/**
	 * @param stv
	 *            of the new stage to be added creates a new scroll panel to
	 *            house the stage view object sets the size and border
	 */
	public void addStageView(StageView stv) {
		stages.add(stv);
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
			for (int i = 1; i == stages.getComponents().length; i++) {
				if (stages.getComponent(i).getName().equals(name)) {
					return (StageView) stages.getComponent(i);
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
		if (visible && controller != null) {
			controller.reloadData();
		}
		super.setVisible(visible);
	}

	@Override
	public void removeAll() {
		if (stages != null) {
			stages.removeAll();
		}
	}

	@Override
	public void revalidate() {
		super.revalidate();
		if (stages != null) {
			stages.revalidate();
		}
	}

	@Override
	public void repaint() {
		super.repaint();
		if (stages != null) {
			stages.repaint();
		}
	}

}
