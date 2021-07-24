package com.wings2d.editor.ui.skeleton;

import com.wings2d.editor.ui.UIElement;

public abstract class SkeletonUIElement extends UIElement{
	protected SkeletonEdit skeleton;
	
	public SkeletonUIElement(final SkeletonEdit edit)
	{
		super(edit);
		this.skeleton = edit;
	}
	public SkeletonEdit getSkeleton()
	{
		return skeleton;
	}
}
