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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import taskManager.controller.StageController;
import taskManager.controller.StageTitleController;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DraggablePanelListener;
import taskManager.draganddrop.DropAreaPanel;

/**
 * @author Beth Martino
 * @version November 9, 2014
 */
public class StageView extends JPanel implements Transferable {

	private static final long serialVersionUID = 1L;
	private StageController controller;

	public static final String TITLE = "label";
	public static final String CHANGE_TITLE = "changeLabel";
	public static final String CHECK = "check";
	public static final String X = "x";
	public static final String TEXT_LABEL = "textLabel";

	private JLabel labelName;
	private JTextField labelText;
	private JPanel label;
	private JButton done;
	private JButton cancel;
	private DropAreaPanel tasks;
	private JScrollPane stage;
	public static final int STAGE_WIDTH = 200;

	/**
	 * 
	 * Creates a new stage interface with a name with all the pretty UI elements
	 *
	 * @param name
	 *            The title of the stage being drawn
	 */
	public StageView(String name) {

		// The tasks panel accepts task drops
		tasks = new DropAreaPanel(DDTransferHandler.getTaskFlavor());

		// stage view is a panel that contains the title and the scroll pane
		// w/tasks
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(STAGE_WIDTH, 450));
		this.setName(name);

		// organizes the tasks in a vertical list
		tasks.setLayout(new BoxLayout(tasks, BoxLayout.Y_AXIS));

		// creates the label for the name of the stage and adds it to the block
		label = new JPanel();
		label.setName(TITLE);
		label.setMaximumSize(new Dimension(STAGE_WIDTH - 15, 25));
		// The stage's title label
		labelName = new JLabel(name);
		labelName.setName(name);
		labelName.setSize(new Dimension(STAGE_WIDTH - 15, 25));
		labelName.setMaximumSize(new Dimension(STAGE_WIDTH - 15, 25));
		labelName.setMinimumSize(new Dimension(STAGE_WIDTH - 15, 25));
		labelName.setPreferredSize(new Dimension(STAGE_WIDTH - 15, 25));
		label.add(labelName);

		// The text field to change the stage's title
		JPanel changeLabel = new JPanel();
		changeLabel.setMaximumSize(new Dimension(185, 25));
		changeLabel.setLayout(new FlowLayout(FlowLayout.LEADING));
		changeLabel.setName(CHANGE_TITLE);
		labelText = new JTextField();
		labelText.setText(name);
		labelText.setName(TEXT_LABEL);
		labelText.addKeyListener(new StageTitleController(this));
		labelText.setSize(new Dimension(135, 25));
		labelText.setMinimumSize(new Dimension(135, 25));
		labelText.setMaximumSize(new Dimension(135, 25));
		labelText.setPreferredSize(new Dimension(135, 25));
		// Checkmark button
		done = new JButton("\u2713");
		done.setName(CHECK);
		done.setEnabled(false);
		done.setFont(done.getFont().deriveFont((float) 12));
		done.setMargin(new Insets(0, 0, 0, 0));
		// 'x' button
		cancel = new JButton("\u2716");
		cancel.setName(X);
		cancel.setFont(cancel.getFont().deriveFont((float) 12));
		cancel.setMargin(new Insets(0, 0, 0, 0));

		changeLabel.add(labelText);
		changeLabel.add(done);
		changeLabel.add(cancel);

		changeLabel.setVisible(false);
		label.setVisible(true);

		this.add(label);
		this.add(changeLabel);

		// creates the scroll containing the stage view and adds it to the block
		stage = new JScrollPane(tasks,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		stage.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(0, 3, 0, 3),
				BorderFactory.createLineBorder(Color.black)));
		stage.setMinimumSize(new Dimension(STAGE_WIDTH, 300));
		stage.setSize(new Dimension(STAGE_WIDTH, 405));

		this.setName(name);
		updateTasks();

		// -----------------------
		// Drag and drop handling:
		MouseAdapter listener = new DraggablePanelListener(this);
		labelName.addMouseListener(listener);
		labelName.addMouseMotionListener(listener);

		setTransferHandler(new DDTransferHandler());

		// setTransferHandler creates DropTarget by default; we don't want
		// stages to respond to stage drops
		setDropTarget(null);
	}

	/**
	 * repopulates the tasks list into the scroll pane
	 */
	public void updateTasks() {
		this.remove(stage);
		stage.setViewportView(tasks);
		this.add(stage);
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

		tasks.setSaveListener(controller);

		// listen for clicks on the stage to remove stuff from view
		stage.addMouseListener(controller);
		// listen for double click on the stage title to change it
		labelName.addMouseListener(controller);
		// listen for clicks on the 'change title' buttons
		done.addActionListener(controller);
		cancel.addActionListener(controller);
	}

	/**
	 * Adds the stage controller to this view
	 * 
	 * @return the controller attached to this view
	 */
	public StageController getController() {
		return controller;
	}

	public void enableChangeTitleCheckmark(Boolean enabled) {
		done.setEnabled(enabled);
	}

	public String getLabelText() {
		return labelText.getText();
	}

	// ----------------------------
	// Drag-and-drop transferable implementation

	/*
	 * @see
	 * java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer
	 * .DataFlavor)
	 */
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (!flavor.equals(DDTransferHandler.getStageFlavor())) {
			throw new UnsupportedFlavorException(flavor);
		}
		// return this panel as the transfer data
		return this;
	}

	/*
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = { DDTransferHandler.getStageFlavor() };
		return flavors;
	}

	/*
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.
	 * datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DDTransferHandler.getStageFlavor());
	}

}
