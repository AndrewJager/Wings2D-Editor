package com.wings2d.editor.objects.save;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBInt extends DBValue<Integer>{

	public DBInt(final Connection con, final String table, final String column) {
		super(con, table, column);
	}

	protected Integer initValue(final ResultSet rs) {
		Integer value = null;
		try {
			value = rs.getInt(column);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
}
