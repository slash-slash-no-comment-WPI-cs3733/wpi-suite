/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import taskManager.TaskManager;
import taskManager.controller.EditTaskController;
import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.ActivityModelType;

/**
 * @author Tyler Jaskoviak
 *
 */
public class ActivityView extends JPanel {

	private static final long serialVersionUID = 6524598229849111521L;
	public static final String EDIT = "edit";

	private JButton edit;
	private boolean editable = false;

	private ActivityModel activityM;
	private EditTaskController controller;
	private JPanel infoPanel;
	private JPanel text;

	/**
	 * Creates an ActivityView panel, meant to display an activity with name of
	 * user, date, and message (activity/comment)
	 */
	public ActivityView(ActivityModel m, EditTaskController controller) {
		this.activityM = m;
		this.controller = controller;
		setLayout(new MigLayout("", "[][grow, fill]", "0[grow, fill]0"));
		this.setBackground(Colors.ACTIVITY);
		// Border
		final Border raisedbevel = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		final TitledBorder title = BorderFactory
				.createTitledBorder(raisedbevel);
		// title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);

		JLabel info = new JLabel(DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.SHORT).format(m.getDateCreated())
				+ "     " + m.getActor());
		info.setMinimumSize(new Dimension(20, 20));

		infoPanel = new JPanel(new MigLayout("", "0[grow, fill][]", "0[]0"));
		infoPanel.setBackground(Colors.ACTIVITY);
		infoPanel.add(info);
		if (m.getType().equals(ActivityModelType.COMMENT)
				&& TaskManager.currentUser.equals(m.getActor())) {
			editable = true;
			edit = new JButton("Edit");
			edit.setName(EDIT);
			edit.setEnabled(true);
			edit.addActionListener(controller);
			edit.setFocusPainted(false);
			edit.setMargin(new Insets(0, 0, 0, 0));
			edit.setContentAreaFilled(false);
			edit.setBorderPainted(false);
			edit.setOpaque(false);

			infoPanel.add(edit);
		}

		// Content of the activity
		JLabel message = new JLabel("<html>" + m.getDescription() + "</html>");
		message.setFont(message.getFont().deriveFont(Font.PLAIN));

		text = new JPanel(new MigLayout("wrap 1", "0[grow, fill]0", "0[][]0"));
		text.setOpaque(false);
		text.add(infoPanel);
		text.add(message);

		JLabel color = new JLabel();
		color.setMinimumSize(new Dimension(20, 20));
		color.setBackground(Color.BLUE);
		// if (activityM.getType().equals(ActivityModelType.COMMENT)) {
		// color.setBackground(Colors.ACTIVITY_COMMENT);
		// } else {
		// color.setOpaque(false);
		// }

		add(color);
		add(text);
		this.setPreferredSize(new Dimension(50, 20));
	}

	/**
	 * 
	 * Adds a controller to the edit comment button.
	 *
	 * @param controller
	 *            the actionListener to be added to the edit comment button
	 */
	public void setEditController(EditTaskController controller) {
		if (editable) {
			edit.addActionListener(controller);
		}
	}

	/**
	 * 
	 * Create a new ActivityView with the same parameters as the current one.
	 * Also maintain the same background color. Used for adding the "same"
	 * ActivityView to the comments and all activities tabs in ActivityPanel.
	 *
	 * @return a "duplicated" ActivityView.
	 */
	public ActivityView duplicate() {
		ActivityView av = new ActivityView(activityM, controller);
		av.setBackground(this.getBackground());
		return av;
	}

	/**
	 *
	 * @return the ActivityModel for this activity
	 */
	public ActivityModel getModel() {
		return activityM;
	}

	/**
	 * 
	 * If this activity is a comment, return the comment.
	 *
	 * @return the comment for this activity
	 */
	public String getComment() {
		if (activityM.getType() != ActivityModelType.COMMENT) {
			throw new UnsupportedOperationException(
					"This is not a comment activity.");
		}

		return activityM.getDescription();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ActivityView)) {
			return false;
		}
		if (!(activityM.equals(((ActivityView) obj).getModel()))) {
			return false;
		}

		return true;
	}

	@Override
	public void setBackground(Color bg) {
		Color oldBg = getBackground();
		super.setBackground(bg);
		if (infoPanel != null) {
			infoPanel.setBackground(bg);
			text.setBackground(bg);
		}
	}
}
