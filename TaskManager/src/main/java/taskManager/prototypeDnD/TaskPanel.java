package taskManager.prototypeDnD;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import taskManager.model.TaskModel;

public class TaskPanel extends JPanel implements Transferable {

	private Point mouseOffset;
	private Point pos;
	private boolean dragActive;
	TaskModel model;

	public TaskPanel(TaskModel model) {
		this.model = model;

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
