/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.draganddrop;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * Listener for initiating a panel drag
 *
 * @author Sam Khalandovsky
 * @version Dec 1, 2014
 */
public class DraggablePanelListener extends MouseAdapter {
	public void mousePressed(MouseEvent e) {
		System.out.println("Panel pressed");

		JComponent comp = (JComponent) e.getSource();
		comp.getTransferHandler().setDragImageOffset(e.getPoint());
	}

	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse dragged");

		JComponent comp = (JComponent) e.getSource();
		comp.getTransferHandler().exportAsDrag(comp, e, TransferHandler.MOVE);
	}

}
