/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.draganddrop;

import javax.swing.JPanel;

/**
 * Interface for providing additional save functionality after a drop in a
 * DropAreaPanel
 *
 * @author Sam Khalandovsky
 * @version Dec 1, 2014
 */
public interface DropAreaSaveListener {
	/**
	 * Save changes after a drag
	 *
	 * @param panel
	 *            transferred panel
	 * @param index
	 *            insertion index
	 */
	public void saveDrop(JPanel panel, int index);

}
