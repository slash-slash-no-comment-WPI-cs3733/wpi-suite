/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import static java.lang.Thread.sleep;

import java.awt.Dimension;
import java.time.Instant;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fest.swing.fixture.FrameFixture;
import org.junit.Before;
import org.junit.Test;

import taskManager.model.StageModel;
import taskManager.model.TaskModel;
import taskManager.view.ReportsToolbarView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Tests for the edit task controller
 *
 * @author Jon Sorrells
 */
public class TestReportsManager {

	private Instant now;
	private ReportsManager rm;
	private FrameFixture fixture;
	private JFrame frame;
	private StageModel finished;
	private User u1;

	@Before
	public void setup() {
		now = Instant.now();
		finished = new StageModel("Finished");
		rm = new ReportsManager(new ReportsToolbarView());
		TaskModel tm1 = new TaskModel("Task", new StageModel("Start"));
		tm1.setEstimatedEffort(5);
		u1 = new User("User 1", "User 1", null, 42);
		tm1.addAssigned(u1);
		finished.addTask(tm1);
		frame = new JFrame();
		fixture = new FrameFixture(frame);
		frame.setPreferredSize(new Dimension(800, 800));
		fixture.show();
	}

	@Test
	public void simpleTest() throws InterruptedException {
		Set<String> users = new HashSet<String>();
		users.add("User 1");
		rm.findVelocityData(users, now, now.plusSeconds(60 * 60 * 24 * 7),
				false, finished);
		rm.generateDataset(false, Period.ofDays(1));
		final JPanel chart = rm.createChart("Title", "Time", "Effort");
		frame.add(chart);
		frame.revalidate();
		sleep(10000);
	}

	@Test
	public void testSingleUser() throws InterruptedException {
		Set<String> users = new HashSet<String>();
		users.add("User 1");
		rm.findVelocityData(users, now, now.plusSeconds(60 * 60 * 24 * 7),
				false, finished);
		rm.generateDataset(false, Period.ofDays(1));
		final JPanel chart = rm.createChart("Title", "Time", "Effort");
		frame.add(chart);
		frame.revalidate();
		sleep(10000);
	}
}
