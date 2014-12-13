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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.ActivityModelType;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ActivityPanel;

/**
 * Controller for a single task's activities. Controlls both the comments
 * ActivityPanel and the all activities ActivityPanel.
 *
 * @author Clark Jacobsohn
 */
public class ActivityController implements ActionListener, KeyListener {

	public static final String SUBMIT = "submit";

	private TaskModel taskM;
	private List<ActivityModel> activities;
	private ActivityPanel activityPanel;
	private ActivityPanel commentsPanel;

	/**
	 * Constructs an ActivityController for a given task
	 * 
	 * @param taskM
	 *            The task whose activities are being controlled. If this is
	 *            null, then activities are being tracked for a new task.
	 */
	public ActivityController(TaskModel taskM) {
		this.taskM = taskM;
		if (taskM != null) {
			activities = taskM.getActivities();
		} else {
			// If the task is null, make a list for the new task's activities
			activities = new ArrayList<ActivityModel>();
		}
		this.activityPanel = new ActivityPanel(ActivityPanel.Type.ALL,
				activities, this);
		this.commentsPanel = new ActivityPanel(ActivityPanel.Type.COMMENTS,
				activities, this);
	}

	/**
	 * 
	 * Reloads the activities panel.
	 *
	 */
	public void reloadActivitiesPanel() {
		if (taskM != null) {
			activities = taskM.getActivities();
		}
		activityPanel.reloadActivities(activities);
		commentsPanel.reloadActivities(activities);
	}

	/**
	 * 
	 * Adds an activity.
	 *
	 * @param act
	 *            the activity.
	 */
	public void addActivity(ActivityModel act) {
		if (taskM != null) {
			taskM.addActivity(act);
		} else {
			activities.add(act);
		}
		reloadActivitiesPanel();
	}

	/**
	 * enables or disables the comment submit button
	 * 
	 * @param e
	 *            true is enabled false is disabled
	 */
	public void setCommentSubmitEnabled(boolean e) {
		commentsPanel.setCommentSubmitEnabled(e);
	}

	/**
	 * Returns the activities panel for this task, showing all activities
	 * 
	 * @return the activities panel for this task
	 */
	public ActivityPanel getActivitiesPanel() {
		return activityPanel;
	}

	/**
	 * Returns the comments panel for this task, showing just comments
	 * 
	 * @return the comments panel for this task
	 */
	public ActivityPanel getCommentsPanel() {
		return commentsPanel;
	}

	/**
	 * Returns the list of activities
	 * 
	 * @return the list of activities
	 */
	public List<ActivityModel> getActivities() {
		return activities;
	}

	/**
	 * Returns the text in the comments panel text box.
	 * 
	 * @return The text in the comments panel text box
	 */
	public String getCommentsFieldText() {
		return commentsPanel.getCommentsFieldText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (((JButton) e.getSource()).getName().equals(SUBMIT)) {
			ActivityModel comment = new ActivityModel(getCommentsFieldText(),
					ActivityModelType.COMMENT);
			// add the activity
			addActivity(comment);
			commentsPanel.clearText();
			WorkflowModel.getInstance().save();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		validateSubmitButton();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (validateSubmitButton() && e.getKeyCode() == 10) {
			ActionEvent ae = new ActionEvent(commentsPanel.getSubmitBtn(), 0,
					null);
			actionPerformed(ae);
			commentsPanel.scrollActivitiesToBottom();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		validateSubmitButton();
	}

	/**
	 * Checks if the comment box input is valid and sets the comment submit
	 * button accordingly.
	 */
	private boolean validateSubmitButton() {
		if (commentsPanel.getCommentsFieldText().trim().isEmpty()) {
			commentsPanel.setCommentSubmitEnabled(false);
			return false;
		} else {
			commentsPanel.setCommentSubmitEnabled(true);
			return true;
		}
	}
}
