package taskManager.controller;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.ToolbarView;
import taskManager.view.WorkflowView;

public class TestTabPaneController {

	private static WorkflowView wfv = null;
	private static final WorkflowModel wfm = WorkflowModel.getInstance();
	private static WorkflowController wfc = JanewayModule.tabPaneC.getTabView()
			.getWorkflowController();

	private final String[] stageNames = { "New", "Scheduled", "In Progress",
			"Complete" };
	private FrameFixture fixture;
	private JFrame frame;

	@Before
	public void setup() {

		// creates a workflow view
		wfv = new WorkflowView();

		// create a new workflow model
		wfm.makeIdenticalTo(new WorkflowModel());

		// give it the stages
		for (String name : stageNames) {
			new StageModel(name, false);
		}

		// create controller for view
		wfv.setController(wfc);

		createDummyTasks();
		wfc.reloadData();
		wfc.repaintView();

		JFrame frame = new JFrame();
		frame.setSize(1000, 500);
		frame.setPreferredSize(new Dimension(1000, 500));
		frame.add(new ToolbarView());
		frame.add(wfv);

		fixture = new FrameFixture(frame);

		fixture.show();
		frame.pack();
	}

	@Test
	public void testOpenEditTaskTab() {
		// click the task
		fixture.panel("Task 1").doubleClick();

		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// check that the tab is open
		assertEquals(JanewayModule.getTabPaneView().getTitleAt(1), "Task 1");
	}

	public void createDummyTasks() {

		TaskModel t1 = new TaskModel("Task 1", wfm.getStages().get(0));
		TaskModel t2 = new TaskModel("Task 2", wfm.getStages().get(1));
		TaskModel t3 = new TaskModel("Task 3", wfm.getStages().get(2));

		t1.setDueDate(new Date());
		t2.setDueDate(new Date());
		t3.setDueDate(new Date());

		t1.setDescription("description");
		t2.setDescription("description");
		t3.setDescription("description");

	}

}
