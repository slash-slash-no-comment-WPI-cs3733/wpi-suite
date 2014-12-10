/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import taskManager.model.ActivityModel;

/**
 * @author Tyler Jaskoviak
 *
 */
public class ActivityView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6524598229849111521L;
	public static final String INFO = "info";
	public static final String MESSAGE_BODY = "message_body";

	private JLabel info;

	private JTextArea message;

	/*
	 * JXDatePicker nt_dueDateField = new JXDatePicker();
	 * nt_dueDateField.setName("due_date"); this.dateField = nt_dueDateField;
	 * dateField.setDate(Calendar.getInstance().getTime());
	 */

	/**
	 * Creates an ActivityView panel, meant to display an activity with name of
	 * user, date, and message (activity/comment)
	 */
	public ActivityView(ActivityModel m) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// Name of the user who created it
		info = new JLabel();
		info.setName(INFO);
		info.setText(m.getDescription() + "  " + m.getActor());

		// Content of the activity
		message = new JTextArea(14, 22);
		message.setText(m.getDescription());
		message.setName(MESSAGE_BODY);
		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		message.setEditable(false);
	}
}
