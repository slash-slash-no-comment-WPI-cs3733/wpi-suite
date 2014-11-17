package taskManager.controller;

import java.awt.Component;

import taskManager.view.EditTaskView;
import taskManager.view.EditTaskView.Mode;
import taskManager.view.ManageStageView;
import taskManager.view.ManageUsersView;
import taskManager.view.TabPaneView;
import taskManager.view.TabView;
import taskManager.view.WorkflowView;

public class TabPaneController {

	private TabPaneView tabPaneV;
	boolean manageStagesTabOpen = false;
	boolean manageUsersTabOpen = false;

	public TabPaneController(TabPaneView tabPaneV) {
		this.tabPaneV = tabPaneV;
	}

	public void addCreateTaskTab() {
		addTab("Create Task",  new EditTaskView(Mode.CREATE), true);
		int index = tabPaneV.getTabCount()-1;
		tabPaneV.setSelectedIndex(index);
	}

	public void addEditTaskTab(EditTaskView etv) {
		addTab("Edit Task",  new EditTaskView(Mode.EDIT), true);
		int index = tabPaneV.getTabCount()-1;
		tabPaneV.setSelectedIndex(index);
	}

	public void addManageStagesTab() {
		if(!manageStagesTabOpen){
			ManageStageView view = new ManageStageView();
			addTab("Manage Stages", view, true);
			int index = tabPaneV.getTabCount()-1;
			tabPaneV.setSelectedIndex(index);
			manageStagesTabOpen = true;
		}
	}

	public void addManageUsersTab() {
		if(!manageUsersTabOpen){
			addTab("Manage Users",  new ManageUsersView(), true);
			int index = tabPaneV.getTabCount()-1;
			tabPaneV.setSelectedIndex(index);
			manageUsersTabOpen = true;
		}
	}
	
	public void addTab(String title, Component component, boolean closeable){
		tabPaneV.addTab(title, component);
		tabPaneV.setTabComponentAt(tabPaneV.indexOfComponent(component), new TabView(title, component, closeable));
	}

	public void removeTabByComponent(Component component) {
		if(component instanceof ManageStageView){
			manageStagesTabOpen = false;
		}
		if(component instanceof ManageUsersView){
			manageUsersTabOpen = false;
		}
		if(!(component instanceof WorkflowView)){
			tabPaneV.remove(component);;
		}
	}
	
	/**
     * Changes the selected tab to the tab with the given index
     * 
     * @param tabIndex the index of the tab to select
     */
    private void switchToTab(int tabIndex) {
        try {
            tabPaneV.setSelectedIndex(tabIndex);
        } catch (IndexOutOfBoundsException e) {
            // an invalid tab was requested, do nothing
        }
    }

}
