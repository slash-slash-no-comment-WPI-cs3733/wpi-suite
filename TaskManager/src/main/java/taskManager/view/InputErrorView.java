/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputErrorView extends JPanel {

	private static final long serialVersionUID = 1L;
	String error;

	public InputErrorView(String error) {
		this.error = error;
		this.setBackground(Colors.INPUT_ERROR);
		this.add(new JLabel(error));
	}

}
