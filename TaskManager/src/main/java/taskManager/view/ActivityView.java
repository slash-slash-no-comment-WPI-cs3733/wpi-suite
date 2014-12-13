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
import java.awt.Font;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import taskManager.model.ActivityModel;

/**
 * @author Tyler Jaskoviak
 *
 */
public class ActivityView extends JPanel {

	private static final long serialVersionUID = 6524598229849111521L;
	public static final String INFO = "info";
	public static final String MESSAGE_BODY = "message_body";

	private JLabel info;

	private JLabel message;

	/**
	 * Creates an ActivityView panel, meant to display an activity with name of
	 * user, date, and message (activity/comment)
	 */
	public ActivityView(ActivityModel m) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Colors.ACTIVITY);
		// Border
		final Border raisedbevel = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		final TitledBorder title = BorderFactory
				.createTitledBorder(raisedbevel);
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);
		info = new JLabel(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.SHORT).format(m.getDateCreated())
				+ "     " + m.getActor());
		info.setName(INFO);
		info.setMinimumSize(new Dimension(20, 20));

		// Content of the activity
		message = new JLabel("<html>" + m.getDescription() + "</html>");
		message.setName(MESSAGE_BODY);
		message.setFont(message.getFont().deriveFont(Font.PLAIN));

		add(info);
		add(message);

		// this.setMinimumSize(new Dimension(50, 20));
		this.setPreferredSize(new Dimension(50, 20));
	}
}
