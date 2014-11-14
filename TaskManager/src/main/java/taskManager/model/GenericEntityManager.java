/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

import java.lang.reflect.Array;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;

/**
 * A generic entity manager to allow the same code to be reused by objects
 * needing boilerplate entity management.
 *
 * @author Sam Khalandovsky
 * @version Nov 10, 2014
 */
public class GenericEntityManager<T extends AbstractJsonableModel<T>>
		implements EntityManager<T> {
	// The database
	private final Data db;

	Class<T> type;

	public GenericEntityManager(Data db, Class<T> type) {
		this.db = db;
		this.type = type;
	}

	/**
	 * Saves a Model when received from a client
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.
	 *      wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public T makeEntity(Session s, String content) throws BadRequestException,
			ConflictException, WPISuiteException {
		final T newModel = AbstractJsonableModel.fromJson(content, type);
		db.save(newModel, s.getProject());

		return newModel;

	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(edu.wpi.cs.wpisuitetng
	 * .Session, java.lang.String)
	 */
	@Override
	public T[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {
		List<Model> response = db.retrieve(type, "id", id, s.getProject());
		T[] tasks = response.toArray((T[]) Array.newInstance(type, 0));

		if (tasks.length < 1 || tasks[0] == null) {
			throw new NotFoundException("Entity not found");
		}
		return tasks;
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(edu.wpi.cs.wpisuitetng
	 * .Session)
	 */
	@Override
	public T[] getAll(Session s) throws WPISuiteException {
		List<Model> tasks;
		try {
			tasks = db.retrieveAll(type.newInstance(), s.getProject());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}

		return tasks.toArray((T[]) Array.newInstance(type, 0));
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#update(edu.wpi.cs.wpisuitetng
	 * .Session, java.lang.String)
	 */
	@Override
	public T update(Session s, String content) throws WPISuiteException {
		// deserialize
		T newModel = AbstractJsonableModel.fromJson(content, type);

		// check if object already exists
		List<Model> existingModels = db.retrieve(type, "id", newModel.getID(),
				s.getProject());
		if (existingModels.size() < 1 || existingModels.get(0) == null) {
			save(s, newModel); // if it doesn't exist, save it
		} else {
			T existingModel = (T) existingModels.get(0);
			existingModel.makeIdenticalTo(newModel);
			save(s, existingModel);
		}
		return newModel;
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#save(edu.wpi.cs.wpisuitetng
	 * .Session, edu.wpi.cs.wpisuitetng.modules.Model)
	 */
	@Override
	public void save(Session s, T model) throws WPISuiteException {
		db.save(model, s.getProject());

	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(edu.wpi.cs.
	 * wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		T task = getEntity(s, id)[0];
		return (db.delete(task) != null);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedGet(edu.wpi.cs.
	 * wpisuitetng.Session, java.lang.String[])
	 */
	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(edu.wpi.cs.wpisuitetng
	 * .Session)
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub

	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#Count()
	 */
	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPut(edu.wpi.cs.
	 * wpisuitetng.Session, java.lang.String[], java.lang.String)
	 */
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPost(edu.wpi.cs.
	 * wpisuitetng.Session, java.lang.String, java.lang.String)
	 */
	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}
