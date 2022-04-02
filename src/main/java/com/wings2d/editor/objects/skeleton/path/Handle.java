package com.wings2d.editor.objects.skeleton.path;

import java.awt.geom.Point2D;

import com.wings2d.editor.objects.save.DBDouble;

public class Handle extends Point2D{
	private DBDouble x, y;
	private boolean isEnd;
	
	public Handle(final DBDouble x, final DBDouble y, final boolean isEnd) {
		this.x = x;
		this.y = y;
		
		this.isEnd = isEnd;
	}
	
	public Handle(final DBDouble x, final DBDouble y) {
		this(x, y, false);
	}
	
	public boolean getIsEnd() {
		return isEnd;
	}

	@Override
	public double getX() {
		return x.getStoredValue();
	}

	@Override
	public double getY() {
		return y.getStoredValue();
	}

	@Override
	public void setLocation(double x, double y) {
		this.x.setStoredValue(x);
		this.y.setStoredValue(y);
	}
}
