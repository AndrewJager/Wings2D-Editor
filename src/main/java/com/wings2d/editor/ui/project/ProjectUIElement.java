package com.wings2d.editor.ui.project;

import java.awt.Rectangle;
import java.util.List;

import javax.swing.JPanel;

import com.wings2d.editor.ui.UIElement;

public abstract class ProjectUIElement {
	protected JPanel panel;
	protected ProjectEdit projectEdit;

	public ProjectUIElement(final ProjectEdit edit) {
		panel = new JPanel();
		edit.Elements().add(this);
		this.projectEdit = edit;
	}
	public JPanel getPanel()
	{
		return panel;
	}
	public abstract void createEvents();
}
