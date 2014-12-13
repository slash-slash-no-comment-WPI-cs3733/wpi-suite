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

import net.miginfocom.swing.MigLayout;
import taskManager.controller.ActivityController;
import taskManager.model.ActivityModel;

/**
 * This is a panel which contains a list of ActivityView's, a JTextArea, a
 * submit button, and a cancel button.
 *
 * @author Samee Swartz
 * @author Clark Jacobsohn
 * @version Dec 9, 201
 */
public class ActivityPanel extends JPanel {

	private static final long serialVersionUID = -8384336474859145673L;

	JPanel activities;

	public enum Type {
		COMMENTS, ALL;
	}

	private Type type;

	/**
	 * Constructs an ActivityPanel with the given type, list of activities, and
	 * activity controller.
	 * 
	 * @param type
	 *            The type of the activity panel, either COMMENTS or ALL
	 * @param activityList
	 *            The list of activities to show in the panel
	 * @param controller
	 *            The ActivityController that controls this panel
	 */
	public ActivityPanel(Type type, List<ActivityModel> activityList,
			ActivityController controller) {
		this.type = type;
		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new MigLayout("wrap 1", "[grow, fill]", "[grow, fill]"));
		this.setOpaque(false);

		// Create list of activities
		activities = new JPanel();
		activities.setLayout(new MigLayout("wrap 1", "0[grow, fill]0", "0[]"));
		if (!(activityList == null)) {
			for (ActivityModel a : activityList) {
				if ((type == Type.COMMENTS)
						&& (a.getType() == ActivityModel.ActivityModelType.COMMENT)) {
					activities.add(new ActivityView(a));
				} else if (type == Type.ALL) {
					activities.add(new ActivityView(a));
				}
			}
		}

		// Activities
		JScrollPane activityScroll = new JScrollPane(activities,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Scrolls to the bottom of the window where the most recent things are.
		scrollActivitiesToBottom();
		activityScroll.getVerticalScrollBar().setUnitIncrement(12);

		add(activityScroll);
	}

	/**
	 * Reloads the activities panel with the list of activities given.
	 * 
	 * @param activityList
	 *            The list of activities to show in the panel
	 */
	public void reloadActivities(List<ActivityModel> activityList) {
		activities.removeAll();
		if (!(activityList == null)) {
			for (ActivityModel a : activityList) {
				if ((type == Type.COMMENTS)
						&& (a.getType() == ActivityModel.ActivityModelType.COMMENT)) {
					activities.add(new ActivityView(a));
				} else if (type == Type.ALL) {
					activities.add(new ActivityView(a));
				}
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
		Rectangle rect = new Rectangle(0, (int) activities.getPreferredSize()
				.getHeight(), 10, 10);
		activities.scrollRectToVisible(rect);
	}
}
