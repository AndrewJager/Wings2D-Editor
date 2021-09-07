package com.wings2d.editor.objects.save;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBBoolean extends DBValue<Boolean>{

	public DBBoolean(final Connection con, final String table, final String column) {
		super(con, table, column);
	}
	public DBBoolean(final Connection con, final String table, final String column, final String id) {
		super(con, table, column, id);
	}

	@Override
	protected Boolean initValue(final ResultSet rs) {
		Boolean value = null;
		try {
			value = rs.getBoolean(column);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
}
