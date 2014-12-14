/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import java.awt.KeyboardFocusManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import taskManager.controller.EasterEggListener;
import taskManager.controller.StageController;
import taskManager.controller.TabPaneController;
import taskManager.controller.TaskController;
import taskManager.controller.ToolbarController;
import taskManager.controller.WorkflowController;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.model.FetchWorkflowObserver;
import taskManager.model.StageModel;
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
public class TaskManager implements IJanewayModule {

	// The tabs used by this module

	private final List<JanewayTabModel> tabs;

	public static User[] users = {};
	public static String currentUser = null; // the username of the current user

	/**
	 * Construct a blank tab
	 */
	public TaskManager() {

		tabs = new ArrayList<JanewayTabModel>();
		final JanewayTabModel tab = new JanewayTabModel("Task Manager",
				new ImageIcon(), ToolbarController.getInstance().getView(),
				TabPaneController.getInstance().getView());
		tabs.add(tab);

		// Add default stages
		new StageModel("New");
		new StageModel("Scheduled");
		new StageModel("In Progress");
		new StageModel("Complete");

		WorkflowController.getInstance().reloadData();

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new EasterEggListener());
	}

	/**
	 * Does a full reset of the module; useful for testing
	 *
	 */
	public static void reset() {

		StageController.anyChangeTitleOut = false;
		TaskController.anyTaskInfoOut = false;
		DDTransferHandler.dragSaved = false;
		FetchWorkflowObserver.ignoreAllResponses = false;

		// Reset singletons
		ToolbarController.getInstance().reset();
		WorkflowController.getInstance().reset();
		TabPaneController.getInstance().reset();

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

	/*
	 * If we're on OS X
	 * 
	 * @return If we're using a mac.
	 */
	public static boolean isOnMac() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.startsWith("mac os x");
	}
}
