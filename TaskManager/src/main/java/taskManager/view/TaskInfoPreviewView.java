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
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.border.DropShadowBorder;

import taskManager.controller.TabPaneController;
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

	public static final String NAME = "TaskInfoPreviewView";

	private static final long serialVersionUID = -3486346306247702460L;
	private final TaskModel taskM;
	private final TaskController taskC;
	private final TaskInfoPreviewController controller;
	public static final String EDIT = "edit";
	public static final String X = "x";
	private JButton edit;
	public static final int WIDTH = 220;

	/**
	 * Constructs a TaskInfoPreviewView for a task based on the given TaskModel
	 * and TaskController. It will be located next to the TaskView, whose
	 * location is given by the parameter loc.
	 * 
	 * @param model
	 *            The TaskModel for the associated task
	 * @param controller
	 *            The TaskController for the associated task
	 * @param loc
	 *            The location of the associated TaskView
	 */
	public TaskInfoPreviewView(TaskModel model, TaskController controller,
			Point loc) {
		taskM = model;
		taskC = controller;
		this.controller = new TaskInfoPreviewController(taskC);

		this.setLayout(null);
		this.setOpaque(false);
		this.setName(NAME);

		final JPanel bgPane = new JPanel();
		bgPane.setLayout(new MigLayout("wrap 1", "5[]5", "0[]:push[]"));
		setBoundsWithoutClipping(loc, 245, 415);

		bgPane.setBackground(Colors.TASK);
		final Border color = BorderFactory.createLineBorder(getBackground(), 3);
		final DropShadowBorder shadow = new DropShadowBorder();
		shadow.setShadowColor(Color.BLACK);
		shadow.setShowLeftShadow(true);
		shadow.setShowRightShadow(true);
		shadow.setShowBottomShadow(true);
		shadow.setShowTopShadow(true);
		shadow.setShadowSize(10);
		final Border compound = BorderFactory.createCompoundBorder(shadow,
				color);
		this.setBorder(compound);

		// This panel will contain all of the task information
		JPanel info = new JPanel();
		Dimension infoSize = new Dimension(this.getWidth(), 345);
		info.setSize(infoSize);
		info.setPreferredSize(infoSize);
		info.setMinimumSize(infoSize);
		info.setMaximumSize(infoSize);
		info.setLayout(new MigLayout("wrap 1"));
		info.setOpaque(false);

		// The task's titleBar contains the title and the 'x' button

		JPanel titleBar = new JPanel();
		titleBar.setLayout(new MigLayout("wrap 2", "5[]:push[]", "[]0[center]"));
		Dimension titleBarSize = new Dimension(this.getWidth(), 30);
		titleBar.setSize(titleBarSize);
		JLabel title = new JLabel(this.taskM.getName());
		Dimension titleSize = new Dimension(190,
				title.getPreferredSize().height);

		title.setFont(title.getFont().deriveFont(15.0f));
		title.setPreferredSize(titleSize);
		title.setSize(titleSize);
		title.setMaximumSize(titleSize);
		titleBar.add(title);
		// Closable 'x' button
		final JButton closeButton = new JButton("\u2716");
		closeButton.setName(X);
		closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.addActionListener(this.controller);
		titleBar.add(closeButton);
		if (model.isArchived()) {
			titleBar.setBackground(Colors.ARCHIVE_CLICKED);
		} else {
			titleBar.setBackground(Colors.TASK_CLICKED);
		}

		// if the task is archived, say so.
		if (taskC.isArchived()) {
			JLabel archived = new JLabel(
					"<html><font size=\"3\"><i>Archived</i></font></html>",
					SwingConstants.CENTER);
			archived.setFont(archived.getFont().deriveFont(Font.PLAIN));
			Dimension archivedSize = new Dimension(this.getWidth() - 40, 5);
			archived.setSize(archivedSize);
			archived.setPreferredSize(archivedSize);
			titleBar.add(archived, "span");
		}
		info.add(titleBar);

		// The task's description

		JTextArea description = new JTextArea();
		description.setText(this.taskM.getDescription());

		description.setAlignmentX(CENTER_ALIGNMENT);
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setBackground(Colors.TASK);
		description.setCaretPosition(0);
		JScrollPane descScroll = new JScrollPane(description);
		Dimension descScrollSize = new Dimension(this.getWidth() - 30, 80);
		descScroll.setSize(descScrollSize);
		descScroll.setMaximumSize(descScrollSize);
		descScroll.setMinimumSize(descScrollSize);
		descScroll.setPreferredSize(descScrollSize);
		// These remove the border around the JScrollPane. Might be wanted later
		// Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		// descScroll.setViewportBorder(border);
		// descScroll.setBorder(border);
		info.add(descScroll);

		JPanel spacer = new JPanel();
		Dimension spacerSize = new Dimension(50, 5);
		spacer.setSize(spacerSize);
		spacer.setPreferredSize(spacerSize);
		spacer.setMaximumSize(spacerSize);
		spacer.setMinimumSize(spacerSize);
		spacer.setBackground(Colors.TASK);
		info.add(spacer);

		// The task's due date
		// final Calendar calDate = Calendar.getInstance();
		// calDate.setTime();
		// The task's due date
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		JLabel dueDate = new JLabel("<html><b><i>Due:</i></b> "
				+ df.format(this.taskM.getDueDate()) + "</html>");

		dueDate.setFont(dueDate.getFont().deriveFont(Font.PLAIN));
		dueDate.setMaximumSize(new Dimension(this.getWidth(), 20));
		info.add(dueDate);

		// The task's effort
		JLabel estE = new JLabel("<html><b><i>Est Effort: </i></b>"
				+ this.taskM.getEstimatedEffort() + "</html>");
		estE.setFont(estE.getFont().deriveFont(Font.PLAIN));
		JLabel actE = new JLabel("<html><b><i>Act Effort: </i></b>"
				+ this.taskM.getActualEffort() + "</html>");
		actE.setFont(actE.getFont().deriveFont(Font.PLAIN));
		info.add(estE);
		info.add(actE);

		Set<String> userList = taskM.getAssigned();
		// if there are users, add a scrollList to show them. Else just print
		// '[None]'
		if (!userList.isEmpty()) {
			// The task's users
			ScrollList users = new ScrollList("<html><i>Users:</i></html>");
			Dimension usersSize = new Dimension(this.getWidth() - 30, 70);
			users.setSize(usersSize);
			users.setPreferredSize(usersSize);
			users.setMaximumSize(usersSize);
			users.setMinimumSize(usersSize);

			for (String u : userList) {
				if (!users.contains(u)) {
					users.addToList(u);
				}
			}
			users.setEnabled(false);
			info.add(users);
		} else {
			JLabel users = new JLabel(
					"<html><b><i>Users:</i></b> [None]</html>");
			users.setFont(users.getFont().deriveFont(Font.PLAIN));
			info.add(users);
		}

		// The task's requirement
		JLabel req;
		if (this.taskM.getReq() == null) {
			req = new JLabel("<html><b><i>Requirement:</i></b> [None]</html>");
			req.setFont(req.getFont().deriveFont(Font.PLAIN));
			info.add(req);
		} else {
			req = new JLabel("<html><b><i>Requirement:</i></b></html>");
			req.setFont(req.getFont().deriveFont(Font.PLAIN));
			info.add(req);
			final JLabel name = new JLabel("  " + taskM.getReq());
			name.setFont(name.getFont().deriveFont(Font.PLAIN));
			Dimension nameSize = new Dimension(this.getWidth() - 30, 20);
			name.setSize(nameSize);
			name.setMinimumSize(nameSize);
			name.setMaximumSize(nameSize);
			name.setPreferredSize(nameSize);
			info.add(name);
		}

		// This panel contains the edit button
		edit = new JButton(EDIT);
		final JPanel buttonPanel = new JPanel();
		edit.setName(EDIT);
		edit.setMargin(new Insets(5, 77, 5, 77));
		edit.addActionListener(this.controller);
		// Add the pencil image to the edit button
		try {
			Image img = ImageIO.read(this.getClass().getResourceAsStream(
					"edit.png"));
			edit.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}

		buttonPanel.add(edit, "");
		buttonPanel.setSize(new Dimension(this.getWidth(), 80));
		buttonPanel.setBackground(Colors.TASK);

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

		final Rectangle paneBounds = TabPaneController.getInstance().getView()
				.getBounds();
		final int x = (loc.x + StageView.STAGE_WIDTH + width > (paneBounds
				.getWidth())) ? loc.x - width : loc.x + StageView.STAGE_WIDTH;
		final int y = (loc.y + height > (paneBounds.getHeight() - 35)) ? (int) paneBounds
				.getHeight() - 35 - height
				: loc.y;

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

}
