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

	private Boolean manageUsersTabOut = false;
	private Boolean manageStagesTabOut = false;
	private TabController tabC;

	public TabView() {
		this.tabC = JanewayModule.tabC;
		setTabPlacement(TOP);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setBorder(BorderFactory.createEmptyBorder(5, 3, 3, 3));
		// Create a workflow view, controller, and model
		WorkflowView wfv = new WorkflowView();
		WorkflowModel wfm = new WorkflowModel();
		wfv.setController(new WorkflowController(wfv, wfm));

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
			// You can only have one manageUsers or manageStage view out at once
			if (component instanceof ManageUsersView) {
				if (manageUsersTabOut) { // There is already a manageUsers tab
					return;
				}
				this.manageUsersTabOut = true;
			}
			if (component instanceof ManageStageView) {
				if (manageStagesTabOut) { // There is already a manageStages tab
					return;
				}
				this.manageStagesTabOut = true;
			}

			setTabComponentAt(index, new ClosableTab(this));
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

}
