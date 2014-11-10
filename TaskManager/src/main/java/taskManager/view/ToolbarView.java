/**
 * 
 */
package taskManager.view;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.JToolBar;
//import java.awt.*;

//import taskManager.controller.*;
/**
 * The Task Managers tab's toolbar panel.
 */
@SuppressWarnings("serial")
public class ToolbarView extends JToolBar implements IToolbarView {

	// toolbar information
	private JButton createTask;
	private JButton manageUsers;
	private JButton manageStages;
	private JButton statistics;
	private JButton workflow;
	private JLabel projectName;
	
	// TODO: Change ActionListener to ToolbarController when one exists
	private ActionListener controller;

	/**
	 * Create a ToolbarView.
	 * 
	 * @param tabController
	 *            The MainTabController this view should open tabs with
	 */
	public ToolbarView() {

		// Construct and set up the buttons and title panels
		JPanel buttons = new JPanel();
		JPanel title = new JPanel();
		FlowLayout layout = new FlowLayout();
		buttons.setLayout(layout);
		buttons.setOpaque(false);
		title.setLayout(layout);
		title.setOpaque(false);

		Insets margins = new Insets(30, 5, 0, 5);
		this.setMargin(margins);
		
		// Construct the buttons
		workflow = new JButton("workflow");
		workflow.setName("workflow");
		createTask = new JButton("Create Task");
		createTask.setName("createTask");
		manageStages = new JButton("Manage Stages");
		manageStages.setName("manageStages");
		manageUsers = new JButton("Manage Users");
		manageUsers.setName("manageUsers");
		statistics = new JButton("Statistics");
		statistics.setName("statistics");
		

		// Construct the project title
		projectName = new JLabel("Project Title"); // TODO(sswartz): update this
		projectName.setFont(new Font("Serif", Font.BOLD, 20));

		// Add buttons to the content panel
		title.add(projectName);
		buttons.add(workflow);
		buttons.add(createTask);
		buttons.add(manageStages);
		buttons.add(manageUsers);
		buttons.add(statistics);

		// Title and buttons to the toolbar
		this.add(title);
		this.add(buttons);
	}
	
	// TODO: Change ActionListener to ToolbarController
	public void setController(ActionListener controller) {
		this.controller = controller;
	}

	@Override
	public String getName() {
		return this.getName();
	}
}
