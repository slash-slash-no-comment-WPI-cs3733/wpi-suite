/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * Abstract superclass for the taskmanager models (workflow, stage, and task).
 * Handles IDs and serializing with JSON.
 *
 * @author Sam Khalandovsky
 * @version Nov 10, 2014
 */
public abstract class AbstractTaskManagerModel<T> extends AbstractModel {

	// Subclasses should make sure this stays unique
	private String id;

	private static final GenericRequestObserver observer = new GenericRequestObserver();

	/**
	 * Main constructor: should be called by all constructors with a valid ID
	 *
	 * @param id
	 */
	protected AbstractTaskManagerModel(String id) {
		this.id = id;
	}

	protected AbstractTaskManagerModel() {
		this(null);
	}

	/**
	 * Getter for ID
	 *
	 * @return the ID
	 */
	public String getID() {
		return id;
	}

	/**
	 * Getter for Generic Observer
	 *
	 * @return observer object
	 */
	public static GenericRequestObserver getObserver() {
		return observer;
	}

	/**
	 * Setter for ID Should not normally be called, ID should be set in
	 * constructor
	 *
	 * @param id
	 *            the id to set
	 */
	protected void setID(String id) {
		this.id = id;
	}

	/**
	 * Sets fields to match passed model. Should call setID(model.getID());
	 *
	 * @param model
	 * @return set of objects that can be safely deleted
	 */
	protected abstract Set<Object> makeIdenticalTo(T model);

	/**
	 * Static method for handling all json deserialization
	 *
	 * @param json
	 *            the serialized data
	 * @param type
	 *            the type of the serialized data (i.e. TaskModel.class,
	 *            WorkflowModel[].class)
	 * @return the deserialized object
	 */
	public static <T> T fromJson(String json, Class<T> type) {
		final Gson parser = new Gson();
		try {
			return parser.fromJson(json, type);
		} catch (JsonSyntaxException e) {
			// if json is in unicode
			json = json.substring(2);
			try {
				return parser.fromJson(json, type);
			} catch (JsonSyntaxException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJson()
	 */
	@Override
	public String toJson() {
		try {
			return new String(new Gson().toJson(this, this.getClass())
					.getBytes("UTF8"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		Boolean identical = false;
		if (o.getClass().equals(this.getClass())) {
			identical = id.equals(((AbstractTaskManagerModel) o).getID());
		}
		if (o instanceof String) {
			identical = id.equals((String) o);
		}
		return identical;
	}
}
