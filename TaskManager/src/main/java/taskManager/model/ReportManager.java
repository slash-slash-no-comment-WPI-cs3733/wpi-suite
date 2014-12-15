/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * A workflow-level report manager. Tracks data about users, and returns useful
 * metrics about their work habits.
 *
 * @author Joseph Blackman
 * @author Ezra Davis
 * @version Nov 15, 2014
 */
public class ReportManager {

	// The data storage.
	private final Map<String, UserReport> data;

	/**
	 * The internal format for a user's report data. Subject to change.
	 *
	 * @author Joseph Blackman
	 * @author Ezra Davis
	 * @version Nov 15, 2014
	 */
	private class UserReport {
		int numCompleted;
		int actualEffort;
		int guessSum;
		int guessSumSquares;

		private UserReport() {
			numCompleted = 0;
			actualEffort = 0;
			guessSum = 0;
			guessSumSquares = 0;
		}

		/**
		 * The average difference between a user's estimated effort and their
		 * actual effort. Sign is maintained.
		 *
		 * @return the
		 */
		private double getAverageEstimateQuality() {
			return (double) guessSum / numCompleted;
		}
	}

	/**
	 * Default constructor. We are not allowing initialization with pre-existing
	 * hashmaps, since the internal format may change.
	 *
	 */
	public ReportManager() {
		data = new HashMap<String, UserReport>();
	}

	/**
	 * Should be called whenever a user completes a task.
	 *
	 * @param user
	 *            The user to give data about
	 * @param task
	 *            The task they completed
	 * 
	 * @throws IllegalStateException
	 *             if the task is not correctly formed.
	 */
	public void userFinishedTask(User user, TaskModel task)
			throws IllegalStateException {
		if (!trackingUser(user)) {
			addUser(user);
		}
		final UserReport userData = getUserData(user);
		final int taskActualEffort = task.isActualEffortSet() ? task
				.getActualEffort() : 0;
		if (taskActualEffort == 0) {
			throw new IllegalStateException(
					"Task does not have an actual effort");
		}
		final int taskEstimatedEffort = task.isEstimatedEffortSet() ? task
				.getEstimatedEffort() : 0;
		if (taskEstimatedEffort == 0) {
			throw new IllegalStateException(
					"Task does not have an estimated effort");
		}
		userData.numCompleted++;
		userData.actualEffort += taskActualEffort;
		userData.guessSum += (taskEstimatedEffort - taskActualEffort);
		userData.guessSumSquares += (taskEstimatedEffort - taskActualEffort)
				* (taskEstimatedEffort - taskActualEffort);
	}

	/**
	 * This method is private because people should not be allowed to access the
	 * raw data format. It may change at any time.
	 *
	 * @param user
	 *            The user to get data about
	 * @return the raw data
	 * @throws IllegalArgumentException
	 *             If we are not tracking the user.
	 */
	private UserReport getUserData(User user) throws IllegalArgumentException {
		if (!trackingUser(user)) {
			throw new IllegalArgumentException("User " + user.getName()
					+ " not being tracked.");
		}
		return data.get(user.getName());
	}

	/**
	 * If we are currently tracking a user.
	 *
	 * @param user
	 *            The user we may be tracking
	 * 
	 * @return If we are tracking them
	 */
	public boolean trackingUser(User user) {
		return data.containsKey(user.getName());
	}

	/**
	 * Start tracking a user
	 *
	 * @param user
	 *            the user to be tracked
	 */
	public void addUser(User user) {
		if (!trackingUser(user)) {
			data.put(user.getName(), new UserReport());
		}
	}

	/**
	 * The average amount of effort per task for a user
	 *
	 * @param user
	 *            The user to get data about
	 * 
	 * @return the average amount of work done per task
	 */
	public double getUserAverageEffort(User user) {
		final UserReport userData = getUserData(user);
		return (double) userData.actualEffort / userData.numCompleted;
	}

	/**
	 * The average difference between a user's estimated effort and their actual
	 * effort. Sign is maintained.
	 * 
	 * @param user
	 *            The user to get data about
	 * 
	 * @return the average quality of that user's estimations
	 */
	public double getAverageEstimateQuality(User user) {
		final UserReport userData = getUserData(user);
		return userData.getAverageEstimateQuality();
	}

	/**
	 * The standard deviation of differences between a user's estimated effort
	 * and their actual effort. Sign is not maintained. Note: We're using the
	 * population standard deviation.
	 * 
	 * @param user
	 *            the user to get data about
	 * 
	 * @return the standard deviation of the quality of that user's estimations
	 */
	public double getStdDevEstimateQuality(User user) {
		final UserReport userData = getUserData(user);
		final double averageEstimate = userData.getAverageEstimateQuality();
		return java.lang.Math.sqrt((double) userData.guessSumSquares
				/ userData.numCompleted - averageEstimate * averageEstimate);
		// This calcuates the standard deviation. The proof is left as an
		// exercise to the reader.
	}

	/**
	 * 
	 * Gets the number of tasks a user has completed
	 *
	 * @param user
	 *            what user we're asking about
	 * @return the count of all tasks a user has finished
	 */
	public double getTasksCompleted(User user) {
		return getUserData(user).numCompleted;
	}
}
