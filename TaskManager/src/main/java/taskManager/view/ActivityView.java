/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * @author Tyler Jaskoviak
 *
 */
public class ActivityView extends JPanel {

	public static final String USER_NAME = "user_name";
	public static final String MESSAGE_BODY = "message_body";
	public static final String MESSAGE_DATE = "message_date";

	private JLabel userName;
	private JLabel date;

	private JTextArea message;

	/*
	 * JXDatePicker nt_dueDateField = new JXDatePicker();
	 * nt_dueDateField.setName("due_date"); this.dateField = nt_dueDateField;
	 * dateField.setDate(Calendar.getInstance().getTime());
	 */

	/**
	 * Creates an ActivityView panel, meant to display activity done on a task
	 * displays the activity with name of user, date, and message
	 * (activity/comment)
	 */
	public ActivityView() {

		// Name of the user who created it
		userName = new JLabel();
		userName.setName(USER_NAME);

		// Date the activity happened
		date = new JLabel();
		date.setName(MESSAGE_DATE);

		// Content of the activity
		message = new JTextArea(4, 25);
		message.setName(MESSAGE_BODY);
		message.setLineWrap(true);
		message.setEditable(false);
		JScrollPane messageScrollPane = new JScrollPane(message);
		messageScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		messageScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		setLayout(new GridBagLayout());

		GridBagConstraints newActivityGridBag = new GridBagConstraints();

		// First Column ////

		newActivityGridBag.anchor = GridBagConstraints.FIRST_LINE_START;

		newActivityGridBag.weightx = 0.5;
		newActivityGridBag.weighty = 0.2;

		newActivityGridBag.gridx = 0;

		newActivityGridBag.gridy = 0;
		add(userName, newActivityGridBag);

		newActivityGridBag.weighty = 0.8;
		newActivityGridBag.gridy = 1;
		add(messageScrollPane, newActivityGridBag);

		// Second Column //

		newActivityGridBag.anchor = GridBagConstraints.FIRST_LINE_END;

		newActivityGridBag.weightx = 0.5;
		newActivityGridBag.weighty = 0.2;

		newActivityGridBag.gridx = 0;
		newActivityGridBag.gridy = 0;
		add(date, newActivityGridBag);
	}

	/**
	 * Gets the text in the userName field
	 * 
	 * @return the userName field
	 */
	public JLabel getUser() {
		return userName;
	}

	/**
	 * Gets the text in the date field
	 * 
	 * @return the date field
	 */
	public JLabel getDate() {
		return date;
	}

	/**
	 * Gets the message field
	 * 
	 * @return the message field
	 */
	public JTextArea getMessage() {
		return message;
	}

	/**
	 * sets the text in the userName field
	 * 
	 * @param name
	 *            the text in the userName field
	 */
	public void setUser(String name) {
		userName.setText(name);
	}

	/**
	 * sets the text in the date field
	 * 
	 * @param inputDate
	 *            the text in the date field
	 */
	public void setDate(String inputDate) {
		date.setText(inputDate);
	}

	/**
	 * sets the text in the message field
	 * 
	 * @param content
	 *            the text in the message field
	 */
	public void setMessage(String content) {
		message.setText(content);
	}
}
