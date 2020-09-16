package editor.ui.skeleton;

import java.awt.Rectangle;

import editor.ui.UIElement;

public abstract class SkeletonUIElement extends UIElement{
	protected SkeletonEdit skeleton;
	
	public SkeletonUIElement(final SkeletonEdit edit, final Rectangle bounds)
	{
		super(bounds);
		edit.getElements().add(this);
		this.skeleton = edit;
	}
}
