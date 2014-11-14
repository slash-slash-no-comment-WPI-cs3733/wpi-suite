/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * UserList generates a JPanel of UserView objects.
 * 
 * @author samee
 *
 */
public class UserList extends JPanel {

	private static final long serialVersionUID = -6637385817228659411L;
	private List<UserView> usersList;

	public UserList() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Example Users
		this.addUser(new User("Alice", "Alice0001", "A", 1));
		this.addUser(new User("Bobby", "Bob", "B", 2));
		this.addUser(new User("Carol", "Cooky", "C", 3));
		this.addUser(new User("David", "Dude552", "D", 4));
		this.addUser(new User("Earl", "Early.Bird", "B", 2));
		this.addUser(new User("Fiona", "Fia6652", "C", 3));
		this.addUser(new User("Greg", "Greggy", "D", 4));

	}

	/**
	 * addUser creates a new UserView with the given user
	 * 
	 * @param user
	 *            is the user to add to the user list
	 */
	public void addUser(User user) {
		UserView uv = new UserView(user);
		// This makes the tab disappear. No clue why...
		// this.usersList.add(uv);
		this.add(uv);
	}

	/**
	 * Removes a user from the user list
	 */
	public void removeUser(UserView user) {
		usersList.remove(user); // TODO: will this work? Write a test
	}
}
