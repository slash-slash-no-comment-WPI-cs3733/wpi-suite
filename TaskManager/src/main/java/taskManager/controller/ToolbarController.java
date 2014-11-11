/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import taskManager.view.ManageStageView;
import taskManager.view.ToolbarView;
import taskManager.view.WorkflowView;



/**
 * Description A controller for the toolbar  view
 *
 * @author Beth Martino
 */
public class ToolbarController implements ActionListener{

	private final ToolbarView toolbarView;
	private final WorkflowView workflowView;
	//TODO: change JPanels to correct view objects
	private final ManageStageView manageStagesView;
	private final JPanel manageUsersView;
	private final JPanel newTaskView;
	private final JPanel statisticsView;

	/**
	 * 
	 * @param view the toolbar view to be listened to
	 * @param wfv the workflow view to be switched to
	 * @param msv the manageStages view to be switched to
	 * @param muv the manageUsers view to be switched to
	 * @param ntv the newTask view to be switched to
	 * @param sv the statistics view to be switched to
	 */
	public ToolbarController(ToolbarView view, WorkflowView wfv, ManageStageView msv, JPanel muv,
			JPanel ntv, JPanel sv) {
		this.toolbarView = view;
		this.workflowView = wfv;
		this.manageStagesView = msv;
		this.manageUsersView = muv;
		this.newTaskView = ntv;
		this.statisticsView = sv;
		}
	
	@Override
	public void actionPerformed(ActionEvent e){
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton)button).getName();
			switch(name){
			case "createTask": 
				workflowView.setVisible(false);
				manageUsersView.setVisible(false);
				manageStagesView.setVisible(false);
				statisticsView.setVisible(false);
				newTaskView.setVisible(true);
				break;
			case "manageStages": 
				workflowView.setVisible(false);
				manageUsersView.setVisible(false);
				statisticsView.setVisible(false);
				newTaskView.setVisible(false);
				manageStagesView.setVisible(true);
				break;
			case "manageUsers": 
				workflowView.setVisible(false);
				manageStagesView.setVisible(false);
				statisticsView.setVisible(false);
				newTaskView.setVisible(false);
				manageUsersView.setVisible(true);
				break;
			case "statistics": 
				workflowView.setVisible(false);
				manageUsersView.setVisible(false);
				manageStagesView.setVisible(false);
				newTaskView.setVisible(false);
				statisticsView.setVisible(true);
				break;
				
			case "workflow": 
				manageUsersView.setVisible(false);
				manageStagesView.setVisible(false);
				newTaskView.setVisible(false);
				statisticsView.setVisible(false);
				workflowView.setVisible(true);
				break;
			}
		}
	}

}
