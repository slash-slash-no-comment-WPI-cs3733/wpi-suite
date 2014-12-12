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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JPanel;

import taskManager.controller.RotationController;
import taskManager.controller.TaskController;
import taskManager.draganddrop.DDTransferHandler;

/**
 * Description
 *
 * @author Jon Sorrells
 */
public class RotationView extends JPanel implements Transferable {

	private static final long serialVersionUID = 473778538723370745L;

	private final JPanel panel;
	private double angle = Math.random() * 2 * Math.PI;
	private boolean painting = false;
	private RotationController controller = null;

	public RotationView(JPanel panel) {
		this.panel = panel;
		add(panel);
		int height = (int) (Math.abs(215 * Math.sin(angle)) + Math
				.abs(62 * Math.cos(angle)));
		Dimension d = new Dimension(215, height);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		controller = new RotationController(this);
		addMouseListener(controller);
		addMouseMotionListener(controller);

		setTransferHandler(new DDTransferHandler());
		setDropTarget(null);
	}

	public void setListener(TaskController listener) {
		controller.setListener(listener);
	}

	@Override
	public void paintChildren(Graphics g) {
		g.setColor(getParent().getBackground());
		((Graphics2D) g).translate(0, controller.calculateYTranslation());
		((Graphics2D) g).rotate(angle, panel.getWidth() / 2,
				panel.getHeight() / 2);
		painting = true;
		super.paintChildren(g);
		painting = false;
	}

	@Override
	public void paintComponent(Graphics g) {
		int height = (int) controller.calculateHeight();
		Dimension d = new Dimension(panel.getWidth(), height);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		super.paintComponent(g);
	}

	public boolean isPainting() {
		return painting;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double d) {
		angle = d;
		controller.setAngle(d);
	}

	public JPanel getPanel() {
		return panel;
	}

	public RotationController getController() {
		return controller;
	}

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
}
