/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.localization;

/**
 * Interface to listen for locale changes
 *
 * @author Jon Sorrells
 */
public interface LocaleChangeListener {
	/**
	 * The method is called when the user changes the selected languate Views
	 * should update any visible strings to be the new language
	 *
	 */
	void onLocaleChange();
}
