/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import taskManager.controller.TabPaneController;
import taskManager.controller.ToolbarController;
import taskManager.view.TabPaneView;
import taskManager.view.ToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * The JanewayModule for the task manager
 *
 * @author Beth Martino
 * @author Samee Swartz
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class JanewayModule implements IJanewayModule {

	// The tabs used by this module

	private final List<JanewayTabModel> tabs;
	public static final ToolbarView toolV = new ToolbarView();
	private static final TabPaneView tabPaneV = new TabPaneView();
	public static final TabPaneController tabPaneC = new TabPaneController(
			tabPaneV);
	public static User[] users = {};
	public static String currentUser = null; // the username of the current user

	/**
	 * Construct a blank tab
	 */
	public JanewayModule() {
		toolV.setController(new ToolbarController(tabPaneV));

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Task Manager",

		new ImageIcon(), toolV, tabPaneV);
		tabs.add(tab);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Task Manager";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

	/**
	 * 
	 * Returns the tabPaneView
	 *
	 * @return tabPaneV
	 */
	public static TabPaneView getTabPaneView() {
		return tabPaneV;
	}
}
