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

import javax.swing.BoxLayout;
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

		stages.setLayout(new BoxLayout(stages, BoxLayout.LINE_AXIS));
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
			stages.setLayout(new BoxLayout(stages, BoxLayout.LINE_AXIS));
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
	 * returns the requested StageView, or creates one if it does not exist
	 * 
	 * @param name
	 *            the name of the stageview to be returned
	 * @return the requested stageview
	 */
	public StageView getStageViewByName(String name) {
		if (name == null) {
			throw new NullPointerException("name must not be null");
		}
		// goes through all of the stage views it contains until it finds
		// the one that matches the name

		for (Component c : stages.getComponents()) {
			if (name.equals(c.getName())) {
				return (StageView) c;
			}
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

	/**
	 * 
	 * Remove all instances of stage titles as textboxes.
	 *
	 */
	public void removeChangeTitles() {
		for (Component c : stages.getComponents()) {
			if (c instanceof StageView) {
				if (((StageView) c).getController().isNewStage()) {
					stages.remove((StageView) c);
					controller.reloadData();
				}
				if (((StageView) c).getController() != null) {
					((StageView) c).getController().switchTitle(false);
				}
			}
		}
	}
}
