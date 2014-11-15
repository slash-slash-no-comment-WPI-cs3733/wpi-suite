package taskManager.view;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import taskManager.JanewayModule;
import taskManager.view.WorkflowView;
import taskManager.view.ManageStageView;
import taskManager.view.ManageUsersView;
import taskManager.controller.ClosableTab;
import taskManager.controller.TabController;
import taskManager.controller.WorkflowController;
import taskManager.model.WorkflowModel;

/**
 * @author samee
 *
 */
public class TabView extends JTabbedPane {

	// TabView keeps track of which tabs are visible/out
	public Boolean manageUsersTabOut = false;
	public Boolean manageStagesTabOut = false;
	private TabController tabC;
	// Because the workflow is a permanent tab, tabview should keep track of it
	private WorkflowController wfc;

	public TabView() {
		this.tabC = JanewayModule.tabC;
		setTabPlacement(TOP);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setBorder(BorderFactory.createEmptyBorder(5, 3, 3, 3));
		// Create a workflow view, controller, and model
		WorkflowView wfv = new WorkflowView();
		WorkflowModel wfm = new WorkflowModel();
		wfc = new WorkflowController(wfv, wfm);
		wfv.setController(wfc);

		this.addTab("Workflow", new ImageIcon(), wfv, "Workflow");
	}

	/**
	 * Can't find what uses this.
	 */
	@Override
	public void insertTab(String title, Icon icon, Component component,
			String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		// the Workflow tab cannot be closed
		if (!(component instanceof WorkflowView)) {
			ClosableTab ct = new ClosableTab(this, component);
			if (component instanceof EditTaskView) {
				((EditTaskView)component).controller.cTab = ct;
			}
			ct.setTabView(this);
			setTabComponentAt(index, ct);
		}
	}

	@Override
	public void removeTabAt(int index) {
		// if a tab does not have the close button UI, it cannot be removed
		if (getTabComponentAt(index) instanceof ClosableTab) {
			super.removeTabAt(index);
		}
	}

	@Override
	public void setComponentAt(int index, Component component) {
		super.setComponentAt(index, component);
		fireStateChanged(); // hack to make sure toolbar knows if component
							// changes
	}

	// used by controllers to remotely tell TabView when a ManageUsers tab is
	// created or removed
	public void setManageUsersTabOut(Boolean b) {
		this.manageUsersTabOut = b;
	}

	// used by controllers to remotely tell TabView when a ManageStages tab is
	// created or removed
	public void setManageStagesTabOut(Boolean b) {
		this.manageStagesTabOut = b;
	}
	
	public void refreshWorkflow(){
		wfc.fetch();
	}

}
