/**
 * 
 */
package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;

import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.StageView;
import taskManager.view.TaskView;
import taskManager.view.WorkflowView;

/**
 * @author Beth Martino
 *
 */
public class EditTaskController implements ActionListener {

	// TODO: change from JPanel to edit task view
	private final EditTaskView etv;
	private final WorkflowModel wfm;
	private final WorkflowView wfv;

	/**
	 * Constructor, attaches the edit task view to this controller
	 * 
	 * @param etv
	 */
	public EditTaskController(EditTaskView etv, WorkflowModel wfm,
			WorkflowView wfv) {
		this.etv = etv;
		this.wfm = wfm;
		this.wfv = wfv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton) button).getName();
			switch (name) {
			case "save":
				// find the appropriate stage
				// create new task
				TaskModel task = new TaskModel(this.etv.getTitle(),
						wfm.findStageByName(etv.getStageName()));
				if (wfm.findStageByName(etv.getStageName()).containsTask(task)) {
					System.out
							.println("The task is in stage "
									+ wfm.findStageByName(etv.getStageName())
											.getName());
				}

				// create a new task view
				TaskView taskView = new TaskView(task.getName(), new Date(),
						task.getEstimatedEffort());

				// add the task view to the stage view
				StageView s = this.wfv.getStageViewByName(etv.getStageName());
				s.addTaskView(taskView);
				s.updateTasks();
				s.repaint();
				wfv.repaint();
				System.out.println("The string from the dropdown is "
						+ etv.getStageName());
				etv.resetFields();
				// exit the edit view
				this.returnToWorkflowView();
				break;
			case "delete":
				// delete this task
				System.out.println("You've pressed the delete task button");
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

	/**
	 * switches back to workflow view
	 */
	private void returnToWorkflowView() {
		this.etv.setVisible(false);
		this.wfv.setVisible(true);
	}
}
