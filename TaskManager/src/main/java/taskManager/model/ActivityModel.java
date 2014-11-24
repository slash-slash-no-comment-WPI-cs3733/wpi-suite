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

import taskManager.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

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
	public enum activityModelType {
		CREATION, MOVE, COMPLETION, USER_ADD, USER_REMOVE, COMMENT
	};

	// Actual type of this model
	private activityModelType modelType;

	// Date of creation
	private Date dateCreated;

	// Contents of activity
	private String description;

	// Name of user who took the action; null for system activities
	private String actor;

	/**
	 * Constructor for activities with no user actor/unknown user actor
	 *
	 * @param description
	 *            The text in the activity
	 * @param type
	 *            The type of activity
	 */
	public ActivityModel(String description, activityModelType type) {
		this(description, type, null);
	}

	/**
	 * Constructor for activities with user actor
	 *
	 * @param description
	 *            The activity description
	 * @param type
	 *            The type of activity
	 * @param actor
	 *            The user who is doing the activity
	 */
	public ActivityModel(String description, activityModelType type, User actor) {
		this.description = description;
		modelType = type;
		if (actor != null) {
			this.actor = actor.getUsername();
		} else {
			this.actor = null;
		}
		dateCreated = new Date(); // set date to time ActivityModel was
									// instantiated
	}

	/**
	 * Return the type of the activity
	 *
	 * @return the activity type
	 */
	public activityModelType getType() {
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
		if (modelType != activityModelType.COMMENT) {
			throw new UnsupportedOperationException(
					"You cannot change the description of non-comment activities.");
		}
		description = newDescription;
	}

	/**
	 * @return the actor
	 */
	public User getActor() {
		for (User u : JanewayModule.users) {
			if (u.getUsername().equals(actor)) {
				return u;
			}
		}
		return null;
	}

}
