package taskManager.controller;

import static org.junit.Assert.assertEquals;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.core.MouseButton;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.WorkflowModel;
import taskManager.view.EditTaskView;
import taskManager.view.TaskInfoPreviewView;
import taskManager.view.ToolbarView;

public class TestTabPaneController {

	private static final WorkflowModel wfm = WorkflowModel.getInstance();
	private static final WorkflowController wfc = JanewayModule
			.getTabPaneView().getWorkflowController();
	private static final ToolbarView toolV = JanewayModule.toolV;

	private final String[] stageNames = { "New", "Scheduled", "In Progress",
			"Complete" };
	private FrameFixture fixture;
	private JFrame frame;

	private TaskModel t1;
	private TaskModel t2;
	private TaskModel t3;

	private Dimension size = new Dimension(1000, 500);

	@Before
	public void setup() {
		// create a new workflow model
		wfm.makeIdenticalTo(new WorkflowModel());

		// give it some stages
		for (String name : stageNames) {
			new StageModel(name, true);
		}
		createDummyTasks();
		frame = new JFrame();
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setLayout(new BorderLayout());
		frame.add(JanewayModule.toolV, BorderLayout.NORTH);
		frame.add(JanewayModule.tabPaneC.getTabView(), BorderLayout.CENTER);
		fixture = new FrameFixture(frame);

		fixture.show();
	}

	@Test
	public void testOpenEditTaskTab() {
		// click the task
		fixture.panel("Task 1").click();

		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// check that the tab is open
		assertEquals(JanewayModule.getTabPaneView().getTitleAt(1), "Task 1");
	}

	@Test
	public void testArchiveTaskWithOpenTab() {
		// click the task
		JPanelFixture taskFixture = fixture.panel("Task 1");
		taskFixture.click();
		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// go back to the workflow
		JanewayModule.tabPaneC.getTabView().setSelectedIndex(0);

		// archive the task
		// get the fixture for the task panel

		// try dragging really fast
		taskFixture.robot.settings().delayBetweenEvents(0);
		taskFixture.robot.settings().idleTimeout(0);

		// actually drag now
		Point location = taskFixture.target.getLocation();
		location.x += 5;
		location.y += 5;
		taskFixture.robot.pressMouse(taskFixture.target.getParent(), location);
		taskFixture.robot.moveMouse(taskFixture.target.getParent(), toolV
				.getArchive().getLocation());
		taskFixture.robot.releaseMouseButtons();

		// let it process things
		fixture.robot.waitForIdle();

		// switch to edit task tab
		JanewayModule.getTabPaneView().setSelectedIndex(1);

		String result = fixture.button(EditTaskView.ARCHIVE).text();

		assertEquals(result, "Unarchive");
	}

	@Test
	public void testDeleteTaskWithOpenTab() {
		// click the task
		JPanelFixture taskFixture = fixture.panel("Task 1");
		taskFixture.click();
		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// go back to the workflow
		JanewayModule.tabPaneC.getTabView().setSelectedIndex(0);

		// archive the task
		taskFixture.robot.pressMouse(MouseButton.LEFT_BUTTON);
		taskFixture.robot.moveMouse(JanewayModule.toolV.getComponent(4)
				.getLocation());
		taskFixture.robot.releaseMouse(MouseButton.LEFT_BUTTON);

		// show archived tasks
		fixture.robot.click(JanewayModule.toolV.getComponent(3));

		// archive the task
		taskFixture.robot.pressMouse(MouseButton.LEFT_BUTTON);
		taskFixture.robot.moveMouse(JanewayModule.toolV.getComponent(5)
				.getLocation());
		taskFixture.robot.releaseMouse(MouseButton.LEFT_BUTTON);

		assertEquals(JanewayModule.getTabPaneView().getTabCount(), 1);
		assertEquals(JanewayModule.getTabPaneView().getTitleAt(0), "Workflow");

	}

	@After
	public void cleanup() {
		fixture.cleanUp();
		// remove all tabs
		JanewayModule.tabPaneC.getTabView().remove(1);

	}

	public void createDummyTasks() {

		t1 = new TaskModel("Task 1", wfm.getStages().get(0));
		t2 = new TaskModel("Task 2", wfm.getStages().get(1));
		t3 = new TaskModel("Task 3", wfm.getStages().get(2));

		t1.setDueDate(new Date());
		t2.setDueDate(new Date());
		t3.setDueDate(new Date());

		t1.setDescription("description");
		t2.setDescription("description");
		t3.setDescription("description");

		wfc.reloadData();

	}

}
