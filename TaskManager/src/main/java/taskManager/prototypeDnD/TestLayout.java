package taskManager.prototypeDnD;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

public class TestLayout extends JFrame {
	JPanel root;
	Random random = new Random();

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

class StagePanel extends JPanel {

	private JPanel placeholder;
	private int lastIndex;
	private List<Point> compCenters;

	public StagePanel() {
		// super();// TODO
		this.setTransferHandler(new DDTransferHandler());
		this.setDropTarget(new DropTarget(this, new StageDropListener(this)));

		// add placeholder
		placeholder = new JPanel();
		placeholder.setBackground(Color.GRAY);
		placeholder.setPreferredSize(new Dimension(100, 100));
		placeholder.setVisible(false);
		this.add(placeholder);
	}

	public void setPlaceholderSize(Dimension size) {
		placeholder.setPreferredSize(size);
	}

	public void setPlaceholderVisible(boolean visible) {
		placeholder.setVisible(visible);
	}

	public boolean isPlaceholderVisible() {
		return placeholder.isVisible();
	}

	public void setPlaceholderIndex(int index) {
		index = Math.min(index, this.getComponentCount() - 1);
		this.add(placeholder, index);
		if (index != lastIndex) {
			this.revalidate();
			this.repaint();
			lastIndex = index;
		}
	}

	public void setPlaceholderPoint(Point point) {
		setPlaceholderIndex(getInsertionIndex(point));
	}

	// do this ONLY while placeholder is invisible!!! REALLY!!!
	public void calculateCenters() {
		compCenters = new ArrayList<Point>();
		for (Component comp : this.getComponents()) {
			if (comp.isVisible()) {
				compCenters.add(new Point((int) comp.getBounds().getCenterX(),
						(int) comp.getBounds().getCenterY()));
			}
		}
	}

	public int getInsertionIndex(Point point) {
		// Component[] components = this.getComponents();
		// System.out.println(point);
		double minDist = Double.MAX_VALUE;
		int index = -1; // index of closest component
		int i = 0;
		for (Point center : compCenters) {
			final double dist = Math.pow(point.x - center.x, 2)
					+ Math.pow(point.y - center.y, 2);
			System.out.println(dist);
			if (dist < minDist) {
				minDist = dist;
				index = i;
			}
			i++;
		}
		System.out.println("Closest component" + Integer.toString(index));

		if (index < 0) {
			index = 0;
		} else if (point.y > compCenters.get(index).y) {
			index++;
		}// TODO make general for horizontal?
		System.out.println("Insert at " + Integer.toString(index));
		return index;
	}

}

class TaskPanel extends JPanel implements Transferable {

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

class StageDropListener implements DropTargetListener {

	private StagePanel stage;

	public StageDropListener(StagePanel stage) {
		this.stage = stage;
	}

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

		int index = stage.getInsertionIndex(e.getLocation());

		stage.add(transferredPanel, index);
		transferredPanel.setVisible(true);
		stage.setPlaceholderVisible(false);
		stage.calculateCenters();

		stage.revalidate();
		stage.repaint();

		// System.out.println(transferredPanel.getParent().getComponentCount());
	}

	// Careful with this due to it being called after leaving a TaskPanel
	@Override
	public void dragEnter(DropTargetDragEvent e) {
		System.out.println("Stage drag enter");

	}

	// Careful with this due to it being called after entering a TaskPanel
	@Override
	public void dragExit(DropTargetEvent e) {
		System.out.println("Stage drag exit");
		stage.setPlaceholderVisible(false);
	}

	@Override
	public void dragOver(DropTargetDragEvent e) {
		System.out.println("Stage drag over");
		if (!stage.isPlaceholderVisible()) {
			stage.calculateCenters();
		}
		stage.setPlaceholderPoint(e.getLocation());
		stage.setPlaceholderVisible(true);
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent e) {

	}

}

class DDManager extends DragSourceAdapter {

	public static DataFlavor taskPanelFlavor;

	private Icon dragIcon;
	private DragImage dragImage;

	private StagePanel sourceParent;

	public DDManager() {
		try {
			taskPanelFlavor = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType
							+ ";class=taskManager.prototypeDnD.TaskPanel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		DragSource.getDefaultDragSource().addDragSourceListener(this);
		DragSource.getDefaultDragSource().addDragSourceMotionListener(this);
	}

	public void dragMouseMoved(DragSourceDragEvent e) {
		System.out.println("Dragging happening");
		Component comp = e.getDragSourceContext().getComponent();
		if (dragImage == null && comp instanceof TaskPanel) {
			TaskPanel panel = (TaskPanel) comp;

			Point screen = e.getLocation();
			Point origin = panel.getLocationOnScreen();

			Point imageOffset = new Point(origin.x - screen.x, origin.y
					- screen.y);

			System.out.println(imageOffset);
			// imageOffset = new Point(-10, -10);

			imageOffset = new Point(-panel.getOffset().x, -panel.getOffset().y);

			dragIcon = new PanelIcon(panel);
			dragImage = new DragImage(panel, e.getLocation(), dragIcon,
					imageOffset);

			sourceParent = (StagePanel) comp.getParent();
			// sourceParent.remove(comp);
			comp.setVisible(false);
			sourceParent.calculateCenters();
			// sourceParent.setPlaceholderVisible(true);
			// sourceParent.remove(comp);
			sourceParent.revalidate();
			sourceParent.repaint();
		}

		if (dragImage != null) {
			dragImage.move(e.getLocation());

		}
	}

	public void dragDropEnd(DragSourceDropEvent e) {
		if (dragImage != null) {
			dragImage.dispose();
		}
		dragImage = null;
		sourceParent = null;
	}
}

class PanelIcon implements Icon {
	private Image image;
	protected Area clip;
	protected JComponent component;

	protected PanelIcon(JComponent c) {
		this.component = c;
		clip = getClip(c);

		image = new BufferedImage(getIconWidth(), getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		paint(image.getGraphics(), 0, 0);

	}

	protected Area getClip(Component c) {
		return null;
	}

	protected void paint(Graphics g, int x, int y) {
		g = g.create();
		g.translate(x, y);
		g.setClip(clip);
		try {
			component.setDoubleBuffered(false);
			component.paint(g);
		} finally {
			component.setDoubleBuffered(true);
		}
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (image != null) {
			g.drawImage(image, x, y, null);
		} else {
			paint(g, x, y);
		}
	}

	public int getIconWidth() {
		return component.getWidth();
	}

	public int getIconHeight() {
		return component.getHeight();
	}
}