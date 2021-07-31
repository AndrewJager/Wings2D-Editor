package com.wings2d.editor.ui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class UIElement<T extends UIPanel<T>> {
	private T editPanel;
	protected JPanel panel;
	
	final int PADDING = 5;
	
	public UIElement(final T parent)
	{
		this.editPanel = parent;
		parent.getElements().add(this);
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
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
