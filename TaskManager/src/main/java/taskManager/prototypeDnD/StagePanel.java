/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.prototypeDnD;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;

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

	/**
	 * 
	 * Creates a StagePanel and initializes its placeholder.
	 *
	 */
	public StagePanel(StageModel model) {
		this.model = model;
		this.setTransferHandler(new DDTransferHandler());
		this.setDropTarget(new DropTarget(this, new StageDropListener(this)));

		// add placeholder
		// placeholder = new JPanel();
		// placeholder.setBackground(Color.GRAY);

		Image image = new BufferedImage(130, 80, BufferedImage.TYPE_INT_ARGB);
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

		WorkflowModel.getInstance().moveTask(transferredPanel.getModel(),
				((StagePanel) transferredPanel.getParent()).getModel(), model,
				newIndex);

		add(transferredPanel, newIndex);

		transferredPanel.setVisible(true);
		hidePlaceholder();
		calculateCenters();

		revalidate();
		repaint();

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
}
