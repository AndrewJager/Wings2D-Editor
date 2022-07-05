package com.wings2d.editor.ui;

import javax.swing.JPanel;

public abstract class UIGridPanel<T extends UIPanel<T>> extends UIPanel<T> {
	
	public UIGridPanel(final Editor edit, final int gridWidth, final int gridHeight)
	{
		super(edit);
		
		panel = new FixedGrid(gridWidth, gridHeight);
	}

	public FixedGrid getGrid() {
		return (FixedGrid)panel;
	}
	
	@Override
	public JPanel getPanel() {
		return null;
	}
}
