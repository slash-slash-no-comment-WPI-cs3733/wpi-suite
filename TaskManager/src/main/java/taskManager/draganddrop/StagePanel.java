/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.draganddrop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import taskManager.controller.StageController;
import taskManager.model.WorkflowModel;
import taskManager.view.TaskView;

/**
 * The visible panel that contains tasks. It also manages the placeholder and
 * insertion index of the object being dragged.
 *
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 17, 2014
 */
public class StagePanel extends JPanel {

	private static final long serialVersionUID = -3746364753551742673L;

	private static JLabel placeholder;

	private int lastIndex;
	private Map<Component, Point> compCenters;
	private StageController controller;

	/**
	 * Creates a StagePanel and creates its handlers
	 */
	public StagePanel() {
		this.setTransferHandler(new DDTransferHandler());
		this.setDropTarget(new DropTarget(this, new StageDropListener(this)));
	}

	/**
	 * Create a new static placeholder of the given size
	 *
	 * @param size
	 *            desired size of the placeholder
	 */
	public static void generatePlaceholder(Dimension size) {
		if (placeholder != null && placeholder.getParent() != null) {
			placeholder.getParent().remove(placeholder);
		}

		Image image = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g = g.create();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, size.width, size.height);
		placeholder = new JLabel(new ImageIcon(image));
		placeholder.setAlignmentX(CENTER_ALIGNMENT);
	}

	/**
	 * 
	 * Adds a task that's been dropped on this stage.
	 *
	 * @param transferredPanel
	 *            The task that was dropped
	 * @param dropLocation
	 *            Where (relative to the stage) the panel was dropped.
	 */
	public void dropTask(TaskPanel transferredPanel, Point dropLocation) {
		int newIndex = getInsertionIndex(dropLocation);

		// If transferredPanel is lower by index in the same stage, lower the
		// index by 1
		int oldIndex = getComponentIndex(transferredPanel);
		if (oldIndex != -1 && oldIndex < newIndex) {
			newIndex--;
		}

		add(transferredPanel, newIndex);

		// TODO needs cleanup
		boolean changed = controller.addTask(
				((TaskView) transferredPanel).getController(), newIndex);

		if (changed) {
			WorkflowModel.getInstance().save();
		}

		hidePlaceholder();

	}

	/**
	 * 
	 * Draws a placeholder at roughly the given location with the given size.
	 *
	 * @param point
	 *            The location (relative to the StagePanel) where the
	 *            placeholder should be drawn.
	 */
	public void drawPlaceholder(Point point) {
		if (placeholder == null) {
			throw new IllegalStateException("Placeholder not found");
		}

		// Calculate centers if placeholder is not currently showing in this
		// panel
		if (placeholder.getParent() == null
				|| !placeholder.getParent().equals(this)
				|| !placeholder.isVisible()) {
			calculateCenters();
			System.out.println("Calculating centers.");
		}

		setPlaceholderIndex(getInsertionIndex(point));
		placeholder.setVisible(true);
	}

	/**
	 * 
	 * Hides the placeholder
	 *
	 */
	public void hidePlaceholder() {
		placeholder.setVisible(false);
	}

	/**
	 * 
	 * Places the placeholder at the appropriate location in the list of tasks.
	 *
	 * @param index
	 *            Where the placeholder is to be placed.
	 */
	private void setPlaceholderIndex(int index) {
		// index = Math.min(index, this.getComponentCount() - 1);
		System.out.println("Adding placeholder at " + index);
		this.add(placeholder, index);
		if (index != lastIndex) {
			this.revalidate();
			this.repaint();
			lastIndex = index;
		}
	}

	/**
	 * 
	 * Finds the center of each TaskPanel for dealing with the placeholder. Do
	 * this only while placeholder is not visible in this stage
	 *
	 */
	private void calculateCenters() {
		compCenters = new HashMap<Component, Point>();
		for (Component comp : this.getComponents()) {
			if (comp.isVisible()) {
				compCenters.put(comp, new Point((int) comp.getBounds()
						.getCenterX(), (int) comp.getBounds().getCenterY()));
			}
		}
	}

	/**
	 * 
	 * Figures out the nearest index for to the point given, using the
	 * centerpoints calculated by calculateCenters(). Useful for figuring out
	 * where the placeholder should go.
	 *
	 * @param point
	 *            The location we're finding the nearest index for.
	 * @return The index in the list (0 to length) that the location given is
	 *         closest to.
	 */
	private int getInsertionIndex(Point point) {
		double minDist = Double.MAX_VALUE;
		Component closest = null;
		for (Component comp : compCenters.keySet()) {
			Point center = compCenters.get(comp);
			final double dist = Math.pow(point.x - center.x, 2)
					+ Math.pow(point.y - center.y, 2);
			System.out.println(dist);
			if (dist < minDist) {
				minDist = dist;
				closest = comp;
			}
		}

		int index = getComponentIndex(closest);

		// pretend placeholder is not there when picking drop index
		int placeholderIndex = getComponentIndex(placeholder);
		if (placeholderIndex != -1 && placeholderIndex < index) {
			index--;
		}

		System.out.println("Closest component" + Integer.toString(index));

		if (index < 0) {
			index = 0;
		} else if (point.y > compCenters.get(closest).y) {
			index++;
		}// TODO make general for horizontal?
		System.out.println("Insert at " + Integer.toString(index));

		return index;
	}

	/**
	 * Helper method to get index of component
	 *
	 * @param comp
	 *            component to lookup
	 * @return index, -1 if not found
	 */
	private int getComponentIndex(Component comp) {
		for (int i = 0; i < getComponentCount(); i++) {
			if (getComponent(i).equals(comp)) {
				return i;
			}
		}
		return -1;

	}

	public void setController(StageController controller) {
		this.controller = controller;
	}
}

/**
 * 
 * Listens for when a Task is dropped onto a stage, and while the drag occurs.
 * Adds the TaskPanel to its StagePanel.
 *
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 17, 2014
 */
class StageDropListener implements DropTargetListener {

	private StagePanel stage;

	StageDropListener(StagePanel stage) {
		this.stage = stage;
	}

	/**
	 * Drops a task onto the stage after making sure that it is a taskPanel. <br>
	 * <br>
	 * {@inheritDoc}
	 */
	public void drop(DropTargetDropEvent e) {

		System.out.println("Dropping");

		Transferable trans = e.getTransferable();
		TaskPanel transferredPanel;
		if (trans.isDataFlavorSupported(DDTransferHandler.getTaskFlavor())) {
			try {
				transferredPanel = (TaskPanel) trans
						.getTransferData(DDTransferHandler.getTaskFlavor());
			} catch (Exception ex) {
				System.out.println(ex.getStackTrace());
				return;
			}

		} else {
			return;
		}

		stage.dropTask(transferredPanel, e.getLocation());
	}

	/**
	 * Hides the placeholder when the Task is no longer being dragged above the
	 * stage.
	 * 
	 * Careful with this due to it being called after entering a TaskPanel <br>
	 * <br>
	 * {@inheritDoc}
	 */
	@Override
	public void dragExit(DropTargetEvent e) {
		System.out.println("Stage drag exit");
		stage.hidePlaceholder();
	}

	/**
	 * Draws the placeholder on the stage when a task is being dragged above it. <br>
	 * <br>
	 * {@inheritDoc}
	 */
	@Override
	public void dragOver(DropTargetDragEvent e) {
		System.out.println("Stage drag over");

		// Getting placeholder's size & making sure it's a TaskPanel
		Transferable trans = e.getTransferable();
		TaskPanel transferredPanel;
		if (trans.isDataFlavorSupported(DDTransferHandler.getTaskFlavor())) {
			try {
				transferredPanel = (TaskPanel) trans
						.getTransferData(DDTransferHandler.getTaskFlavor());
			} catch (Exception ex) {
				System.out.println(ex.getStackTrace());
				return;
			}

		} else { // Not a TaskPanel
			return;
		}

		transferredPanel.setVisible(false);

		stage.drawPlaceholder(e.getLocation());
	}

	// Careful with this due to it being called after leaving a TaskPanel
	@Override
	public void dragEnter(DropTargetDragEvent e) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent e) {

	}

}
