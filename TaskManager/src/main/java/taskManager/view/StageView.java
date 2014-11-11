package taskManager.view;

/*
 * @author Beth Martino
 */

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class StageView extends JPanel implements IStageView {

	private static final long serialVersionUID = 1L;

	JPanel tasks = new JPanel();
	JScrollPane stage = new JScrollPane(tasks);

	public StageView(String name) {

		// stage view is a panel that contains the title and the scroll pane
		// w/tasks
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(200, 450));

		// organizes the tasks in a vertical list
		tasks.setLayout(new BoxLayout(tasks, BoxLayout.Y_AXIS));

		// creates the label for the name of the stage and adds it to the block
		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(175, 25));
		JLabel labelText = new JLabel(name);
		labelText.setName(name);
		label.add(labelText);
		this.add(label);

		// adds example tasks
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");

		// creates the scroll containing the stage view and adds it to the block
		stage = new JScrollPane(tasks);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175, 350));

		updateTasks();
	}

	private void updateTasks() {
		this.remove(stage);
		stage = new JScrollPane(tasks);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175, 350));
		this.add(stage);
	}

	/*
	 * @param data for new task view will be entered by the user
	 */
	public void addTaskView(String name) {
		tasks.add(new TaskView(name));
	}

	@Override
	public String getName() {
		return super.getName();
	}

}
