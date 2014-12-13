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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import taskManager.controller.TaskController;
import taskManager.controller.ToolbarController;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DraggablePanelListener;

/**
 * @author Beth Martino
 * @author Stefan Alexander
 * @author Thane Hunt
 * @version November 18, 2014
 */

public class TaskView extends JPanel implements Transferable {

	private static final long serialVersionUID = 1L;

	private TaskController controller;
	private RotationView rotationView;

	private JLabel userNumber;
	private JLabel commentNumber;
	private JLabel dueLabel;

	/**
	 * Constructor, creates a list-like view for the following information: the
	 * name of the task, the due date and the estimated effort
	 * 
	 * @param name
	 *            the name of the task
	 * @param duedate
	 *            the due date of the task
	 * @param users
	 *            the number of users assigned to the task
	 * @param comments
	 *            the number of comments on the task
	 */
	public TaskView(String name, Date duedate, int users, int comments) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);

		// creates an empty space around the data
		JPanel spacer = new JPanel();
		spacer.setLayout(new BoxLayout(spacer, BoxLayout.Y_AXIS));
		spacer.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		spacer.setOpaque(false);
		spacer.setAlignmentX(LEFT_ALIGNMENT);

		// sets the border
		// TODO sort this out
		final Border raisedbevel = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		final TitledBorder title = BorderFactory
				.createTitledBorder(raisedbevel);
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);

		this.setMinimumSize(new Dimension(215, 62));
		this.setPreferredSize(new Dimension(215, 62));
		this.setMaximumSize(new Dimension(215, 62));
		this.setSize(new Dimension(215, 62));
		this.setName(name);

		// convert Date object to Calendar object to avoid using deprecated
		// Date methods.
		final Calendar date = Calendar.getInstance();
		date.setTime(duedate);

		// formats the lower section containing date and icons
		JPanel lower = new JPanel();
		lower.setLayout(new BoxLayout(lower, BoxLayout.X_AXIS));
		lower.setAlignmentX(LEFT_ALIGNMENT);
		lower.setOpaque(false);

		dueLabel = new JLabel("Due: " + (date.get(Calendar.MONTH) + 1)
				+ "/" + date.get(Calendar.DATE) + "/"
				+ (date.get(Calendar.YEAR)));
		dueLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
		lower.add(dueLabel);
		JPanel icons = new JPanel(new FlowLayout());
		icons.setOpaque(false);
		JLabel userIcon = new JLabel();
		JLabel commentIcon = new JLabel();
		// formats the user number
		Integer uNum = new Integer(users);
		if (users > 99) {
			userNumber = new JLabel("99+");
		} else {
			userNumber = new JLabel(uNum.toString());
		}
		userNumber.setPreferredSize(new Dimension(21, 12));
		// formats the comment number
		Integer cNum = new Integer(comments);
		if (comments > 99) {
			commentNumber = new JLabel("99+");
		} else {
			commentNumber = new JLabel(cNum.toString());
		}
		commentNumber.setPreferredSize(new Dimension(21, 12));

		// icons from:
		// <div>Icon made by <a href="http://catalinfertu.com"
		// title="Catalin Fertu">Catalin Fertu</a> from <a
		// href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a>
		// is licensed under <a
		// href="http://creativecommons.org/licenses/by/3.0/"
		// title="Creative Commons BY 3.0">CC BY 3.0</a></div>
		//
		// <div>Icon made by <a href="http://buditanrim.co"
		// title="Budi Tanrim">Budi Tanrim</a> from <a
		// href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a>
		// is licensed under <a
		// href="http://creativecommons.org/licenses/by/3.0/"
		// title="Creative Commons BY 3.0">CC BY 3.0</a></div>
		try {
			Image u = ImageIO.read(this.getClass().getResourceAsStream(
					"user146.png"));
			userIcon.setIcon(new ImageIcon(u));
			u = ImageIO.read(this.getClass().getResourceAsStream("chat51.png"));
			commentIcon.setIcon(new ImageIcon(u));
		} catch (IOException e) {
			e.printStackTrace();
		}
		icons.add(userIcon);
		icons.add(userNumber);
		icons.add(commentIcon);
		icons.add(commentNumber);
		lower.add(icons);

		// This creates a maximum text-string length before the name gets
		// truncated in the view
		JLabel nameLabel = new JLabel();
		nameLabel.setText("Average Name Length plu");
		nameLabel.setSize(this.getWidth(), 20);
		nameLabel.setMaximumSize(this.getSize());
		nameLabel.setPreferredSize(this.getSize());
		nameLabel.setAlignmentX(LEFT_ALIGNMENT);
		nameLabel.setText(name);
		nameLabel.setFont(new Font("Default", Font.BOLD, 14));
		nameLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// adds the title, date and icons to the task view
		spacer.add(nameLabel);
		spacer.add(lower);
		this.add(spacer);

		// in fun mode, create a rotation view
		if (ToolbarController.getInstance().getView().isFunMode()) {
			this.rotationView = new RotationView(this);
			// the rotation view will handle drag/drop
		} else {
			// Drag and drop handling:
			final MouseAdapter listener = new DraggablePanelListener(this);
			addMouseListener(listener);
			addMouseMotionListener(listener);
			setTransferHandler(new DDTransferHandler());

			// setTransferHandler creates DropTarget by default; we don't want
			// tasks to respond to drops
			setDropTarget(null);
		}

	}

	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * Attaches the task controller to this view and associates listeners
	 * 
	 * @param controller
	 *            the controller to be attached to this view
	 */
	public void setController(TaskController controller) {
		this.controller = controller;
		if (ToolbarController.getInstance().getView().isFunMode()) {
			rotationView.setListener(controller);
		} else {
			addMouseListener(controller);
		}

	}

	@Override
	public void setVisible(boolean visible) {
		controller.resetBackground();
		super.setVisible(visible);
	}

	/**
	 * 
	 * Returns the TaskController.
	 *
	 * @return the TaskController
	 */
	public TaskController getController() {
		return controller;
	}
	
	/**
	 * Sets the font color of the due date
	 * used to show red overdue dates
	 * 
	 * @param color to set the date to
	 */
	public void setDateColor(Color color){
		dueLabel.setForeground(color);
	}

	// ----------------------------
	// Drag-and-drop transferable implementation

	/*
	 * @see
	 * java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer
	 * .DataFlavor)
	 */
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException {
		if (!flavor.equals(DDTransferHandler.getTaskFlavor())) {
			throw new UnsupportedFlavorException(flavor);
		}
		// return this panel as the transfer data
		return this;
	}

	/*
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		final DataFlavor[] flavors = { DDTransferHandler.getTaskFlavor() };
		return flavors;
	}

	/*
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.
	 * datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DDTransferHandler.getTaskFlavor());
	}

	/*
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		// in fun mode, use the rotation view to draw the task rotated
		if (ToolbarController.getInstance().getView().isFunMode()) {
			if (rotationView.isPainting()) {
				super.paintComponent(g);
			} else {
				g.setColor(rotationView.getParent().getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				rotationView.repaint();
			}
		} else {
			super.paintComponent(g);
		}
	}

	/*
	 * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
	 */
	@Override
	public void paintChildren(Graphics g) {
		// in fun mode, use the rotation view to draw the task rotated
		if (ToolbarController.getInstance().getView().isFunMode()) {
			if (rotationView.isPainting()) {
				super.paintChildren(g);
			} else {
				g.setColor(rotationView.getParent().getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		} else {
			super.paintChildren(g);
		}
	}

	public JPanel getRotationPane() {
		return rotationView;
	}

}
