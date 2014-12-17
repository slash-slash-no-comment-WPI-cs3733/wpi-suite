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
 * A view to hold a rotated task view
 *
 * @author Jon Sorrells
 */
public class RotationView extends JPanel implements Transferable {

	private static final long serialVersionUID = 473778538723370745L;

	private final JPanel panel;
	private double angle = Math.random() * 2 * Math.PI;
	private boolean painting = false;
	private RotationController controller = null;

	/**
	 * Creates a new rotation view
	 *
	 * @param panel
	 *            the panel to go inside the rotation view
	 */
	public RotationView(JPanel panel) {
		this.panel = panel;
		setName("rotation - " + panel.getName());
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

	/**
	 * @param listener
	 *            the listener this view should forward mouse events to
	 */
	public void setListener(TaskController listener) {
		controller.setListener(listener);
	}

	/*
	 * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
	 */
	@Override
	public void paintChildren(Graphics g) {
		// rotate
		g.setColor(getParent().getBackground());
		((Graphics2D) g).translate(0, controller.calculateYTranslation());
		((Graphics2D) g).rotate(angle, panel.getWidth() / 2,
				panel.getHeight() / 2);

		// now paint the children on the rotated graphics
		painting = true;
		super.paintChildren(g);
		painting = false;
	}

	/**
	 * Generate placeholder for rotation view
	 *
	 * @return image used as placeholder
	 */
	public Image createPlaceholder() {
		final Image image = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		((Graphics2D) g).translate(0, controller.calculateYTranslation());
		((Graphics2D) g).rotate(angle, component.getWidth() / 2,
				component.getHeight() / 2);

		final float dash1[] = { 2 * (6 - 1.0f), 2 * (4 + 1.0f) };
		final BasicStroke dashed = new BasicStroke(2, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 4, dash1, 0.0f);
		g.setColor(Color.GRAY);
		((Graphics2D) g).setStroke(dashed);
		((Graphics2D) g).draw(new Rectangle2D.Double(0, 0,
				component.getWidth(), component.getHeight()));

		return image;
	}

	/*
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		// figure out what height this component should be
		int height = (int) controller.calculateHeight();
		Dimension d = new Dimension(panel.getWidth(), height);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);

		// now paint
		super.paintComponent(g);
	}

	/**
	 *
	 * @return whether or not this view is currently painting
	 */
	public boolean isPainting() {
		return painting;
	}

	/**
	 *
	 * @return the angle this rotation view is at
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Set the angle for this rotation view
	 *
	 * @param d
	 *            the angle to set
	 */
	public void setAngle(double d) {
		angle = d;
		controller.setAngle(d);
	}

	/**
	 * Get the panel inside this rotation view
	 *
	 * @return the panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Get the controller for this view
	 *
	 * @return the controller
	 */
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
