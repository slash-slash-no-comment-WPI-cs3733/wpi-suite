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
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.border.DropShadowBorder;

import taskManager.JanewayModule;
import taskManager.controller.TaskController;
import taskManager.controller.TaskInfoPreviewController;
import taskManager.model.TaskModel;

/**
 * The view that pop's up when a task is clicked on.
 *
 * @author Samee Swartz
 * @author Clark Jacobsohn
 * @version Nov 21, 2014
 */
public class TaskInfoPreviewView extends JPanel {

	private static final long serialVersionUID = -3486346306247702460L;
	private TaskModel taskM;
	private TaskController taskC;
	private TaskInfoPreviewController controller;
	public static final String EDIT = "edit";
	public static final String X = "x";
	public final Color SHADOW_COLOR = Color.BLACK;
	public final Color BACKGROUND = Color.decode("#FCFCFC");
	public final int WIDTH = 220;

	public TaskInfoPreviewView(TaskModel model, TaskController controller,
			Point loc) {
		this.taskM = model;
		this.taskC = controller;
		this.controller = new TaskInfoPreviewController(this.taskC);

		this.setLayout(null);
		this.setOpaque(false);

		JPanel bgPane = new JPanel();
		bgPane.setLayout(new MigLayout("wrap 1", "5[]5", "0[]:push[]"));
		setBoundsWithoutClipping(loc, 245, 415);

		bgPane.setBackground(BACKGROUND);
		Border color = BorderFactory.createLineBorder(getBackground(), 3);
		DropShadowBorder shadow = new DropShadowBorder();
		shadow.setShadowColor(Color.BLACK);
		shadow.setShowLeftShadow(true);
		shadow.setShowRightShadow(true);
		shadow.setShowBottomShadow(true);
		shadow.setShowTopShadow(true);
		shadow.setShadowSize(10);
		Border compound = BorderFactory.createCompoundBorder(shadow, color);
		this.setBorder(compound);

		// this.setBorder(shadow);

		// This panel will contain all of the task information
		JPanel info = new JPanel();
		info.setSize(new Dimension(this.getWidth(), 345));
		info.setPreferredSize(new Dimension(this.getWidth(), 345));
		info.setMinimumSize(new Dimension(this.getWidth(), 345));
		info.setMaximumSize(new Dimension(this.getWidth(), 345));
		info.setLayout(new MigLayout("wrap 1"));
		info.setOpaque(false);

		// The task's titleBar contains the title and the 'x' button
		JPanel titleBar = new JPanel();
		titleBar.setLayout(new MigLayout("", "0[]:push[]"));
		titleBar.setSize(new Dimension(this.getWidth(), 30));
		JLabel title = new JLabel(this.taskM.getName());
		title.setFont(title.getFont().deriveFont(15.0f));
		title.setPreferredSize(new Dimension(this.getWidth(), title
				.getPreferredSize().height));
		title.setSize(new Dimension(this.getWidth(),
				title.getPreferredSize().height));
		title.setMaximumSize(new Dimension(this.getWidth(), title
				.getPreferredSize().height));
		titleBar.add(title);
		titleBar.setBackground(BACKGROUND);
		// Closable 'x' button
		final JButton closeButton = new JButton("\u2716");
		closeButton.setName(X);
		closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.addActionListener(this.controller);
		titleBar.add(closeButton);
		info.add(titleBar);

		// The task's description
		JTextArea description = new JTextArea();
		description.setText(ellipsize(this.taskM.getDescription(), 175));
		description.setSize(new Dimension(this.getWidth(), 80));
		description.setMaximumSize(new Dimension(this.getWidth(), 80));
		description.setMinimumSize(new Dimension(this.getWidth(), 80));
		description.setPreferredSize(new Dimension(this.getWidth(), 80));
		description.setAlignmentX(CENTER_ALIGNMENT);
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setBackground(BACKGROUND);
		info.add(description);

		// The task's due date
		JLabel due = new JLabel("Due:");
		final Calendar calDate = Calendar.getInstance();
		calDate.setTime(this.taskM.getDueDate());
		JLabel date = new JLabel("  " + (calDate.get(Calendar.MONTH) + 1) + "/"
				+ calDate.get(Calendar.DATE) + "/"
				+ (calDate.get(Calendar.YEAR)));
		date.setMaximumSize(new Dimension(this.getWidth(), 20));
		info.add(due);
		info.add(date);

		// The task's effort
		JLabel estE = new JLabel("Est Effort: "
				+ this.taskM.getEstimatedEffort());
		JLabel actE = new JLabel("Act Effort: " + this.taskM.getActualEffort());
		info.add(estE);
		info.add(actE);

		// The task's users
		JLabel userL;
		JPanel users = new JPanel();
		JScrollPane usersS = new JScrollPane();
		usersS.setSize(new Dimension(this.getWidth(), 60));
		usersS.setMinimumSize(new Dimension(this.getWidth(), 60));
		usersS.setMaximumSize(new Dimension(this.getWidth(), 60));
		usersS.setPreferredSize(new Dimension(this.getWidth(), 60));
		usersS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		Object[] userList = this.taskM.getAssigned().toArray();
		System.out.println(userList);
		if (userList.length > 0) {
			System.out.println(userList[0]);
			userL = new JLabel("Users:");

			for (int i = 0; i < userList.length; i++) {
				JLabel temp = new JLabel(" " + userList[i]);
				temp.setSize(new Dimension(this.getWidth(), 20));
				temp.setMinimumSize(new Dimension(this.getWidth(), 20));
				temp.setMaximumSize(new Dimension(this.getWidth(), 20));
				temp.setPreferredSize(new Dimension(this.getWidth(), 20));
				users.add(temp);
			}
			info.add(userL);
			usersS.setViewportView(users);
			info.add(usersS);
		} else {
			userL = new JLabel("Users: [None]");
			info.add(userL);
		}

		// The task's requirement
		JLabel req;
		if (this.taskM.getReq() == null) {
			req = new JLabel("Requirement: [None]");
			info.add(req);
		} else {
			req = new JLabel("Requirement:");
			info.add(req);
			JLabel name = new JLabel("  " + this.taskM.getReq());
			name.setSize(new Dimension(this.getWidth(), 20));
			name.setMinimumSize(new Dimension(this.getWidth(), 20));
			name.setMaximumSize(new Dimension(this.getWidth(), 20));
			name.setPreferredSize(new Dimension(this.getWidth(), 20));
			info.add(name);
		}

		// This panel contains the edit button
		JPanel buttonPanel = new JPanel(new MigLayout("", "[center]"));
		JButton edit = new JButton("edit");
		edit.setName(EDIT);
		edit.setMargin(new Insets(5, 87, 5, 87));
		edit.addActionListener(this.controller);
		buttonPanel.add(edit, "");
		buttonPanel.setSize(new Dimension(this.getWidth(), 80));
		buttonPanel.setBackground(BACKGROUND);

		bgPane.add(info);
		bgPane.add(buttonPanel);

		this.add(bgPane);
		bgPane.setBounds(0, 0, this.getWidth() - 10, this.getHeight() - 10);

	}

	/**
	 * Sets the bounds of the view to the given location with the given width
	 * and height, correcting for screen edges so that it does not go off screen
	 * 
	 * @param loc
	 *            The location of the view
	 * @param width
	 *            The width of the view
	 * @param height
	 *            The height of the view
	 */
	private void setBoundsWithoutClipping(Point loc, int width, int height) {
		int x, y;
		Rectangle paneBounds = JanewayModule.getTabPaneView().getBounds();
		x = (loc.x + StageView.STAGE_WIDTH + width > (paneBounds.getWidth())) ? loc.x
				- width
				: loc.x + StageView.STAGE_WIDTH;
		y = (loc.y + height > (paneBounds.getHeight() - 35)) ? (int) paneBounds
				.getHeight() - 35 - height : loc.y;
		this.setBounds(x, y, width, height);
	}

	/**
	 * 
	 * returns the taskController.
	 *
	 * @return The taskController
	 */
	public TaskController getTaskController() {
		return taskC;
	}

	/**
	 * 
	 * Takes a String and if its length is greater than max, it truncates and
	 * adds '...'. This example is modified from:
	 * http://stackoverflow.com/questions
	 * /3597550/ideal-method-to-truncate-a-string-with-ellipsis
	 *
	 * @param text
	 *            The string to add '...' to
	 * @param max
	 *            The max number of characters allowed
	 * @return
	 */
	private static String ellipsize(String text, int max) {

		if (text.length() <= max)
			return text;

		// Start by chopping off at the word before max
		// the 3 is to account for '...'
		int end = text.lastIndexOf(' ', max - 3);
		return text.substring(0, end) + "...";
	}
}
