/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package taskManager.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import taskManager.controller.EditTaskController;

/**
 *  Edit panel for a new task
 */

/**
 * @author Tyler Jaskoviak
 *
 */

// TODO
// Need Access to
// Tasks
// Stages
// Users
public class EditTaskView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton save;
	private JButton cancel;
	private JButton addUser;
	private JButton delete;
	private JButton addReq;
	private JButton submitComment;

	private JTextField titleField;
	private JTextField descripField;
	private JTextField dateField;
	private JTextField estEffortField;
	private JTextField actEffortField;
	private JTextField commentsField;

	private JComboBox<String> stages;

	private EditTaskController controller;

	public EditTaskView() {
		// TODO
		// When Task added make EditTask take in a Task called currTask

		Dimension nt_panelSize = getPreferredSize();
		nt_panelSize.width = 625; // TODO
		nt_panelSize.height = 500; // Decide size
		setPreferredSize(nt_panelSize);

		setBorder(BorderFactory.createTitledBorder("New Task"));

		// JLabels
		JLabel nt_titleLabel = new JLabel("Title ");
		JLabel nt_descriptionLabel = new JLabel("Description ");
		JLabel nt_dueDateLabel = new JLabel("Due Date (MM/DD/YY)");
		JLabel nt_stageLabel = new JLabel("Stage ");
		JLabel nt_usersLabel = new JLabel("Users ");
		JLabel nt_estimatedEffortLabel = new JLabel("Estimated Effort ");
		JLabel nt_actualEffortLabel = new JLabel("Actual Effort ");
		JLabel nt_commentsLabel = new JLabel("Comments ");
		JLabel nt_requirementLabel = new JLabel("Requirements ");

		// JTextFields
		// sets all text fields editable and adds them to global variables
		JTextField nt_titleField = new JTextField(25);
		nt_titleField.setEditable(true);
		this.titleField = nt_titleField;
		JTextField nt_descriptionField = new JTextField(25);
		nt_descriptionField.setEditable(true);
		this.descripField = nt_descriptionField;
		JTextField nt_dueDateField = new JTextField(25);
		nt_dueDateField.setEditable(true);
		this.dateField = nt_dueDateField;
		JTextField nt_estimatedEffortField = new JTextField(10);
		nt_estimatedEffortField.setEditable(true);
		this.estEffortField = nt_estimatedEffortField;
		JTextField nt_actualEffortField = new JTextField(10);
		nt_actualEffortField.setEditable(true);
		this.actEffortField = nt_actualEffortField;
		JTextField nt_commentsField = new JTextField(25);
		nt_commentsField.setEditable(true);
		this.commentsField = nt_estimatedEffortField;
		// TODO
		// When task is added switch to these
		// JTextField nt_titleField = new JTextField(currTask.getTitle, 25);
		// JTextField nt_descriptionField = new
		// JTextField(currTask.getDescription, 25);
		// JTextField nt_dueDateField = new JTextField(currTask.getDueDate, 25);
		// JTextField nt_estimatedEffortField = new
		// JTextField(currTask.getEstimatedEffort, 10);
		// JTextField nt_actualEffortField = new
		// JTextField(currTask.getActualEffort, 10);
		// JTextField nt_commentsField = new JTextField(25);

		// JTextArea
		// TODO
		// Get to add users
		// TODO
		// When task is added switch/include this
		// String[] nt_currUsersList = currTask.getUsers().getName();
		// JList<String> nt_usersList = new JList<String>(nt_currUsersList);
		JList<String> nt_usersList = new JList<String>();
		nt_usersList.setVisibleRowCount(3);
		nt_usersList.setFixedCellWidth(this.getWidth() * 2 / 5);
		// usersField placed within scrollPan to maintain size
		JScrollPane nt_usersScrollPane = new JScrollPane(nt_usersList);
		nt_usersScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// TODO
		// Comment Pane

		// TODO
		// Requirement Pane

		// JButtons
		// Delete Task and close the window
		JButton nt_deleteBtn = new JButton("Delete");
		this.delete = nt_deleteBtn;
		this.delete.setName("delete");
		// Add user to list
		JButton nt_addUsersBtn = new JButton("Add Users");
		this.addUser = nt_addUsersBtn;
		this.addUser.setName("addUser");
		// Add comment to comments
		JButton nt_submitCommentBtn = new JButton("Submit Comment");
		this.submitComment = nt_submitCommentBtn;
		this.submitComment.setName("submitComment");
		// add requirement
		JButton nt_addRequirementBtn = new JButton("Add Requirement");
		this.addReq = nt_addRequirementBtn;
		this.addReq.setName("addReq");
		// saves all the data and closes the window
		JButton nt_saveBtn = new JButton("Save");
		this.save = nt_saveBtn;
		this.save.setName("save");
		// closes the window without saving
		JButton nt_cancelBtn = new JButton("Cancel");
		this.cancel = nt_cancelBtn;
		this.cancel.setName("cancel");

		// Combo Box for Stage
		// TODO
		// Options are currently fixed
		// Need access to stages, preferably in a list
		JComboBox<String> nt_stagesBoxes = new JComboBox<String>();
		this.stages = nt_stagesBoxes;

		setLayout(new GridBagLayout());

		GridBagConstraints newTaskGridBag = new GridBagConstraints();

		// First Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;

		newTaskGridBag.weightx = 0.15;
		newTaskGridBag.weighty = 0.077;

		newTaskGridBag.gridx = 0;

		newTaskGridBag.gridy = 0;
		add(nt_titleLabel, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		add(nt_descriptionLabel, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		add(nt_dueDateLabel, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		add(nt_stageLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		add(nt_usersLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 5;
		add(nt_estimatedEffortLabel, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		add(nt_actualEffortLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.10;
		newTaskGridBag.gridy = 7;
		add(nt_commentsLabel, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 9;
		add(nt_requirementLabel, newTaskGridBag);

		// Second Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = 0.3;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 1;

		newTaskGridBag.gridy = 0;
		add(nt_titleField, newTaskGridBag);

		newTaskGridBag.gridy = 1;
		add(nt_descriptionField, newTaskGridBag);

		newTaskGridBag.gridy = 2;
		add(nt_dueDateField, newTaskGridBag);

		newTaskGridBag.gridy = 3;
		add(nt_stagesBoxes, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 4;
		add(nt_usersScrollPane, newTaskGridBag);

		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridy = 5;
		add(nt_estimatedEffortField, newTaskGridBag);

		newTaskGridBag.gridy = 6;
		add(nt_actualEffortField, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		add(nt_commentsField, newTaskGridBag);

		// TODO
		// List of Comments
		// newTaskGridBag.gridy = 8;

		// TODO
		// List of Requirements
		// newTaskGridBag.gridy = 9;

		// Third Column ////

		newTaskGridBag.anchor = GridBagConstraints.CENTER;
		newTaskGridBag.weightx = 0.15;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 2;

		newTaskGridBag.gridy = 4;
		add(nt_addUsersBtn, newTaskGridBag);

		newTaskGridBag.gridy = 7;
		add(nt_submitCommentBtn, newTaskGridBag);

		newTaskGridBag.gridy = 9;
		add(nt_addRequirementBtn, newTaskGridBag);

		newTaskGridBag.gridy = 10;
		add(nt_saveBtn, newTaskGridBag);

		// Fourth Column ////

		newTaskGridBag.anchor = GridBagConstraints.LINE_START;
		newTaskGridBag.weightx = 0.15;
		newTaskGridBag.weighty = 0.077;
		newTaskGridBag.gridx = 3;

		newTaskGridBag.gridy = 0;
		add(nt_deleteBtn, newTaskGridBag);

		newTaskGridBag.gridy = 10;
		add(nt_cancelBtn, newTaskGridBag);
	}

	/**
	 * Adds the action listener (controller) to this view
	 * 
	 * @param controller
	 *            the controller to be attached to this view
	 */
	public void setController(EditTaskController controller) {
		this.controller = controller;
		this.cancel.addActionListener(controller);
		this.save.addActionListener(controller);
		this.addUser.addActionListener(controller);
		this.addReq.addActionListener(controller);
		this.submitComment.addActionListener(controller);
		this.delete.addActionListener(controller);
	}

	/**
	 * Gets the text in the title field
	 * 
	 * @return the text in the title field
	 */
	public JTextField getTitle() {
		return titleField;
	}

	/**
	 * Gets the text in the description field
	 * 
	 * @return the text in the description field
	 */
	public JTextField getDescription() {
		return descripField;
	}

	/**
	 * Gets the text in the date field
	 * 
	 * @return the text in the date field
	 */
	public JTextField getDate() {
		return dateField;
	}

	/**
	 * Gets the number in the estimated effort field
	 * 
	 * @return the number in the estimated effort field
	 */
	public JTextField getEstEffort() {
		return estEffortField;
	}

	/**
	 * Gets the number in the actual effort field
	 * 
	 * @return the number in the actual effort field
	 */
	public JTextField getActEffort() {
		return actEffortField;
	}

	public JComboBox<String> getStages() {
		return stages;
	}

	/**
	 * sets the text in the title field
	 * 
	 * @param the
	 *            text in the title field
	 */
	public void setTitle(String d) {
		this.titleField.setText(d);
	}

	/**
	 * sets the text in the description field
	 * 
	 * @param d
	 *            the text in the description field
	 */
	public void setDescription(String d) {
		this.descripField.setText(d);
	}

	/**
	 * Sets the text in the date field
	 * 
	 * @param d
	 *            the text in the date field
	 */
	public void setDate(Date d) {
		SimpleDateFormat q = new SimpleDateFormat("MM/dd/yyyy");
		this.dateField.setText(q.format(d));
	}

	/**
	 * Sets the estimated effort to the value i
	 * 
	 * @param i
	 *            the value to set the estimated effort field to
	 */
	public void setEstEffort(Integer i) {
		this.estEffortField.setText(i.toString());
	}

	/**
	 * Set the the actual effort field to the value of i
	 * 
	 * @param i
	 *            the value to set the actual effort field to
	 */
	public void setActEffort(Integer i) {
		this.actEffortField.setText(i.toString());
	}

	/**
	 * set stage dropdown box to the stage associated with the task
	 * 
	 * @param n
	 *            the index of the stage in the workflow
	 */
	public void setStageDropdown(int n) {
		String p = this.stages.getItemAt(n);
		this.stages.setSelectedItem(p);
	}

	/**
	 * makes all of the text fields blank
	 */
	public void resetFields() {
		this.titleField.setText("");
		this.descripField.setText("");
		this.estEffortField.setText("");
		this.actEffortField.setText("");
		this.dateField.setText("");
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible && controller != null) {
			controller.reloadData();
		}
		super.setVisible(visible);
	}
}
