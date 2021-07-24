package com.wings2d.editor.ui.project;

import com.wings2d.editor.ui.UIElement;

public abstract class ProjectUIElement extends UIElement{
	protected ProjectEdit projectEdit;

	public ProjectUIElement(final ProjectEdit edit) {
		super(edit);
		this.projectEdit = edit;
	}
}
