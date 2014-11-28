/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

/*
 * @author Beth Martino
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import taskManager.controller.StageController;
import taskManager.draganddrop.StagePanel;

/**
 * @author Beth Martino
 * @version November 9, 2014
 */
public class StageView extends JPanel {

	private static final long serialVersionUID = 1L;
	private StageController controller;
	public static final String TITLE = "label";
	public static final String CHANGE_TITLE = "changeLabel";
	public static final String CHECK = "check";
	public static final String X = "x";
	public static final String TEXT_LABEL = "textLabel";

	private JLabel labelName;
	private JPanel label;
	private JButton done;
	private JButton cancel;
	private StagePanel tasks = new StagePanel();
	private JScrollPane stage = new JScrollPane(tasks,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	/**
	 * 
	 * Creates a new stage interface with a name with all the pretty UI elements
	 *
	 * @param name
	 *            The title of the stage being drawn
	 */
	public StageView(String name, Boolean editting) {

		// stage view is a panel that contains the title and the scroll pane
		// w/tasks
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(200, 450));
		this.setName(name);

		// organizes the tasks in a vertical list
		tasks.setLayout(new BoxLayout(tasks, BoxLayout.Y_AXIS));

		// creates the label for the name of the stage and adds it to the block
		label = new JPanel();
		label.setName(TITLE);
		label.setMaximumSize(new Dimension(175, 25));
		// The stage's title label
		labelName = new JLabel(name);
		labelName.setSize(new Dimension(175, 25));
		labelName.setMaximumSize(new Dimension(175, 25));
		labelName.setMinimumSize(new Dimension(175, 25));
		labelName.setPreferredSize(new Dimension(175, 25));
		label.add(labelName);

		// The text field to change the stage's title
		JPanel changeLabel = new JPanel();
		// changeLabel.setLayout(new BoxLayout(changeLabel, BoxLayout.X_AXIS));
		changeLabel.setMaximumSize(new Dimension(185, 25));
		changeLabel.setLayout(new FlowLayout(FlowLayout.LEADING));
		changeLabel.setName(CHANGE_TITLE);
		JTextField labelText = new JTextField();
		labelText.setText(name);
		labelText.setName(TEXT_LABEL);
		labelText.setSize(new Dimension(140, 25));
		labelText.setMinimumSize(new Dimension(140, 25));
		labelText.setMaximumSize(new Dimension(140, 25));
		labelText.setPreferredSize(new Dimension(140, 25));
		// Checkmark button
		done = new JButton("\u2713");
		done.setName(CHECK);
		done.setFont(done.getFont().deriveFont((float) 12));
		done.setMargin(new Insets(0, 0, 0, 0));
		// 'x' button
		cancel = new JButton("\u2716");
		cancel.setName(X);
		cancel.setFont(cancel.getFont().deriveFont((float) 12));
		cancel.setMargin(new Insets(0, 0, 0, 0));
		cancel.addActionListener(controller);
		changeLabel.add(labelText);
		changeLabel.add(done);
		changeLabel.add(cancel);

		if (editting) {
			System.out.println("editting");
			label.setVisible(false);
			changeLabel.setVisible(true);
		} else {
			changeLabel.setVisible(false);
			label.setVisible(true);
		}
		this.add(label);
		this.add(changeLabel);

		// creates the scroll containing the stage view and adds it to the block
		stage = new JScrollPane(tasks,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		stage.setBorder(BorderFactory.createLineBorder(Color.black));
		stage.setMinimumSize(new Dimension(175, 450));
		stage.setSize(new Dimension(175, 450));

		this.setName(name);
		updateTasks();
	}

	/**
	 * repopulates the tasks list into the scroll pane
	 */
	public void updateTasks() {
		this.remove(stage);
		stage = new JScrollPane(tasks,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		stage.setBorder(BorderFactory.createTitledBorder(this.getName()));
		stage.setMinimumSize(new Dimension(175, 350));
		this.add(stage);
	}

	public void updateStageName() {
		labelName.removeAll();
		labelName = new JLabel(stage.getName());
		labelName.setSize(new Dimension(175, 25));
		labelName.setMaximumSize(new Dimension(175, 25));
		labelName.setMinimumSize(new Dimension(175, 25));
		labelName.setPreferredSize(new Dimension(175, 25));
		label.add(labelName);
	}

	/**
	 * @param tkv
	 *            for new task view will be entered by the user
	 */
	public void addTaskView(TaskView tkv) {
		tkv.setAlignmentX(CENTER_ALIGNMENT);
		tasks.add(tkv);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * Adds the stage controller to this view
	 * 
	 * @param controller
	 */
	public void setController(StageController controller) {
		this.controller = controller;
		tasks.setController(controller);
		labelName.addMouseListener(controller);
	}

	/**
	 * Adds the stage controller to this view
	 * 
	 * @return the controller attached to this view
	 */
	public StageController getController() {
		return controller;
	}

	public void setStageName(String name) {
		stage.setName(name);
		this.setName(name);
		this.updateStageName();
	}

}
