/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;

import taskManager.JanewayModule;
import taskManager.model.ActivityModel;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.EditTaskView.Mode;
import taskManager.view.TaskView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * Controller for Tasks.
 *
 * @author Stefan Alexander
 * @version November 9, 2014
 */
public class TaskController implements MouseListener {

	private final TaskView view;
	private final TaskModel model;
	private StageModel sm;
	private final WorkflowModel wfm;
	private TabPaneController tabPaneC;
	private EditTaskView etv;
	private Requirement req;
	private final User[] projectUsers = JanewayModule.users;
	private Set<String> assignedUsers;

	/**
	 * Constructor for the TaskController, currently just sets the corresponding
	 * view and model parameters.
	 *
	 * @param view
	 *            the corresponding TaskView object
	 * @param model
	 *            the corresponding TaskModel object
	 */
	public TaskController(TaskView view, TaskModel model) {
		this.tabPaneC = JanewayModule.tabPaneC;
		this.view = view;
		this.model = model;
		sm = model.getStage();

		wfm = WorkflowModel.getInstance();
		etv = new EditTaskView(Mode.EDIT);
		etv.setController(new EditTaskController(etv));
		etv.setFieldController(new TaskInputController(etv));

		assignedUsers = model.getAssigned();

		req = model.getReq();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// etv.removeAll();

		// TODO: Populate with data?

		// uses the title field to hold the unique id
		etv.getTitle().setName(this.model.getID());

		// uses description field to hold the name of the stage
		etv.getDescription().setName(this.model.getStage().getName());
		// makes the delete button unclickable
		etv.enableDelete();

		// populate editable fields with this tasks info
		etv.setTitle(model.getName());
		etv.setDescription(model.getDescription());
		etv.setDate(model.getDueDate());
		etv.setEstEffort(model.getEstimatedEffort());
		etv.setActEffort(model.getActualEffort());

		// figures out the index of the stage, then sets the drop down to the
		// stage at that index
		JComboBox<String> stages = etv.getStages();
		for (int i = 0; i < stages.getItemCount(); i++) {
			if (etv.getStages().getItemAt(i) == sm.getName()) {
				etv.setStageDropdown(i);
				break;
			}
		}

		// Set actual effort field enabled only if the selected stage is
		// "Complete"
		if (etv.getSelectedStage().equals("Complete")) {
			etv.getActEffort().setEnabled(true);
		} else {
			etv.getActEffort().setEnabled(false);
		}

		// Enable stage dropdown when editing a task.
		etv.getStages().setSelectedItem(model.getStage());
		etv.setStageSelectorEnabled(true);

		// populates the project users list
		ArrayList<String> projectUserNames = new ArrayList<String>();
		for (User u : projectUsers) {
			String name = u.getUsername();
			if (!etv.getProjectUsersList().contains(name)) {
				projectUserNames.add(name);
			}
		}
		etv.getProjectUsersList().addAllToList(projectUserNames);

		// populates the assigned users panel
		ArrayList<String> assignedUserNames = new ArrayList<String>();
		for (String u : assignedUsers) {
			assignedUserNames.add(u);
		}
		etv.getUsersList().addAllToList(assignedUserNames);

		// Enable save button when editing a task.
		etv.enableSave();

		// Clear the activities list.
		etv.clearActivities();

		// set activities pane
		List<ActivityModel> tskActivities = model.getActivities();
		etv.setActivities(tskActivities);
		etv.setActivitiesPanel(tskActivities);

		etv.setRefreshEnabled(true);

		// set the requirement dropdown
		if (req != null) {
			etv.getRequirements().setSelectedItem(req.getName());
		} else {
			etv.getRequirements().setSelectedItem(EditTaskView.NO_REQ);
		}
		tabPaneC.addEditTaskTab(etv);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
