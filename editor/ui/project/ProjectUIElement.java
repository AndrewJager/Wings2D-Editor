package editor.ui.project;

import java.awt.Rectangle;

import editor.ui.UIElement;

public abstract class ProjectUIElement extends UIElement{
	protected ProjectEdit projectEdit;

	public ProjectUIElement(final ProjectEdit edit, final Rectangle bounds) {
		super(bounds);
		edit.getElements().add(this);
		this.projectEdit = edit;
	}
}
