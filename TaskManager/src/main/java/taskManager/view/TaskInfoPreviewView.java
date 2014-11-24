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
import java.awt.FlowLayout;
import java.awt.Point;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.border.DropShadowBorder;

import taskManager.controller.TaskController;
import taskManager.controller.TaskInfoPreviewController;
import taskManager.model.TaskModel;

/**
 * Description
 *
 * @author Samee Swartz
 * @version Nov 21, 2014
 */
public class TaskInfoPreviewView extends JPanel {
	private TaskModel taskM;
	private TaskController taskC;
	private TaskInfoPreviewController controller;
	private Point taskLoc;

	public TaskInfoPreviewView(TaskModel model, TaskController controller,
			Point loc) {
		this.taskM = model;
		this.taskC = controller;
		this.taskLoc = loc;
		this.controller = new TaskInfoPreviewController(this.taskC);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBounds(0, 0, 300, 400);

		// this.taskV.getLocation().y, 300, 400);
		// this.setPreferredSize(new Dimension(300, 400));

		// Drop shadow
		DropShadowBorder shadow = new DropShadowBorder();
		shadow.setShadowColor(Color.BLACK);
		shadow.setShowLeftShadow(true);
		shadow.setShowRightShadow(true);
		shadow.setShowBottomShadow(true);
		shadow.setShowTopShadow(true);
		shadow.setShadowSize(10);
		this.setBorder(shadow);

		JPanel top = new JPanel();
		// top.setBackground(Color.LIGHT_GRAY);
		top.setLayout(new FlowLayout());
		JButton edit = new JButton("edit");
		edit.addActionListener(this.controller);
		JLabel title = new JLabel(this.taskM.getName());
		JLabel description = new JLabel(this.taskM.getDescription());
		JLabel info = new JLabel();
		// info.setBackground(Color.LIGHT_GRAY);
		String text = "Due " + this.taskM.getDueDate() + "\nEst Effort: "
				+ this.taskM.getEstimatedEffort() + "\nAct Effort: "
				+ this.taskM.getActualEffort();
		info.setText(text);

		top.add(title);
		top.add(edit);
		this.add(top);
		this.add(description);
		this.add(info);
	}

	public TaskController getTaskController() {
		return taskC;
	}

	public Point getTaskLocation() {
		return taskLoc;
	}
}
