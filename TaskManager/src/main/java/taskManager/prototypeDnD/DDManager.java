package taskManager.prototypeDnD;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * 
 * Draws the currently being dragged TaskPanel.
 *
 * @author Sam Khalandovsky
 * @version Nov 17, 2014
 */
public class DDManager extends DragSourceAdapter {

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
			// Point origin = panel.getLocationOnScreen();

			// Point imageOffset = new Point(origin.x - screen.x, origin.y
			// - screen.y);

			// System.out.println(imageOffset);
			// imageOffset = new Point(-10, -10);

			Point imageOffset = new Point(-panel.getOffset().x,
					-panel.getOffset().y);

			// dragIcon = new PanelIcon(panel);
			// System.out.println("New icon with width: "
			// + dragIcon.getIconWidth());
			// dragImage = new DragImage(panel, e.getLocation(), dragIcon,
			// imageOffset);

			sourceParent = (StagePanel) comp.getParent();
			// sourceParent.remove(comp);
			comp.setVisible(false);
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
		Component droppedComp = e.getDragSourceContext().getComponent();
		droppedComp.setVisible(true);
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