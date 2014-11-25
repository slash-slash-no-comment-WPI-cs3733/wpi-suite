/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

/**
 * 
 * The Layout manager for WorkflowView. This overrides a bunch of FlowLayout
 * functions to only work with non-TaskInfoPreviewView objects. Should ONLY be
 * used for WorkflowView.
 *
 * @author Samee Swartz
 * @version Nov 24, 2014
 */
public class WorkflowLayout extends FlowLayout {

	private int hgap = 5, vgap = 5;

	public WorkflowLayout() {
	}

	@Override
	public void addLayoutComponent(final String name, final Component comp) {
		// Do nothing
	}

	/**
	 * Basically the same as FlowLayout's layoutContainer but does not manage
	 * TaskInfoPreviewView objects
	 */
	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int maxwidth = target.getWidth()
					- (insets.left + insets.right + hgap * 2);
			int nmembers = target.getComponentCount();
			int x = 0, y = insets.top + vgap;
			int rowh = 0, start = 0;

			boolean ltr = target.getComponentOrientation().isLeftToRight();

			boolean useBaseline = getAlignOnBaseline();
			int[] ascent = null;
			int[] descent = null;

			if (useBaseline) {
				ascent = new int[nmembers];
				descent = new int[nmembers];
			}

			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible() && !(m instanceof TaskInfoPreviewView)) {
					Dimension d = m.getPreferredSize();
					m.setSize(d.width, d.height);

					if (useBaseline) {
						int baseline = m.getBaseline(d.width, d.height);
						if (baseline >= 0) {
							ascent[i] = baseline;
							descent[i] = d.height - baseline;
						} else {
							ascent[i] = -1;
						}
					}
					if ((x == 0) || ((x + d.width) <= maxwidth)) {
						if (x > 0) {
							x += hgap;
						}
						x += d.width;
						rowh = Math.max(rowh, d.height);
					} else {
						rowh = moveComponents(target, insets.left + hgap, y,
								maxwidth - x, rowh, start, i, ltr, useBaseline,
								ascent, descent);
						x = d.width;
						y += vgap + rowh;
						rowh = d.height;
						start = i;
					}
				} else if (m instanceof TaskInfoPreviewView) {
					m.setBounds(m.getBounds());
				}
			}
			moveComponents(target, insets.left + hgap, y, maxwidth - x, rowh,
					start, nmembers, ltr, useBaseline, ascent, descent);
		}
	}

	/**
	 * Basically the same as FlowLayout's moveComponents but does not manage
	 * TaskInfoPreviewView objects
	 */
	private int moveComponents(Container target, int x, int y, int width,
			int height, int rowStart, int rowEnd, boolean ltr,
			boolean useBaseline, int[] ascent, int[] descent) {
		switch (FlowLayout.CENTER) {
		case LEFT:
			x += ltr ? 0 : width;
			break;
		case CENTER:
			x += width / 2;
			break;
		case RIGHT:
			x += ltr ? width : 0;
			break;
		case LEADING:
			break;
		case TRAILING:
			x += width;
			break;
		}
		int maxAscent = 0;
		int nonbaselineHeight = 0;
		int baselineOffset = 0;
		if (useBaseline) {
			int maxDescent = 0;
			for (int i = rowStart; i < rowEnd; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible() && !(m instanceof TaskInfoPreviewView)) {
					if (ascent[i] >= 0) {
						maxAscent = Math.max(maxAscent, ascent[i]);
						maxDescent = Math.max(maxDescent, descent[i]);
					} else {
						nonbaselineHeight = Math.max(m.getHeight(),
								nonbaselineHeight);
					}
				}
			}
			height = Math.max(maxAscent + maxDescent, nonbaselineHeight);
			baselineOffset = (height - maxAscent - maxDescent) / 2;
		}
		for (int i = rowStart; i < rowEnd; i++) {
			Component m = target.getComponent(i);
			if (m.isVisible() && !(m instanceof TaskInfoPreviewView)) {
				int cy;
				if (useBaseline && ascent[i] >= 0) {
					cy = y + baselineOffset + maxAscent - ascent[i];
				} else {
					cy = y + (height - m.getHeight()) / 2;
				}
				if (ltr) {
					m.setLocation(x, cy);
				} else {
					m.setLocation(target.getWidth() - x - m.getWidth(), cy);
				}
				x += m.getWidth() + hgap;
			}
		}
		return height;
	}

	/**
	 * Returns the minimum dimensions needed to layout the <i>visible</i>
	 * components contained in the specified target container.
	 * 
	 * Basically the same as FlowLayout's minimumLayoutSize but does not manage
	 * TaskInfoPreviewView objects
	 *
	 * 
	 * @param target
	 *            the container that needs to be laid out
	 * @return the minimum dimensions to lay out the subcomponents of the
	 *         specified container
	 * @see #preferredLayoutSize
	 * @see java.awt.Container
	 * @see java.awt.Container#doLayout
	 */
	@Override
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			boolean useBaseline = getAlignOnBaseline();
			Dimension dim = new Dimension(0, 0);
			int nmembers = target.getComponentCount();
			int maxAscent = 0;
			int maxDescent = 0;
			boolean firstVisibleComponent = true;

			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible() && !(m instanceof TaskInfoPreviewView)) {
					Dimension d = m.getMinimumSize();
					dim.height = Math.max(dim.height, d.height);
					if (firstVisibleComponent) {
						firstVisibleComponent = false;
					} else {
						dim.width += hgap;
					}
					dim.width += d.width;
					if (useBaseline) {
						int baseline = m.getBaseline(d.width, d.height);
						if (baseline >= 0) {
							maxAscent = Math.max(maxAscent, baseline);
							maxDescent = Math.max(maxDescent, dim.height
									- baseline);
						}
					}
				}
			}

			if (useBaseline) {
				dim.height = Math.max(maxAscent + maxDescent, dim.height);
			}

			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right + hgap * 2;
			dim.height += insets.top + insets.bottom + vgap * 2;
			return dim;

		}
	}

	/**
	 * Returns the preferred dimensions for this layout given the <i>visible</i>
	 * components in the specified target container.
	 * 
	 * Basically the same as FlowLayout's preferredLayoutSize but does not
	 * manage TaskInfoPreviewView objects
	 *
	 *
	 * @param target
	 *            the container that needs to be laid out
	 * @return the preferred dimensions to lay out the subcomponents of the
	 *         specified container
	 * @see Container
	 * @see #minimumLayoutSize
	 * @see java.awt.Container#getPreferredSize
	 */
	@Override
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension(0, 0);
			int nmembers = target.getComponentCount();
			boolean firstVisibleComponent = true;
			boolean useBaseline = getAlignOnBaseline();
			int maxAscent = 0;
			int maxDescent = 0;

			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible() && !(m instanceof TaskInfoPreviewView)) {
					Dimension d = m.getPreferredSize();
					dim.height = Math.max(dim.height, d.height);
					if (firstVisibleComponent) {
						firstVisibleComponent = false;
					} else {
						dim.width += hgap;
					}
					dim.width += d.width;
					if (useBaseline) {
						int baseline = m.getBaseline(d.width, d.height);
						if (baseline >= 0) {
							maxAscent = Math.max(maxAscent, baseline);
							maxDescent = Math.max(maxDescent, d.height
									- baseline);
						}
					}
				}
			}
			if (useBaseline) {
				dim.height = Math.max(maxAscent + maxDescent, dim.height);
			}
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right + hgap * 2;
			dim.height += insets.top + insets.bottom + vgap * 2;
			return dim;
		}
	}

	@Override
	public void removeLayoutComponent(final Component comp) {
		// Do Nothing
	}

}