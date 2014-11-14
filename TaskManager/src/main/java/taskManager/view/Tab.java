package taskManager.view;

import java.awt.Component;
import taskManager.view.TabView;

import javax.swing.Icon;

/**
 * A wrapper class for MainTabView that can be given to components within that view
 * in order to allow them to easily change their titles and icons.
 */
public class Tab {

	private final TabView view;
	private final Component tabComponent;

	/**
	 * Create a Tab identified by the given MainTabView and tabComponent.
	 * 
	 * @param view The MainTabView this Tab belongs to
	 * @param tabComponent The tabComponent for this Tab
	 */
	public Tab(TabView view, Component tabComponent) {
		this.view = view;
		this.tabComponent = tabComponent;
	}
	
	private int getIndex() {
		return view.indexOfTabComponent(tabComponent);
	}
	
	public String getTitle() {
		return view.getTitleAt(getIndex());
	}
	
	/**
	 * @param title Set the title of the Tab to this String
	 */
	public void setTitle(String title) {
		view.setTitleAt(getIndex(), title);
		tabComponent.invalidate(); // needed to make tab shrink with smaller title
	}
	
	public Icon getIcon() {
		return view.getIconAt(getIndex());
	}
	
	/**
	 * @param icon Set the icon of the Tab to this Icon
	 */
	public void setIcon(Icon icon) {
		view.setIconAt(getIndex(), icon);
	}
	
	public String getToolTipText() {
		return view.getToolTipTextAt(getIndex());
	}
	
	/**
	 * @param toolTipText Set the tooltip of the Tab to this String
	 */
	public void setToolTipText(String toolTipText) {
		view.setToolTipTextAt(getIndex(), toolTipText);
	}
	
	public Component getComponent() {
		return view.getComponentAt(getIndex());
	}
	
	/**
	 * @param component Set the component contained by this Tab to this Component
	 */
	public void setComponent(Component component) {
		view.setComponentAt(getIndex(), component);
	}
	
}
