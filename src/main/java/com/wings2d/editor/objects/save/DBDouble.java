package com.wings2d.editor.objects.save;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDouble extends DBValue<Double>{

	public DBDouble(final Connection con, final String table, final String column) {
		super(con, table, column);
	}
	public DBDouble(final Connection con, final String table, final String column, final String id) {
		super(con, table, column, id);
	}

	protected Double initValue(final ResultSet rs) {
		Double value = null;
		try {
			value = rs.getDouble(column);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
}
