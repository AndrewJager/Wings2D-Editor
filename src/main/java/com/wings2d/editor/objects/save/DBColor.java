package com.wings2d.editor.objects.save;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBColor extends DBValue<Color>{

	public static class ColorUtils {
		public static String toString(final Color col) {
			return col.getRed() + ","
					+ col.getGreen() + ","
					+ col.getBlue() + ","
					+ col.getAlpha();
		}
		public static Color fromString(final String str) {
			String[] tokens = str.split(",");
			return new Color(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
		}
	}

	public DBColor(final Connection con, final String table, final String column) {
		super(con, table, column);
	}
	public DBColor(final Connection con, final String table, final String column, final String id) {
		super(con, table, column, id);
	}

	@Override
	protected Color readValue(ResultSet rs) {
		Color value = null;
		try {
			value = ColorUtils.fromString(rs.getString(column));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	protected String saveValue(final Color value) {
		return ColorUtils.toString(value);
	}
}
