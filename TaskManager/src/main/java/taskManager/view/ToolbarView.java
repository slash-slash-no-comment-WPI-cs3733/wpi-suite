/**
 * 
 */
package taskManager.view;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import taskManager.controller.ToolbarController;

//import java.awt.*;

/**
 * The Task Managers tab's toolbar panel.
 */
@SuppressWarnings("serial")
public class ToolbarView extends JToolBar implements IToolbarView {

	public static final String REFRESH = "refresh";
	public static final String STATISTICS = "statistics";
	public static final String MANAGE_USERS = "manageUsers";
	public static final String MANAGE_STAGES = "manageStages";
	public static final String CREATE_TASK = "createTask";
	public static final String WORKFLOW = "workflow";
	// toolbar information
	private JButton createTask;
	private JButton manageUsers;
	private JButton manageStages;
	private JButton statistics;
	private JButton workflow;
	private JButton refresh;
	private JLabel projectName;

	// TODO: Change ActionListener to ToolbarController when one exists
	private ToolbarController controller;

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
		workflow = new JButton("Workflow");
		workflow.setName(WORKFLOW);
		createTask = new JButton("Create Task");
		createTask.setName(CREATE_TASK);
		manageStages = new JButton("Manage Stages");
		manageStages.setName(MANAGE_STAGES);
		manageUsers = new JButton("Manage Users");
		manageUsers.setName(MANAGE_USERS);
		statistics = new JButton("Statistics");
		statistics.setName(STATISTICS);
		refresh = new JButton("Refresh");
		refresh.setName(REFRESH);

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
		buttons.add(refresh);

		// Title and buttons to the toolbar
		this.add(title);
		this.add(buttons);
	}

	/**
	 * adds the toolbar controller as the action listener for all buttons
	 * 
	 * @param controller
	 *            the toolbar controller to be addded to the buttons
	 */
	public void setController(ToolbarController controller) {
		this.controller = controller;
		workflow.addActionListener(controller);
		createTask.addActionListener(controller);
		manageStages.addActionListener(controller);
		manageUsers.addActionListener(controller);
		statistics.addActionListener(controller);
		refresh.addActionListener(controller);
	}

	@Override
	public String getName() {
		return super.getName();
	}
}
