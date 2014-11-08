package taskManager.view;

/*
 * @author Beth Martino
 */

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class StageView extends JPanel implements IStageView {

	/**
	 * not sure what this is, ask Jon or Sam?
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel tasks = new JPanel();
	JScrollPane stage = new JScrollPane(tasks);
	

	public StageView(String name) {
		
		//stage view is a panel that contains the title and the scroll pane w/tasks
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		//organizes the tasks in a vertical list
		tasks.setLayout(new BoxLayout(tasks,BoxLayout.Y_AXIS));
		
		
		//creates the label for the name of the stage and adds it to the block
		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(175,25));
		label.add(new JLabel(name));
		this.add(label);
		
		//creates the scroll containing the stage view and adds it to the block
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175,350));
		this.add(stage);
		
		//adds example tasks
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		this.addTaskView("Task1");
		updateTasks();
	}
	
	private void updateTasks(){
		this.remove(stage);
		stage  = new JScrollPane(tasks);
		this.add(stage);
	}
	
	
	/*
	 * @param data for new task view
	 * will be entered by the user
	 */
	public void addTaskView(String name){
		tasks.add(new TaskView(name));
	}
	
	@Override
	public String getName(){
		return this.getName();
	}



}
