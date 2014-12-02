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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import taskManager.controller.TaskController;
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

	/**
	 * Constructor, creates a list-like view for the following information: the
	 * name of the task, the due date and the estimated effort
	 * 
	 * @param name
	 *            the name of the task
	 * @param duedate
	 *            the due date of the task
	 * @param estEffort
	 *            the estimated effort for the task
	 * @param taskID
	 *            The ID of the task being displayed
	 */
	public TaskView(String name, Date duedate, int estEffort) {

		// organizes the data in a vertical list
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		final TitledBorder title = BorderFactory
				.createTitledBorder(raisedbevel);
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);
		this.setMinimumSize(new Dimension(200, 100));

		//
		// convert Date object to Calendar object to avoid using deprecated
		// Date methods.
		final Calendar date = Calendar.getInstance();
		date.setTime(duedate);

		// adds the data to the view
		// note: the Calendar.MONTH value ranges between 0-11 so here we add 1
		// to the month.

		JLabel nameLabel = new JLabel();
		JLabel dueLabel = new JLabel("Due: " + (date.get(Calendar.MONTH) + 1)
				+ "/" + date.get(Calendar.DATE) + "/"
				+ (date.get(Calendar.YEAR)));

		// This creates a maximum text-string length before the name gets
		// truncated in the view

		nameLabel.setText("Average Name Length");
		final Dimension size = nameLabel.getPreferredSize();

		nameLabel.setMaximumSize(size);
		nameLabel.setPreferredSize(size);
		nameLabel.setText(name);

		this.add(nameLabel);
		this.add(dueLabel);

		// -----------------------
		// Drag and drop handling:
		MouseAdapter listener = new DraggablePanelListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);

		setTransferHandler(new DDTransferHandler());

		// setTransferHandler creates DropTarget by default; we don't want tasks
		// to respond to drops
		setDropTarget(null);

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
		this.addMouseListener(controller);
	}

	public TaskController getController() {
		return controller;
	}

	@Override
	public void setVisible(boolean visible) {
		controller.resetBackground();
		super.setVisible(visible);
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
			throws UnsupportedFlavorException, IOException {
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
		DataFlavor[] flavors = { DDTransferHandler.getTaskFlavor() };
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

}
