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
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import taskManager.model.StageModel;
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

	private JLabel placeholder;
	private int lastIndex;
	private Map<Component, Point> compCenters;
	private StageModel model;

	private Map<Component, Rectangle> drawnBounds; // store animated panel
													// bounds

	/**
	 * 
	 * Creates a StagePanel and initializes its placeholder.
	 *
	 */
	public StagePanel() {
		this.setTransferHandler(new DDTransferHandler());
		this.setDropTarget(new DropTarget(this, new StageDropListener(this)));

		drawnBounds = new HashMap<Component, Rectangle>();

		Timer timer = new Timer(1000 / 24, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (updateDrawnBounds()) {
					repaint();
				}
			}
		});
		timer.start();

		// add placeholder
		// placeholder = new JPanel();
		// placeholder.setBackground(Color.GRAY);

		Image image = new BufferedImage(130, 30, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g = g.create();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 200, 100);
		placeholder = new JLabel(new ImageIcon(image));
		placeholder.setAlignmentX(CENTER_ALIGNMENT);

		// placeholder.setPreferredSize(new Dimension(200, 100));
		// placeholder.setMaximumSize(new Dimension(200, 100));
		// placeholder.setMinimumSize(new Dimension(200, 100));
		// placeholder.setSize(new Dimension(200, 100));
		// this.setMinimumSize(new Dimension(300, 100));
		placeholder.setVisible(false);
		this.add(placeholder);
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

		// TODO needs cleanup, maybe we need a separate controller?
		WorkflowModel.getInstance().moveTask(
				((TaskView) transferredPanel).getModel(),
				((StagePanel) transferredPanel.getParent()).getModel(), model,
				newIndex);
		WorkflowModel.getInstance().save();

		add(transferredPanel, newIndex);

		hidePlaceholder();
		calculateCenters();

		// TODO Put controller callback here!
	}

	/**
	 * 
	 * Draws a placeholder at roughly the given location with the given size.
	 * (only one placeholder at a time)
	 *
	 * @param point
	 *            The location (relative to the StagePanel) where the
	 *            placeholder should be drawn.
	 * @param placeholderSize
	 *            The size of the placeholder.
	 */
	public void drawPlaceholder(Point point, Dimension placeholderSize) {
		if (!placeholder.isVisible()) {
			calculateCenters();
		}
		placeholder.setPreferredSize(placeholderSize);

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
		index = Math.min(index, this.getComponentCount() - 1);
		this.add(placeholder, index);
		if (index != lastIndex) {
			this.revalidate();
			this.repaint();
			lastIndex = index;
		}
	}

	/**
	 * 
	 * Finds the center of each TaskPanel for dealing with the placeholder. **
	 * do this ONLY while placeholder is invisible!!! REALLY!!! - otherwise it
	 * includes the placeholder in its center calculations
	 *
	 */
	private void calculateCenters() throws IllegalStateException {
		if (placeholder.isVisible()) {
			throw new IllegalStateException(
					"Can't calculate panel centers when placholder is visible!");
		}
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
		// Component[] components = this.getComponents();
		// System.out.println(point);
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
		if (getComponentIndex(placeholder) < index) {
			index--;
		}

		System.out.println("Closest component" + Integer.toString(index));

		if (index < 0) {
			index = 0;
		} else if (point.y > compCenters.get(closest).y) {
			index++;
		}// TODO make general for horizontal?
		System.out.println("Insert at " + Integer.toString(index));
		/*
		 * if (index >= compCenters.size()) { index = compCenters.size() - 1; }
		 */

		return index;
	}

	/**
	 * Helper method to get index of component
	 *
	 * @param comp
	 *            component to lookup
	 * @return index
	 */
	private int getComponentIndex(Component comp) {
		for (int i = 0; i < getComponentCount(); i++) {
			if (getComponent(i).equals(comp)) {
				return i;
			}
		}
		return -1;

	}

	public StageModel getModel() {
		return model;
	}

	public void setModel(StageModel model) {
		this.model = model;
	}

	private synchronized boolean updateDrawnBounds() {

		boolean changed = false;

		List<Component> components = Arrays.asList(getComponents());

		// For each component, add it to drawnBounds if it's not there and it is
		// visible
		for (Component comp : components) {
			if (!drawnBounds.containsKey(comp) && comp.isVisible()) {
				drawnBounds.put(comp, comp.getBounds());
			}
		}

		Set<Component> drawnComps = new HashSet<Component>(drawnBounds.keySet());
		// Update each drawn bound
		for (Component comp : drawnComps) {
			Rectangle drawn = drawnBounds.get(comp);

			// If remove components from drawnBounds that are no longer in the
			// panel or invisible
			if (!components.contains(comp) || !comp.isVisible()) {
				drawnBounds.remove(comp);
			} else if (drawn.x != comp.getBounds().x
					|| drawn.y != comp.getBounds().y) {
				// move the drawn panel halfway between current drawn location
				// and target position
				drawn.x = (drawn.x + comp.getBounds().x) / 2;
				drawn.y = (drawn.y + comp.getBounds().y) / 2;

				changed = true;
			}
		}
		return changed;
	}

	@Override
	public synchronized void paintChildren(Graphics g) {
		List<Rectangle> layoutBounds = new ArrayList<Rectangle>();
		for (Component comp : getComponents()) {
			layoutBounds.add(comp.getBounds());
			if (drawnBounds.containsKey(comp)) {
				Rectangle bounds = drawnBounds.get(comp);
				comp.setBounds(bounds.x, bounds.y, comp.getBounds().width,
						comp.getBounds().height);
			}
		}
		super.paintChildren(g);
		for (int i = 0; i < getComponentCount(); i++) {
			Component comp = getComponent(i);
			comp.setBounds(layoutBounds.get(i));
		}
	}
}

/**
 * 
 * Listens to when a Task is dropped onto a stage. Adds the TaskPanel to its
 * StagePanel.
 *
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 17, 2014
 */
class StageDropListener implements DropTargetListener {

	private StagePanel stage;

	public StageDropListener(StagePanel stage) {
		this.stage = stage;
	}

	/**
	 * Drops a task onto the stage after making sure that it is a taskPanel.
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
	 * Careful with this due to it being called after entering a TaskPanel
	 * 
	 */
	@Override
	public void dragExit(DropTargetEvent e) {
		System.out.println("Stage drag exit");
		stage.hidePlaceholder();
	}

	/**
	 * Draws the placeholder on the stage when a task is being dragged above it.
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
		Dimension placeholderSize = transferredPanel.getSize();

		transferredPanel.setVisible(false);

		stage.drawPlaceholder(e.getLocation(), placeholderSize);
	}

	// Careful with this due to it being called after leaving a TaskPanel
	@Override
	public void dragEnter(DropTargetDragEvent e) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent e) {

	}

}
