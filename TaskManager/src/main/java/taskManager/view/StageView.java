package taskManager.view;

/*
 * @author Beth Martino
 */

import javax.swing.*;

import taskManager.controller.StageController;

public class StageView extends JPanel implements IStageView {

	/**
	 * not sure what this is, ask Jon or Sam?
	 */
	private static final long serialVersionUID = 1L;
	
	private StageController controller;

	public StageView() {
		//organizes the tasks in a vertical list
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	}
	
	/*
	 * @param data for new task view
	 * will be entered by the user
	 */
	public void addTaskView(String name){
		this.add(new TaskView(name));
	}
	
	@Override
	public String getName(){
		return this.getName();
	}

	public void setController(StageController controller) {
		this.controller = controller;
	}

}
