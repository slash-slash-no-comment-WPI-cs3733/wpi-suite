package taskManager.draganddrop;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import taskManager.model.FetchWorkflowObserver;

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

			TaskPanel panel = (TaskPanel) comp;

			// Create drag images
			Image image = new BufferedImage(panel.getWidth(),
					panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			g = g.create();
			// g.translate(panel.getWidth(), panel.getHeight());
			panel.paint(g);
			panel.getTransferHandler().setDragImage(image);
			panel.getTransferHandler().setDragImageOffset(panel.getOffset());

			return panel;
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

	// Do all setup actions when drag is initiated
	@Override
	public void exportAsDrag(JComponent comp, InputEvent e, int action) {
		FetchWorkflowObserver.ignoreAllResponses = true;
		super.exportAsDrag(comp, e, action);
	}

	// Do all cleanup actions after drag is complete
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		System.out.println("Export DONE");
		FetchWorkflowObserver.ignoreAllResponses = false;
		super.exportDone(source, data, action);
	}

}