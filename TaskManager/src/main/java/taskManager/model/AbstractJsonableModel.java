/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * Abstract superclass for the taskmanager models (workflow, stage, and task).
 * Handles IDs and serializing with JSON.
 *
 * @author Sam Khalandovsky
 * @version Nov 10, 2014
 */
public abstract class AbstractJsonableModel<T> extends AbstractModel {

	// Subclasses should make sure this stays unique
	private String id;

	private static GenericRequestObserver observer = new GenericRequestObserver();

	/**
	 * Main constructor: should be called by all constructors with a valid ID
	 *
	 * @param id
	 */
	protected AbstractJsonableModel(String id) {
		this.id = id;
	}

	protected AbstractJsonableModel() {
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
	public GenericRequestObserver getObserver() {
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
	 * Sets fields to match passed model Should call setID(model.getID());
	 *
	 * @param model
	 */
	protected abstract void makeIdenticalTo(T model);

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
		return parser.fromJson(json, type);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJson()
	 */
	@Override
	public String toJson() {
		return new Gson().toJson(this, this.getClass());
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		Boolean identical = false;
		if (o.getClass().equals(this.getClass())) {
			identical = id == ((AbstractJsonableModel) o).getID();
		}
		if (o instanceof String) {
			identical = id == (String) o;
		}
		return identical;
	}
}
