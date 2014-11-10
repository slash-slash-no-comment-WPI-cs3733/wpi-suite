package taskManager;

/**
 * @author Samee Swartz
 */

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import taskManager.controller.ToolbarController;
import taskManager.controller.WorkflowController;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
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
		// TODO change JPanels to ManageStagesView, ManageUsersView,
		// NewTaskView, StatisticsView
		// TODO move set visible into constructors?
		WorkflowView wfv = new WorkflowView();
		wfv.setVisible(true);
		JPanel msv = new JPanel();
		msv.setVisible(false);
		JPanel muv = new JPanel();
		muv.setVisible(false);
		JPanel ntv = new JPanel();
		ntv.setVisible(false);
		JPanel sv = new JPanel();
		sv.setVisible(false);

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

		JPanel allPanels = new JPanel();
		allPanels.add(wfv);
		allPanels.add(msv);
		allPanels.add(muv);
		allPanels.add(ntv);
		allPanels.add(sv);

		// Create the toolbar view
		ToolbarView tv = new ToolbarView();
		ToolbarController tc = new ToolbarController(tv, wfv, msv, muv, ntv, sv);
		tv.setController(tc);
		// toolbarPanel.setController(ToolbarController);

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Task Manager",
				new ImageIcon(), tv, allPanels);
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
