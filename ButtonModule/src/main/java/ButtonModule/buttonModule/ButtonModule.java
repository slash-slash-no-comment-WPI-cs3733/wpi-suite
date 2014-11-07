///**
// * @author Jon Sorrells
// */
//
//package ButtonModule.buttonModule;
//
//import java.awt.BorderLayout;
//import java.awt.FlowLayout;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
//import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
//import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
//
//
///**
// * A test module to test if I can add a module
// *
// */
//public class ButtonModule implements IJanewayModule {
//	
//	/** The tabs used by this module */
//	private ArrayList<JanewayTabModel> tabs;
//	
//	/**
//	 * Construct a new ButtonModule for test purposes
//	 */
//	public ButtonModule() {
//			
//		// Setup button panel
//		// ------ The top panel with buttons
//		JPanel buttonPanel = new JPanel();
//		buttonPanel.setLayout(new FlowLayout());
//		
//		ToolbarGroupView testGroup = new ToolbarGroupView("Test thing");
//
//		//Setup teammember's buttons
//		JButton bethButton = new JButton("Beth");		
//		JButton jonButton = new JButton("Jon");
//		JButton sameeButton = new JButton("Samee!");
//		JButton stefanButton = new JButton("Stefan");
//		JButton buttonJoseph = new JButton("Joseph");
//		JButton samButton = new JButton("Sam's Amazing Button");
//		JButton ezraButton = new JButton("Ezra");
//		JButton thaneButton = new JButton("Thane");
//		JButton clarkButton = new JButton("Clark");
//
//		// Setup the main panel
//		// ----- Panel with label and text field
//		JPanel mainPanel = new JPanel();
//		mainPanel.setLayout(new BorderLayout());
//		mainPanel.add(new JLabel("//no comment--"), BorderLayout.PAGE_START);
//		JTextField text = new JTextField();		// Text field
//		mainPanel.add(text, BorderLayout.CENTER);
//		
//		//Team member buttons' listeners
//		// ------ Add buttons to the button panel
//		sameeButton.addActionListener(new TestListener("Boo!", text));
//		buttonPanel.add(sameeButton);
//		jonButton.addActionListener(new TestListener("You pushed the Jon button", text));
//		buttonPanel.add(jonButton);
//		bethButton.addActionListener(new TestListener("You pushed the Beth button!", text));
//		buttonPanel.add(bethButton);
//		stefanButton.addActionListener(new TestListener("You pushed stefanButton", text));
//		buttonPanel.add(stefanButton);
//		buttonJoseph.addActionListener(new TestListener("Blackman", text));
//		buttonPanel.add(buttonJoseph);
//		samButton.addActionListener(new TestListener("Nobody expects the Dialog Box!", text));
//		buttonPanel.add(samButton);
//		ezraButton.addActionListener(new TestListener("You clicked my button!\nHow nice.", text));
//		buttonPanel.add(ezraButton);
//		thaneButton.addActionListener(new TestListener("Thane has a button.", text));
//		buttonPanel.add(thaneButton);
//		clarkButton.addActionListener(new TestListener("Congrats!  You found the best button!", text));
//		buttonPanel.add(clarkButton);
//
//		// -------- Add stuff to the tabs array
//		tabs = new ArrayList<JanewayTabModel>();
//		JanewayTabModel tab = new JanewayTabModel("//--no comment", new ImageIcon(), buttonPanel, mainPanel);
//		tabs.add(tab);
//	}
//
//	/**
//	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
//	 */
//	@Override
//	public String getName() {
//		return "Test Module";
//	}
//
//	/**
//	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
//	 */
//	@Override
//	public List<JanewayTabModel> getTabs() {
//		return tabs;
//	}
//}

/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    Chris Casola
 ******************************************************************************/

//package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
//import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
//import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;

/**
 * The Defect tab's toolbar panel.
 * Always has a group of global commands (Create Defect, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	private JButton createDefect;
	private JButton searchDefects;
	private JPlaceholderTextField searchField;
	
	/**
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public ToolbarView(MainTabController tabController) {

		// Construct the content panel
		JPanel content = new JPanel();
		SpringLayout layout  = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);
				
		// Construct the create defect button
		createDefect = new JButton();
		createDefect.setAction(new CreateDefectAction(tabController));
		
		// Construct the search button
		searchDefects = new JButton("Search Defects");
		searchDefects.setAction(new SearchDefectsAction(tabController));
		
		// Construct the search field
		searchField = new JPlaceholderTextField("Lookup by ID", 15);
		searchField.addActionListener(new LookupDefectController(tabController, searchField, this));
		
		// Configure the layout of the buttons on the content panel
		layout.putConstraint(SpringLayout.NORTH, createDefect, 5, SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.WEST, createDefect, 8, SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.WEST, searchDefects, 10, SpringLayout.EAST, createDefect);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, searchDefects, 0, SpringLayout.VERTICAL_CENTER, createDefect);
		layout.putConstraint(SpringLayout.NORTH, searchField, 15, SpringLayout.SOUTH, createDefect);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, searchField, 5, SpringLayout.EAST, createDefect);
		
		// Add buttons to the content panel
		content.add(createDefect);
		content.add(searchDefects);
		content.add(searchField);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Home", content);
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = createDefect.getPreferredSize().getWidth() + searchDefects.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(toolbarGroup);
	}

}

