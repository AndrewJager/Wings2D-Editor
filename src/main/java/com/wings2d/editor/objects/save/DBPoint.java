package com.wings2d.editor.objects.save;

import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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

	public DBPoint(final Connection con, final String table, final String column) {
		super(con, table, column);
	}
	public DBPoint(final Connection con, final String table, final String column, final String id) {
		super(con, table, column, id);
	}

	@Override
	protected Point2D initValue(ResultSet rs) {
		Point2D value = null;
		try {
			value = PointUtils.fromString(rs.getString(column));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	protected String saveValue(final Point2D value) {
		return PointUtils.toString(value);
	}
}
