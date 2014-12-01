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
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fest.swing.fixture.FrameFixture;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the edit task controller
 *
 * @author Jon Sorrells
 */
public class TestReportsManager {

	private FrameFixture fixture;
	private JFrame frame;

	@Before
	public void setup() {
		frame = new JFrame();
		fixture = new FrameFixture(frame);
		frame.setPreferredSize(new Dimension(800, 800));
		fixture.show();
	}

	@Test
	public void testStuff() throws InterruptedException {

		// create a sample dataset
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String u1 = "User 1";
		String u2 = "User 2";
		Random r = new Random();
		for (int i = 0; i < 7; i++) {
			int x1 = r.nextInt();
			int x2 = r.nextInt();
			dataset.addValue(Integer.max(x1, -x1), u1, "Day " + i);
			dataset.addValue(Integer.max(x2, -x2), u2, "Day " + i);
		}

		// create the chart
		final JPanel chart = ReportsManager.createChart(dataset, "Title",
				"Time", "Effort");

		frame.add(chart);
		frame.revalidate();
		sleep(1000);
	}
}
