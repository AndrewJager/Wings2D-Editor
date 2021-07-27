package com.wings2d.editor.ui;

import javax.swing.JPanel;

public abstract class UIElement<T extends UIPanel> {
	private T editPanel;
	protected JPanel panel;
	
	public UIElement(final T parent)
	{
		this.editPanel = parent;
		parent.getElements().add(this);
		panel = new JPanel();
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public T getEditPanel() {
		return editPanel;
	}
	
	public abstract void createEvents();
}
