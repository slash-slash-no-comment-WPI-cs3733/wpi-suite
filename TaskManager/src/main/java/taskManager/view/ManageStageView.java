/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Description
 *
 * @author Jon Sorrells
 */
public class ManageStageView extends JPanel {

	// TODO: change this to ManageStageController when that exists
	private ActionListener controller;

	private List<JButton> buttonsWithoutAController;

	private JPanel stageArea;
	private JTextField text;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The view for managing stages
	 *
	 */
	public ManageStageView() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.stageArea = new JPanel();
		this.stageArea
				.setLayout(new BoxLayout(this.stageArea, BoxLayout.Y_AXIS));
		this.controller = null;
		this.buttonsWithoutAController = new ArrayList<JButton>();
		this.add(stageArea);
		this.add(addNewStagePanel());

	}

	/**
	 * Adds a stage to the view
	 *
	 * @param name
	 *            The name of the stage to add
	 */
	public void addStage(String name) {
		this.stageArea.add(newStagePanel(name));
	}

	/**
	 * Remove the stage with the specified name
	 *
	 * @param name
	 *            The name of the stage to remove
	 */
	public void removeStage(String name) {
		for (Component stage : this.stageArea.getComponents()) {
			if (stage.getName() == name) {
				this.stageArea.remove(stage);
				break;
			}
		}
	}

	/**
	 * Removes all of the stages from the view
	 *
	 */
	public void removeAllStages() {
		this.stageArea.removeAll();
	}

	/**
	 * Sets the controller on this view, and attaches it to the buttons
	 *
	 * @param controller
	 *            The controller to attach to this view
	 */
	public void setController(ActionListener controller) {
		this.controller = controller;

		// add action listeners to any buttons created before this was called
		for (JButton button : buttonsWithoutAController) {
			button.addActionListener(controller);
		}
		buttonsWithoutAController.clear();
	}

	/**
	 * Gets the text field for the new stage name
	 *
	 * @return The text field
	 */
	public JTextField getNewStageNameField() {
		return text;
	}

	/**
	 * Creates a panel for this stage
	 *
	 * @param name
	 *            The name of the stage
	 */
	private JPanel newStagePanel(String name) {
		JPanel panel = new JPanel();
		panel.setName(name);

		// add the name of the stage
		panel.add(new JLabel(name));

		// add buttons
		panel.add(newButtonWithListener("Delete"));
		panel.add(newButtonWithListener("Move Up"));
		panel.add(newButtonWithListener("Move Down"));
		return panel;
	}

	/**
	 * Creates a button with the controller as the action listener
	 *
	 * @param title
	 *            The name of the button
	 * @return the created JButton
	 */
	private JButton newButtonWithListener(String title) {
		JButton button = new JButton(title);
		button.setName(title);
		if (controller != null) {
			button.addActionListener(controller);
		} else {
			buttonsWithoutAController.add(button);
		}
		return button;
	}

	/**
	 * Creates the panel with the add new stage text field and button
	 *
	 * @return The created panel
	 */
	private JPanel addNewStagePanel() {
		JPanel panel = new JPanel();
		text = new JTextField();
		text.setName("newStageName");

		// I don't understand why it won't become this size unless I set both of
		// these
		text.setPreferredSize(new Dimension(200, 25));
		text.setSize(new Dimension(200, 25));

		text.setText("New Stage Name");
		panel.add(text);
		panel.add(newButtonWithListener("Add new stage"));
		return panel;
	}
}
