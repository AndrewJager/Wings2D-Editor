package com.wings2d.editor.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public abstract class UIPanel<T extends UIPanel<T>> {
	protected JPanel panel;
	protected Editor editor;
	protected List<UIElement<T>> elements;
	
	public UIPanel(final Editor edit)
	{
		this.editor = edit;
		panel = new JPanel();
		panel.setLayout(null);
		elements = new ArrayList<UIElement<T>>();
	}
	
	public void initElements()
	{
		for (int i = 0; i < elements.size(); i++)
		{
			elements.get(i).createEvents();
		}
	}
	
	public JPanel getPanel()
	{
		return panel;
	}	
	public List<UIElement<T>> getElements()
	{
		return elements;
	}
	public Editor getEditor()
	{
		return editor;
	}
}
