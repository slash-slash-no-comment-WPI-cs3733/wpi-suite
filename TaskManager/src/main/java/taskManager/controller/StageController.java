/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DropAreaSaveListener;
import taskManager.localization.Localizer;
import taskManager.model.ActivityModel;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.RotationView;
import taskManager.view.StageView;
import taskManager.view.TaskView;

/**
 * Controller for stages.
 *
 * @author Stefan Alexander
 * @author Sam Khalandovsky
 * @version November 9, 2014
 */

public class StageController implements DropAreaSaveListener, MouseListener,
		ActionListener, KeyListener {

	private final StageView view;
	private StageModel model;

	private boolean newStage = false;

	/**
	 * Constructor for the StageController. Use this when a new stage is being
	 * created (after clicking add stage button).
	 *
	 * @param view
	 *            the corresponding StageView object
	 */
	public StageController() {
		this.view = new StageView("", this);
		this.newStage = true;
	}

	/**
	 * Constructor for the StageController gets all the tasks from the
	 * StageModel, creates the corresponding TaskView and TaskControllers for
	 * each, and final adds all of the TaskViews to the UI. Use this when
	 * loading a stage from a database.
	 *
	 * @param view
	 *            the corresponding StageView object
	 * @param model
	 *            the corresponding StageModel object
	 * @param newStage
	 *            true if this is a new stage. false if it is being loaded from
	 *            the database
	 */
	public StageController(StageModel model) throws IllegalArgumentException {
		if (model == null) {
			throw new IllegalArgumentException("Model cannot be null");
		}
		this.model = model;
		this.view = new StageView(model.getName(), this);
		this.newStage = false;

		// Get state of archive shown check box.
		final boolean showArchive = ToolbarController.getInstance().getView()
				.isArchiveShown();

		// Add the tasks.
		if (model != null) {
			final List<TaskModel> tasks = this.model.getTasks();
			for (TaskModel task : tasks) {
				// Add only if task is not archived or when task is archived and
				// archive shown is set to true.
				if (!task.isArchived() || showArchive) {
					// create stage view and controller.
					int comments = 0;
					for (ActivityModel a : task.getActivities()) {
						if (a.getType() == ActivityModel.ActivityModelType.COMMENT) {
							comments++;
						}
					}

					TaskView tkv = new TaskView(task.getName(),
							task.getDueDate(), task.getAssigned().size(),
							comments);
					tkv.setController(new TaskController(tkv, task));

					// if we're in fun mode, put the rotation view in the stage
					// view
					if (ToolbarController.getInstance().getView().isFunMode()) {
						this.view.addTaskView(tkv.getRotationPane());
					} else {
						this.view.addTaskView(tkv);
					}
				}
			}
		}
	}

	/*
	 * @see
	 * taskManager.draganddrop.DropAreaSaveListener#saveDrop(javax.swing.JPanel,
	 * int)
	 */
	@Override
	public void saveDrop(JPanel panel, int index) {
		// ignore rotation views
		if (panel instanceof RotationView) {
			panel = ((RotationView) panel).getPanel();
		}

		// Make sure we cast safely
		if (!(panel instanceof TaskView)) {
			System.err.println("Tried to save something that isn't a TaskView");
			return;
		}
		final TaskController tc = ((TaskView) panel).getController();

		// if archived tasks are hidden, change index to account for the hidden
		// tasks
		if (!ToolbarController.getInstance().getView().isArchiveShown()) {
			final List<TaskModel> taskList = model.getTasks();
			for (int i = 0; i < index; i++) {
				if (taskList.get(i).isArchived()) {
					index++;
				}
			}
		}

		final boolean changed = tc.moveToStage(model, index);

		if (changed) {
			WorkflowModel.getInstance().save();
			DDTransferHandler.dragSaved = true;
			if (ToolbarController.getInstance().getView().isFunMode()) {
				WorkflowController.getInstance().reloadData();
			}
		}

	}

	/**
	 * Move associated stage to index in workflow
	 *
	 * @param index
	 *            index to be moved to
	 * @return whether the workflow changed as a result
	 */
	public boolean moveStageToIndex(int index) {
		return WorkflowModel.getInstance().addStage(model, index);

	}

	/**
	 * Removes this stage from the workflow
	 *
	 */
	public void deleteStage() {
		WorkflowModel.getInstance().removeStage(model);
	}

	/**
	 * Checks if stage is empty
	 *
	 * @return whether or not it is empty
	 */
	public boolean isEmpty() {
		return model.getTasks().isEmpty();
	}

	/**
	 *
	 * @return true if this is a new stage. false if it was loaded from the
	 *         database
	 */
	public boolean isNewStage() {
		return newStage;
	}

	/**
	 *
	 * @return the StageView for this controller
	 */
	public StageView getView() {
		return view;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// clicked on the title
		if (e.getSource() instanceof JLabel) {
			// Don't reload while changing a stage name is open.
			WorkflowController.getInstance().removeChangeTitles();
			WorkflowController.getInstance().removeTaskInfos(true);
			WorkflowController.pauseInformation = true;
			// bring up the title textbox
			view.switchTitles(true);
		} else {
			WorkflowController.pauseInformation = false;
			WorkflowController.getInstance().reloadData();
			WorkflowController.getInstance().repaintView();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing

	}

	public void checkButton() {
		if (view.isCheckEnabled()
				&& WorkflowModel.getInstance().findStageByName(
						view.getLabelText()) != null) {
			JOptionPane.showConfirmDialog(view,
					MessageFormat.format(Localizer.getString("DuplicateStage"),
							view.getLabelText()), Localizer
							.getString("DuplicateWarning"),
					JOptionPane.CLOSED_OPTION);
		} else if (view.isCheckEnabled()) {
			if (model == null) {
				model = new StageModel(view.getLabelText());
			} else {
				model.setName(view.getLabelText());
			}

			// refresh the workflow with the new stage
			WorkflowController.getInstance().reloadData();
			WorkflowController.getInstance().repaintView();

			// save to the server
			WorkflowModel.getInstance().save();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Object button = e.getSource();
		if (button instanceof JButton) {
			switch (((JButton) button).getName()) {
			case StageView.CHECK:
				checkButton();
				break;
			case StageView.X:
				// reset the flags
				WorkflowController.pauseInformation = false;
				// reload which will remove the textbox
				WorkflowController.getInstance().reloadData();
				break;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// do nothing

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			checkButton();
		}
	}

	/**
	 * Generate string for table export (Excel format)
	 *
	 * @return export string
	 */
	public String getExportString() {
		String fields[] = { "Name", "Description", "Due Date",
				"Assigned Users", "Estimated Effort", "Actual Effort" };
		List<String[]> taskStringArrays = new ArrayList<String[]>();
		for (TaskModel tm : model.getTasks()) {
			String values[] = {
					tm.getName(),
					tm.getDescription(),
					new SimpleDateFormat("MM/dd/yy").format(tm.getDueDate()),
					String.join(",", tm.getAssigned()),
					tm.isEstimatedEffortSet() ? Integer.toString(tm
							.getEstimatedEffort()) : "None",
					tm.isActualEffortSet() ? Integer.toString(tm
							.getActualEffort()) : "None" };
			taskStringArrays.add(values);
		}
		List<String> rows = new ArrayList<String>();
		rows.add(model.getName());
		for (int i = 0; i < fields.length; i++) {
			List<String> cells = new ArrayList<String>();
			cells.add(fields[i]);
			for (String[] taskStringArray : taskStringArrays) {
				String val = taskStringArray[i];
				// remove tabs and newlines
				val = val.replace("\t", "        ");
				val = val.replace("\n", " ");
				val = val.replace("\r", "");
				cells.add(val);
			}
			rows.add(String.join("\t", cells));
		}
		return String.join("\n", rows);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// do nothing
	}
}
