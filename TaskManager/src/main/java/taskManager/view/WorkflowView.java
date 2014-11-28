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

import javax.swing.JLayeredPane;

import taskManager.controller.WorkflowController;

/**
 * @author Beth Martino
 * @author Clark Jacobsohn
 * @version November 9, 2014
 */
public class WorkflowView extends JLayeredPane {

	private static final long serialVersionUID = 1L;

	private WorkflowController controller;

	/**
	 * Constructor for WorkflowView.
	 */
	public WorkflowView() {
		this.setLayout(new WorkflowLayout());
		this.addMouseListener(controller);
	}

	/**
	 * 
	 * Adds a stageView to the workflow.
	 * 
	 * @param stv
	 *            the new stage to be added
	 */
	public void addStageView(StageView stv) {
		// stv.setPreferredSize(new Dimension(stv.getPreferredSize().width, this
		// .getSize().height - 20));
		add(stv, new Integer(0));
	}

	/**
	 * 
	 * Adds a TaskInfoPreviewView "pop-up" to the workflow.
	 *
	 * @param ti
	 */
	public void addTaskInfo(TaskInfoPreviewView ti) {
		removeTaskInfos();
		add(ti, new Integer(1));
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
		this.addMouseListener(controller);
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
			for (int i = 1; i == getComponents().length; i++) {
				if (getComponent(i).getName().equals(name)) {
					return (StageView) getComponent(i);
				}
			}
		} catch (NullPointerException e) {
			System.out.println("How did you actually do this?");
		}
		return new StageView(name, false);
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

	/**
	 * 
	 * Removes all instances of TaskInfoPreviewView from the workflow.
	 *
	 */
	public void removeTaskInfos() {
		for (Component c : this.getComponents()) {
			if (c instanceof TaskInfoPreviewView) {
				this.remove(c);
			}
		}
	}
}
