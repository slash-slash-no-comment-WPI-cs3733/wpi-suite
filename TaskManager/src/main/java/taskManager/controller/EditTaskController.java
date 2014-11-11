/**
 * 
 */
package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.WorkflowView;

/**
 * @author Beth Martino
 *
 */
public class EditTaskController implements ActionListener {

	private final EditTaskView etv;
	private final WorkflowModel wfm;
	private final WorkflowView wfv;

	private String taskID;

	/**
	 * Constructor, attaches the edit task view to this controller
	 * 
	 * @param etv
	 */
	public EditTaskController(WorkflowModel wfm) {
		this.etv = JanewayModule.etv;
		this.wfm = wfm;
		this.wfv = JanewayModule.wfv;

		reloadData();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();

			StageModel stage = wfm.findStageByName((String) etv.getStages()
					.getSelectedItem());
			taskID = etv.getTitle().getName();

			switch (name) {

			case "save":
				// find the appropriate stage
				// create new task

				if (stage.containsTask(stage.findTaskByID(taskID))) {
					TaskModel t = stage.findTaskByID(taskID);

					// updates text fields
					t.setName(etv.getTitle().getText());
					t.setDescription(etv.getDescription().getText());
					t.setEstimatedEffort(Integer.parseInt(etv.getEstEffort()
							.getText()));
					t.setActualEffort(Integer.parseInt(etv.getActEffort()
							.getText()));

					// formats the date
					SimpleDateFormat d = new SimpleDateFormat("MM/dd/yyyy");
					try {
						t.setDueDate(d.parse(etv.getDate().getText()));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {

					TaskModel task = new TaskModel(etv.getTitle().getText(),
							stage);

					// sets all task values according to fields
					SimpleDateFormat d = new SimpleDateFormat("MM/dd/yyyy");
					try {
						task.setDueDate(d.parse(etv.getDate().getText()));
					} catch (ParseException p) {
						p.printStackTrace();
					}
					task.setEstimatedEffort(Integer.parseInt(etv.getEstEffort()
							.getText()));
					task.setActualEffort(Integer.parseInt(etv.getActEffort()
							.getText()));
					task.setDescription(etv.getDescription().getText());
				}

				// makes all the fields blank again
				etv.resetFields();

				// exit the edit view
				this.returnToWorkflowView();
				break;

			case "delete":
				// delete this task
				TaskModel task = stage.findTaskByID(taskID);
				stage.getTasks().remove(task);

				this.returnToWorkflowView();
				break;

			case "addUser":
				// add a user to this task
				System.out.println("You've pressed the add user button");
				break;

			case "addReq":
				// add a requirement to this task
				System.out.println("You've pressed the add requirement button");
				break;

			case "cancel":
				// go back to workflow view
				this.returnToWorkflowView();
				break;

			case "submitComment":
				// creates a new activity
				System.out.println("You've pressed the submit comment button");
				break;
			}
		}
	}

	public void reloadData() {
		JComboBox<String> stages = etv.getStages();
		stages.removeAllItems();
		for (StageModel stage : wfm.getStages()) {
			stages.addItem(stage.getName());
		}
	}

	/**
	 * switches back to workflow view
	 */
	private void returnToWorkflowView() {
		this.etv.setVisible(false);
		this.wfv.setVisible(true);
	}

	/**
	 * Enter the task id that will be edited
	 * 
	 * @param id
	 *            the id that new task info will be saved to
	 */
	public void setTaskID(String id) {
		this.taskID = id;
	}
}
