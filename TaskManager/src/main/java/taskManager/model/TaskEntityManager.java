/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.model;

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
 * Entity Manager for saving and loading TaskModel objects to and from the
 * database
 *
 * @author Sam Khalandovsky
 * @version Nov 8, 2014
 */
public class TaskEntityManager implements EntityManager<TaskModel> {

	// The database
	private final Data db;

	public TaskEntityManager(Data db) {
		this.db = db;
	}

	/**
	 * Saves a TaskModel when received from a client
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.
	 *      wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public TaskModel makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {

		// Parse JSON
		final TaskModel newModel = TaskModel.fromJson(content);

		// Save model in database, with current project
		db.save(newModel, s.getProject());
		return null;
	}

	/**
	 * Retrieves all TaskModels with given id
	 * 
	 * @param s
	 *            Session to specify Project to search in
	 * @param id
	 *            TaskModel ID
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(edu.wpi.cs.wpisuitetng
	 *      .Session, java.lang.String)
	 */
	@Override
	public TaskModel[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {

		List<Model> response = db.retrieve(TaskModel.class, "id", id,
				s.getProject());
		TaskModel[] tasks = response.toArray(new TaskModel[0]);

		if (tasks.length < 1 || tasks[0] == null) {
			throw new NotFoundException();
		}
		return tasks;
	}

	/**
	 * Retrives all TaskModel objects in current project
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(edu.wpi.cs.wpisuitetng
	 *      .Session)
	 * @param s
	 *            current session
	 * @return array of TaskModels
	 * @throws WPISuiteException
	 **/
	@Override
	public TaskModel[] getAll(Session s) throws WPISuiteException {
		List<Model> tasks = db.retrieveAll(new TaskModel(), s.getProject());

		return tasks.toArray(new TaskModel[0]);
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#update(edu.wpi.cs.wpisuitetng
	 * .Session, java.lang.String)
	 */
	@Override
	public TaskModel update(Session s, String content) throws WPISuiteException {
		// deserialize
		TaskModel task = TaskModel.fromJson(content);

		// check if object already exists
		List<Model> existingTasks = db.retrieve(TaskModel.class, "id",
				task.getID(), s.getProject());
		if (existingTasks.size() < 1 || existingTasks.get(0) == null) {
			save(s, task); // if it doesn't exist, save it
		} else {
			TaskModel oldTask = (TaskModel) existingTasks.get(0);
			db.delete(oldTask); // TODO should we update?
			save(s, task);
		}
		return task;
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#save(edu.wpi.cs.wpisuitetng
	 * .Session, edu.wpi.cs.wpisuitetng.modules.Model)
	 */
	@Override
	public void save(Session s, TaskModel model) throws WPISuiteException {
		db.save(model, s.getProject());

	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(edu.wpi.cs.
	 * wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {

		TaskModel task = getEntity(s, id)[0];
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