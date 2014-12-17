/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskManager.TaskManager;
import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.model.TaskModel.TaskCategory;
import taskManager.model.WorkflowModel;
import taskManager.view.Colors;
import taskManager.view.FilterView;
import taskManager.view.ToolbarView;

/**
 * Tests for applying task filters to the workflow
 * 
 * @author Beth Martino
 *
 */
public class TestFilterController {

	private static final WorkflowModel wfm = WorkflowModel.getInstance();
	private static final WorkflowController wfc = WorkflowController
			.getInstance();
	private static ToolbarView toolV;

	private final String[] stageNames = { "New", "Scheduled", "In Progress",
			"Complete" };
	private FrameFixture fixture;
	private JFrame frame;

	private TaskModel t1;
	private TaskModel t2;
	private TaskModel t3;
	private TaskModel t4;
	private TaskModel t5;
	private TaskModel t6;

	private JPanelFixture taskFixture1;
	private JPanelFixture taskFixture2;
	private JPanelFixture taskFixture3;
	private JPanelFixture taskFixture4;
	private JPanelFixture taskFixture5;
	private JPanelFixture taskFixture6;

	private Dimension size = new Dimension(1000, 500);

	@Before
	public void setup() {

		// create a new workflow model
		TaskManager.reset();
		toolV = ToolbarController.getInstance().getView();

		// give it some stages
		for (String name : stageNames) {
			new StageModel(name, false);
		}

		wfc.reloadData();
		wfc.repaintView();

		createDummyTasks();

		frame = new JFrame();
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setLayout(new BorderLayout());
		frame.add(toolV, BorderLayout.NORTH);
		frame.add(TabPaneController.getInstance().getView());

		fixture = new FrameFixture(frame);
		fixture.show();

		taskFixture1 = fixture.panel("Task one");
		taskFixture2 = fixture.panel("Task two");
		taskFixture4 = fixture.panel("Task four");
		taskFixture5 = fixture.panel("Task five");
		taskFixture6 = fixture.panel("Task six");

	}

	@Test
	public void testApplyFilter() {
		// make sure all tasks are visible
		taskFixture1.requireVisible();
		taskFixture2.requireVisible();

		// 3 is archived
		taskFixture4.requireVisible();
		taskFixture5.requireVisible();
		taskFixture6.requireVisible();

		// click the red filter
		fixture.panel(Colors.CATEGORY_NAMES[1]).click();
		taskFixture1.requireVisible();

		// click all other filters
		fixture.panel(Colors.CATEGORY_NAMES[2]).click();
		fixture.panel(Colors.CATEGORY_NAMES[3]).click();
		fixture.panel(Colors.CATEGORY_NAMES[4]).click();
		fixture.panel(Colors.CATEGORY_NAMES[5]).click();
		taskFixture1.requireVisible();
		taskFixture2.requireVisible();
		taskFixture4.requireVisible();
		taskFixture5.requireVisible();

		// unclick the middle three filters
		fixture.panel(Colors.CATEGORY_NAMES[2]).click();
		fixture.panel(Colors.CATEGORY_NAMES[3]).click();
		fixture.panel(Colors.CATEGORY_NAMES[4]).click();

		taskFixture1.requireVisible();
		taskFixture5.requireVisible();

		// check archived
		fixture.panel(Colors.CATEGORY_NAMES[3]).click();
		fixture.checkBox(FilterView.SHOW_ARCHIVE).click();
		taskFixture3 = fixture.panel("Task three");

		taskFixture1.requireVisible();
		taskFixture3.requireVisible();
		taskFixture5.requireVisible();

		// check my tasks
		fixture.checkBox(FilterView.MY_TASKS).click();
		taskFixture2.requireVisible();
	}

	@Test
	public void testSearch() {

		// all except task 3 should be visible
		taskFixture1.requireVisible();
		taskFixture2.requireVisible();
		taskFixture4.requireVisible();
		taskFixture5.requireVisible();
		taskFixture6.requireVisible();

		// test search task title
		fixture.textBox(FilterView.SEARCH).enterText("one");
		taskFixture1.requireVisible();

		// test search description
		fixture.textBox(FilterView.SEARCH).deleteText();
		fixture.textBox(FilterView.SEARCH).enterText("def");
		taskFixture2.requireVisible();

		// test search wth archive showing
		fixture.checkBox(FilterView.SHOW_ARCHIVE).click();
		fixture.textBox(FilterView.SEARCH).deleteText();
		fixture.textBox(FilterView.SEARCH).enterText("ghi");
		taskFixture3 = fixture.panel("Task three");
		taskFixture3.requireVisible();

		// test search with cat clicked
		fixture.textBox(FilterView.SEARCH).deleteText();
		fixture.textBox(FilterView.SEARCH).enterText("jkl");
		fixture.panel(Colors.CATEGORY_NAMES[4]).click();
		taskFixture4.requireVisible();

		// test search with mutiple tasks
		fixture.panel(Colors.CATEGORY_NAMES[4]).click();
		taskFixture1.requireVisible();
		taskFixture6.requireVisible();

	}

	/*
	 * create test tasks
	 */
	public void createDummyTasks() {

		t1 = new TaskModel("Task one", wfm.getStages().get(0));
		t2 = new TaskModel("Task two", wfm.getStages().get(1));
		t3 = new TaskModel("Task three", wfm.getStages().get(2));
		t4 = new TaskModel("Task four", wfm.getStages().get(2));
		t5 = new TaskModel("Task five", wfm.getStages().get(3));
		t6 = new TaskModel("Task six", wfm.getStages().get(0));

		t1.setDueDate(new Date());
		t2.setDueDate(new Date());
		t3.setDueDate(new Date());
		t4.setDueDate(new Date());
		t5.setDueDate(new Date());
		t6.setDueDate(new Date());

		t1.setDescription("abc");
		t2.setDescription("def");
		t3.setDescription("ghi");
		t4.setDescription("jkl");
		t5.setDescription("mno");
		t6.setDescription("c");

		t1.setCategory(TaskCategory.RED);
		t2.setCategory(TaskCategory.GREEN);
		t3.setCategory(TaskCategory.BLUE);
		t4.setCategory(TaskCategory.YELLOW);
		t5.setCategory(TaskCategory.PURPLE);

		t3.setArchived(true);

		t2.addAssigned(TaskManager.currentUser);

		wfc.reloadData();
		wfc.repaintView();

	}

	@After
	public void cleanUp() {
		fixture.cleanUp();
	}

}
