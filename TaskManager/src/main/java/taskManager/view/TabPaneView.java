package taskManager.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import taskManager.controller.WorkflowController;
import taskManager.model.WorkflowModel;

public class TabPaneView extends JTabbedPane {

	// Because the workflow is a permanent tab, tabview should keep track of it
	private WorkflowController wfc;

	public TabPaneView() {
		setTabPlacement(TOP);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setBorder(BorderFactory.createEmptyBorder(5, 3, 3, 3));

		// Create a workflow view, controller, and model
		WorkflowView wfv = new WorkflowView();
		WorkflowModel wfm = new WorkflowModel();
		wfc = new WorkflowController(wfv, wfm);
		wfv.setController(wfc);
		
		JScrollPane scroll = new JScrollPane(wfv);
		scroll.setBorder(BorderFactory.createLineBorder(Color.black));

		this.addTab("Workflow", new ImageIcon(), scroll, "Workflow");
	}

	public void refreshWorkflow() {
		wfc.fetch();
	}
}
