package editor.ui.project;

import java.awt.Rectangle;

import editor.ui.UIElement;

public abstract class ProjectUIElement extends UIElement{
	protected ProjectEdit project;

	public ProjectUIElement(ProjectEdit edit, Rectangle bounds) {
		super(bounds);
		this.project = edit;
	}
}
