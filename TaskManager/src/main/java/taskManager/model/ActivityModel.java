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

import taskManager.TaskManager;

/**
 * Description Activities represent changes to a Task and comments users have
 * written about a Task.
 *
 * @author Sam Khalandovsky
 * @version Nov 6, 2014
 */
public class ActivityModel {

	// List of possible model types
	/**
	 */
	public enum ActivityModelType {
		CREATION, MOVE, COMPLETION, USER_ADD, USER_REMOVE, COMMENT, ARCHIVE
	};

	// Actual type of this model
	private ActivityModelType modelType;

	// Date of creation
	private Date dateCreated;

	// Contents of activity
	private String description;

	// Name of user who took the action; null for system activities
	private final String actor;

	/**
	 * Constructor for activities with no user actor/unknown user actor
	 *
	 * @param description
	 *            The text in the activity
	 * @param type
	 *            The type of activity
	 */
	public ActivityModel(String description, ActivityModelType type) {
		actor = TaskManager.currentUser;
		this.description = description;
		modelType = type;
		dateCreated = new Date(); // set date to time ActivityModel was
									// instantiated
	}

	/**
	 * Return the type of the activity
	 *
	 * @return the activity type
	 */
	public ActivityModelType getType() {
		return modelType;
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
	 * Sets the description of the activity, only if the activity is a comment.
	 *
	 * @param newDescription
	 *            the new description.
	 */
	public void setDescription(String newDescription) {
		if (modelType != ActivityModelType.COMMENT) {
			throw new UnsupportedOperationException(
					"You cannot change the description of non-comment activities.");
		}
		description = newDescription;
	}

	/**
	 * @return the actor
	 */
	public String getActor() {
		return actor;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ActivityModel)) {
			return false;
		}
		if (!(modelType.equals(((ActivityModel) obj).getType()))) {
			return false;
		}
		if (!(dateCreated.equals(((ActivityModel) obj).getDateCreated()))) {
			return false;
		}
		if (!(description.equals(((ActivityModel) obj).getDescription()))) {
			return false;
		}
		// actor != null needed for tests
		if ((actor != null)
				&& (!(actor.equals(((ActivityModel) obj).getActor())))) {
			return false;
		}

		return true;
	}
}
