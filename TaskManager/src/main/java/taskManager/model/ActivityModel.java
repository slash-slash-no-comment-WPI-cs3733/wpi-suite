/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Description
 *
 * @author Sam Khalandovsky
 * @version Nov 6, 2014
 */
public class ActivityModel implements IModel {

	// Static ID counter; incremented each time task is created. Rollover can be
	// safely ignored.
	private static int idCount = 0;

	// Unique identifier
	private final int id;

	// Date of creation
	private final Date dateCreated;

	// Contents of activity
	private final String description;

	// User who took the action; null for system activities
	private final User actor;

	/**
	 * Constructor for automatic activities
	 *
	 * @param description
	 */
	public ActivityModel(String description) {
		this(description, null);
	}

	/**
	 * Constructor for activities with user actor
	 *
	 * @param description
	 * @param actor
	 */
	public ActivityModel(String description, User actor) {
		this.description = description;
		this.actor = actor;
		id = idCount++;
		dateCreated = new Date(); // set date to time ActivityModel was
									// instantiated
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @return the contents
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the actor
	 */
	public User getActor() {
		return actor;
	}

}
