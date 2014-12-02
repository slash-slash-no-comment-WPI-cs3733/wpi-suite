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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.dnd.DropTarget;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import taskManager.controller.ToolbarController;
import taskManager.draganddrop.DDTransferHandler;

/**
 * The Task Managers tab's toolbar panel.
 * 
 * @author Clark Jacobsohn
 */
@SuppressWarnings("serial")
public class ToolbarView extends JToolBar {

	public static final String STATISTICS = "statistics";
	public static final String REFRESH = "refresh";
	public static final String REPORT = "report";
	public static final String MANAGE_STAGES = "manageStages";
	public static final String CREATE_TASK = "createTask";
	public static final String WORKFLOW = "workflow";
	public static final String ARCHIVE = "archive";
	public static final String DELETE = "delete";

	// toolbar information
	private JButton createTask;
	private JButton manageStages;
	private JButton statistics;
	private JLabel archive;
	private JLabel delete;

	private JLabel projectName;

	private ToolbarController controller;

	/**
	 * Create a ToolbarView.
	 * 
	 * @param tabController
	 *            The MainTabController this view should open tabs with
	 */
	public ToolbarView() {

		// Construct and set up the buttons and title panels
		JPanel buttons = new JPanel();
		JPanel title = new JPanel();
		JPanel targets = new JPanel();
		FlowLayout layout = new FlowLayout();
		buttons.setLayout(layout);
		buttons.setOpaque(false);
		title.setLayout(layout);
		title.setOpaque(false);
		targets.setLayout(layout);
		targets.setOpaque(false);

		Insets margins = new Insets(15, 5, 0, 5);
		this.setMargin(margins);

		this.setFloatable(false);

		// Construct the buttons
		createTask = new JButton("Create Task");
		createTask.setName(CREATE_TASK);
		manageStages = new JButton("Manage Stages");
		manageStages.setName(MANAGE_STAGES);
		statistics = new JButton("Statistics");
		statistics.setName(REPORT);

		// Add icons
		Image img;
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"create-task-icon.png"));
			createTask.setIcon(new ImageIcon(img));
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"stages-icon.png"));
			manageStages.setIcon(new ImageIcon(img));
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"reports-icon.png"));
			statistics.setIcon(new ImageIcon(img));

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add archive and delete drop targets
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"archive-icon.png"));
			archive = new JLabel(new ImageIcon(img));
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"delete-icon.png"));
			delete = new JLabel(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		archive.setToolTipText("Drag here to archive task");
		archive.setEnabled(false);
		archive.setName(ARCHIVE);
		JPanel spacer = new JPanel();
		spacer.setMinimumSize(new Dimension(40, 10));
		spacer.setSize(new Dimension(40, 10));
		spacer.setPreferredSize(new Dimension(40, 10));
		delete.setToolTipText("Drag here to delete task");
		delete.setEnabled(false);
		delete.setName(DELETE);

		// Construct the project title
		projectName = new JLabel();
		projectName.setFont(new Font("TextField.font", Font.BOLD, 20));

		// Add buttons to the content panel
		title.add(projectName);
		buttons.add(createTask);
		buttons.add(manageStages);
		buttons.add(statistics);
		targets.add(archive);
		targets.add(spacer);
		targets.add(delete);

		// Title and buttons to the toolbar
		this.add(title);
		this.add(buttons);
		this.add(targets);
	}

	/**
	 * adds the toolbar controller as the action listener for all buttons
	 * 
	 * @param controller
	 *            the toolbar controller to be addded to the buttons
	 */
	public void setController(ToolbarController controller) {
		this.controller = controller;
		createTask.addActionListener(this.controller);
		manageStages.addActionListener(this.controller);
		statistics.addActionListener(this.controller);

		delete.setTransferHandler(new DDTransferHandler());
		delete.setDropTarget(new DropTarget(delete, controller));
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setProjectName(String name) {
		projectName.setText(name);
	}
}
