/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

/**
 * @author Tyler Jaskoviak
 *
 */
public class TabGraphPaneView extends JTabbedPane {
	
	//private static final long serialVersionUID = -4912871689110151496L;
	// Track both graphs always
	
	//private GraphFlowController
	//private final GraphFlowModel
	
	//private GraphVelocityController
	//private final GraphVelocityModel

	public TabGraphPaneView() {
		setTabPlacement(TOP);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setBorder(BorderFactory.createEmptyBorder(5, 3, 3, 3));
		this.setSize(new Dimension(500, 500));
		
		//TODO
		//make GraphFlowView
		//set model and controller
		//set controller for view
		//repeat for GraphVelocityView

		//this.addTab->flow
		//this.addTab->velocity
	}
}
