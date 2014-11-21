//This file is only for testing the layout without Janeway.
//Not in production

package taskManager.prototypeDnD;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import taskManager.draganddrop.DDManager;
import taskManager.draganddrop.StagePanel;
import taskManager.draganddrop.TaskPanel;
import taskManager.view.TaskView;

/**
 * 
 * The example layout for Drag and drop
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
public class TestLayout extends JFrame {
	JPanel root;
	Random random = new Random(); // Randomizing for colors...

	public TestLayout() {
		new DDManager();

		this.setTitle("Test Layout");
		root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));
		// root.setLayout(new FlowLayout());
		root.setPreferredSize(new Dimension(900, 900));

		for (int i = 0; i < 3; i++) {
			JPanel stage = new StagePanel();
			stage.setLayout(new BoxLayout(stage, BoxLayout.Y_AXIS));
			// stage.setLayout(new FlowLayout());

			// stage.setLayout(new GridLayout(0, 1));
			// GridBagConstraints gbc = new GridBagConstraints();
			// gbc.gridwidth = GridBagConstraints.REMAINDER;
			// gbc.weighty = 1.0;

			stage.setPreferredSize(new Dimension(200, 450));
			// stage.setMinimumSize(new Dimension(175, 450));
			// stage.setSize(new Dimension(175, 450));
			stage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

			stage.add(new JLabel("StageWithAName"));
			for (int j = 0; j < 3; j++) {
				/*
				 * TaskPanel task = new TaskPanel();
				 * 
				 * task.setLayout(new BoxLayout(task, BoxLayout.Y_AXIS));
				 * task.add(new JLabel("Task" + Integer.toString(j)));
				 * task.add(new JLabel(Integer.toString(i) + " " +
				 * Integer.toString(j))); task.add(new
				 * JLabel(Integer.toString(i) + " " + Integer.toString(j)));
				 * task.setBackground(new Color(random.nextFloat(), random
				 * .nextFloat(), random.nextFloat())); task.setPreferredSize(new
				 * Dimension(100, 100));
				 */
				TaskPanel task = new TaskView("Task", new Date(), 10, null);
				task.setAlignmentX(CENTER_ALIGNMENT);
				stage.add(task);

			}

			root.add(stage);
		}
		this.add(root);
		root.setMinimumSize(new Dimension(400, 400));
		pack();
	}

	public static void main(String args[]) {
		TestLayout win = new TestLayout();
		win.setSize(new Dimension(650, 500));
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
	}
}
