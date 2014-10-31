package testModule;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;


/**
 * A dummy module to demonstrate the Janeway client
 *
 */
public class TestModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	/**
	 * Construct a new DummyModule for demonstration purposes
	 */
	public TestModule() {
		
		// Setup button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(new JButton("Button 0"));
		buttonPanel.add(new JButton("Button 1"));
		
		// Setup the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new JLabel("//no comment"), BorderLayout.PAGE_START);
		mainPanel.add(new JTextField(), BorderLayout.CENTER);
		mainPanel.add(new JTextField(), BorderLayout.CENTER);
		
		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("//no comment", new ImageIcon(), buttonPanel, mainPanel);
		tabs.add(tab);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Test Module";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}
}