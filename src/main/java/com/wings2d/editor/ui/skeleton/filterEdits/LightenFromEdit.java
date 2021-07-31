package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;

import com.wings2d.framework.imageFilters.LightenFrom;

public class LightenFromEdit extends ShadeFromEdit{
	private static final long serialVersionUID = 1L;
	
	public LightenFromEdit(final Frame owner) {
		super(owner, ShadeType.LIGHTEN);
	}
	public LightenFromEdit(final LightenFrom filter, final Frame owner) {
		super(filter, owner, ShadeType.LIGHTEN);
	}
}
