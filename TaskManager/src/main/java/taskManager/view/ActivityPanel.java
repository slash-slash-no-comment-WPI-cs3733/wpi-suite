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

	private JTextArea commentBox;
	private JButton submit;

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
		this.setLayout(new MigLayout("wrap 1", "[grow, fill]",
				"[grow, fill][][]"));
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

		// Comment textbox
		commentBox = new JTextArea();
		commentBox.setRows(5);
		commentBox.setWrapStyleWord(true);
		commentBox.setLineWrap(true);
		commentBox.addKeyListener(controller);

		JScrollPane commentScroll = new JScrollPane(commentBox,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		commentScroll.setMinimumSize(new Dimension(20, 100));

		// Buttons
		JPanel buttons = new JPanel();
		buttons.setOpaque(false);
		submit = new JButton("Submit");
		submit.setName(ActivityController.SUBMIT);
		submit.addActionListener(controller);
		submit.setEnabled(false);
		buttons.add(submit);
		buttons.setMaximumSize(new Dimension(10000, 40));

		add(activityScroll);
		add(commentScroll);
		add(buttons);

		if (type == Type.ALL) {
			disableEditing();
		}
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
	 * Disables editing of comments
	 */
	private void disableEditing() {
		commentBox.setEditable(false);
		submit.setEnabled(false);
	}

	/**
	 * Sets the comment submit button to be enabled or disabled.
	 * 
	 * @param e
	 *            true to make the submit button enabled, false to disable it
	 */
	public void setCommentSubmitEnabled(boolean e) {
		submit.setEnabled(e);
	}

	/**
	 * Returns the text in the comments field.
	 * 
	 * @return The text in the comments field
	 */
	public String getCommentsFieldText() {
		return commentBox.getText();
	}

	/**
	 * Clears the text in the comments field.
	 */
	public void clearText() {
		commentBox.setText("");
	}
}
