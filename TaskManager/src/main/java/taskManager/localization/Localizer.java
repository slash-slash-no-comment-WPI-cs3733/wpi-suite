/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.localization;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Description
 *
 * @author Jon Sorrells
 */
public class Localizer {

	// constants with the names of the language files
	public static final String ENGLISH = "english";
	public static final String PIRATE = "pirate";

	// the current language, default to english
	private static String currentLanguage = ENGLISH;

	// the current resource bundle object
	private static ResourceBundle rb = ResourceBundle
			.getBundle("taskManager.localization." + currentLanguage);

	/**
	 * @param the
	 *            key for the string you want
	 * @return a localized version of the string
	 */
	public static String getString(String key) {
		// try to load the string for the appropriate language
		try {
			return rb.getString(key);
		} catch (MissingResourceException e) {
			// return the key if no string exists
			System.err.println("no string found for key " + key
					+ " in language " + currentLanguage);
			return key;
		}
	}

	/**
	 * Set the current language
	 *
	 * @param language
	 *            the language to set
	 */
	public static void setLanguage(String language) {
		currentLanguage = language;
		rb = ResourceBundle.getBundle("taskManager.localization."
				+ currentLanguage);
	}

}
