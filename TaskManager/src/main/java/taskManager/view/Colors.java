/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.Color;

/**
 * 
 * Defines the colors to be used in the TaskManager module.
 *
 * @author Beth Martino
 * @version 2014/12/03
 */
public class Colors {

	public static final Color TASK = Color.decode("#FCFCFC");
	public static final Color TASK_HOVER = Color.decode("#E0E0E0");
	public static final Color TASK_CLICKED = Color.decode("#BDBDBD");
	public static final Color SHADOW_COLOR = Color.BLACK;
	public static final Color STAGE = Color.decode("#C8DDF1");
	public static final Color INPUT_ERROR = Color.decode("#FCFCFC");
	public static final Color ACTIVITY = Color.decode("#FCFCFC");
	public static final Color ACTIVITY_EDIT = Color.LIGHT_GRAY;
	public static final Color ACTIVITY_COMMENT = Color.CYAN;
	public static final Color ARCHIVE_HOVER = Color.decode("#E6B000");
	// The default grey color of a JLabel
	public static final Color STUPID_GRAY = Color.decode("#EAEAEA");

	public static final Color[] CAT_COLORS = new Color[] { // first is a
			// "no category color"
			Color.decode("#B6B6B6"), Color.decode("#EF5350"),
			Color.decode("#4CAF50"), Color.decode("#2196F3"),
			Color.decode("#FBC02D"), Color.decode("#7E57C2") };

	public static final String[] CATEGORY_NAMES = new String[] {
			"Select category", "red", "green", "blue", "yellow", "purple" };
}
