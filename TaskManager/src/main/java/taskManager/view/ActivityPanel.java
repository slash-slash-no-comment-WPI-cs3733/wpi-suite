/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.Rectangle;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;
import taskManager.controller.EditTaskController;
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;
import taskManager.model.ActivityModel;

/**
 * This is a panel which contains a list of ActivityView's, a JTextArea, a
 * submit button, and a cancel button.
 *
 * @author Samee Swartz
 * @author Clark Jacobsohn
 * @version Dec 9, 2014
 */
public class ActivityPanel extends JTabbedPane implements LocaleChangeListener {

	private static final long serialVersionUID = -8384336474859145673L;

	private JPanel activities;
	private JPanel comments;
	private EditTaskController controller;
	private ActivityView activityBeingEdited;
	private List<ActivityModel> activityList;

	/**
	 * Constructs an ActivityPanel with the given type, list of activities, and
	 * activity controller.
	 * 
	 * @param activityList
	 *            The list of activities to show in the panel
	 * @param controller
	 *            The EditTaskController that controls the ActivityView's
	 */
	public ActivityPanel(List<ActivityModel> activityList,
			EditTaskController controller) {
		this.activityList = activityList;
		this.controller = controller;
		this.setOpaque(false);

		// Create list of activities
		activities = new JPanel();
		activities.setLayout(new MigLayout("wrap 1", "0[grow, fill]0", "0[]"));

		comments = new JPanel();
		comments.setLayout(new MigLayout("wrap 1", "0[grow, fill]0", "0[]"));

		reloadActivities(activityList);

		// Activities
		JScrollPane activityScroll = new JScrollPane(activities,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Scrolls to the bottom of the window where the most recent things are.
		activityScroll.getVerticalScrollBar().setUnitIncrement(12);

		// Comments
		JScrollPane commentsScroll = new JScrollPane(comments,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Scrolls to the bottom of the window where the most recent things are.
		commentsScroll.getVerticalScrollBar().setUnitIncrement(12);

		scrollActivitiesToBottom();
		addTab("", commentsScroll);
		addTab("", activityScroll);
		onLocaleChange();
		Localizer.addListener(this);
	}

	/**
	 * Reloads the activities panel with the list of activities given.
	 * 
	 * @param activityList
	 *            The list of activities to show in the panel
	 */
	public void reloadActivities(List<ActivityModel> activityList) {
		// if this is called by a function outside this class the incoming list
		// is most likely newer than the stored list.
		this.activityList = activityList;
		activities.removeAll();
		comments.removeAll();
		if (!(activityList == null)) {
			for (ActivityModel a : activityList) {
				ActivityView activity = new ActivityView(a, controller);
				if (activity.equals(activityBeingEdited)) {
					activity.setBackground(Colors.ACTIVITY_COMMENT);
					activity.setEditing(true);
				}

				if (a.getType() == ActivityModel.ActivityModelType.COMMENT) {
					comments.add(activity.duplicate());
				}
				activities.add(activity);
			}
		}
		this.repaint();
	}

	/**
	 * 
	 * Makes the activitiesScroll JScrollPanel scroll to the bottom.
	 *
	 */
	public void scrollActivitiesToBottom() {
		Rectangle rectA = new Rectangle(0, (int) activities.getPreferredSize()
				.getHeight(), 10, 10);
		activities.scrollRectToVisible(rectA);

		Rectangle rectC = new Rectangle(0, (int) comments.getPreferredSize()
				.getHeight(), 10, 10);
		comments.scrollRectToVisible(rectC);
	}

	/**
	 * 
	 * Set which ActivityView is currently being editted.
	 *
	 * @param v
	 *            the ActivityView being editted.
	 */
	public void setEditedTask(ActivityView v) {
		activityBeingEdited = v;
		reloadActivities(activityList);
	}

	/**
	 *
	 * @return the ActivityView being editted
	 */
	public ActivityView getEditedTask() {
		return activityBeingEdited;
	}

	@Override
	public void onLocaleChange() {
		if (getTabCount() > 1) {
			setTitleAt(0, Localizer.getString("Comments"));
			setToolTipTextAt(0, Localizer.getString("Comments"));

			setTitleAt(1, Localizer.getString("AllActivities"));
			setToolTipTextAt(1, Localizer.getString("AllActivities"));
		}
	}
}
