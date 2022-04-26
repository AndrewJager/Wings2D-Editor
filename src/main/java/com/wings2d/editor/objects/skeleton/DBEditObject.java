package com.wings2d.editor.objects.skeleton;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public abstract class DBEditObject extends DBObject{

	/**
	 * Calls {@link DBEditObject#DBEditObject(String, boolean)} with tableName and true
	 * @param tableName Name of the database table
	 */
	public DBEditObject(final String tableName) {
		this(tableName, true);
	}
	
	/**
	 * @param tableName Name of the database table
	 * @param hasName If the table has a "Name" column. 
	 */
	public DBEditObject(final String tableName, final boolean hasName) {
		super(tableName, hasName);
	}
	
	/** Deletes the record from the database */
	public void delete(final Connection con) {
		UUID idValue = this.getID();
		deleteChildren(con);
		
		String sql = "DELETE FROM " + this.getTableName() + " WHERE ID = " + quoteStr(idValue.toString());
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Updates the record with the values in the object */
	public void update(final Connection con) {
		UUID idValue = this.getID();
		String sql = "UPDATE " + this.getTableName() + " SET ";
		for (int i = 0; i < fields.size(); i++) {
			sql = sql + fields.get(i).getColumn() + " = " + quoteStr(fields.get(i).asString());
			if (i < (fields.size() - 1)) {
				sql = sql + ",";
			}
		}
		sql = sql + " WHERE ID = " + "'" + idValue.toString() +"'";
		
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			
			updateChildren(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Inserts a new record with the values in the object */
	public void insert(final Connection con) {
		UUID newID = UUID.randomUUID();
		id.setStoredValue(newID);
		String sql = "INSERT INTO " + tableName + " (";
		for (int i = 0; i < fields.size(); i++) {
			sql = sql + fields.get(i).getColumn();
			if (i < (fields.size() - 1)) {
				sql = sql + ",";
			}
		}
		sql = sql + ") VALUES(";
		for (int i = 0; i < fields.size(); i++) {
			sql = sql + quoteStr(fields.get(i).asString());
			if (i < (fields.size() - 1)) {
				sql = sql + ",";
			}
		}
		sql = sql + ")";

		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	protected abstract void deleteChildren(final Connection con);
	protected abstract void updateChildren(final Connection con);
}
