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
public abstract class AbstractJsonableModel<T> extends AbstractModel {

	// Subclasses should make sure this stays unique
	private String id;

	private static final GenericRequestObserver observer = new GenericRequestObserver();

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
		try {
			return parser.fromJson(json, type);
		} catch (Exception e) {
			// e.printStackTrace();
			json = json.substring(2);
			char[] temp = json.toCharArray();
			byte[] bytes = new byte[temp.length];
			for (int i = 0; i < temp.length; i++) {
				bytes[i] = (byte) temp[i];
			}
			try {
				return parser.fromJson(new String(bytes, "UTF8"), type);
			} catch (JsonSyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			identical = id.equals(((AbstractJsonableModel) o).getID());
		}
		if (o instanceof String) {
			identical = id.equals((String) o);
		}
		return identical;
	}
}
