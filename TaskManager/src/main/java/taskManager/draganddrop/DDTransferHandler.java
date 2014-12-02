/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.draganddrop;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import taskManager.model.FetchWorkflowObserver;
import taskManager.model.WorkflowModel;

/**
 * 
 * Handler for a drag-and-drop transfer
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
public class DDTransferHandler extends TransferHandler {

	private static final long serialVersionUID = -7859524821673270515L;

	// This DataFlavor represents a task being dragged
	private static DataFlavor taskFlavor;

	public static boolean dragSaved = false;

	/**
	 * Lazy-load the DataFlavor associated with tasks
	 *
	 * @return the DataFlavor
	 */
	public static DataFlavor getTaskFlavor() {
		if (taskFlavor == null) {
			try {
				taskFlavor = new DataFlavor(
						DataFlavor.javaJVMLocalObjectMimeType
								+ ";class=taskManager.draganddrop.TaskPanel");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return taskFlavor;
	}

	/**
	 * Creates a Transferable object; draggable panels implement Transferable,
	 * so simply return the casted object
	 * 
	 * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
	 */
	public Transferable createTransferable(JComponent comp) {
		// Make sure the component really is Transferable
		if (comp instanceof Transferable) {
			return (Transferable) comp;
		}
		return null;
	}

	/**
	 * Returns the transfer types supported by the passed object.
	 * 
	 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
	 */
	public int getSourceActions(JComponent comp) {
		// All of our drags are of type MOVE
		return TransferHandler.MOVE;
	}

	/**
	 * Perform all necessary setup actions before a drag.
	 * 
	 * Note: dragOver won't be called on a stage until the mouse is moved AFTER
	 * this method is called. That's why we don't make the task visible here.
	 * 
	 * @see javax.swing.TransferHandler#exportAsDrag(javax.swing.JComponent,
	 *      java.awt.event.InputEvent, int)
	 */
	@Override
	public void exportAsDrag(JComponent comp, InputEvent e, int action) {
		// Ignore all responses from server while drag is active
		FetchWorkflowObserver.ignoreAllResponses = true;

		// Create drag image
		Image image = new BufferedImage(comp.getWidth(), comp.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g = g.create();
		comp.paint(g);
		setDragImage(image);

		// Create placeholder
		DropAreaPanel.generatePlaceholder(comp.getSize());

		// Initiate the drag
		super.exportAsDrag(comp, e, action);
	}

	/**
	 * Perform all necessary cleanup actions after a drag.
	 * 
	 * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent,
	 *      java.awt.datatransfer.Transferable, int)
	 */
	@Override
	protected void exportDone(JComponent comp, Transferable data, int action) {
		// Resume updating from the server
		FetchWorkflowObserver.ignoreAllResponses = false;

		if (DDTransferHandler.dragSaved == false) {
			// update now in case we missed anything while dragging
			// (if the drag saved, our changes overwrite anything we may have
			// missed)
			WorkflowModel.getInstance().updateNow();
		}
		DDTransferHandler.dragSaved = false;

		// Show the component
		comp.setVisible(true);
	}

}