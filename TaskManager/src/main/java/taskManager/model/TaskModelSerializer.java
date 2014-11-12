/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.model;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Sam Khalandovsky
 * @version Nov 10, 2014
 */
public class TaskModelSerializer implements JsonSerializer<TaskModel> {

	/**
	 * Serialze a TaskModel to a JsonObject
	 * 
	 * @param task
	 *            TaskModel
	 * @param type
	 *            Type
	 * @param context
	 *            JsonSerializationContext
	 * 
	 * @return JsonElement
	 */
	@Override
	public JsonElement serialize(TaskModel task, Type type,
			JsonSerializationContext context) {
		final JsonObject deflated = new JsonObject();

		deflated.addProperty("name", task.getName());
		deflated.addProperty("id", task.getID());
		deflated.addProperty("description", task.getDescription());
		deflated.addProperty("stage", task.getStage().getName());
		final Date dueDate = task.getDueDate();
		deflated.addProperty("dueDate", dueDate.getTime());
		deflated.addProperty("estimatedEffort", task.getEstimatedEffort());
		deflated.addProperty("actualEffort", task.getActualEffort());
		deflated.addProperty("requirement", dueDate.getTime());
		// Requirement ...?

		return deflated;
	}
}
