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
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;

import taskManager.prototypeDnD.DragImage;

/**
 * 
 * The DDManager listens for the drag across the entire screen during the drag
 * and preforms helper functions
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
public class DDManager extends DragSourceAdapter {

	// The DataFlavor represents a task being dragged
	public static DataFlavor taskPanelFlavor;

	private DragImage dragImage;

	private StagePanel sourceParent;

	public DDManager() {
		try {
			taskPanelFlavor = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType
							+ ";class=taskManager.draganddrop.TaskPanel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		DragSource.getDefaultDragSource().addDragSourceListener(this);
		DragSource.getDefaultDragSource().addDragSourceMotionListener(this);
	}

	public void dragMouseMoved(DragSourceDragEvent e) {
		System.out.println("Dragging happening");
		Component comp = e.getDragSourceContext().getComponent();
		if (dragImage == null && comp instanceof TaskPanel) {

			sourceParent = (StagePanel) comp.getParent();

			comp.setVisible(false);

			sourceParent.revalidate();
			sourceParent.repaint();
		}

		if (dragImage != null) {
			dragImage.move(e.getLocation());

		}
	}

	public void dragDropEnd(DragSourceDropEvent e) {
		if (dragImage != null) {
			dragImage.dispose();
		}
		Component droppedComp = e.getDragSourceContext().getComponent();
		droppedComp.setVisible(true);
		dragImage = null;
		sourceParent = null;
	}
}