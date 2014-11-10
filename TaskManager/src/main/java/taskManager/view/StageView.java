/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import taskManager.controller.StageController;

/**
 * @author Beth Martino
 * @version November 9, 2014
 */
public class StageView extends JPanel implements IStageView {

	/**
	 * not sure what this is, ask Jon or Sam?
	 */
	private static final long serialVersionUID = 1L;

	private StageController controller;

	/**
	 * Constructor for StageView.
	 */
	public StageView() {
		// organizes the tasks in a vertical list
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	/**
	 * Method addTaskView.
	 * 
	 * @param tkv
	 *            data for new task view will be entered by the user
	 */
	public void addTaskView(TaskView tkv) {
		this.add(tkv);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public void setController(StageController controller) {
		this.controller = controller;
	}

}
