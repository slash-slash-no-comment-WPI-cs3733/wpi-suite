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
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * A custom deserializer class for the GSON JSON library.
 * 
 * @author joe
 * @version $Revision: 1.0 $
 */
public class TaskModelDeserializer implements JsonDeserializer<TaskModel> {

	private static final Logger logger = Logger
			.getLogger(TaskModelDeserializer.class.getName());

	@Override
	public TaskModel deserialize(JsonElement taskElement, Type taskType,
			JsonDeserializationContext context) throws JsonParseException {
		final JsonObject deflated = taskElement.getAsJsonObject();

		if (!deflated.has("name")) {
			throw new JsonParseException(
					"The serialized Task did not contain the required name field.");
		}

		if (!deflated.has("id")) {
			// TODO: Missing IDs can be regenerated.
			throw new JsonParseException(
					"The serialized Task did not contain the required id field.");
		}

		// For all other attributes: instantiate as null, fill in if given.
		String name = null;
		String id = null;
		String description = null;
		WorkflowModel workflow = null; // TODO: How do we deserialize/get
										// this?
		// From the controller?
		String stage = null;
		JsonArray userList = null;
		List<User> users = null;
		Date dueDate = null;
		int estimatedEffort = 0;
		int actualEffort = 0;
		JsonArray activityList = null;
		List<ActivityModel> activities = null;
		Requirement req = null;

		name = deflated.get("name").getAsString();
		id = deflated.get("id").getAsString();
		description = deflated.get("description").getAsString();
		// Get workflow
		stage = deflated.get("stage").getAsString();
		userList = deflated.get("users").getAsJsonArray();
		dueDate = new Date(deflated.get("dueDate").getAsLong());
		estimatedEffort = deflated.get("estimatedEffort").getAsInt();
		actualEffort = deflated.get("actualEffort").getAsInt();
		activityList = deflated.get("activities").getAsJsonArray();
		for (JsonElement activity : activityList) {
			activities.add(ActivityModel.fromJson(activity));
		}
		// TODO: Add Requirement
		final TaskModel inflated = new TaskModel(name, null);
		inflated.setEstimatedEffort(estimatedEffort);
		inflated.setActualEffort(actualEffort);
		// TODO: finish adding in bits of task model here.
		return inflated;
	}
}
