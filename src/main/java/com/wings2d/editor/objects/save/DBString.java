package com.wings2d.editor.objects.save;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBString extends DBValue<String>{

	public DBString(final Connection con, final String table, final String column) {
		super(con, table, column);
	}
	public DBString(final Connection con, final String table, final String column, final String id) {
		super(con, table, column, id);
	}

	@Override
	protected String readValue(final ResultSet rs) {
		String value = null;
		try {
			value = rs.getString(column);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
}
