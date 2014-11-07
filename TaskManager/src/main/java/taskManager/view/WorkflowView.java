package taskManager.view;

/*
 * @author Beth Martino
 */
import java.awt.*;

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
	private void addStageView(String name){
		
		//creates the container for both the label and the scroll
		JPanel block = new JPanel();
		block.setLayout(new BoxLayout(block,BoxLayout.Y_AXIS));
		
		//creates the label for the name of the stage and adds it to the block
		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(175,25));
		label.add(new JLabel(name));
		block.add(label);
		
		//creates the scroll containing the stage view and adds it to the block
		JScrollPane stage = new JScrollPane(new StageView());
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setPreferredSize(new Dimension(175,350));
		block.add(stage);
		
		//adds the combined label and scroll to the workflow view
		this.add(block);
	}
	
	@Override
	public String getName(){
		return this.getName();
	}

	

}
