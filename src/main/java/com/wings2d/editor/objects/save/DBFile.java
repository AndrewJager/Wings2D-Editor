package com.wings2d.editor.objects.save;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFile extends DBValue<File>{

	public DBFile(final Connection con, final String table, final String column) {
		super(con, table, column);
	}
	public DBFile(final Connection con, final String table, final String column, final String id) {
		super(con, table, column, id);
	}

	@Override
	protected File initValue(final ResultSet rs) {
		File value = null;
		try {
			value = new File(rs.getString(column));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

}
