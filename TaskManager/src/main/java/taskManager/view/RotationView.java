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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import taskManager.controller.RotationController;

/**
 * Description
 *
 * @author Jon Sorrells
 */
public class RotationView extends JPanel {

	private static final long serialVersionUID = 473778538723370745L;

	private final JPanel panel;
	private double angle = Math.PI / 4;
	private boolean painting = false;
	private RotationController controller = null;

	public RotationView(JPanel panel) {
		this.panel = panel;
		panel.setOpaque(true);
		add(panel);
		Border border = BorderFactory.createLineBorder(Color.black);
		setBorder(border);
		setBackground(Color.cyan);
		Dimension d = new Dimension(215, 215);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		controller = new RotationController(this);
		addMouseListener(controller);
	}

	public void setListener(MouseListener listener) {
		controller.setListener(listener);
	}

	@Override
	public void paintChildren(Graphics g) {
		BufferedImage image = new BufferedImage(panel.getWidth(),
				panel.getHeight(), BufferedImage.TYPE_INT_RGB);
		painting = true;
		panel.paint(image.getGraphics());
		painting = false;
		g.setColor(Color.cyan);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		((Graphics2D) g).rotate(angle);
		g.drawImage(image, 0, 0, panel.getWidth(), panel.getHeight(), this);
	}

	public boolean isPainting() {
		return painting;
	}

	public double getAngle() {
		return angle;
	}

	public JPanel getPanel() {
		return panel;
	}
}
