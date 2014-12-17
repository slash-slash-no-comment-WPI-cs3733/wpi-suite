/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.draganddrop;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.SwingUtilities;

/**
 * This listener redispatches any incoming drag/drop events to a target
 * DropTargetListener, and provides convenience methods for converting drag
 * event coordinates
 *
 * @author Sam Khalandovsky
 * @version Dec 6, 2014
 */
public class DropTargetRedispatcher implements DropTargetListener {

	private final Component source;
	private final Component target;
	private final DataFlavor flavor;

	/**
	 * Constructor with target
	 *
	 * @param source
	 *            Component to which this listener is attached
	 * @param target
	 *            Component to which events will be dispatched
	 * @param flavor
	 *            Accepted flavor to redispatch
	 */
	public DropTargetRedispatcher(Component source, Component target,
			DataFlavor flavor) {
		if (target.getDropTarget() == null) {
			throw new IllegalArgumentException(
					"Component passed to DropTargetRedispatcher does not have a DropTarget");
		}
		this.source = source;
		this.target = target;
		this.flavor = flavor;
	}

	/*
	 * @see
	 * java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent
	 * )
	 */
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		try {
			final DropTarget dt = getDropTarget(dtde);
			dt.dragEnter(convertCoords(dtde, dt.getComponent()));
		} catch (NullPointerException e) {
			dtde.rejectDrag();
		}
	}

	/*
	 * @see
	 * java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent
	 * )
	 */
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		try {
			final DropTarget dt = getDropTarget(dtde);
			dt.dragOver(convertCoords(dtde, dt.getComponent()));
		} catch (NullPointerException e) {
			dtde.rejectDrag();
		}
	}

	/*
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.
	 * DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		try {
			final DropTarget dt = getDropTarget(dtde);
			dt.dropActionChanged(convertCoords(dtde, dt.getComponent()));
		} catch (NullPointerException e) {
			dtde.rejectDrag();
		}
	}

	/*
	 * @see
	 * java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(DropTargetEvent dte) {
		target.getDropTarget().dragExit(dte);
	}

	/*
	 * @see
	 * java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent dtde) {
		try {
			final DropTarget dt = getDropTarget(dtde);
			dt.drop(convertCoords(dtde, dt.getComponent()));
		} catch (NullPointerException e) {
			dtde.rejectDrop();
		}
	}

	/**
	 * Get target dropTarget if supported, ancestor otherwise
	 *
	 * @param dtde
	 *            event for checking whether flavor is supported
	 * @return correct drop target for redispatching
	 */
	private DropTarget getDropTarget(DropTargetDragEvent dtde) {
		if (dtde.getTransferable().isDataFlavorSupported(flavor)) {
			return target.getDropTarget();
		} else {
			return findAncestorDropTarget(source);
		}
	}

	/**
	 * Get target dropTarget if supported, ancestor otherwise
	 *
	 * @param dtde
	 *            event for checking whether flavor is supported
	 * @return correct drop target for redispatching
	 */
	private DropTarget getDropTarget(DropTargetDropEvent dtde) {
		if (dtde.getTransferable().isDataFlavorSupported(flavor)) {
			return target.getDropTarget();
		} else {
			return findAncestorDropTarget(source);
		}
	}

	/**
	 * Convert DropTargetDropEvent to the coordinate system of a different
	 * component
	 *
	 * @param e
	 *            event to convert
	 * @param comp
	 *            target component to convert coordinates to
	 * @return converted event
	 */
	public static DropTargetDropEvent convertCoords(DropTargetDropEvent e,
			Component comp) {

		final Point newPoint = SwingUtilities.convertPoint(e
				.getDropTargetContext().getComponent(), e.getLocation(), comp);
		final DropTargetDropEvent newE = new DropTargetDropEvent(
				e.getDropTargetContext(), newPoint, e.getDropAction(),
				e.getSourceActions());
		return newE;
	}

	/**
	 * Convert DropTargetDragEvent to the coordinate system of a different
	 * component
	 * 
	 * @param e
	 *            event to convert
	 * @param comp
	 *            target component to convert coordinates to
	 *
	 * @return converted event
	 */
	public static DropTargetDragEvent convertCoords(DropTargetDragEvent e,
			Component comp) {

		final Point newPoint = SwingUtilities.convertPoint(e
				.getDropTargetContext().getComponent(), e.getLocation(), comp);
		final DropTargetDragEvent newE = new DropTargetDragEvent(
				e.getDropTargetContext(), newPoint, e.getDropAction(),
				e.getSourceActions());
		return newE;
	}

	/**
	 * Finds closest ancestor with a drop target. Returns null if not found.
	 *
	 * @param comp
	 *            source component
	 * @return ancestor component
	 */
	public static DropTarget findAncestorDropTarget(Component comp) {
		Component ancestor = comp.getParent();
		// find ancestor with DropTarget
		while (ancestor != null && ancestor.getDropTarget() == null) {
			ancestor = ancestor.getParent();
		}
		if (ancestor != null) {
			return ancestor.getDropTarget();
		} else {
			return null;
		}
	}

}
