package taskManager.view;

/*
 * @author Beth Martino
 */

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;

public class TaskView extends JPanel implements ITaskView {

	/**
	 * not sure what this is, ask Jon or Sam?
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param the name of the new task
	 * creates a list-like view for the following information
	 * the name of the task, the due date and the estimated effort
	 */
	public TaskView(String name){
		//organizes the data in a vertical list
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(
                raisedbevel, name);
		title.setTitlePosition(TitledBorder.LEFT);
		this.setBorder(title);
		this.setMinimumSize(new Dimension(300,100));
		
		
		//adds the data to the view
		this.add(new JLabel("Due Date: 00/00/00")); 
		this.add(new JLabel("Est Effort: 3#"));
	}
	
	
	@Override
	public String getName(){
		return this.getName();
	}

	

}