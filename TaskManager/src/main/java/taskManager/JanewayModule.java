package taskManager;

/**
 * @author Beth Martino
 */

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.WorkflowView;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;


/**
 * The JanewayModule for our task manager
 *
 */
public class JanewayModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	/**
	 * Construct a blank tab
	 */
	public JanewayModule() {
		
		//creates the workflow view
		WorkflowView wfv = new WorkflowView();

		// create a new workflow model
		WorkflowModel wfm = new WorkflowModel();
		// give it the default stages
		new StageModel(wfm, "Backlog", false);
		new StageModel(wfm, "In Progress", false);
		new StageModel(wfm, "Review", false);
		new StageModel(wfm, "To Merge", false);
		new StageModel(wfm, "Merged", false);
		
		// add sample tasks to the workflow model.
		TaskModel task1 = new TaskModel("Some task", "Backlog", wfm);
		task1.setDueDate(new Date(114, 5, 5)); // This date constructor is deprecated.
		task1.setEstimatedEffort(3);
		TaskModel task2 = new TaskModel("Working on this task", "In Progress", wfm);
		task2.setDueDate(new Date());
		task2.setEstimatedEffort(1);
		TaskModel task3 = new TaskModel("Plz review", "Review", wfm);
		task3.setDueDate(new Date());
		task3.setEstimatedEffort(5);
		TaskModel task4 = new TaskModel("blah", "Review", wfm);
		task4.setDueDate(new Date());
		task4.setEstimatedEffort(2);
		TaskModel task5 = new TaskModel("This is merged", "Merged", wfm);
		task5.setDueDate(new Date());
		task5.setEstimatedEffort(2);
		
		// create the controller for the view
		wfv.setController(new WorkflowController(wfv, wfm));
		
		//creates the menu view
		JPanel menu = new JPanel(); //in the future, Samee's menu needs to go here
		menu.setLayout(new FlowLayout());

		//Setup teammember's buttons for now, it will be Samee's menu
		JButton bethButton = new JButton("Beth");		
		JButton jonButton = new JButton("Jon");
		JButton sameeButton = new JButton("Samee!");
		JButton stefanButton = new JButton("Stefan");
		JButton buttonJoseph = new JButton("Joseph");
		JButton samButton = new JButton("Sam's Amazing Button");
		JButton ezraButton = new JButton("Ezra");
		JButton thaneButton = new JButton("Thane");
		JButton clarkButton = new JButton("Clark");
		JButton jackButton = new JButton("Jack");
		JButton peterButton = new JButton("Peter");
		JButton tylerButton = new JButton("Tyler");
		menu.add(sameeButton);
		menu.add(jonButton);
		menu.add(bethButton);
		menu.add(stefanButton);
		menu.add(buttonJoseph);
		menu.add(samButton);
		menu.add(ezraButton);
		menu.add(thaneButton);
		menu.add(clarkButton);
		menu.add(jackButton);
		menu.add(peterButton);
		menu.add(tylerButton);
		
		//this adds the menu and the main panel to the pre-configured janeway module view. 
		//It uses the spring layout
		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Task Manager", new ImageIcon(), menu,wfv);
		tabs.add(tab);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Task Manager";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}
}
