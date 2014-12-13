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
import java.awt.dnd.DropTarget;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import taskManager.controller.WorkflowController;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DropAreaPanel;
import taskManager.draganddrop.DropTargetRedispatcher;

/**
 * @author Beth Martino
 * @author Clark Jacobsohn
 * @version November 9, 2014
 */
public class WorkflowView extends JLayeredPane {

	private static final long serialVersionUID = 1L;

	private WorkflowController controller;

	private DropAreaPanel stages;
	private JTextField search;

	/**
	 * Constructor for WorkflowView.
	 * 
	 * @param controller
	 *            The workflow controller for this view
	 */
	public WorkflowView(WorkflowController controller) {
		this.controller = controller;
		this.addMouseListener(controller);

		// The stages panel accepts stage drops
		stages = new DropAreaPanel(DDTransferHandler.getStageFlavor());
		stages.setSaveListener(controller);

		// arranges the stages horizontally and evenly spaced
		this.setLayout(new WorkflowLayout());

		stages.setLayout(new BoxLayout(stages, BoxLayout.LINE_AXIS));
		this.add(stages);
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

			// Redispatch drag events down to DropAreaPanel
			this.setDropTarget(new DropTarget(this, new DropTargetRedispatcher(
					stages, DDTransferHandler.getStageFlavor())));
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
	 * returns the text in the search bar
	 * 
	 * @return
	 * 
	 * @return getSearch
	 */
	public JTextField getSearch() {
		return search;
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
			controller.removeTaskInfos(false);
			controller.reloadData();
		}
		super.setVisible(visible);
	}
}
