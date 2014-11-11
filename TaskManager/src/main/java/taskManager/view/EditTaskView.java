package taskManager.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

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
		JLabel nt_dueDateLabel = new JLabel("Due Date ");
		JLabel nt_stageLabel = new JLabel("Stage ");
		JLabel nt_usersLabel = new JLabel("Users ");
		JLabel nt_estimatedEffortLabel = new JLabel("Estimated Effort ");
		JLabel nt_actualEffortLabel = new JLabel("Actual Effort ");
		JLabel nt_commentsLabel = new JLabel("Comments ");
		JLabel nt_requirementLabel = new JLabel("Requirements ");

		// JTextFields
		JTextField nt_titleField = new JTextField(25);
		JTextField nt_descriptionField = new JTextField(25);
		JTextField nt_dueDateField = new JTextField(25);
		JTextField nt_estimatedEffortField = new JTextField(10);
		JTextField nt_actualEffortField = new JTextField(10);
		JTextField nt_commentsField = new JTextField(25);
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
		// Add user to list
		JButton nt_addUsersBtn = new JButton("Add Users");
		// Add comment to comments
		JButton nt_submitCommnetBtn = new JButton("Submit Comment");
		// add requirement
		JButton nt_addRequirementBtn = new JButton("Add Requirement");
		// saves all the data and closes the window
		JButton nt_saveBtn = new JButton("Save");
		// closes the window without saving
		JButton nt_cancelBtn = new JButton("Cancel");

		// Combo Box for Stage
		// TODO
		// Options are currently fixed
		// Need access to stages, preferably in a list
		JComboBox<String> nt_stagesBoxes = new JComboBox<String>();
		nt_stagesBoxes.addItem("Backlog");
		nt_stagesBoxes.addItem("In Progress");
		nt_stagesBoxes.addItem("Review");
		nt_stagesBoxes.addItem("To Merge");
		nt_stagesBoxes.addItem("Done");

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
		add(nt_submitCommnetBtn, newTaskGridBag);

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
}
