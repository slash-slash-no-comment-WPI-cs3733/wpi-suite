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
import javax.swing.JScrollPane;

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
	private JPanel taskInfo;

	public final String STAGES = "Stages";
	public final String TASK_INFO = "Task Info";

	/**
	 * Constructor for WorkflowView.
	 */
	public WorkflowView() {
		// ignores the task info view and arranges the stages horizontally
		// and evenly spaced
		this.setLayout(new WorkflowLayout(this));
		this.stages = new JPanel();
		// this.stages.setName(STAGES);
		this.stages.setLayout(new FlowLayout());

		this.taskInfo = new JPanel();
		this.taskInfo.setName(TASK_INFO);
		this.taskInfo.setLayout(null);

		JScrollPane test = new JScrollPane(stages);
		test.setName(STAGES);
		// test.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// test.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.taskInfo.setVisible(false);
		this.add(test, JLayeredPane.DEFAULT_LAYER);
		this.add(taskInfo, 1);
	}

	/**
	 * @param stv
	 *            of the new stage to be added creates a new scroll panel to
	 *            house the stage view object sets the size and border
	 */
	public void addStageView(StageView stv) {
		stages.add(stv, JLayeredPane.DEFAULT_LAYER);
	}

	public void addTaskInfo(TaskInfoPreviewView ti) {
		// taskInfo.removeAll();
		// taskInfo.setBounds(ti.getTaskLocation().x, ti.getTaskLocation().y +
		// 5,
		// 300, 400);
		// taskInfo.add(ti);
		// taskInfo.setVisible(true);
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
		stages.removeAll();
		taskInfo.removeAll();
	}

	@Override
	public void repaint() {
		stages.repaint();
		taskInfo.repaint();
	}

	@Override
	public void revalidate() {
		stages.revalidate();
		taskInfo.revalidate();
	}
}
