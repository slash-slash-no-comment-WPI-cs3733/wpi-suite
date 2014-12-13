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
import java.awt.Image;
import java.awt.dnd.DropTarget;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
	private JComboBox<String> languages;

	private JLabel projectName;

	private ToolbarController controller;

	/**
	 * Create a ToolbarView.
	 * 
	 * @param controller
	 *            The ToolbarController associated with this view
	 * @throws IOException
	 */
	public ToolbarView(ToolbarController controller) {

		this.controller = controller;

		// Construct and set up the buttons and title panels
		final JPanel buttons = new JPanel();
		final JPanel name = new JPanel();
		final JPanel targets = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		buttons.setOpaque(false);
		name.setLayout(new BoxLayout(name, BoxLayout.LINE_AXIS));
		name.setOpaque(false);
		targets.setLayout(new BoxLayout(targets, BoxLayout.LINE_AXIS));
		targets.setOpaque(false);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		this.setFloatable(false);

		// Construct the buttons
		createTask = new JButton();
		createTask.setName(CREATE_TASK);
		createTask.setMaximumSize(new Dimension(160, 58));
		createTask.addActionListener(controller);

		createStage = new JButton();
		createStage.setName(CREATE_STAGE);
		createStage.setMaximumSize(new Dimension(160, 58));
		createStage.addActionListener(controller);

		statistics = new JButton();
		statistics.setName(REPORT);
		statistics.setMaximumSize(new Dimension(160, 58));
		statistics.addActionListener(controller);

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

		// Checkbox for toggling showing archived tasks.
		archiveCheckBox = new JCheckBox();
		archiveCheckBox.addItemListener(controller);
		archiveCheckBox.setOpaque(false);

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
		archive.setToolTipText("");
		archive.setEnabled(false);
		archive.setName(ARCHIVE);

		// TODO: look at this
		archive.setTransferHandler(new DDTransferHandler());
		archive.setDropTarget(new DropTarget(archive, controller));

		delete.setToolTipText("");
		delete.setEnabled(false);
		delete.setName(DELETE);

		delete.setTransferHandler(new DDTransferHandler());
		delete.setDropTarget(new DropTarget(delete, controller));

		// Construct the project title
		projectName = new JLabel();
		projectName.setFont(new Font("TextField.font", Font.BOLD, 20));

		// Make language selection drop down
		languages = new JComboBox<String>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths
				.get(getClass().getResource("/taskManager/localization")
						.toURI()))) {
			for (Path entry : stream) {
				String filename = entry.getFileName().toString();
				if (filename.endsWith(".properties")) {
					languages.addItem(filename.substring(0, filename.toString()
							.length() - ".properties".length()));
				}
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		languages.addActionListener(controller);

		// Add title to the title panel
		name.add(Box.createHorizontalStrut(10));
		name.add(projectName);

		// Add buttons with to the button panel
		buttons.add(Box.createHorizontalGlue());
		buttons.add(createTask);
		buttons.add(createStage);
		buttons.add(statistics);
		buttons.add(languages);
		buttons.add(archiveCheckBox);
		buttons.add(Box.createHorizontalGlue());

		// Add targets to the target panel
		targets.add(archive);
		targets.add(new Box.Filler(new Dimension(5, 0), new Dimension(40, 0),
				new Dimension(40, 0)));
		targets.add(delete);
		targets.add(Box.createHorizontalStrut(10));

		// Set vertical alignments of panels to be centered.
		name.setAlignmentY(CENTER_ALIGNMENT);
		buttons.setAlignmentY(CENTER_ALIGNMENT);
		targets.setAlignmentY(CENTER_ALIGNMENT);

		// Add panels to the toolbar
		this.add(name);
		this.add(buttons);
		this.add(targets);

		// Add resize listener to fix title
		this.addComponentListener(controller);

		onLocaleChange();
		Localizer.addListener(this);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * Set the displayed project name
	 *
	 * @param name
	 *            the name of the project
	 */
	public void setProjectName(String name) {
		projectName.setText("<html>" + name + "</html>");
	}

	public boolean isIconEnabled(String iconName) {
		switch (iconName) {
		case ARCHIVE:
		case UNARCHIVE:
			return archive.isEnabled();
		case DELETE:
			return delete.isEnabled();
		default:
			return false;
		}
	}

	/**
	 * Sets if the archive icon is lit up
	 *
	 * @param bool
	 *            if the archive should be enabled or not
	 */
	public void setArchiveEnabled(boolean bool) {
		archive.setEnabled(bool);
	}

	/**
	 * Sets if the delete icon is lit up and whether it is currently a drop
	 * target
	 *
	 * @param bool
	 *            if the delete should be enabled or not
	 */
	public void setDeleteEnabled(boolean bool) {
		delete.setEnabled(bool);
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

	/**
	 * Returns the project name component
	 * 
	 * @return The project name component
	 */
	public JLabel getProjectName() {
		return projectName;
	}

	/**
	 * @return The selected language
	 */
	public String getSelectedLanguage() {
		return (String) languages.getSelectedItem();
	}

	@Override
	public void onLocaleChange() {
		createTask.setText("<html>" + Localizer.getString("CreateTask")
				+ "</html>");
		createStage.setText("<html>" + Localizer.getString("CreateStage")
				+ "</html>");
		statistics.setText("<html>" + Localizer.getString("Reports")
				+ "</html>");
		archiveCheckBox.setText("<html>" + Localizer.getString("ShowArchive")
				+ "</html>");
		archive.setToolTipText(Localizer.getString("DragArchive"));
		delete.setToolTipText(Localizer.getString("DragDelete"));

	}
}
