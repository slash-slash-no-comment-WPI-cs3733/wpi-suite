/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import taskManager.model.ActivityModel;

/**
 * Description
 *
 * @author Samee Swartz
 * @version Dec 9, 2014
 */
public class ActivityPanel extends JPanel {

	private static final long serialVersionUID = -8384336474859145673L;

	public enum Type {
		COMMENTS, ALL;
	}

	public ActivityPanel(Type type, List<ActivityModel> activityList) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Create list of activities
		JPanel activities = new JPanel();
		if (!(activityList == null)) {
			for (ActivityModel a : activityList) {
				if ((type == Type.COMMENTS)
						&& (a.getType() == ActivityModel.activityModelType.COMMENT)) {
					activities.add(new ActivityView(a));
				} else if (type == Type.ALL) {
					activities.add(new ActivityView(a));
				}
			}
		}
		JScrollPane activityScroll = new JScrollPane(activities);
		add(activityScroll);

	}
}
