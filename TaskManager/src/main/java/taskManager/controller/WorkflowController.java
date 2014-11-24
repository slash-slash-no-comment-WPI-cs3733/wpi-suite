/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.util.List;

import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.StageView;
import taskManager.view.WorkflowView;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;

/**
 * A controller for the workflow view
 *
 * @author Jon Sorrells
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class WorkflowController {

	private final WorkflowView view;
	private final WorkflowModel model;

	public static boolean alive = true;
	public static int timeout = 60000; // 1 minute

	/**
	 * Constructor for the WorkflowController, gets all the stages from the
	 * WorkflowView, creates the corresponding StageView and StageControllers,
	 * and adds the StageViews to the UI. Polls the server every 1 second until
	 * it receives the workflow model.
	 * 
	 * @param view
	 *            the corresponding WorkflowView object
	 * @param model
	 *            the corresponding WorkflowModel object
	 */
	public WorkflowController(WorkflowView view) {
		this.view = view;
		this.model = WorkflowModel.getInstance();

		// poll for requirements
		Thread thread = new Thread() {
			public void run() {
				GetRequirementsController reqController = GetRequirementsController
						.getInstance();
				while (alive) {
					try {
						sleep(5000);
						reqController.retrieveRequirements();
					} catch (NullPointerException e) {
						// this is expected, do nothing
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.setName("polling");
		thread.setDaemon(true);
		thread.start();

		// set up connection to let server send workflow and user changes
		Thread thread2 = new Thread() {
			private boolean fetchCompleted = false;

			public void run() {
				while (alive) {
					try {
						sleep(1000);

						// make sure we don't fetch an extra time if fetchUsers
						// fails after fetch completes
						if (!fetchCompleted) {
							fetch();
						}
						fetchCompleted = true;

						fetchUsers();

						// once those both complete successfully, we are done
						// here
						return;

					} catch (NullPointerException e) {
						// the network has not been initialized yet, just keep
						// trying
					} catch (InterruptedException e) {
						// sleep failed for some reason, print it and keep going
						e.printStackTrace();
					}
				}
			}
		};
		thread2.setName("setting up connections");
		thread2.setDaemon(true);
		// thread2.start();

		reloadData();
	}

	/**
	 * Tells the other threads to die
	 *
	 */
	public static void dispose() {
		alive = false;
	}

	/**
	 * Reloads all the data on the view to match the data in the model
	 *
	 */
	public void reloadData() {
		// clear the stages previously on the view
		view.removeAll();

		// get all the stages in this workflow
		final List<StageModel> stages = model.getStages();
		// and add them all to the view
		for (StageModel stage : stages) {
			// create stage view and controller.
			StageView stv = new StageView(stage.getName());
			stv.setController(new StageController(stv, stage));

			// add stage view to workflow
			view.addStageView(stv);
		}
		view.revalidate();
		view.repaint();
	}

	/**
	 * Asks the model to pull from the server. When the server responds, model
	 * data is updated and reloadData is called.
	 *
	 */
	public void fetch() {
		model.update();
	}

	public void fetchUsers() {
		model.updateUsers();
	}

	/**
	 * 
	 * Returns the workflow model.
	 *
	 * @return the WorkflowModel
	 */
	public WorkflowModel getModel() {
		return model;
	}
}
