package taskManager.view;

/*
 * @author Beth Martino
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import taskManager.controller.WorkflowController;

public class WorkflowView extends JPanel implements IWorkflowView{

	/**
	 * not sure what this is, ask Jon or Sam?
	 */
	private static final long serialVersionUID = 1L;

	private WorkflowController controller;

	public WorkflowView() {
		
		//arranges the stages horizontally and evenly spaced
		this.setLayout(new FlowLayout());
		
	}
	
	/**
	 * @param name of the new stage to be added
	 * creates a new scroll panel to house the stage view object 
	 * sets the size and border
	 */
	public void addStageView(StageView stv, String name){
		
		//creates the container for both the label and the scroll
		JPanel block = new JPanel();
		block.setLayout(new BoxLayout(block,BoxLayout.Y_AXIS));
		
		//creates the label for the name of the stage and adds it to the block
		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(175,25));
		label.add(new JLabel(name));
		block.add(label);
		
		//creates the scroll containing the stage view and adds it to the block
		JScrollPane stage = new JScrollPane(stv);
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

	public void setController(WorkflowController controller) {
		this.controller = controller;
	}

}
