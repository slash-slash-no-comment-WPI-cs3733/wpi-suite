/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import org.junit.Test;

/**
 * Some basic tests
 *
 * @author Sam Khalandovsky
 * @version Nov 10, 2014
 */
public class ConstructorTest {

	/**
	 * Check that everything has a blank constructor
	 */
	@Test
	public void blankConstructors() throws InstantiationException,
			IllegalAccessException {
		TaskModel.class.newInstance();
		StageModel.class.newInstance();
		WorkflowModel.class.newInstance();
	}

	@Test
	public void entityCreateTest() {
		new GenericEntityManager<TaskModel>(null, TaskModel.class);
	}
}
