/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import taskManager.controller.TabPaneController;
import taskManager.controller.ToolbarController;
import taskManager.view.TabPaneView;
import taskManager.view.ToolbarView;

/**
 * Reset Janeway in the ugliest way possible
 *
 * @author Sam Khalandovsky
 * @version Dec 7, 2014
 */
public class JanewayReset {
	public static void reset() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {

		// Private and final? Not a problem, lets just undo that...

		Field modifiers = Field.class.getDeclaredField("modifiers");
		modifiers.setAccessible(true);

		Field toolV = JanewayModule.class.getDeclaredField("toolV");
		toolV.setAccessible(true);
		modifiers.setInt(toolV, toolV.getModifiers() & ~Modifier.FINAL);

		Field tabPaneV = JanewayModule.class.getDeclaredField("tabPaneV");
		tabPaneV.setAccessible(true);
		modifiers.setInt(tabPaneV, tabPaneV.getModifiers() & ~Modifier.FINAL);

		Field tabPaneC = JanewayModule.class.getDeclaredField("tabPaneC");
		modifiers.setInt(tabPaneC, tabPaneC.getModifiers() & ~Modifier.FINAL);
		tabPaneC.setAccessible(true);

		// Wonderful, now we can do whatever we like
		toolV.set(null, new ToolbarView());

		TabPaneView tpv = new TabPaneView();
		tabPaneV.set(null, tpv);

		tabPaneC.set(null, new TabPaneController(tpv));

		JanewayModule.toolV.setController(new ToolbarController(
				JanewayModule.tabPaneC.getTabView()));
	}

}
