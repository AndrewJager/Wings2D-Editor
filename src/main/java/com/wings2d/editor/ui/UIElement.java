package com.wings2d.editor.ui;

import javax.swing.JPanel;

public abstract class UIElement {
	protected JPanel panel;
	
	public UIElement()
	{
		panel = new JPanel();
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public abstract void createEvents();
}
