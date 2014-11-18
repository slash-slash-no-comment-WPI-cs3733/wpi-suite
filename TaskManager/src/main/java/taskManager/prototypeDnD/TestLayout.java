package taskManager.prototypeDnD;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

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
			// stage.setLayout(new BoxLayout(stage, BoxLayout.Y_AXIS));
			stage.setLayout(new FlowLayout());
			stage.setPreferredSize(new Dimension(200, 450));
			// stage.setMinimumSize(new Dimension(175, 450));
			// stage.setSize(new Dimension(175, 450));
			stage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

			stage.add(new JLabel("StageWithAName"));
			for (int j = 0; j < 3; j++) {

				TaskPanel task = new TaskPanel();

				task.setLayout(new BoxLayout(task, BoxLayout.Y_AXIS));
				task.add(new JLabel("Task" + Integer.toString(j)));
				task.add(new JLabel(Integer.toString(i) + " "
						+ Integer.toString(j)));
				task.add(new JLabel(Integer.toString(i) + " "
						+ Integer.toString(j)));
				task.setBackground(new Color(random.nextFloat(), random
						.nextFloat(), random.nextFloat()));
				task.setPreferredSize(new Dimension(100, 100));
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

/**
 * 
 * Creates the transferable representation of TaskPanel.
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
class DDTransferHandler extends TransferHandler {
	// TODO super?

	public Transferable createTransferable(JComponent comp) {
		if (comp instanceof TaskPanel) {
			System.out.println("Transferable created");
			// Container parent = comp.getParent();
			//
			// parent.remove(comp);
			// parent.revalidate();
			// parent.repaint();

			return (TaskPanel) comp;
		}
		return null;
	}

	public int getSourceActions(JComponent comp) {
		System.out.println("Return allowed actions");
		if (comp instanceof TaskPanel) {
			return TransferHandler.COPY; // TODO should be move
		}
		return TransferHandler.NONE;
	}

}

/**
 * 
 * The listener that triggers if a task is dropped on another task. Simply
 * delegates calls to the stage's drop listener.
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
class TaskDropListener extends DropTargetAdapter {
	private TaskPanel panel;

	public TaskDropListener(TaskPanel panel) {
		this.panel = panel;
	}

	@Override
	public void drop(DropTargetDropEvent e) {
		DropTargetDropEvent newE = convertToParentCoords(e);
		if (panel.getParent() instanceof StagePanel) {
			panel.getParent().getDropTarget().drop(newE);
		}
	}

	@Override
	public void dragOver(DropTargetDragEvent e) {
		DropTargetDragEvent newE = convertToParentCoords(e);
		if (panel.getParent() instanceof StagePanel) {
			panel.getParent().getDropTarget().dragOver(newE);
		}
	}

	public DropTargetDropEvent convertToParentCoords(DropTargetDropEvent e) {
		Container parent = panel.getParent();

		Point newPoint = SwingUtilities.convertPoint(e.getDropTargetContext()
				.getComponent(), e.getLocation(), parent);
		DropTargetDropEvent newE = new DropTargetDropEvent(
				e.getDropTargetContext(), newPoint, e.getDropAction(),
				e.getSourceActions());
		return newE;
	}

	public DropTargetDragEvent convertToParentCoords(DropTargetDragEvent e) {
		Container parent = panel.getParent();

		Point newPoint = SwingUtilities.convertPoint(e.getDropTargetContext()
				.getComponent(), e.getLocation(), parent);
		DropTargetDragEvent newE = new DropTargetDragEvent(
				e.getDropTargetContext(), newPoint, e.getDropAction(),
				e.getSourceActions());
		return newE;
	}
}

/**
 * 
 * Listens to when a Task is dropped onto a stage. Adds the TaskPanel to its
 * StagePanel.
 *
 * @author Sam Khalandovsky
 * @author Ezra Davis
 * @version Nov 17, 2014
 */
class StageDropListener implements DropTargetListener {

	private StagePanel stage;

	public StageDropListener(StagePanel stage) {
		this.stage = stage;
	}

	/**
	 * Drops a task onto the stage after making sure that it is a taskPanel.
	 */
	public void drop(DropTargetDropEvent e) {

		System.out.println("Dropping");

		Transferable trans = e.getTransferable();
		TaskPanel transferredPanel;
		if (trans.isDataFlavorSupported(DDManager.taskPanelFlavor)) {
			try {
				transferredPanel = (TaskPanel) trans
						.getTransferData(DDManager.taskPanelFlavor);
			} catch (Exception ex) {
				System.out.println(ex.getStackTrace());
				return;
			}

		} else {
			return;
		}

		stage.dropTask(transferredPanel, e.getLocation());
	}

	/**
	 * Hides the placeholder when the Task is no longer being dragged above the
	 * stage.
	 * 
	 * Careful with this due to it being called after entering a TaskPanel
	 * 
	 */
	@Override
	public void dragExit(DropTargetEvent e) {
		System.out.println("Stage drag exit");
		stage.hidePlaceholder();
	}

	/**
	 * Draws the placeholder on the stage when a task is being dragged above it.
	 */
	@Override
	public void dragOver(DropTargetDragEvent e) {
		System.out.println("Stage drag over");

		// Getting placeholder's size & making sure it's a TaskPanel
		Transferable trans = e.getTransferable();
		TaskPanel transferredPanel;
		if (trans.isDataFlavorSupported(DDManager.taskPanelFlavor)) {
			try {
				transferredPanel = (TaskPanel) trans
						.getTransferData(DDManager.taskPanelFlavor);
			} catch (Exception ex) {
				System.out.println(ex.getStackTrace());
				return;
			}

		} else { // Not a TaskPanel
			return;
		}
		Dimension placeholderSize = transferredPanel.getSize();

		// only draw placeholder if task has been made invisible
		// necessary to avoid flicker when placeholder is drawn before task is
		// hidden
		if (!transferredPanel.isVisible()) {
			stage.drawPlaceholder(e.getLocation(), placeholderSize);
		}
	}

	// Careful with this due to it being called after leaving a TaskPanel
	@Override
	public void dragEnter(DropTargetDragEvent e) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent e) {

	}

}
