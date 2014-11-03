/**
 * @author Jon Sorrells
 */

package ButtonModule.buttonModule;

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
 * A test module to test if I can add a module
 *
 */
public class ButtonModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	/**
	 * Construct a new ButtonModule for test purposes
	 */
	public ButtonModule() {
		
		// Setup button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton button0 = new JButton("Button 0");
		JButton button1 = new JButton("Button 1");
		JButton bethButton = new JButton("Beth");
		
		JButton jonButton = new JButton("Jon");
		
		// Setup the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new JLabel("//no comment"), BorderLayout.PAGE_START);
		JTextField text = new JTextField();
		mainPanel.add(text, BorderLayout.CENTER);
		
		// Add listeners to the buttons
		button0.addActionListener(new TestListener("You pushed button 0", text));
		buttonPanel.add(button0);
		button1.addActionListener(new TestListener("You pushed button 1", text));
		buttonPanel.add(button1);
		
		//Teammember buttons
		jonButton.addActionListener(new TestListener("You pushed the Jon button", text));
		buttonPanel.add(jonButton);
		
		bethButton.addActionListener(new TestListener("You pushed the Beth button!", text));
		buttonPanel.add(bethButton);
		
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