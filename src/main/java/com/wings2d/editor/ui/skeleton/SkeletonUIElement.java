package com.wings2d.editor.ui.skeleton;

import java.awt.Rectangle;

import com.wings2d.editor.ui.UIElement;

public abstract class SkeletonUIElement extends UIElement{
	protected SkeletonEdit skeleton;
	
	public SkeletonUIElement(final SkeletonEdit edit, final Rectangle bounds)
	{
		super(bounds);
		edit.getElements().add(this);
		this.skeleton = edit;
	}
	public SkeletonEdit getSkeleton()
	{
		return skeleton;
	}
}
