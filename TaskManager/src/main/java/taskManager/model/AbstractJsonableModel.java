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
 * Description
 *
 * @author Sam Khalandovsky
 * @version Nov 10, 2014
 */
public abstract class AbstractJsonableModel<T> extends AbstractModel {

	private String id;

	private static GenericRequestObserver observer = new GenericRequestObserver();

	/**
	 * Constructor with unique id
	 *
	 * @param id
	 */
	public AbstractJsonableModel(String id) {
		this.id = id;
	}

	/**
	 * Null constructor
	 *
	 */
	public AbstractJsonableModel() {
		this(null);
	}

	/**
	 * Gets the item id
	 *
	 * @return the id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Returns a Request Observer for the object
	 *
	 * @return the GenericRequestObserver
	 */
	public GenericRequestObserver getObserver() {
		return observer;
	}

	/**
	 * Set the object's id
	 *
	 * @param id
	 *            the new id
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
	 * Deserialize an object of type from json
	 *
	 * @param json
	 *            The serialized json
	 * @param type
	 *            The object class to convert to
	 * @return the deserialized object
	 */
	public static <T> T fromJson(String json, Class<T> type) {
		final Gson parser = new Gson();
		return parser.fromJson(json, type);
	}

	public String toJson() {
		return new Gson().toJson(this, this.getClass());
	}
}
