/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.draganddrop;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import taskManager.JanewayModule;

/**
 * Listener for initiating a panel drag
 *
 * @author Sam Khalandovsky
 * @version Dec 1, 2014
 * 
 */
public class DraggablePanelListener extends MouseAdapter {
	private final JComponent exportedComponent;

	/**
	 * @param exportedComponent
	 *            component that will be exported in drag
	 */
	public DraggablePanelListener(JComponent exportedComponent) {
		this.exportedComponent = exportedComponent;
	}

	/*
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Panel pressed");
		Point p = e.getPoint();
		if (JanewayModule.isOnMac()) {
			// Macs appear to define the coordinates differently.
			p.x = -p.x;
			p.y = -p.y;
		}

		exportedComponent.getTransferHandler().setDragImageOffset(p);
	}

	/*
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse dragged");

		exportedComponent.getTransferHandler().exportAsDrag(exportedComponent,
				e, TransferHandler.MOVE);
	}

}
