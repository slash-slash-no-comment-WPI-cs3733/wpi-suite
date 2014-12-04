package taskManager.controller;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.junit.Before;
import org.junit.Test;

import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.WorkflowView;

public class TestTabPaneController {

	private static WorkflowView wfv = null;
	private static final WorkflowModel wfm = WorkflowModel.getInstance();

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
		wfv.setController(new WorkflowController(wfv));

		createDummyTasks();

		JFrame frame = new JFrame();
		frame.add(wfv);

		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testOpenEditTaskTab() {
		wfv.getStageViewByName("New");
	}

	public void createDummyTasks() {

		TaskModel t1 = new TaskModel();
		TaskModel t2 = new TaskModel();
		TaskModel t3 = new TaskModel();

		t1.setName("Task 1");
		t2.setName("Task 2");
		t3.setName("Task 3");

		t1.setDescription("Decsription 1");
		t2.setDescription("Decsription 2");
		t3.setDescription("Decsription 3");

		t1.setStage(wfm.getStages().get(0));
		t2.setStage(wfm.getStages().get(1));
		t3.setStage(wfm.getStages().get(2));
	}

}
