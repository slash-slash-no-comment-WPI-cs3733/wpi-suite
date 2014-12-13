/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.localization;

import java.lang.ref.WeakReference;
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
	public static final String JAPANESE = "japanese";

	// the current language, default to english
	private static String currentLanguage = ENGLISH;

	// the current resource bundle object
	private static ResourceBundle rb = ResourceBundle
			.getBundle("taskManager.localization." + currentLanguage);

	// the list of views listening for local changes
	// use weak references to allow views to be garbage collected without
	// needing to unregister as a listener
	private static List<WeakReference<LocaleChangeListener>> listeners = new ArrayList<WeakReference<LocaleChangeListener>>();

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
		for (WeakReference<LocaleChangeListener> l : listeners) {
			LocaleChangeListener listener = l.get();
			if (listener == null) {
				listeners.remove(l);
			} else {
				listener.onLocaleChange();
			}
		}
	}

	/**
	 * Add something to the list of listeners
	 *
	 * @param listener
	 */
	public static void addListener(LocaleChangeListener listener) {
		listeners.add(new WeakReference<LocaleChangeListener>(listener));
	}

	/**
	 * Remove a listener from the list
	 *
	 * @param listener
	 */
	public static void removeListener(LocaleChangeListener listener) {
		for (WeakReference<LocaleChangeListener> l : listeners) {
			if (listener.equals(l.get())) {
				listeners.remove(l);
				return;
			}
		}
	}
}
