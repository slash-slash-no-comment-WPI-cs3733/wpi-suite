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
import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
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

	private final ArrayList<JanewayTabModel> tabs;
	private static ToolbarView toolV;
	public static User[] users = {};
	public static String currentUser = null; // the username of the current user

	/**
	 * Construct a blank tab
	 */
	public JanewayModule() {
		toolV = new ToolbarView();
		toolV.setController(new ToolbarController());

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Task Manager",
				new ImageIcon(), getToolV(), TabPaneController.getInstance()
						.getView());
		tabs.add(tab);

		// Add default stages
		new StageModel("New");
		new StageModel("Scheduled");
		new StageModel("In Progress");
		new StageModel("Complete");

		WorkflowController.getInstance().reloadData();
	}

	/**
	 * Does a full reset of the module; useful for testing
	 *
	 */
	public static void reset() {
		toolV = new ToolbarView();
		toolV.setController(new ToolbarController());

		// Reset singletons
		TabPaneController.getInstance().reset();
		WorkflowController.getInstance().reset();
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
	 * @return the toolV
	 */
	public static ToolbarView getToolV() {
		if (toolV == null) {
			throw new IllegalStateException("JanewayModule not initialized");
		}
		return toolV;
	}
}
