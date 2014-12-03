package taskManager.draganddrop;

import javax.swing.JPanel;

/**
 * Interface for providing additional save functionality after a drop in a
 * DropAreaPanel
 *
 * @author Sam Khalandovsky
 * @version Dec 1, 2014
 */
public interface DropAreaSaveListener {
	/**
	 * Save changes after a drag
	 *
	 * @param panel
	 *            transferred panel
	 * @param index
	 *            insertion index
	 */
	public void saveDrop(JPanel panel, int index);

}
