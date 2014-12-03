package taskManager.view;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;


public class ContextMenu extends JPopupMenu implements MouseListener {

	/**
	 * Written by Jack Rivadeneira. This class allows you to create a context
	 * menu for a specific component. Add JMenuItems just like you would with a
	 * JMenu. This class is a mouse listener for the given component.
	 */
	private static final long serialVersionUID = -2779863978648080386L;
	/**
	 * Constructor for the context menu.
	 * 
	 * @param parent
	 *            the component the context menu is associated with. Right
	 *            clicking this component will show the menu.
	 */
	public ContextMenu(Component parent) {
		parent.addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3)// if it has been right clicked
			show((Component) e.getSource(), e.getX(), e.getY()); // show menu
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
}