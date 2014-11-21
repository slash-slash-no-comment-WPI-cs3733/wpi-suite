/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.draganddrop;

import java.awt.Container;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

public class TaskPanel extends JPanel implements Transferable {

	private Point mouseOffset;
	private Point pos;
	private boolean dragActive;

	public TaskPanel() {

		TaskMouseListener listener = new TaskMouseListener();
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		this.setTransferHandler(new DDTransferHandler());
		this.setDropTarget(new DropTarget(this, new TaskDropListener(this)));
	}

	private class TaskMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			System.out.println("Panel pressed");
			getTransferHandler().setDragImageOffset(e.getPoint());
			pos = TaskPanel.this.getLocationOnScreen();
		}

		public void mouseDragged(MouseEvent e) {
			System.out.println("Mouse dragged");

			dragActive = true;

			JComponent comp = (JComponent) e.getSource();
			TransferHandler handler = comp.getTransferHandler();
			handler.exportAsDrag(comp, e, TransferHandler.MOVE);// TODO
																// should be
																// move
		}
	}

	public void stopDragging() {
		dragActive = false;
		setVisible(true);
	}

	public boolean isBeingDragged() {
		return dragActive;
	}

	public void move(Point pos) {
		this.pos = pos;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		System.out.println("Gettin task transfer data");
		if (flavor.equals(DDTransferHandler.getTaskFlavor())) {
			return this;
		}
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		System.out.println("Gettin task transfer data flavors");
		DataFlavor[] flavors = { DDTransferHandler.getTaskFlavor() };
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		System.out.println("Is data flavor supported?");
		return flavor.equals(DDTransferHandler.getTaskFlavor());
	}
}

/**
 * 
 * The listener that triggers if a task is dropped on another task. Simply
 * delegates calls to the stage's drop listener.
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
class TaskDropListener extends DropTargetAdapter {
	private TaskPanel panel;

	public TaskDropListener(TaskPanel panel) {
		this.panel = panel;
	}

	@Override
	public void drop(DropTargetDropEvent e) {
		DropTargetDropEvent newE = convertToParentCoords(e);
		if (panel.getParent() instanceof StagePanel) {
			panel.getParent().getDropTarget().drop(newE);
		}
	}

	@Override
	public void dragOver(DropTargetDragEvent e) {
		DropTargetDragEvent newE = convertToParentCoords(e);
		if (panel.getParent() instanceof StagePanel) {
			panel.getParent().getDropTarget().dragOver(newE);
		}
	}

	public DropTargetDropEvent convertToParentCoords(DropTargetDropEvent e) {
		Container parent = panel.getParent();

		Point newPoint = SwingUtilities.convertPoint(e.getDropTargetContext()
				.getComponent(), e.getLocation(), parent);
		DropTargetDropEvent newE = new DropTargetDropEvent(
				e.getDropTargetContext(), newPoint, e.getDropAction(),
				e.getSourceActions());
		return newE;
	}

	public DropTargetDragEvent convertToParentCoords(DropTargetDragEvent e) {
		Container parent = panel.getParent();

		Point newPoint = SwingUtilities.convertPoint(e.getDropTargetContext()
				.getComponent(), e.getLocation(), parent);
		DropTargetDragEvent newE = new DropTargetDragEvent(
				e.getDropTargetContext(), newPoint, e.getDropAction(),
				e.getSourceActions());
		return newE;
	}
}
