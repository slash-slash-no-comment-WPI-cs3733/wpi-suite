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
import java.io.IOException;

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

		// final MouseAdapter listener = new DraggablePanelListener(this);
		// addMouseListener(listener);
		// addMouseMotionListener(listener);

		setTransferHandler(new DDTransferHandler());
	}

	public void setListener(TaskController listener) {
		controller.setListener(listener);
	}

	@Override
	public void paintChildren(Graphics g) {
		g.setColor(getParent().getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
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
	}

	public JPanel getPanel() {
		return panel;
	}

	public RotationController getController() {
		return controller;
	}

	@Override
	public Object getTransferData(DataFlavor arg0)
			throws UnsupportedFlavorException, IOException {
		return ((Transferable) panel).getTransferData(arg0);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return ((Transferable) panel).getTransferDataFlavors();
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		return ((Transferable) panel).isDataFlavorSupported(arg0);
	}
}
