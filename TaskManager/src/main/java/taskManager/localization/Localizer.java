/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.localization;

import java.util.ArrayList;
import java.util.List;
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
	public static final String TODO = "TODO";

	// the current language, default to english
	private static String currentLanguage = ENGLISH;

	// the current resource bundle object
	private static ResourceBundle rb = ResourceBundle
			.getBundle("taskManager.localization." + currentLanguage);

	// the list of views listening for local changes
	private static List<LocaleChangeListener> listeners = new ArrayList<LocaleChangeListener>();

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

		// tell all of the listeners that the locale changed
		listeners.forEach(l -> {
			l.onLocaleChange();
		});
	}

	/**
	 * Add something to the list of listeners
	 *
	 * @param listener
	 */
	public static void addListener(LocaleChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a listener from the list
	 * 
	 * TODO: actually call this when needed
	 *
	 * @param listener
	 */
	public static void removeListener(LocaleChangeListener listener) {
		listeners.remove(listener);
	}
}
