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

		//Setup teammember's buttons
		JButton bethButton = new JButton("Beth");		
		JButton jonButton = new JButton("Jon");
		JButton sameeButton = new JButton("Samee!");
		JButton stefanButton = new JButton("Stefan");
		JButton buttonJoseph = new JButton("Joseph");
		JButton samButton = new JButton("Sam's Amazing Button");
		JButton ezraButton = new JButton("Ezra");
		JButton thaneButton = new JButton("Thane");
		JButton clarkButton = new JButton("Clark");
		JButton peterButton = new JButton("Peter");

		// Setup the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new JLabel("//no comment"), BorderLayout.PAGE_START);
		JTextField text = new JTextField();
		mainPanel.add(text, BorderLayout.CENTER);
		
		//Team member buttons' listeners
		sameeButton.addActionListener(new TestListener("Boo!", text));
		buttonPanel.add(sameeButton);
		jonButton.addActionListener(new TestListener("You pushed the Jon button", text));
		buttonPanel.add(jonButton);
		bethButton.addActionListener(new TestListener("You pushed the Beth button!", text));
		buttonPanel.add(bethButton);
		stefanButton.addActionListener(new TestListener("You pushed stefanButton", text));
		buttonPanel.add(stefanButton);
		buttonJoseph.addActionListener(new TestListener("Blackman", text));
		buttonPanel.add(buttonJoseph);
		samButton.addActionListener(new TestListener("Nobody expects the Dialog Box!", text));
		buttonPanel.add(samButton);
		ezraButton.addActionListener(new TestListener("You clicked my button!\nHow nice.", text));
		buttonPanel.add(ezraButton);
		thaneButton.addActionListener(new TestListener("Thane has a button.", text));
		buttonPanel.add(thaneButton);
		clarkButton.addActionListener(new TestListener("Congrats!  You found the best button!", text));
		buttonPanel.add(clarkButton);
		peterButton.addActionListener(new TestListener("You pushed the Peter button! Go you!", text));
		buttonPanel.add(peterButton);

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
