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

	static private GenericRequestObserver observer = new GenericRequestObserver();

	public AbstractJsonableModel(String id) {
		this.id = id;
	}

	public AbstractJsonableModel() {
		this(null);
	}

	public String getID() {
		return id;
	}

	public GenericRequestObserver getObserver() {
		return observer;
	}

	protected void setID(String id) {
		this.id = id;
	}

	/**
	 * Sets fields to match passed model Should call setID(model.getID());
	 *
	 * @param model
	 */
	protected abstract void makeIdenticalTo(T model);

	public static <T> T fromJson(String json, Class<T> type) {
		final Gson parser = new Gson();
		return parser.fromJson(json, type);
	}

	public String toJson() {
		return new Gson().toJson(this, this.getClass());
	}
}
