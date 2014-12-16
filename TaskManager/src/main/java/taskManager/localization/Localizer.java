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
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.UIManager;

/**
 * Returns strings in the appropriate language, defaulting to english. Notifies
 * all listeners whenever the language is changed
 *
 * @author Jon Sorrells
 */
public class Localizer {

	public static final String defaultLanguage = "english";

	// the current language, default to english
	private static String currentLanguage = defaultLanguage;

	private static final ResourceBundle defaultRB = ResourceBundle
			.getBundle("taskManager.localization." + defaultLanguage);

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
			// return english if string not found in that language
			System.err.println("no string found for key " + key
					+ " in language " + currentLanguage);
			return defaultRB.getString(key);
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

		UIManager.put("OptionPane.yesButtonText", getString("Yes"));
		UIManager.put("OptionPane.noButtonText", getString("No"));
		UIManager.put("OptionPane.cancelButtonText", getString("Cancel"));
		UIManager.put("JXDatePicker.linkFormat", getString("JXtoday"));

		// tell all of the listeners that the locale changed
		Iterator<WeakReference<LocaleChangeListener>> it = listeners.iterator();
		while (it.hasNext()) {
			LocaleChangeListener listener = it.next().get();
			if (listener == null) {
				it.remove();
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
