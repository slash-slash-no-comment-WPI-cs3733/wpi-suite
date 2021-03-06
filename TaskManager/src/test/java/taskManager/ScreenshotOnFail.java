/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.fest.swing.image.ScreenshotTaker;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Takes a screenshot if the test fails
 *
 * @author Jon Sorrells
 */
public class ScreenshotOnFail {

	@Rule
	public TestWatcher tw = new TestWatcher() {
		@Override
		protected void failed(Throwable e, Description description) {
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice[] gs = ge.getScreenDevices();
			for (int j = 0; j < gs.length; j++) {
				GraphicsDevice gd = gs[j];
				System.out.println(gd.toString());
			}
			System.out.println("done listing graphics devices");
			new ScreenshotTaker().saveDesktopAsPng(description.getMethodName()
					+ ".png");

		}
	};

}
