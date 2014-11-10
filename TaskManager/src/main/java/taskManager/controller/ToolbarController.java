/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import taskManager.view.ToolbarView;



/**
 * Description A controller for the toolbar  view
 *
 * @author Beth Martino
 */
public class ToolbarController implements ActionListener{

	private final ToolbarView view;

	public ToolbarController(ToolbarView view) {
		this.view = view;
		}
	
	@Override
	public void actionPerformed(ActionEvent e){
		Object button = e.getSource();
		if (button instanceof JButton) {
			String name = ((JButton)button).getName();
			switch(name){
			case "createTask": 
				System.out.println("blah");
				break;
			case "manageStages": 
				System.out.println("blah2");
				break;
			case "manageUsers": 
				System.out.println("blah3");
				break;
			case "statistics": 
				System.out.println("blah4");
				break;
			}
		}
	}

}
