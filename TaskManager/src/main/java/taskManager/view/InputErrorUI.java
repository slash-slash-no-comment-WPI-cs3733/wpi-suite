/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

public class InputErrorUI extends BasicTextFieldUI {

	private String hint;
	private Color hintColor;

	public InputErrorUI(String hint, Color hintColor) {
		this.hint = hint;
		this.hintColor = hintColor;
	}

	public void repaint() {
		if (getComponent() != null) {
			getComponent().repaint();
		}

	}

	@Override
	protected void paintSafely(Graphics g) {
		// Render the default text field UI
		super.paintSafely(g);
		// Render the hint text
		JTextComponent component = getComponent();
		if (component.getText().length() == 0 && !component.hasFocus()) {
			g.setColor(hintColor);
			int padding = (component.getHeight() - component.getFont()
					.getSize()) / 2;
			int inset = 3;
			g.drawString(hint, inset, component.getHeight() - padding - inset);
		}
	}

}
