/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.ClientDataStore;
import taskManager.MockNetwork;
import taskManager.TaskManager;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.network.Network;

/**
 * Description
 *
 * @author Sam Khalandovsky
 * @version Dec 12, 2014
 */
public class TestEntityManager {

	static ClientDataStore db;

	@Before
	public void setup() {
		TaskManager.reset();
		Network.setInstance(new MockNetwork(true));
		db = ClientDataStore.getDataStore();
	}

	@Test
	public void testRecursiveDelete() throws WPISuiteException {
		StageModel sm = new StageModel("stage");
		TaskModel tm = new TaskModel("task", sm);
		TaskModel tm2 = new TaskModel("task2", sm);
		printDB();
		sm.save();
		printDB();
		assertDBHas(StageModel.class, sm.getID());
		assertDBHas(TaskModel.class, tm.getID());
		assertDBHas(TaskModel.class, tm2.getID());

		sm.delete();
		printDB();
		assertDBHasNot(StageModel.class, sm.getID());
		assertDBHasNot(TaskModel.class, tm.getID());
		assertDBHasNot(TaskModel.class, tm2.getID());

	}

	@Test
	public void storeTest() throws WPISuiteException {
		StageModel sm = new StageModel("stage");
		TaskModel tm = new TaskModel("task", sm);

		tm.save();
		tm.delete();

		sm.save();
		sm.delete();

		sm.save();
		tm.setName("task2");
		tm.save();

		assertEquals("task2",
				((StageModel) db.retrieve(StageModel.class, "id", sm.getID())
						.get(0)).getTasks().get(0).getName());
		tm.save();

	}

	@Test
	public void testCleanUpdate() throws WPISuiteException {
		StageModel sm = new StageModel("stage");
		TaskModel tm = new TaskModel("task", sm);
		TaskModel tm2 = new TaskModel("task2", sm);

		sm.save();

		tm.setName("task1-2");
		tm.save();
		assertEquals("task1-2",
				((TaskModel) db.retrieve(TaskModel.class, "id", tm.getID())
						.get(0)).getName());
		sm.setName("stage-2");
		sm.save();
		assertEquals("stage-2",
				((StageModel) db.retrieve(StageModel.class, "id", sm.getID())
						.get(0)).getName());
		assertEquals("task1-2",
				((TaskModel) db.retrieve(TaskModel.class, "id", tm.getID())
						.get(0)).getName());

		tm2.save();
		sm.save();

		assertEquals(1, db.retrieveAll(new StageModel()).size());
		assertEquals(2, db.retrieveAll(new TaskModel()).size());

		sm.delete();
		assertEquals(0, db.retrieveAll(new StageModel()).size());
		assertEquals(0, db.retrieveAll(new TaskModel()).size());

	}

	public void assertDBHas(Class c, String id) throws WPISuiteException {
		assertFalse(db.retrieve(c, "id", id).isEmpty());
	}

	public void assertDBHasNot(Class c, String id) throws WPISuiteException {
		assertTrue(db.retrieve(c, "id", id).isEmpty());
	}

	public void printDB() {
		System.out.println("DB: " + db.retrieveAll(new Object()));
	}

	@After
	public void netTeardown() {
		ClientDataStore.deleteDataStore();
	}

}
