/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    twack
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.SessionException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Manages Sessions for the ManagerLayer. Wraps a Map of Sessions and provides
 * interaction and manipulation functionality
 * 
 * @author twack
 *
 */
public class SessionManager {

	private Map<String, Session> sessions; // key: cookie value, value: Session

	private static final Logger logger = Logger.getLogger(SessionManager.class
			.getName());

	/**
	 * The default constructor.
	 */
	public SessionManager() {
		sessions = new HashMap<String, Session>();
	}

	/**
	 * Given a user, determines if this user has an active session.
	 * 
	 * @param u
	 * @return True if the map contains a Session for this user, False
	 *         otherwise.
	 */
	public boolean sessionExists(String sessionId) {
		return sessions.containsKey(sessionId);
	}

	/**
	 * Wipes all of the sessions currently stored in the manager.
	 */
	public void clearSessions() {
		logger.log(Level.INFO, "Session Manager clearing all sessions...");
		sessions = new HashMap<String, Session>();
	}

	/**
	 * Retrieves the Session for the user with the given name.
	 * 
	 * @param sessionToken
	 *            the tokenize cookie given from the client
	 * @return The session matching the token.
	 */
	public Session getSession(String sessionId) {

		return sessions.get(sessionId);
		// TODO: determine how to handle 'not found' case
	}

	/**
	 * Removes the session with the given username
	 * 
	 * @param sessionToken
	 */
	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}

	/**
	 * Returns a new Session for the given User.
	 * 
	 * @param username
	 * @return the identifying session long ID.
	 */
	public String createSession(User user) {
		// ignore the possibility of duplicate sessions per-user.

		// add session
		String ssid = this.generateUniqueSessionId();
		Session ses = new Session(user, ssid);
		sessions.put(ssid, ses);

		return ssid;
	}

	/**
	 * Returns a new Session for the given User into the given project
	 * 
	 * @param username
	 * @return the identifying session long ID.
	 */
	public String createSession(User user, Project p) {
		// ignore the possibility of duplicate sessions per-user.

		// add session
		String ssid = this.generateUniqueSessionId();
		Session ses = new Session(user, p, ssid);
		sessions.put(ssid, ses);

		return ssid;
	}

	/**
	 * Generates the Session ID as a random long. If the SSID has already been
	 * instantiated then that value is returned.
	 * 
	 * @return a long
	 */
	public String generateUniqueSessionId() {
		Random rand = new Random();
		long randomId = rand.nextLong();
		String ssid = String.valueOf(randomId);

		if (this.sessionExists(ssid)) {
			return this.generateUniqueSessionId();
		} else {
			return ssid;
		}
	}

	/**
	 * @return Retrieves the number of sessions currently in the Manager
	 */
	public int sessionCount() {
		return this.sessions.size();
	}

	/**
	 * Renews the Session for a given sessionToken. Parses the username from the
	 * token, then creates a new session for the given user.
	 * 
	 * @param sessionId
	 *            the ID of the session being switched.
	 * @param projectName
	 *            the name of the project being switched to.
	 * @return the new Session ID
	 * @throws WPISuiteException
	 */
	public String switchToProject(String sessionId, String projectName)
			throws WPISuiteException {
		logger.log(Level.INFO, "User attempting Project Session Switch...");
		// get a copy of the session so we can touch projects.
		Session current = this.getSession(sessionId);
		if (current == null) {
			logger.log(Level.WARNING,
					"Project Session switch attempted with invalid SSID");
			throw new SessionException(
					"Session matching the givenId does not exist");
		}

		User u = current.getUser();

		// find the project
		ManagerLayer manager = ManagerLayer.getInstance();
		ProjectManager projects = manager.getProjects();
		Project p = null;

		try {
			p = projects.getEntityByName(current, projectName)[0];

			if (p == null) {
				throw new NotFoundException(
						"Could not find project with given name to switch to.");
			}
		} catch (NotFoundException e) {
			logger.log(Level.WARNING,
					"Project Session switch attempted with nonexistent project");
			throw new SessionException(
					"Session-project switch failed because requested project does not exist.");
		}

		this.removeSession(sessionId);

		logger.log(Level.INFO, "User Project Session Switch successful!");
		return createSession(u, p);
	}

}