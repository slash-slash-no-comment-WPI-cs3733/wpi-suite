/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import taskManager.view.RotationView;

/**
 * Description
 *
 * @author Jon Sorrells
 */
public class RotationController implements MouseListener, MouseMotionListener {

	private RotationView view;
	private TaskController listener = null;

	public RotationController(RotationView view) {
		this.view = view;
	}

	public void setListener(TaskController listener) {
		this.listener = listener;
		// view.setAngle(listener.getAngle());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (listener != null && checkBounds(arg0)) {
			listener.mouseClicked(arg0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		listener.mouseExited(arg0);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (listener != null && checkBounds(arg0)) {
			listener.mousePressed(arg0);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (listener != null && checkBounds(arg0)) {
			listener.mouseReleased(arg0);
		}
	}

	private boolean checkBounds(MouseEvent e) {
		int panelX = view.getPanel().getWidth();
		int panelY = view.getPanel().getHeight();
		AffineTransform transform = new AffineTransform();
		transform.translate(0, calculateYTranslation());
		transform.rotate(view.getAngle(), panelX / 2, panelY / 2);

		Point2D tp = null;
		try {
			tp = transform.inverseTransform(e.getPoint(), null);
		} catch (NoninvertibleTransformException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		Rectangle panelArea = new Rectangle(panelX, panelY);
		if (panelArea.contains(tp)) {
			return true;
		}
		return false;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// do nothing
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (checkBounds(arg0)) {
			listener.mouseEntered(arg0);
		} else {
			listener.mouseExited(arg0);
		}
	}

	public double calculateYTranslation() {
		// int panelX = view.getPanel().getWidth();
		int panelY = view.getPanel().getHeight();
		return (calculateHeight() - panelY - 10) / 2;
	}

	public double calculateHeight() {
		int panelX = view.getPanel().getWidth();
		int panelY = view.getPanel().getHeight();
		double angle = view.getAngle();
		return (Math.abs(panelX * Math.sin(angle)) + Math.abs(panelY
				* Math.cos(angle)));
	}
}
