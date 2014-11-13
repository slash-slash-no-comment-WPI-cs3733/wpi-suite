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
public class SaveTest {

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

	@Test
	public void saveLoad() {
		WorkflowModel wf = new WorkflowModel("wf1");
		StageModel stage = new StageModel(wf, "stage1");
		TaskModel model = new TaskModel("model1", stage);

		model.save();
	}

	@Test
	public void modelJSON() {
		WorkflowModel wf = new WorkflowModel("wf1");
		StageModel stage = new StageModel(wf, "stage1");
		System.out.println(stage.toJson());
		TaskModel model = new TaskModel("model1", stage);
		System.out.println(stage.toJson());
		TaskModel model1 = new TaskModel("model1", stage);
		System.out.println(stage.toJson());
		TaskModel model2 = new TaskModel("model1", stage);
		System.out.println(stage.toJson());
		TaskModel model3 = new TaskModel("model1", stage);

		System.out.println(model.toJson());
		System.out.println(model1.toJson());
		System.out.println(model2.toJson());
		System.out.println(model3.toJson());

	}

}
