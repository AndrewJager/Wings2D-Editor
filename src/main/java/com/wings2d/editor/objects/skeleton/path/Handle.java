package com.wings2d.editor.objects.skeleton.path;

import java.awt.geom.Point2D;

@SuppressWarnings("serial")
public class Handle extends Point2D.Double{
	private boolean isEnd;
	
	public Handle(final double x, final double y, final boolean isEnd) {
		super(x, y);
		
		this.isEnd = isEnd;
	}
	
	public Handle(final double x, final double y) {
		this(x, y, false);
	}
	
	public boolean getIsEnd() {
		return isEnd;
	}
}
