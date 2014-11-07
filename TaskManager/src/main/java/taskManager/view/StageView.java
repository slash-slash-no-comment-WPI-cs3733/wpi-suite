package taskManager.view;

/*
 * @author Beth Martino
 */

import javax.swing.*;

public class StageView extends JPanel implements IStageView {

	/**
	 * not sure what this is, ask Jon or Sam?
	 */
	private static final long serialVersionUID = 1L;
	

	public StageView() {
		
		//organizes the tasks in a vertical list
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		//adds example tasks
		this.addTaskView("Task1");
		this.addTaskView("Task2");
		this.addTaskView("Task3");
		this.addTaskView("Task4");
		this.addTaskView("Task5");
		this.addTaskView("Task5");
		this.addTaskView("Task5");
		this.addTaskView("Task5");
		this.addTaskView("Task5");
		this.addTaskView("Task5");
		this.addTaskView("Task5");
		this.addTaskView("Task5");

	}
	
	/*
	 * @param data for new task view
	 * will be entered by the user
	 */
	private void addTaskView(String name){
		this.add(new TaskView(name));
	}
	
	@Override
	public String getName(){
		return this.getName();
	}



}
