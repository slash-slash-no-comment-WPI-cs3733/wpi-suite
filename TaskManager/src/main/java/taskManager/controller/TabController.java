package taskManager.controller;

/**
 * Contributors:
 * AHurle
 * JPage
 */

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.event.ChangeListener;

import taskManager.model.WorkflowModel;
import taskManager.view.TabView;
import taskManager.view.EditTaskView;
import taskManager.view.EditTaskView.Mode;
import taskManager.view.Tab;
import taskManager.view.ManageStageView;
import taskManager.view.ManageUsersView;

/**
 * Controls the behavior of a given MainTabView. Provides convenient public
 * methods for controlling the MainTabView. Keep in mind that this controller is
 * visible as a public field in the module.
 */
public class TabController {

	private final TabView view;

	/**
	 * @param view
	 *            Create a controller that controls this MainTabView
	 */
	public TabController(TabView view) {
		this.view = view;
		this.view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				TabController.this.onMouseClick(event);
			}
		});
	}

	/**
	 * @return the view
	 */
	public TabView getView() {
		return this.view;
	}

	/**
	 * Adds a tab.
	 * 
	 * @param title
	 *            The tab's title.
	 * @param icon
	 *            The icon for the tab.
	 * @param component
	 *            The component that will be displayed inside the tab.
	 * @param tip
	 *            The tooltip to display when the cursor hovers over the tab
	 *            title.
	 * @return The created Tab
	 */
	public Tab addTab(String title, Icon icon, Component component, String tip) {
		view.addTab(title, icon, component, tip);
		int index = view.getTabCount() - 1;
		view.setSelectedIndex(index);
		return new Tab(view, view.getTabComponentAt(index));
	}

	/**
	 * @return Same as addTab(null, null, null, null)
	 */
	public Tab addTab() {
		return addTab(null, null, null, null);
	}

	/**
	 * Adds a tab that displays the given task in the given mode
	 * 
	 * @param tv
	 *            The EditTaskView to display
	 * @param mode
	 *            The Mode to use
	 */
	public Tab addTaskTab(EditTaskView tv, EditTaskView.Mode mode) {
		Tab tab = addTab();
		if (mode == EditTaskView.Mode.CREATE) {
			tab.setTitle("Create a Task");
		} else if (mode == EditTaskView.Mode.EDIT) {
			tab.setTitle("Edit a Task");
		}
		EditTaskController etc = new EditTaskController(new WorkflowModel(), tv, tab);
		tv.setController(etc);
		tab.setComponent(tv);
		tv.requestFocus();
		return tab;
	}

	/**
	 * Adds a tab that allows the user to create a new Task
	 * 
	 * @return The created Tab
	 */
	public Tab addCreateTaskTab() {
		return addTaskTab(new EditTaskView(Mode.CREATE), Mode.CREATE);
	}

	// TODO: Implement this when reportsView exists
	// /**
	// * Adds a tab that allows the user to view reports
	// *
	// * @return The created Tab
	// */
	// public Tab addReportsTab() {
	// ReportsView msv = new ReportsView();
	// Tab tab = addTab();
	// tab.setComponent(msv);
	// msv.requestFocus();
	// return tab;
	// }

	/**
	 * Adds a tab that allows the user to manage stages
	 * 
	 * @return The created Tab or null if there is already a ManageStageView tab
	 *         visible
	 */
	public Tab addManageStagesTab() {
		if (!view.manageStagesTabOut) {
			ManageStageView msv = new ManageStageView();
			Tab tab = addTab();
			tab.setTitle("Manage Stages");
			tab.setComponent(msv);
			msv.requestFocus();
			view.manageStagesTabOut = true;
			return tab;
		}
		return null;
	}

	/**
	 * Adds a tab that allows the user to manage users
	 * 
	 * @return The created Tab
	 */
	public Tab addManageUsersTab() {
		if (!view.manageUsersTabOut) {
			ManageUsersView muv = new ManageUsersView();
			Tab tab = addTab();
			tab.setTitle("Manage Users");
			tab.setComponent(muv);
			muv.requestFocus();
			view.manageUsersTabOut = true;
			return tab;
		}
		return null;
	}

	/**
	 * Add a change listener to the view this is controlling.
	 * 
	 * @param listener
	 *            the ChangeListener that should receive ChangeEvents
	 */
	public void addChangeListener(ChangeListener listener) {
		view.addChangeListener(listener);
	}

	/**
	 * Changes the selected tab to the tab left of the current tab
	 */
	public void switchToLeftTab() {
		if (view.getSelectedIndex() > 0) {
			switchToTab(view.getSelectedIndex() - 1);
		}
	}

	/**
	 * Changes the selected tab to the tab right of the current tab
	 */
	public void switchToRightTab() {
		switchToTab(view.getSelectedIndex() + 1);
	}

	/**
	 * Closes the currently active tab
	 */
	public void closeCurrentTab() {
		try {
			view.removeTabAt(view.getSelectedIndex());
		} catch (IndexOutOfBoundsException e) {
			// do nothing, tried to close tab that does not exist
		}
	}

	/**
	 * Changes the selected tab to the tab with the given index
	 * 
	 * @param tabIndex
	 *            the index of the tab to select
	 */
	private void switchToTab(int tabIndex) {
		try {
			view.setSelectedIndex(tabIndex);
		} catch (IndexOutOfBoundsException e) {
			// an invalid tab was requested, do nothing
		}
	}

	/**
	 * Close tabs upon middle clicks.
	 * 
	 * @param event
	 *            MouseEvent that happened on this.view
	 */
	private void onMouseClick(MouseEvent event) {
		// only want middle mouse button
		if (event.getButton() == MouseEvent.BUTTON2) {
			final int clickedIndex = view.indexAtLocation(event.getX(),
					event.getY());
			if (clickedIndex > -1) {
				view.removeTabAt(clickedIndex);
			}
		}
	}
}
