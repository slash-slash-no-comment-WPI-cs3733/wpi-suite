/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.draganddrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

/**
 * A draggable panel
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
public class TaskPanel extends JPanel implements Transferable {

	private static final long serialVersionUID = 1449783093892045363L;

	/**
	 * Constructor sets up listeners and drag handlers
	 */
	public TaskPanel() {

		TaskMouseListener listener = new TaskMouseListener();
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		this.setTransferHandler(new DDTransferHandler());

		// setTransferHandler creates DropTarget by default; we don't want tasks
		// to respond to drops
		this.setDropTarget(null);
	}

	/**
	 * Mouse listener records drag location and initiates drag action
	 */
	private class TaskMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			System.out.println("Panel pressed");
			getTransferHandler().setDragImageOffset(e.getPoint());
		}

		public void mouseDragged(MouseEvent e) {
			System.out.println("Mouse dragged");

			JComponent comp = (JComponent) e.getSource();
			TransferHandler handler = comp.getTransferHandler();
			handler.exportAsDrag(comp, e, TransferHandler.MOVE);
		}
	}

	/*
	 * @see
	 * java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer
	 * .DataFlavor)
	 */
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(DDTransferHandler.getTaskFlavor())) {
			return this;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
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