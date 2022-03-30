package com.wings2d.editor.objects.skeleton.path;

import java.awt.geom.Point2D;

public class OrderedHandle implements Comparable<OrderedHandle>{
	private Handle handle;
	final double dist;
	
	public OrderedHandle(final double dist, final Handle handle) {
		this.handle = handle;
		this.dist = dist;
	}

	@Override
	public int compareTo(OrderedHandle o) {
		if (this.getDistance() > o.getDistance()) {
			return 1;
		}
		else if (this.getDistance() < o.getDistance()) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
	public double getDistance() {
		return dist;
	}
	public Point2D getHandle() {
		return handle;
	}
}
