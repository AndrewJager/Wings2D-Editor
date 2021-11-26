package com.wings2d.editor.objects.save;

import java.awt.geom.Point2D;

public class DBPoint extends DBValue<Point2D>{
	
	public static class PointUtils {
		public static String toString(final Point2D point) {
			return point.getX() + "," + point.getY();
		}
		public static Point2D fromString(final String str) {
			String[] tokens = str.split(",");
			return new Point2D.Double(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
		}
	}
	
	public DBPoint(final String column) {
		super(column);
	}

	@Override
	public String asString() {
		return PointUtils.toString(value);
	}

	@Override
	public void fromString(String str) {
		value = PointUtils.fromString(str);	
	}
}
