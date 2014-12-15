/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import taskManager.TaskManager;
import taskManager.localization.Localizer;

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
		CREATION, MOVE, USER_ADD, USER_REMOVE, COMMENT, ARCHIVE, UNARCHIVE
	}

	// Number of parameters each type accepts
	private static Map<ActivityModelType, Integer> pNums;
	static {
		pNums = new HashMap<ActivityModelType, Integer>();
		pNums.put(ActivityModelType.CREATION, 1);
		pNums.put(ActivityModelType.MOVE, 2);
		pNums.put(ActivityModelType.USER_ADD, 1);
		pNums.put(ActivityModelType.USER_REMOVE, 1);
		pNums.put(ActivityModelType.COMMENT, 1);
		pNums.put(ActivityModelType.ARCHIVE, 0);
		pNums.put(ActivityModelType.UNARCHIVE, 0);
	}

	// Actual type of this model
	private final ActivityModelType type;

	// Parameters of the activity type
	private String[] params;

	// Date of creation
	private Date dateCreated;

	// Name of user who took the action; null for system activities
	private final String actor;

	/**
	 * Activity Constructor
	 *
	 * @param type
	 *            The type of the activity
	 * @param args
	 *            Parameters of the activity
	 * 
	 */
	public ActivityModel(ActivityModelType type, String... args) {
		// Check that args is the right length
		if (args.length != pNums.get(type)) {
			throw new IllegalArgumentException();
		}
		actor = TaskManager.currentUser;
		this.type = type;
		dateCreated = new Date(); // set date to time ActivityModel was
									// instantiated
		params = args;
	}

	/**
	 * Return the type of the activity
	 *
	 * @return the activity type
	 */
	public ActivityModelType getType() {
		return type;
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * Get the localized, formatted string describing the activity
	 * 
	 * @return the string
	 */
	public String getDescription() {
		String format;
		if (type == null) {
			return "";
		}
		switch (type) {
		case CREATION:
			format = Localizer.getString("ActivityCreate");
			break;
		case MOVE:
			format = Localizer.getString("ActivityMove");
			break;
		case USER_ADD:
			format = Localizer.getString("ActivityUserAdd");
			break;
		case USER_REMOVE:
			format = Localizer.getString("ActivityUserRemove");
			break;
		case COMMENT:
			// Comments aren't localized
			return params[0];
		case ARCHIVE:
			format = Localizer.getString("ActivityArchive");
			break;
		case UNARCHIVE:
			format = Localizer.getString("ActivityUnarchive");
			break;
		default:
			throw new IllegalStateException();
		}

		try {
			return MessageFormat.format(format, (Object[]) params);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Sets the description of the activity, only if the activity is a comment.
	 *
	 * @param newDescription
	 *            the new description.
	 */
	public void setDescription(String newDescription) {
		if (type != ActivityModelType.COMMENT) {
			throw new UnsupportedOperationException(
					"You cannot change the description of non-comment activities.");
		}
		params[0] = newDescription;
	}

	/**
	 * @return the actor
	 */
	public String getActor() {
		return actor;
	}

}
