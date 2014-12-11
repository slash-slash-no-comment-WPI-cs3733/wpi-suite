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

import javax.swing.JPanel;

/**
 * Description
 *
 * @author Jon Sorrells
 */
public class RotationView extends JPanel {

	private static final long serialVersionUID = 473778538723370745L;

	private final JPanel panel;
	private double θ = 0;

	public RotationView(JPanel panel) {
		this.panel = panel;
		add(panel);

		Dimension d = new Dimension(215, 215);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);

	}

	public Graphics prepareRotation(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		θ = Math.PI / 4;
		int oldWidth = panel.getWidth();
		int oldHeight = panel.getHeight();
		g2d.rotate(θ, oldWidth / 2, oldHeight / 2);
		int newWidth = (int) (Math.sin(θ) * getWidth() + Math.cos(θ)
				* getHeight());
		int newHeight = (int) (Math.sin(θ) * getHeight() + Math.cos(θ)
				* getWidth());
		// Dimension d = new Dimension(newWidth, newHeight);
		// setSize(d);
		// setPreferredSize(d);
		// setMaximumSize(d);
		// setMinimumSize(d);
		return g2d;
	}
}
