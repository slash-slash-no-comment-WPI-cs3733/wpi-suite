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
import java.awt.Font;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import org.jdesktop.swingx.border.DropShadowBorder;

import taskManager.controller.StageController;
import taskManager.controller.StageTitleController;
import taskManager.draganddrop.DDTransferHandler;
import taskManager.draganddrop.DraggablePanelListener;
import taskManager.draganddrop.DropAreaPanel;
import taskManager.draganddrop.DropTargetRedispatcher;
import taskManager.localization.LocaleChangeListener;
import taskManager.localization.Localizer;

/**
 * @author Beth Martino
 * @version November 9, 2014
 */
public class StageView extends JPanel implements Transferable,
		LocaleChangeListener {

	private static final long serialVersionUID = 1L;
	private StageController controller;

	public static final String TITLE = "label";
	public static final String CHANGE_TITLE = "changeLabel";
	public static final String CHECK = "check";
	public static final String X = "x";
	public static final String TEXT_LABEL = "textLabel";

	private final JLabel labelName;
	private final JTextField labelText;
	private final JPanel changeLabel;
	private final JPanel label;
	private final JButton check;
	private final JButton cancel;
	private final DropAreaPanel tasks;
	private final JScrollPane stage;
	public static final int STAGE_WIDTH = 225;

	/**
	 * 
	 * Creates a new stage interface with a name with all the pretty UI elements
	 *
	 * @param name
	 *            The title of the stage being drawn
	 */
	public StageView(String name, StageController stageC) {

		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		// The tasks panel accepts task drops
		tasks = new DropAreaPanel(DDTransferHandler.getTaskFlavor());

		// stage view is a panel that contains the title and the scroll pane
		// w/tasks
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(STAGE_WIDTH, 450));

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
		labelName.setFont(new Font("Default", Font.PLAIN, 16));
		label.add(labelName);

		// The text field to change the stage's title
		changeLabel = new JPanel();
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
		check = new JButton("\u2713");
		check.setName(CHECK);
		check.setEnabled(false);
		check.setFont(check.getFont().deriveFont((float) 12));
		check.setMargin(new Insets(0, 0, 0, 0));
		// 'x' button
		cancel = new JButton();
		cancel.setName(X);
		cancel.setFont(cancel.getFont().deriveFont((float) 12));
		cancel.setMargin(new Insets(0, 0, 0, 0));

		changeLabel.add(labelText);
		changeLabel.add(check);
		changeLabel.add(cancel);

		changeLabel.setVisible(false);

		label.setVisible(true);

		this.add(label);
		this.add(changeLabel);

		// creates the scroll containing the stage view and adds it to the block
		stage = new JScrollPane(tasks,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		label.setBackground(Colors.STAGE);
		final Border color = BorderFactory.createLineBorder(
				label.getBackground(), 3);
		final DropShadowBorder shadow = new DropShadowBorder();
		shadow.setShadowColor(Color.BLACK);
		shadow.setShowLeftShadow(true);
		shadow.setShowRightShadow(true);
		shadow.setShowBottomShadow(true);
		shadow.setShowTopShadow(true);
		final Border compound = BorderFactory.createCompoundBorder(shadow,
				color);
		this.setBorder(compound);

		stage.setMinimumSize(new Dimension(STAGE_WIDTH, 300));
		stage.setSize(new Dimension(STAGE_WIDTH, 405));

		this.setName(name);
		updateTasks();

		// -----------------------
		// Drag and drop handling:
		final MouseAdapter listener = new DraggablePanelListener(this);

		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		label.addMouseListener(listener);
		label.addMouseMotionListener(listener);
		labelName.addMouseListener(listener);
		labelName.addMouseMotionListener(listener);

		setTransferHandler(new DDTransferHandler());

		// setTransferHandler creates DropTarget by default; we don't want
		// stages to respond to stage drops
		setDropTarget(null);

		// Make scrollpane redispatch drag events down to DropAreaPanel to avoid
		// scrollbar flicker
		stage.setDropTarget(new DropTarget(stage, new DropTargetRedispatcher(
				this, tasks, DDTransferHandler.getTaskFlavor())));

		setController(stageC);
		onLocaleChange();
		Localizer.addListener(this);
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
	public void addTaskView(JPanel tkv) {
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
		labelText.addKeyListener(controller);

		// listen for clicks on the 'change title' buttons
		check.addActionListener(controller);
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

	/**
	 * set if the check box is enabled
	 *
	 * @param enabled
	 *            wether the box should be enabled or not
	 */
	public void enableChangeTitleCheckmark(Boolean enabled) {
		check.setEnabled(enabled);
	}

	/**
	 * 
	 * @return the text in the titleTextBox.
	 */
	public String getLabelText() {
		return labelText.getText();
	}

	/**
	 * Sets the focus to the text area
	 */
	public void focusTextArea() {
		this.labelText.requestFocus();
		this.labelText.requestFocusInWindow();
		this.labelText.grabFocus();
		this.labelText.selectAll();
	}

	/**
	 * returns the editable label text field
	 * 
	 * @return the label text field
	 */
	public JTextField getLabelField() {
		return labelText;
	}

	/**
	 *
	 * Changes which title is visible, the label or the textbox. If editable is
	 * true, the textbox is visible.
	 *
	 * @param editable
	 *            true to make the textbox visible, false to make the label
	 *            visible
	 */
	public void switchTitles(boolean editable) {
		label.setVisible(!editable);
		changeLabel.setVisible(editable);
		focusTextArea();
	}

	public boolean isCheckEnabled() {
		return check.isEnabled();
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
			throws UnsupportedFlavorException {
		if (flavor.equals(DDTransferHandler.getStageFlavor())) {
			// return this panel as the transfer data
			return this;

		} else if (flavor.equals(DataFlavor.stringFlavor)) {
			return controller.getExportString();
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	/*
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		final DataFlavor[] flavors = { DDTransferHandler.getStageFlavor(),
				DataFlavor.stringFlavor };
		return flavors;
	}

	/*
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.
	 * datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DDTransferHandler.getStageFlavor())
				|| flavor.equals(DataFlavor.stringFlavor);
	}

	@Override
	public void onLocaleChange() {
		cancel.setText(Localizer.getString("x"));
	}
}
