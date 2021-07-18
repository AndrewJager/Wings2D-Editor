package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;

import com.wings2d.framework.imageFilters.DarkenFrom;

public class DarkenFromEdit extends ShadeFromEdit{
	private static final long serialVersionUID = 1L;

	public DarkenFromEdit(Frame owner) {
		super(owner, ShadeType.DARKEN);
	}
	
	public DarkenFromEdit(final DarkenFrom filter, final Frame owner) {
		super(filter, owner, ShadeType.DARKEN);
	}
}
