package com.wings2d.editor.ui;

import javax.swing.JPanel;

public abstract class UIElement {
	protected JPanel panel;
	
	public UIElement(final UIPanel parent)
	{
		panel = new JPanel();
		parent.getElements().add(this);
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public abstract void createEvents();
}
