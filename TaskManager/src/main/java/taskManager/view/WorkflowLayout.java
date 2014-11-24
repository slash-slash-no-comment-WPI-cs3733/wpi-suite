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
import java.awt.LayoutManager;

public class WorkflowLayout implements LayoutManager {
	private final Container target;
	private static final Dimension preferredSize = new Dimension(500, 500);

	public WorkflowLayout(final Container target) {
		this.target = target;
	}

	@Override
	public void addLayoutComponent(final String name, final Component comp) {
	}

	@Override
	public void layoutContainer(final Container container) {
		for (final Component component : container.getComponents()) {
			if (target instanceof WorkflowView) {
				if (component.isVisible()) {
					if (component.getName() == ((WorkflowView) target).STAGES) {
						component.setBounds(0, 0, target.getWidth(),
								target.getHeight());
					} else if (component.getName() == ((WorkflowView) target).TASK_INFO) {
						System.out.println(component.getBounds());
						component.setBounds(component.getBounds());
					} else {
						System.out.println("Using an undocumented JPanel");
					}
				}
			} else {
				System.out
						.println("Using WorkflowLayout for something other than WorkflowView...");
			}
		}
	}

	@Override
	public Dimension minimumLayoutSize(final Container parent) {
		return preferredLayoutSize(parent);
	}

	@Override
	public Dimension preferredLayoutSize(final Container parent) {
		return preferredSize;
	}

	@Override
	public void removeLayoutComponent(final Component comp) {
	}

}