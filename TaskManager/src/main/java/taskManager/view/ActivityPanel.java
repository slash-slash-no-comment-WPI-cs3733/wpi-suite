/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import taskManager.model.ActivityModel;

/**
 * This is a panel which contains a list of ActivityView's, a JTextArea, a
 * submit button, and a cancel button.
 *
 * @author Samee Swartz
 * @version Dec 9, 2014
 */
public class ActivityPanel extends JPanel {

	private static final long serialVersionUID = -8384336474859145673L;

	private static final String SUBMIT = "submit";
	private static final String CANCEL = "cancel";

	private JTextArea commentBox;
	private JButton submit;
	private JButton cancel;

	public enum Type {
		COMMENTS, ALL;
	}

	public ActivityPanel(Type type, List<ActivityModel> activityList) {
		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new MigLayout("wrap 1", "[grow, fill]",
				"[grow, fill][][]"));
		this.setOpaque(false);

		// Create list of activities
		JPanel activities = new JPanel();
		activities.setLayout(new MigLayout("wrap 1", "0[grow, fill]0", "0[]"));
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

		// Activities
		JScrollPane activityScroll = new JScrollPane(activities,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Comment textbox
		commentBox = new JTextArea();
		commentBox.setRows(5);
		commentBox.setWrapStyleWord(true);
		commentBox.setLineWrap(true);
		JScrollPane commentScroll = new JScrollPane(commentBox,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		commentScroll.setMinimumSize(new Dimension(20, 100));

		// Buttons
		JPanel buttons = new JPanel();
		buttons.setOpaque(false);
		submit = new JButton("Submit");
		submit.setName(SUBMIT);
		cancel = new JButton("Cancel");
		submit.setName(CANCEL);
		buttons.add(submit);
		buttons.add(cancel);
		buttons.setMaximumSize(new Dimension(10000, 40));

		if (type.equals(Type.ALL)) {
			disableEditing();
		}

		add(activityScroll);
		add(commentScroll);
		add(buttons);
	}

	public void disableEditing() {
		commentBox.setEnabled(false);
		submit.setEnabled(false);
		cancel.setEnabled(false);
	}
}
