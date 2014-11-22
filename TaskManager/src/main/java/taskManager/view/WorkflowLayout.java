package taskManager.view;

import java.awt.Component;
import java.awt.FlowLayout;



/**
 * This layout manager behaves like a FlowLayout but ignores
 * any TaskInfoPreviewView
 * 
 * @author Clark Jacobsohn
 *
 */
@SuppressWarnings("serial")
public class WorkflowLayout extends FlowLayout {
	
	/* 
	 * @see java.awt.FlowLayout#addLayoutComponent(java.lang.String, java.awt.Component)
	 */
	@Override
	public void addLayoutComponent(String name, Component comp){
		if(!(comp instanceof TaskInfoPreviewView)){
			super.addLayoutComponent(name, comp);
		}
	}
	
	/* 
	 * @see java.awt.FlowLayout#removeLayoutComponent(java.awt.Component)
	 */
	@Override
	public void removeLayoutComponent(Component comp){
		if(!(comp instanceof TaskInfoPreviewView)){
			super.removeLayoutComponent(comp);
		}
	}
}
