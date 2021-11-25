package com.wings2d.editor.objects.save;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DBValue<T> {
	protected Connection con;
	protected String table;
	protected String column;
	protected String id;
	
	protected T bufferValue;
	
	public DBValue(final Connection con, final String table, final String column) {
		this(con, table, column, null);
	}
	
	public DBValue(final Connection con, final String table, final String column, final String id) {
		this.con = con;
		this.table = table;
		this.column = column;
		this.id = id;
	}
	
	public T getValue() {
		return getValue(false);
	}
	public T getValue(final boolean useBuffer) {
		T value = null;
		if (useBuffer) {
			value = bufferValue;
		}
		else {
			String query = "SELECT * FROM " + table;
			if (id != null) {
				query = query + " WHERE ID = " + "'" + id +"'";
			}
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				
				value = initValue(rs);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	public void setValue(final T value) {
		setValue(value, false);
	}
	public void setValue(final T value, final boolean useBuffer) {
		bufferValue = value;
		if (!useBuffer) {
			String query = getBaseQuery() + "'" + saveValue(value) + "'";
			if (id != null) {
				query = query + "WHERE ID = " + "'" + id +"'";
			}
			try {
				Statement stmt = con.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/** Commit the value in the buffer to the database */
	public void commitBuffer() {
		String query = getBaseQuery() + "'" + saveValue(bufferValue) + "'";
		if (id != null) {
			query = query + "WHERE ID = " + "'" + id +"'";
		}
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected String getBaseQuery() {
		return "UPDATE " + table
				+ " SET " + column + " = ";
	}
	public T initValue(final ResultSet rs) {
		bufferValue = readValue(rs);
		return bufferValue;
	}
	protected abstract T readValue(final ResultSet rs);
	
	protected String saveValue(final T value) {
		if (value != null) {
			return value.toString();
		}
		else { 
			return "";
		}
	}
}
