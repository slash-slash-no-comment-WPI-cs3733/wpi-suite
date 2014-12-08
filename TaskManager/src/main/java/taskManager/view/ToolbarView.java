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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	public static final String CREATE_STAGE = "createStage";
	public static final String CREATE_TASK = "createTask";
	public static final String WORKFLOW = "workflow";
	public static final String ARCHIVE = "archive";
	public static final String UNARCHIVE = "unarchive";
	public static final String DELETE = "delete";

	// toolbar information
	private JButton createTask;
	private JButton createStage;
	private JButton statistics;
	private JLabel archive;
	private JLabel delete;
	private JCheckBox archiveCheckBox;

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
		FlowLayout flowLayout = new FlowLayout();
		BoxLayout toolbarLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		BoxLayout targetsLayout = new BoxLayout(targets, BoxLayout.LINE_AXIS);
		buttons.setLayout(flowLayout);
		buttons.setOpaque(false);
		title.setLayout(flowLayout);
		title.setOpaque(false);
		targets.setLayout(targetsLayout);
		targets.setOpaque(false);
		this.setLayout(toolbarLayout);

		Insets margins = new Insets(15, 5, 0, 5);
		this.setMargin(margins);

		this.setFloatable(false);

		// Construct the buttons
		createTask = new JButton("Create Task");
		createTask.setName(CREATE_TASK);

		createStage = new JButton("Create Stage");
		createStage.setName(CREATE_STAGE);

		statistics = new JButton("Statistics");
		statistics.setName(REPORT);

		// Add icons
		Image img;
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"create-task-icon.png"));
			createTask.setIcon(new ImageIcon(img));
			img = ImageIO.read(this.getClass().getResourceAsStream(
					"create-stage-icon.png"));
			createStage.setIcon(new ImageIcon(img));
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

		// Checkbox for toggling showing archived tasks.
		archiveCheckBox = new JCheckBox("Show archived tasks");

		archive.setName(ARCHIVE);
		delete.setToolTipText("Drag here to delete task");
		delete.setEnabled(false);
		delete.setName(DELETE);

		// Construct the project title
		projectName = new JLabel();
		projectName.setFont(new Font("TextField.font", Font.BOLD, 20));

		// Add buttons to the content panel
		title.add(projectName);
		buttons.add(createTask);
		buttons.add(createStage);
		buttons.add(statistics);
		buttons.add(archiveCheckBox);
		targets.add(archive);
		targets.add(new Box.Filler(new Dimension(5, 0), new Dimension(40, 0),
				new Dimension(40, 0)));
		targets.add(delete);

		// Title and buttons to the toolbar
		this.add(title);
		this.add(Box.createHorizontalGlue());
		this.add(buttons);
		this.add(Box.createHorizontalGlue());
		this.add(targets);
		this.add(Box.createHorizontalGlue());
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
		createStage.addActionListener(this.controller);
		statistics.addActionListener(this.controller);

		archiveCheckBox.addItemListener(controller);

		archive.setTransferHandler(new DDTransferHandler());
		archive.setDropTarget(new DropTarget(delete, controller));

	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setProjectName(String name) {
		projectName.setText("<html>" + name + "</html>");
	}

	public void setArchiveEnabled(boolean bool) {
		archive.setEnabled(bool);
	}

	public void setDeleteEnabled(boolean bool) {
		delete.setEnabled(bool);

		if (bool) {
			delete.setTransferHandler(new DDTransferHandler());
			delete.setDropTarget(new DropTarget(delete, controller));
			return;
		}
		delete.setTransferHandler(null);
		delete.setDropTarget(null);
	}

	public boolean isArchiveShown() {
		return archiveCheckBox.isSelected();
	}

	/**
	 * 
	 * Sets the archive icon to the specified type.
	 *
	 * @param iconType
	 *            the string that describes which type to set the icon to.
	 */
	public void setArchiveIcon(String iconType) {
		String imgFile = "";
		if (iconType.equals(ARCHIVE)) {
			imgFile = "archive-icon.png";
		} else if (iconType.equals(UNARCHIVE)) {
			imgFile = "unarchive-icon.png";
		}
		try {
			archive.setIcon(new ImageIcon(ImageIO.read(this.getClass()
					.getResourceAsStream(imgFile))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
