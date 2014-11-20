package taskManager.draganddrop;

import java.awt.Container;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

public class TaskPanel extends JPanel implements Transferable {

	private Point mouseOffset;
	private Point pos;
	private boolean dragActive;

	public TaskPanel() {

		TaskMouseListener listener = new TaskMouseListener();
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		this.setTransferHandler(new DDTransferHandler());
		this.setDropTarget(new DropTarget(this, new TaskDropListener(this)));
	}

	private class TaskMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			System.out.println("Panel pressed");
			mouseOffset = e.getPoint();
			pos = TaskPanel.this.getLocationOnScreen();
		}

		public void mouseDragged(MouseEvent e) {
			System.out.println("Mouse dragged");

			dragActive = true;

			JComponent comp = (JComponent) e.getSource();
			TransferHandler handler = comp.getTransferHandler();
			handler.exportAsDrag(comp, e, TransferHandler.COPY);// TODO
																// should be
																// move
		}
	}

	public void stopDragging() {
		dragActive = false;
		setVisible(true);
	}

	public boolean isBeingDragged() {
		return dragActive;
	}

	public Point getOffset() {
		return mouseOffset;
	}

	public void move(Point pos) {
		this.pos = pos;
	}

	/*
	 * public void paint(Graphics g) { if (dragActive) { g = g.create();
	 * g.translate(pos.x, pos.y); super.paint(g); } else { super.paint(g); } }
	 */

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		System.out.println("Gettin task transfer data");
		if (flavor.equals(DDManager.taskPanelFlavor)) {
			return this;
		}
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		System.out.println("Gettin task transfer data flavors");
		DataFlavor[] flavors = { DDManager.taskPanelFlavor };
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		System.out.println("Is data flavor supported?");
		return flavor.equals(DDManager.taskPanelFlavor);
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
