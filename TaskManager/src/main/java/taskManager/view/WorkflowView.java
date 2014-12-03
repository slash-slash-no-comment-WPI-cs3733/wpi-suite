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

import taskManager.controller.WorkflowController;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DropAreaPanel;

/**
 * @author Beth Martino
 * @author Clark Jacobsohn
 * @version November 9, 2014
 */
public class WorkflowView extends JLayeredPane {

	private static final long serialVersionUID = 1L;

	private WorkflowController controller;

	private DropAreaPanel stages;

	/**
	 * Constructor for WorkflowView.
	 */
	public WorkflowView() {

		// The stages panel accepts stage drops
		stages = new DropAreaPanel(DDTransferHandler.getStageFlavor());

		// arranges the stages horizontally and evenly spaced
		this.setLayout(new WorkflowLayout());

		stages.setLayout(new FlowLayout());
		this.add(stages);

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
		if (stages == null || stages.getParent() == null) {
			stages = new DropAreaPanel(DDTransferHandler.getStageFlavor());
			stages.setLayout(new FlowLayout());
			stages.setSaveListener(controller);
			add(stages);
		}
		stages.add(stv);
	}

	/**
	 * 
	 * Adds a TaskInfoPreviewView bubble to the workflow.
	 *
	 * @param ti
	 */
	public void addTaskInfo(TaskInfoPreviewView ti) {
		controller.removeTaskInfos(true);
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

		stages.setSaveListener(controller);

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

			for (int i = 1; i == stages.getComponents().length; i++) {
				if (stages.getComponent(i).getName().equals(name)) {
					return (StageView) stages.getComponent(i);
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
