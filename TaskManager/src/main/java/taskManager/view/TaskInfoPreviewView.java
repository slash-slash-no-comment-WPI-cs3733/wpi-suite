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
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;
import taskManager.model.TaskModel;

/**
 * The view that pop's up when a task is clicked on.
 *
 * @author Samee Swartz
 * @author Clark Jacobsohn
 * @version Nov 21, 2014
 */
public class TaskInfoPreviewView extends JPanel implements LocaleChangeListener {

	public static final String NAME = "TaskInfoPreviewView";

	private static final long serialVersionUID = -3486346306247702460L;
	private final TaskModel taskM;
	private final TaskController taskC;
	private final TaskInfoPreviewController controller;
	private final JPanel titleBar;
	private final JButton closeButton;
	public static final String EDIT = "edit";
	public static final String X = "x";
	private final JButton edit;
	public static final int WIDTH = 220;
	private JLabel archived = new JLabel();
	private JLabel dueDate = new JLabel();
	private final JLabel estE;
	private final JLabel actE;
	private ScrollList usersSome = new ScrollList("");
	private JLabel usersNone = new JLabel();
	private final JLabel req;

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
	 * @param titleColor
	 *            The color that the title should be for this view
	 */
	public TaskInfoPreviewView(TaskModel model, TaskController controller,
			Point loc, Color titleColor) {
		taskM = model;
		taskC = controller;
		this.controller = new TaskInfoPreviewController(taskC, this);

		this.setLayout(null);
		this.setOpaque(false);
		this.setName(NAME);

		final JPanel bgPane = new JPanel();
		bgPane.setLayout(new MigLayout("wrap 1", "5[]5", "0[]:push[]"));
		setBoundsWithoutClipping(loc, 245, 415);

		bgPane.setBackground(Colors.TASK);
		final Border color = BorderFactory.createLineBorder(Color.GRAY, 3);
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
		final JPanel info = new JPanel();
		final Dimension infoSize = new Dimension(this.getWidth(), 345);
		info.setSize(infoSize);
		info.setPreferredSize(infoSize);
		info.setMinimumSize(infoSize);
		info.setMaximumSize(infoSize);
		info.setLayout(new MigLayout("wrap 1"));
		info.setOpaque(false);

		// The task's titleBar contains the title and the 'x' button
		titleBar = new JPanel();
		titleBar.setLayout(new MigLayout("wrap 2", "5[]:push[]", "[]0[center]"));
		final Dimension titleBarSize = new Dimension(this.getWidth() - 15, 30);
		titleBar.setSize(titleBarSize);
		final JLabel title = new JLabel(taskM.getName());
		final Dimension titleSize = new Dimension(185,
				title.getPreferredSize().height + 10);

		title.setFont(new Font("Default", Font.BOLD, 15));
		title.setForeground(Color.white);
		title.setPreferredSize(titleSize);
		title.setSize(titleSize);
		title.setMaximumSize(titleSize);
		titleBar.add(title);

		// Closable 'x' button
		closeButton = new JButton();
		closeButton.setName(X);
		closeButton.setFont(closeButton.getFont().deriveFont((float) 10));
		closeButton.setMargin(new Insets(0, 2, 0, 2));
		closeButton.setFocusPainted(false);
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setOpaque(false);
		closeButton.addActionListener(this.controller);
		closeButton.addMouseListener(this.controller);
		titleBar.add(closeButton);
		titleBar.setBackground(titleColor);

		// if the task is archived, say so.
		if (taskC.isArchived()) {
			archived = new JLabel("", SwingConstants.CENTER);
			archived.setFont(archived.getFont().deriveFont(Font.PLAIN));
			final Dimension archivedSize = new Dimension(this.getWidth() - 40,
					5);
			archived.setSize(archivedSize);
			archived.setPreferredSize(archivedSize);
			titleBar.add(archived, "span");
		}
		info.add(titleBar);

		// The task's description
		final JTextArea description = new JTextArea();
		description.setText(taskM.getDescription());

		description.setAlignmentX(CENTER_ALIGNMENT);
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setBackground(Colors.TASK);
		description.setCaretPosition(0);
		final JScrollPane descScroll = new JScrollPane(description);
		final Dimension descScrollSize = new Dimension(this.getWidth() - 30, 80);
		descScroll.setSize(descScrollSize);
		descScroll.setMaximumSize(descScrollSize);
		descScroll.setMinimumSize(descScrollSize);
		descScroll.setPreferredSize(descScrollSize);
		descScroll.setBorder(BorderFactory.createEmptyBorder());
		info.add(descScroll);

		final JPanel spacer = new JPanel();
		final Dimension spacerSize = new Dimension(50, 5);
		spacer.setSize(spacerSize);
		spacer.setPreferredSize(spacerSize);
		spacer.setMaximumSize(spacerSize);
		spacer.setMinimumSize(spacerSize);
		spacer.setBackground(Colors.TASK);
		info.add(spacer);

		// The task's due date
		final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		dueDate = new JLabel("<html><b><i>Due:</i></b> "
				+ df.format(taskM.getDueDate()) + "</html>");

		dueDate.setFont(dueDate.getFont().deriveFont(Font.PLAIN));
		dueDate.setMaximumSize(new Dimension(this.getWidth(), 20));
		info.add(dueDate);

		final String estString = taskM.isEstimatedEffortSet() ? taskM
				.getEstimatedEffort().toString() : "";
		final String actString = taskM.isActualEffortSet() ? taskM
				.getActualEffort().toString() : "";

		// The task's effort
		estE = new JLabel();
		estE.setFont(estE.getFont().deriveFont(Font.PLAIN));
		actE = new JLabel();
		actE.setFont(actE.getFont().deriveFont(Font.PLAIN));
		info.add(estE);
		info.add(actE);

		final Set<String> userList = taskM.getAssigned();
		// if there are users, add a scrollList to show them. Else just print
		// '[None]'
		if (!userList.isEmpty()) {
			// The task's users
			usersSome = new ScrollList("");
			final Dimension usersSize = new Dimension(this.getWidth() - 30, 70);
			usersSome.setSize(usersSize);
			usersSome.setPreferredSize(usersSize);
			usersSome.setMaximumSize(usersSize);
			usersSome.setMinimumSize(usersSize);

			for (String u : userList) {
				if (!usersSome.contains(u)) {
					usersSome.addToList(u);
				}
			}
			usersSome.setEnabled(false);
			info.add(usersSome);
		} else {
			usersNone = new JLabel();
			usersNone.setFont(usersNone.getFont().deriveFont(Font.PLAIN));
			info.add(usersNone);
		}

		// The task's requirement
		if (taskM.getReq() == null) {
			req = new JLabel();
			req.setFont(req.getFont().deriveFont(Font.PLAIN));
			info.add(req);
		} else {
			req = new JLabel();
			req.setFont(req.getFont().deriveFont(Font.PLAIN));
			info.add(req);
			final JLabel name = new JLabel("  " + taskM.getReq());
			name.setFont(name.getFont().deriveFont(Font.PLAIN));
			final Dimension nameSize = new Dimension(this.getWidth() - 30, 20);
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
		edit.setPreferredSize(new Dimension(205, 35));
		// edit.setMargin(new Insets(5, 30, 5, 30));
		// edit.setMargin(new Insets(5, 77, 5, 77));
		edit.addActionListener(this.controller);
		// Add the pencil image to the edit button
		try {
			final Image img = ImageIO.read(this.getClass().getResourceAsStream(
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

		onLocaleChange();
		Localizer.addListener(this);
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

	/**
	 * 
	 * Set the BorderPainted boolean for the closeButton.
	 *
	 * @param border
	 *            true to make the border visible, false otherwise
	 */
	public void setCloseBorder(boolean border) {
		closeButton.setBorderPainted(border);
	}

	@Override
	public void onLocaleChange() {
		closeButton.setText(Localizer.getString("x"));
		archived.setText("<html><font size=\"3\"><i>"
				+ Localizer.getString("Archived") + "</i></font></html>");
		final DateFormat df = new SimpleDateFormat(
				Localizer.getString("DateFormat"));
		dueDate.setText("<html><b><i>" + Localizer.getString("Due")
				+ " </b></i>" + df.format(taskM.getDueDate()) + "</html>");
		final String estString = taskM.isEstimatedEffortSet() ? taskM
				.getEstimatedEffort().toString() : "";
		final String actString = taskM.isActualEffortSet() ? taskM
				.getActualEffort().toString() : "";
		estE.setText("<html><b><i>" + Localizer.getString("EstEffort")
				+ ": </i></b>" + estString + "</html>");
		actE.setText("<html><b><i>" + Localizer.getString("ActEffort")
				+ ": </i></b>" + actString + "</html>");
		usersSome.setTitle("<html><i>" + Localizer.getString("Users")
				+ ":</i></html>");
		usersNone.setText("<html><b><i>" + Localizer.getString("Users")
				+ ":</i></b> " + Localizer.getString("None") + "</html>");
		if (taskM.getReq() == null) {
			req.setText("<html><b><i>" + Localizer.getString("Requirement")
					+ ":</i></b> " + Localizer.getString("None") + "</html>");
		} else {
			req.setText("<html><b><i>" + Localizer.getString("Requirement")
					+ ":</i></b></html>");
		}
		edit.setText(Localizer.getString("Edit"));
	}
}
