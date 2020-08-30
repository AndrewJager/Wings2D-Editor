package editor.ui.skeleton;

import java.awt.Rectangle;

import editor.ui.UIElement;

public abstract class SkeletonUIElement extends UIElement{
	protected SkeletonEdit skeleton;
	
	public SkeletonUIElement(SkeletonEdit edit, Rectangle bounds)
	{
		super(bounds);
		this.skeleton = edit;
	}
}
