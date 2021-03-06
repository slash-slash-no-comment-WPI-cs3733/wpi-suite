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

import taskManager.controller.ToolbarController;
import taskManager.controller.WorkflowController;
import taskManager.model.WorkflowModel;
import taskManager.view.RotationView;
import taskManager.view.StageView;
import taskManager.view.TaskView;

/**
 * 
 * Handler for a drag-and-drop transfer
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
public class DDTransferHandler extends TransferHandler {

	private static final long serialVersionUID = -7859524821673270515L;

	// DataFlavor representing a TaskView being dragged
	private static DataFlavor taskFlavor = null;

	// DataFlavor representing a StageView being dragged
	private static DataFlavor stageFlavor = null;

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
						DataFlavor.javaJVMLocalObjectMimeType + ";class="
								+ TaskView.class.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return taskFlavor;
	}

	/**
	 * Lazy-load the DataFlavor associated with stages
	 *
	 * @return the DataFlavor
	 */
	public static DataFlavor getStageFlavor() {
		if (stageFlavor == null) {
			try {
				stageFlavor = new DataFlavor(
						DataFlavor.javaJVMLocalObjectMimeType + ";class="
								+ StageView.class.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return stageFlavor;
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
		WorkflowController.getInstance().removeChangeTitles();
		WorkflowController.getInstance().removeTaskInfos(true);
		WorkflowController.pauseInformation = true;
		// Create drag image
		final Image image = new BufferedImage(comp.getWidth(),
				comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g = g.create();
		if (comp instanceof RotationView) {
			// just paint the rotated task inside the rotation view
			final RotationView rotationView = (RotationView) comp;
			rotationView.paintChildren(g);
		} else {
			comp.paint(g);
		}
		setDragImage(image);

		// Create placeholder, rotated for rotationviews
		if (comp instanceof RotationView) {
			final RotationView rot = (RotationView) comp;
			DropAreaPanel.generatePlaceholder(rot.createPlaceholder());
		} else {
			DropAreaPanel.generatePlaceholder(comp.getSize());
		}

		// Set toolbar icon state
		ToolbarController.getInstance().setIconState(comp);

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
		WorkflowController.pauseInformation = false;

		if (!DDTransferHandler.dragSaved) {
			// update now in case we missed anything while dragging
			// (if the drag saved, our changes overwrite anything we may have
			// missed)
			WorkflowModel.updateNow();
		}
		DDTransferHandler.dragSaved = false;

		// Show the component
		comp.setVisible(true);

		// Reset icons
		ToolbarController.getInstance().resetIconState();
	}
}