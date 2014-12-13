/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import taskManager.controller.TaskFilter;
import taskManager.controller.TaskFilter.ArchiveState;
import taskManager.controller.TaskFilter.StringField;
import taskManager.model.ActivityModel;
import taskManager.model.ActivityModel.ActivityModelType;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.TaskModel.TaskCategory;
import taskManager.model.WorkflowModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Description
 *
 * @author Sam Khalandovsky
 * @version Dec 11, 2014
 */
public class TestTaskFilter {

	StageModel sm;
	TaskFilter f;
	TaskModel tm;

	@Before
	public void setup() {
		WorkflowModel.getInstance().reset();
		sm = new StageModel("Stage");
		f = new TaskFilter();
	}

	@Test
	public void testArchive() {
		tm = new TaskModel("task", sm);
		f.setArchive(ArchiveState.ARCHIVED);
		assertFalse(f.check(tm));
		f.setArchive(ArchiveState.NOT_ARCHIVED);
		assertTrue(f.check(tm));
		f.setArchive(ArchiveState.NOT_ARCHIVED, ArchiveState.ARCHIVED);
		assertTrue(f.check(tm));
	}

	@Test
	public void testUser() {
		tm = new TaskModel("task", sm);
		User u = new User("name", "username", "pass", 99);
		User u2 = new User("name2", "username2", "pass", 100);
		User u3 = new User("name3", "username3", "pass", 101);
		tm.addAssigned(u);
		tm.addAssigned(u2);

		HashSet<String> fUsers = new HashSet<String>();
		fUsers.add(u2.getUsername());
		fUsers.add(u3.getUsername());

		f.setUsers(fUsers);

		assertTrue(f.check(tm));
		tm.removeAssigned(u2);
		assertFalse(f.check(tm));
		f.setUser(u.getUsername());
		assertTrue(f.check(tm));
	}

	@Test
	public void testCategory() {
		tm = new TaskModel("task", sm);
		tm.setCategory(TaskCategory.RED);

		f.setCategory(TaskCategory.RED);
		assertTrue(f.check(tm));

		HashSet<TaskCategory> cats = new HashSet<TaskCategory>();
		cats.add(TaskCategory.RED);
		cats.add(TaskCategory.BLUE);
		f.setCategories(cats);
		assertTrue(f.check(tm));
		cats.remove(TaskCategory.RED);
		assertFalse(f.check(tm));
	}

	@Test
	public void testString() {
		tm = new TaskModel("Task Name", sm);
		tm.setDescription("A Fine Description");
		tm.addActivity(new ActivityModel("A New Comment",
				ActivityModelType.COMMENT));

		f.setString("k na");
		assertTrue(f.check(tm));

		f.setString("k na", StringField.NAME);
		assertTrue(f.check(tm));

		f.setString("k na", StringField.DESCRIPTION);
		assertFalse(f.check(tm));

		f.setString("k na", StringField.COMMENTS);
		assertFalse(f.check(tm));

		f.setString("New");
		assertTrue(f.check(tm));

		f.setString("Newt");
		assertFalse(f.check(tm));

		f.setString("comments");
		assertFalse(f.check(tm));

		f.setString(" ");
		assertTrue(f.check(tm));

		f.setString("  ");
		assertFalse(f.check(tm));
	}

}
