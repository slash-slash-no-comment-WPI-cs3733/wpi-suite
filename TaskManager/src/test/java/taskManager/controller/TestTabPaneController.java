package taskManager.controller;

import static org.junit.Assert.assertEquals;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.core.ComponentDragAndDrop;
import org.fest.swing.core.MouseButton;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import taskManager.JanewayModule;
import taskManager.draganddrop.DDTransferHandler;
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
	private TaskModel task;

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

	@Ignore
	public void testArchiveTaskWithOpenTab() {
		// click the task
		JPanelFixture taskFixture = fixture.panel("Task 1");
		taskFixture.click();
		// click the edit button
		fixture.button(TaskInfoPreviewView.EDIT).click();

		// go back to the workflow
		JanewayModule.tabPaneC.getTabView().setSelectedIndex(0);

		// archive the task
		// make sure it is visible
		fixture.requireVisible();

		// make sure stage is visible
		fixture.panel("New").requireVisible();

		// make sure task is visible
		taskFixture.requireVisible();

		// set the timing.

		// get the dragon drop targets
		Component task = fixture.panel("Task 1").target;
		Component archive = fixture.label(ToolbarView.ARCHIVE).target;

		// need to set to true to avoid network errors
		DDTransferHandler.dragSaved = true;

		// archive icon should be disabled at first.
		fixture.label(ToolbarView.ARCHIVE).requireDisabled();
		fixture.robot.settings().dropDelay(0);
		fixture.robot.settings().dragDelay(0);
		fixture.robot.settings().delayBetweenEvents(0);

		ComponentDragAndDrop q = new ComponentDragAndDrop(taskFixture.robot);

		Point loc = task.getLocation();
		loc.x += 5;
		loc.y += 5;

		fixture.robot.pressMouse(task, loc, MouseButton.LEFT_BUTTON);

		mouseMove(
				task,
				new Point(task.getLocation().x + 10 / 2,
						task.getLocation().y + 10 / 2),
				new Point(task.getLocation().x + 10, task.getLocation().y + 10),
				new Point(task.getLocation().x + 10 / 2,
						task.getLocation().y + 10 / 2),
				new Point(task.getLocation().x, task.getLocation().y));
		fixture.robot.waitForIdle();

		fixture.robot.moveMouse(task, toolV.getArchive().getLocation().x - 4,
				toolV.getArchive().getLocation().y);
		fixture.robot.moveMouse(task, toolV.getArchive().getLocation().x, toolV
				.getArchive().getLocation().y);

		fixture.robot.releaseMouseButtons();
		fixture.robot.waitForIdle();

		// switch to edit task tab
		JanewayModule.getTabPaneView().setSelectedIndex(1);

		String result = fixture.button(EditTaskView.ARCHIVE).text();

		assertEquals("Unarchive", result);
	}

	@Ignore
	public void testDeleteTaskWithOpenTab() {
		// click the task
		JPanelFixture taskFixture = fixture.panel("test");
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

	private void mouseMove(Component target, Point... points) {
		for (Point p : points)
			fixture.robot.moveMouse(target, p.x, p.y);
	}

}
