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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import taskManager.draganddrop.DraggablePanelListener;
import taskManager.view.RotationView;

/**
 * A controller for the rotation view. This class calculates the needed
 * translation and bounds for the rotation view, and forwards mouse events to
 * the task controller if appropriate.
 *
 * @author Jon Sorrells
 */
public class RotationController implements MouseListener, MouseMotionListener {

	private RotationView view;
	private TaskController listener = null;
	private MouseAdapter dragListener = null;
	private static Map<String, Double> taskAngles = new HashMap<String, Double>();

	/**
	 * Construct a new rotation controller
	 *
	 * @param view
	 *            The view this controller will be controlling
	 */
	public RotationController(RotationView view) {
		this.view = view;
		dragListener = new DraggablePanelListener(view);
	}

	/**
	 * A listener to forward mouse events to if they are within the drawn task
	 * view
	 *
	 * @param listener
	 *            The task controller
	 */
	public void setListener(TaskController listener) {
		this.listener = listener;
		Double angle = taskAngles.get(listener.getID());
		if (angle != null) {
			view.setAngle(angle);
		} else {
			taskAngles.put(listener.getID(), view.getAngle());
		}
	}

	/*
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (listener != null && checkBounds(arg0)) {
			listener.mouseClicked(arg0);
		}
	}

	/*
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// do nothing
	}

	/*
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		listener.mouseExited(arg0);
	}

	/*
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		if (checkBounds(arg0)) {
			if (listener != null) {
				listener.mousePressed(arg0);
			}
			if (dragListener != null) {
				dragListener.mousePressed(arg0);
			}
		}
	}

	/*
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (listener != null && checkBounds(arg0)) {
			listener.mouseReleased(arg0);
		}
	}

	/**
	 * Checks if the given mouse event occured within the task view inside this
	 * rotation view
	 *
	 * @param e
	 *            The mouse event
	 * @return if the even is inside the taskview
	 */
	private boolean checkBounds(MouseEvent e) {
		int panelX = view.getPanel().getWidth();
		int panelY = view.getPanel().getHeight();
		Point2D tp = calculatePoint(e.getPoint());
		Rectangle panelArea = new Rectangle(panelX, panelY);
		if (panelArea.contains(tp)) {
			return true;
		}
		return false;
	}

	/**
	 * Converts a point from the rotation view's coordinate system to the task
	 * view's coordinate system
	 *
	 * @param p
	 *            the point
	 * @return the point in the new coordinate system
	 */
	private Point2D calculatePoint(Point2D p) {
		int panelX = view.getPanel().getWidth();
		int panelY = view.getPanel().getHeight();

		AffineTransform transform = new AffineTransform();
		transform.translate(0, calculateYTranslation());
		transform.rotate(view.getAngle(), panelX / 2, panelY / 2);

		Point2D tp = null;
		try {
			tp = transform.inverseTransform(p, null);
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
		return tp;
	}

	/*
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (dragListener != null && checkBounds(arg0)) {
			dragListener.mouseDragged(arg0);
		}
	}

	/*
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (checkBounds(arg0)) {
			if (!listener.isHovered()) {
				listener.mouseEntered(arg0);
			}
		} else {
			if (listener.isHovered()) {
				listener.mouseExited(arg0);
			}
		}
	}

	/**
	 * Calculates how far down the task view needs to be translated so that the
	 * top is not clipped
	 *
	 * @return the distance to translate the task view
	 */
	public double calculateYTranslation() {
		// int panelX = view.getPanel().getWidth();
		int panelY = view.getPanel().getHeight();
		return (calculateHeight() - panelY) / 2;
	}

	/**
	 * Calculates the height for the rotation view in order to fit the task view
	 *
	 * @return the height for the rotation view
	 */
	public double calculateHeight() {
		int panelX = view.getPanel().getWidth();
		int panelY = view.getPanel().getHeight();
		double angle = view.getAngle();
		return (Math.abs(panelX * Math.sin(angle)) + Math.abs(panelY
				* Math.cos(angle)));
	}

	/**
	 * Sets the angle for the rotation view
	 *
	 * @param d
	 *            the angle to set (in radians)
	 */
	public void setAngle(double d) {
		taskAngles.put(listener.getID(), d);
	}

	/**
	 * Forgets all previously set angles (they will be initialized to random
	 * values the next time they are needed)
	 *
	 */
	public static void resetAngles() {
		taskAngles = new HashMap<String, Double>();
	}
}
