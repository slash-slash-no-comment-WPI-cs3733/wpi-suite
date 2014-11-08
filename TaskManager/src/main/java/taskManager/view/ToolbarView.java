/**
 * 
 */
package taskManager.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import java.awt.Font;

import taskManager.controller.*;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;

/**
 * The Task Managers tab's toolbar panel.
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	private JButton createTask;
	private JButton manageUsers;
	private JButton manageStages;
	private JButton statistics;
	private JLabel projectName;

	/**
	 * Create a ToolbarView.
	 * 
	 * @param tabController
	 *            The MainTabController this view should open tabs with
	 */
	public ToolbarView(IController toolbarController) {

		// Construct the content panel
		JPanel content = new JPanel();
		SpringLayout layout = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);

		// Construct the buttons
		createTask = new JButton("Create Task");
		manageStages = new JButton("Manage Stages");
		manageUsers = new JButton("Manage Users");
		statistics = new JButton("Statistics");

		// Add button actions
		// createTask.setAction(new CreateTaskAction(toolbarController));
		// manageStages.setAction(new ManageStagesAction(toolbarController));
		// manageUsers.setAction(new ManageUsersAction(toolbarController));
		// statistics.setAction(new StatisticsAction(toolbarController));

		// Construct the project title
		projectName = new JLabel("Project Title"); // getter?
		projectName.setFont(new Font("Serif", Font.BOLD, 20));

		// Calculate the width of the toolbar
		Double toolbarGroupWidth = createTask.getPreferredSize().getWidth()
				+ manageStages.getPreferredSize().getWidth()
				+ manageUsers.getPreferredSize().getWidth() +
				+ statistics.getPreferredSize().getWidth() + 90; // margin
		
		Double projectLoc = (toolbarGroupWidth-projectName.getPreferredSize().getWidth())/2;

		// Configure the layout of the buttons on the content panel
		layout.putConstraint(SpringLayout.NORTH, projectName, 20,
				SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.WEST, projectName, projectLoc.intValue(),
				SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.NORTH, createTask, 15,
				SpringLayout.SOUTH, projectName);
		layout.putConstraint(SpringLayout.WEST, createTask, 15,
				SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.NORTH, manageStages, 15,
				SpringLayout.SOUTH, projectName);
		layout.putConstraint(SpringLayout.WEST, manageStages, 15,
				SpringLayout.EAST, createTask);
		layout.putConstraint(SpringLayout.NORTH, manageUsers, 15,
				SpringLayout.SOUTH, projectName);
		layout.putConstraint(SpringLayout.WEST, manageUsers, 15,
				SpringLayout.EAST, manageStages);
		layout.putConstraint(SpringLayout.NORTH, statistics, 15,
				SpringLayout.SOUTH, projectName);
		layout.putConstraint(SpringLayout.WEST, statistics, 15,
				SpringLayout.EAST, manageUsers);

		// Add buttons to the content panel
		content.add(projectName);
		content.add(createTask);
		content.add(manageStages);
		content.add(manageUsers);
		content.add(statistics);

		// Construct a new toolbar group to be added to the end of the toolbar
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("", content);

		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(toolbarGroup);
	}
}
