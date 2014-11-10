package taskManager.view;

/*
 * @author Beth Martino
 */
//<<<<<<< HEAD
import java.awt.FlowLayout;
import javax.swing.JPanel;

import taskManager.controller.WorkflowController;

public class WorkflowView extends JPanel implements IWorkflowView {

	private static final long serialVersionUID = 1L;

	private WorkflowController controller;

	public WorkflowView() {

		// arranges the stages horizontally and evenly spaced
		this.setLayout(new FlowLayout());
	}

	/**
	 * @param name
	 *            of the new stage to be added creates a new scroll panel to
	 *            house the stage view object sets the size and border
	 */
	public void addStageView(String name) {
		this.add(new StageView(name));
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setController(WorkflowController controller) {
		this.controller = controller;
	}

}
