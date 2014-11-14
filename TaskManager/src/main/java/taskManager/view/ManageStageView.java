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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import taskManager.controller.ManageStageController;

/**
 * A view to add/remove/move stages
 *
 * @author Jon Sorrells
 */
public class ManageStageView extends JPanel {

	private ManageStageController controller;

	private List<JButton> buttonsWithoutAController;

	private JPanel stageArea;
	private JTextField text;

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
	 * @param id
	 *            The ID of the stage to add
	 * @param removable
	 *            If the stage is removable
	 */
	public void addStage(String name, String id, boolean removable) {
		this.stageArea.add(newStagePanel(name, id, removable));
	}

	/**
	 * Remove the stage with the specified name
	 *
	 * @param id
	 *            The id of the stage to remove
	 */
	public void removeStage(String id) {
		for (Component stage : this.stageArea.getComponents()) {
			if (stage.getName().equals(id)) {
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
	public void setController(ManageStageController controller) {
		this.controller = controller;

		// add action listeners to any buttons that were created before a
		// controller was attached to this view
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

	/*
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible && controller != null) {
			controller.reloadData();
		}
		super.setVisible(visible);
	}

	/**
	 * Creates a panel for this stage
	 *
	 * @param name
	 *            The name of the stage
	 * @param id
	 *            The ID of the stage
	 * @param removable
	 *            If the stage is removable
	 * @return A panel containing the stage name and some buttons
	 */
	private JPanel newStagePanel(String name, String id, boolean removable) {
		JPanel panel = new JPanel();
		// set the id, so to locate this stage later
		panel.setName(id);

		// add the name of the stage
		panel.add(new JLabel(name));

		// add buttons
		JButton delButton = newButtonWithListener("Delete");
		delButton.setEnabled(removable);
		panel.add(delButton);
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

		// both of these need to be set for it to become that size
		text.setPreferredSize(new Dimension(200, 25));
		text.setSize(new Dimension(200, 25));

		text.setText("New Stage Name");
		panel.add(text);
		panel.add(newButtonWithListener("Add new stage"));
		return panel;
	}
}
