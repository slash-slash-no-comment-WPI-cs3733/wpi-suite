package taskManager;

/**
 * @author Beth Martino
 */

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import taskManager.controller.*;
import taskManager.view.*;

import javax.swing.*;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

/**
 * The JanewayModule for our task manager
 *
 */
public class JanewayModule implements IJanewayModule {

	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	public final IController workflowController;
	public IController toolbarController;

	/**
	 * Construct a blank tab
	 */
	public JanewayModule() {

		// Create the controllers
		workflowController = new Controller(); // WorkflowController();
		toolbarController = new Controller(); // ToolbarController();

		// Create the panels
		// mainPanel is the entire window - toolbar
		WorkflowView mainPanel = new WorkflowView();//workflowController);
		ToolbarView toolbarPanel = new ToolbarView(toolbarController);

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Task Manager",
				new ImageIcon(), toolbarPanel, mainPanel);
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
