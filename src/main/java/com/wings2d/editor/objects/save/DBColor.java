package com.wings2d.editor.objects.save;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBColor extends DBValue<Color>{

	public DBColor(final Connection con, final String table, final String column) {
		super(con, table, column);
	}

	@Override
	protected Color initValue(ResultSet rs) {
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
