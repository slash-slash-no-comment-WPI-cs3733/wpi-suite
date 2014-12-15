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
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * A panel that allows for dynamic drops of other panels into it. It also
 * manages the placeholder and insertion index of the object being dragged.
 *
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 17, 2014
 */
public class DropAreaPanel extends JPanel {

	private static final long serialVersionUID = -3746364753551742673L;

	private static JLabel placeholder;

	private int lastIndex;
	private Map<Component, Point> compCenters;
	private DropAreaSaveListener listener;

	private final Map<Component, Rectangle> drawnBounds; // store animated panel
	// bounds

	private final Timer animTimer; // Timer for animating while dragging

	/**
	 * Creates a DropAreaPanel and creates its handlers
	 * 
	 * @param flavor
	 *            The type of data that will be dropped in this panel
	 */
	public DropAreaPanel(DataFlavor flavor) {
		this.setTransferHandler(new DDTransferHandler());

		this.setDropTarget(new DropTarget(this, new DropAreaListener(this,
				flavor)));

		drawnBounds = new HashMap<Component, Rectangle>();

		animTimer = new Timer(1000 / 24, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (updateDrawnBounds()) {
					repaint();
				} else {
					animTimer.stop();
				}
			}
		});
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

		final Image image = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_ARGB);
		placeholder = new JLabel(new ImageIcon(image));
		// Create border with color, thickness, length, spacing, rounded
		placeholder.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 2,
				6, 4, true));
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
	public void dropPanel(JPanel transferredPanel, Point dropLocation) {
		int newIndex = getInsertionIndex(dropLocation);

		// If transferredPanel is lower by index in the same stage, lower the
		// index by 1
		final int oldIndex = getComponentIndex(transferredPanel);
		if (oldIndex != -1 && oldIndex < newIndex) {
			newIndex--;
		}

		add(transferredPanel, newIndex);
		setPlaceholderVisible(false);

		// If listener assigned, save changes.
		if (listener != null) {
			listener.saveDrop(transferredPanel, newIndex);
		}
	}

	/**
	 *
	 * Draws a placeholder at roughly the given location with the given size.
	 *
	 * @param point
	 *            The location (relative to the DropAreaPanel) where the
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
		setPlaceholderVisible(true);
	}

	/**
	 * Hide/show placeholder: make sure animation timer starts when visibility
	 * changes
	 *
	 * @param visible
	 */
	public void setPlaceholderVisible(boolean visible) {
		if (placeholder.isVisible() != visible) { // state changed
			if (!animTimer.isRunning()) {
				updateDrawnBounds();
				animTimer.start();
			}
			placeholder.setVisible(visible);
		}
	}

	/**
	 *
	 * Places the placeholder at the appropriate location.
	 *
	 * @param index
	 *            Where the placeholder is to be placed.
	 */
	private void setPlaceholderIndex(int index) {
		System.out.println("Adding placeholder at " + index);
		this.add(placeholder, index);
		if (index != lastIndex) {
			if (!animTimer.isRunning()) { // start animation timer if not
											// running
				animTimer.start();
			}
			this.revalidate();
			this.repaint();
			lastIndex = index;
		}
	}

	/**
	 *
	 * Finds the center of each child panel for dealing with the placeholder. Do
	 * this only while placeholder is not visible in this DropAreaPanel
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
		final int placeholderIndex = getComponentIndex(placeholder);
		if (placeholderIndex != -1 && placeholderIndex < index) {
			index--;
		}

		System.out.println("Closest component" + Integer.toString(index));

		// Determine layout axis
		boolean vertical;
		final LayoutManager mgr = getLayout();
		if (mgr instanceof BoxLayout) {
			vertical = ((BoxLayout) mgr).getAxis() == BoxLayout.Y_AXIS;
		} else {
			vertical = false;
		}
		// Shift index by one as appropriate
		if (index < 0) {
			index = 0;
		} else if (vertical && point.y > compCenters.get(closest).y
				|| !vertical && point.x > compCenters.get(closest).x) {
			index++;
		}
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

	public void setSaveListener(DropAreaSaveListener listener) {
		this.listener = listener;
	}

	/**
	 * Update the bounds of the animated object
	 *
	 * @return whether the drawn bounds changed
	 */
	private synchronized boolean updateDrawnBounds() {

		boolean changed = false;

		final List<Component> components = Arrays.asList(getComponents());

		// For each component, add it to drawnBounds if it's not there and it is
		// visible
		for (Component comp : components) {
			if (!drawnBounds.containsKey(comp) && comp.isVisible()) {
				drawnBounds.put(comp, comp.getBounds());
			}
		}

		final Set<Component> drawnComps = new HashSet<Component>(
				drawnBounds.keySet());
		// Update each drawn bound
		for (Component comp : drawnComps) {
			Rectangle drawn = drawnBounds.get(comp);

			// Remove components from drawnBounds that are no longer in the
			// panel or invisible
			if (!components.contains(comp) || !comp.isVisible()) {
				drawnBounds.remove(comp);
			} else {
				// move the drawn panel halfway between current drawn location
				// and target position
				int deltaX = (comp.getBounds().x - drawn.x) / 2;
				int deltaY = (comp.getBounds().y - drawn.y) / 2;

				if (deltaX != 0 || deltaY != 0) {
					drawn.x += deltaX;
					drawn.y += deltaY;
					changed = true;
				}
			}
		}
		return changed;
	}

	/**
	 * Move the bounds to what we want, paint children, set bounds back
	 *
	 * @param g
	 *            Graphics context
	 *
	 * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
	 */
	@Override
	public synchronized void paintChildren(Graphics g) {
		final List<Rectangle> layoutBounds = new ArrayList<Rectangle>();
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
 * Listens for when a panel is dropped onto a the drop area, and while the drag
 * occurs.
 *
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 17, 2014
 */
class DropAreaListener implements DropTargetListener {

	private final DropAreaPanel panel;
	private final DataFlavor flavor;

	/**
	 * Constructor for a controller for drop areas
	 *
	 * @param panel
	 *            The panel where items can be dropped
	 * @param flavor
	 *            The flavor of data allowed to be dropped on panel
	 */
	DropAreaListener(DropAreaPanel panel, DataFlavor flavor) {
		this.panel = panel;
		this.flavor = flavor;
	}

	/**
	 * Drops a panel onto the DropAreaPanel after making sure that it is
	 * supported. <br>
	 * <br>
	 * {@inheritDoc}
	 */
	public void drop(DropTargetDropEvent e) {

		System.out.println("Dropping");

		final Transferable trans = e.getTransferable();
		final JPanel transferredPanel;
		if (trans.isDataFlavorSupported(flavor)) {
			try {
				transferredPanel = (JPanel) trans.getTransferData(flavor);
			} catch (Exception ex) {
				e.rejectDrop();
				System.out.println(ex.getStackTrace());
				return;
			}
			panel.dropPanel(transferredPanel, e.getLocation());
			e.acceptDrop(e.getDropAction());

		} else { // if not supported, try to redispatch to ancestor
			Component ancestor = panel.getParent();
			// find ancestor with DropTarget
			while (ancestor != null && ancestor.getDropTarget() == null) {
				ancestor = ancestor.getParent();
			}
			// Redispatch to ancestor drop target
			if (ancestor != null) {
				ancestor.getDropTarget().drop(
						DropTargetRedispatcher.convertCoords(e, ancestor));
			} else { // valid ancestor not found
				e.rejectDrop();
			}
		}

	}

	/**
	 * Hides the placeholder when the panel is no longer being dragged above the
	 * drop area.
	 *
	 * <br>
	 * {@inheritDoc}
	 */
	@Override
	public void dragExit(DropTargetEvent e) {
		System.out.println("Drop area drag exit");
		panel.setPlaceholderVisible(false);
	}

	/**
	 * Draws the placeholder on the drop area when a valid panel is being
	 * dragged above it. <br>
	 * <br>
	 * {@inheritDoc}
	 */
	@Override
	public void dragOver(DropTargetDragEvent e) {
		System.out.println("Drop area drag over");

		// Getting placeholder's size & making sure it's supported
		final Transferable trans = e.getTransferable();
		final JPanel transferredPanel;
		if (trans.isDataFlavorSupported(flavor)) {
			try {
				transferredPanel = (JPanel) trans.getTransferData(flavor);
			} catch (Exception ex) {
				e.rejectDrag();
				System.out.println(ex.getStackTrace());
				return;
			}

			transferredPanel.setVisible(false);
			e.acceptDrag(e.getDropAction());
			panel.drawPlaceholder(e.getLocation());

		} else { // if not supported, try to redispatch to ancestor
			Component ancestor = panel.getParent();
			// find ancestor with DropTarget
			while (ancestor != null && ancestor.getDropTarget() == null) {
				ancestor = ancestor.getParent();
			}
			// Redispatch to ancestor drop target
			if (ancestor != null) {
				ancestor.getDropTarget();
				ancestor.getDropTarget().dragOver(
						DropTargetRedispatcher.convertCoords(e, ancestor));
			} else { // valid ancestor not found
				e.rejectDrag();
			}
		}

	}

	@Override
	public void dragEnter(DropTargetDragEvent e) {
		// Treat start of drag the same as the rest of the drag
		dragOver(e);
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent e) {

	}
}
