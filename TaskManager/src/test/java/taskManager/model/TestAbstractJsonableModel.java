/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;

/**
 * Description
 *
 * @author Sam Khalandovsky
 * @version Dec 4, 2014
 */
public class TestAbstractJsonableModel {

	WorkflowModel wm;
	StageModel sm1, sm2;
	TaskModel tm1, tm2, tm3;

	@Before
	public void setUp() {

		JanewayModule.reset();

		sm1 = new StageModel("T");
		sm2 = new StageModel("T2");
		tm1 = new TaskModel("T", sm1);
		tm2 = new TaskModel("T", sm2);
		tm3 = new TaskModel("T", sm2);
	}

	@Test
	public void testIdentify() {
		assertTrue(wm.identify(wm));
		assertTrue(tm1.identify(tm1));

		assertFalse(wm.identify(sm1));
		assertFalse(tm1.identify(tm2));
		assertFalse(tm1.identify(sm1));
		assertFalse(sm1.identify(sm2));

		assertTrue(wm.identify(wm.getID()));
		assertTrue(tm1.identify(tm1.getID()));

		assertFalse(tm2.identify(tm1.getID()));
		assertFalse(sm1.identify(sm2.getID()));
	}
}
