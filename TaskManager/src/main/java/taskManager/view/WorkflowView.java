package taskManager.view;

/*
 * @author Beth Martino
 */
import java.awt.*;
import javax.swing.*;

import javax.swing.*;


public class WorkflowView extends JPanel implements IWorkflowView{

	/**
	 * not sure what this is, ask Jon or Sam?
	 */
	private static final long serialVersionUID = 1L;

	public WorkflowView() {
		
		//arranges the stages horizontally and evenly spaced
		this.setLayout(new FlowLayout());
		
		this.addStageView("Backlog");
		this.addStageView("In Progress");
		this.addStageView("Review");
		this.addStageView("To Merge");
		
	}
	
	/**
	 * @param name of the new stage to be added
	 * creates a new scroll panel to house the stage view object 
	 * sets the size and border
	 */
	public void addStageView(String name){
		this.add(new StageView(name));
	}
	
	@Override
	public String getName(){
		return this.getName();
	}

	

}
