package taskManager.view;

/*
 * @author Beth Martino
 */

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import taskManager.controller.TaskController;

import java.awt.*;
import java.util.Date;

public class TaskView extends JPanel implements ITaskView {

	/**
	 * not sure what this is, ask Jon or Sam?
	 */
	private static final long serialVersionUID = 1L;
	
	private TaskController controller;
	
	/**
	 * @param the name of the new task
	 * creates a list-like view for the following information
	 * the name of the task, the due date and the estimated effort
	 */
	public TaskView(String name, Date duedate, int estEffort){
		//organizes the data in a vertical list
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(
                raisedbevel, name);
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);
		this.setMinimumSize(new Dimension(300,100));
		
		//adds the data to the view
		this.add(new JLabel("Due Date: "+duedate)); 
		this.add(new JLabel("Est Effort: "+estEffort+"#"));
	}
	
	
	@Override
	public String getName(){
		return this.getName();
	}

	public void setController(TaskController controller) {
		this.controller = controller;
	}

}
