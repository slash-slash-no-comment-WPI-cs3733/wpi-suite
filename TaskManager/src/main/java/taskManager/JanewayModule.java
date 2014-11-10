package taskManager;

/**
 * @author Samee Swartz
 */

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import taskManager.controller.ManageStageController;
import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ManageStageView;
import taskManager.view.ToolbarView;
import taskManager.view.WorkflowView;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

/**
 * The JanewayModule for the task manager
 *
 */
public class JanewayModule implements IJanewayModule {

	// The tabs used by this module
	private ArrayList<JanewayTabModel> tabs;

	/**
	 * Construct a blank tab
	 */
	public JanewayModule() {

		// creates the workflow view
		WorkflowView wfv = new WorkflowView();
		// create a new workflow model
		WorkflowModel wfm = new WorkflowModel();
		// give it the default stages
		new StageModel(wfm, "Backlog", false);
		new StageModel(wfm, "In Progress", false);
		new StageModel(wfm, "Review", false);
		new StageModel(wfm, "To Merge", false);
		new StageModel(wfm, "Merged", false);
		// create the controller for the view
		wfv.setController(new WorkflowController(wfv, wfm));

		// Create the toolbar view
		ToolbarView tv = new ToolbarView();
		// toolbarPanel.setController(ToolbarController);

		tabs = new ArrayList<JanewayTabModel>();
		// JanewayTabModel tab = new JanewayTabModel("Task Manager",
		// new ImageIcon(), tv, wfv);

		// TODO: remove this when done testing
		ManageStageView msv = new ManageStageView();
		msv.setController(new ManageStageController(msv, wfm));
		JanewayTabModel tab = new JanewayTabModel("Task Manager",
				new ImageIcon(), tv, msv);

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
