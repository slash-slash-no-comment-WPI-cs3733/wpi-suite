/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

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
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;

/**
 * The Task Managers tab's toolbar panel.
 * 
 * @author Clark Jacobsohn
 */
@SuppressWarnings("serial")
public class ToolbarView extends JToolBar implements LocaleChangeListener {

	public static final String STATISTICS = "statistics";
	public static final String REFRESH = "refresh";
	public static final String REPORT = "report";
	public static final String MANAGE_USERS = "manageUsers";
	public static final String MANAGE_STAGES = "manageStages";
	public static final String CREATE_TASK = "createTask";
	public static final String WORKFLOW = "workflow";
	public static final String ARCHIVE = "archive";
	public static final String DELETE = "delete";

	// toolbar information
	private JButton createTask = new JButton();
	private JButton manageUsers = new JButton();
	private JButton manageStages = new JButton();
	private JButton statistics = new JButton();
	private JLabel archive = new JLabel();
	private JLabel delete = new JLabel();

	private JButton english;
	private JButton pirate;
	private JButton todo;

	private JLabel projectName = new JLabel();

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
		createTask.setName(CREATE_TASK);
		manageStages.setName(MANAGE_STAGES);
		manageUsers.setName(MANAGE_USERS);
		statistics.setName(REPORT);

		// language select buttons
		english = new JButton("English");
		english.setName(Localizer.ENGLISH);
		pirate = new JButton("Pirate");
		pirate.setName(Localizer.PIRATE);
		todo = new JButton("TODO");
		todo.setName(Localizer.TODO);

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
					"users-icon.png"));
			manageUsers.setIcon(new ImageIcon(img));
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
		archive.setEnabled(false);
		archive.setName(ARCHIVE);
		delete.setEnabled(false);
		delete.setName(DELETE);

		projectName.setFont(new Font("TextField.font", Font.BOLD, 20));

		// Add buttons to the content panel
		title.add(projectName);
		buttons.add(createTask);
		buttons.add(manageStages);
		buttons.add(manageUsers);
		buttons.add(statistics);
		targets.add(archive);
		targets.add(delete);

		buttons.add(english);
		buttons.add(pirate);
		buttons.add(todo);

		// Title and buttons to the toolbar
		this.add(title);
		this.add(buttons);
		this.add(targets);

		Localizer.addListener(this);
		onLocaleChange();
	}

	/**
	 * adds the toolbar controller as the action listener for all buttons
	 * 
	 * @param controller
	 *            the toolbar controller to be addded to the buttons
	 */
	public void setController(ToolbarController controller) {
		this.controller = controller;
		createTask.addActionListener(controller);
		manageStages.addActionListener(controller);
		manageUsers.addActionListener(controller);
		statistics.addActionListener(controller);

		delete.setTransferHandler(new DDTransferHandler());
		delete.setDropTarget(new DropTarget(delete, controller));

		english.addActionListener(controller);
		pirate.addActionListener(controller);
		todo.addActionListener(controller);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setProjectName(String name) {
		projectName.setText(name);
	}

	@Override
	public void onLocaleChange() {
		createTask.setText(Localizer.getString("Create Task"));
		manageStages.setText(Localizer.getString("Manage Stages"));
		manageUsers.setText(Localizer.getString("Manage Users"));
		statistics.setText(Localizer.getString("Statistics"));

		archive.setToolTipText(Localizer.getString("Drag here to archive task"));
		delete.setToolTipText(Localizer.getString("Drag here to delete task"));
	}
}
