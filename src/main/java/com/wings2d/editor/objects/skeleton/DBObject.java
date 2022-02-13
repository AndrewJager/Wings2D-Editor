package com.wings2d.editor.objects.skeleton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.wings2d.editor.objects.save.DBString;
import com.wings2d.editor.objects.save.DBUUID;
import com.wings2d.editor.objects.save.DBValue;

/** Represents a row in the database */
public abstract class DBObject {
	protected String tableName;
	protected DBString name;
	protected DBUUID id;
	
	/** Fields must be added to this list in the same order as they exist in the db */
	protected List<DBValue<?>> fields;
	
	/**
	 * Calls {@link DBObject#DBObject(String, boolean)} with tableName and true
	 * @param tableName Name of the database table
	 */
	public DBObject(final String tableName) {
		this(tableName, true);
	}
	
	@SuppressWarnings({ "serial" })
	/**
	 * @param tableName Name of the database table
	 * @param hasName If the table has a "Name" column. 
	 */
	public DBObject(final String tableName, final boolean hasName) {
		this.tableName = tableName;
		
		if (hasName) {
			name = new DBString("Name");
		}
		id = new DBUUID("ID");
		fields = new ArrayList<DBValue<?>>(){
			{
                add(id);
                if (hasName) {
                	add(name);
                }
            }
        };
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
	
	/** Updates the values in the object with the values in the database record */
	public void query(final Connection con, final UUID idValue) {
		String sql = " SELECT * FROM " + this.tableName + " WHERE ID = " + "'" + idValue.toString() + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			for (int i = 0; i < fields.size(); i++) {
				fields.get(i).fromString(rs.getString(fields.get(i).getColumn()));
			}
			queryChildren(idValue, con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Updates the values in the object with the values in the database record. Use for tables where only one record will exist */
	public void querySingleRecord(final Connection con) {
		String sql = " SELECT * FROM " + this.tableName;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			for (int i = 0; i < fields.size(); i++) {
				fields.get(i).fromString(rs.getString(fields.get(i).getColumn()));
			}
			queryChildren(UUID.fromString(rs.getString("ID")), con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void deleteChildren(final Connection con);
	protected abstract void queryChildren(final UUID id, final Connection con);
	protected abstract void updateChildren(final Connection con);
	
	/** Test */
	public void setName(final String newName) {
		this.name.setStoredValue(newName);
	}
	public String getName() {
		return name.getStoredValue();
	}
	public UUID getID() {
		return id.getStoredValue();
	}
	public String getTableName() {
		return tableName;
	}
	
	public static String quoteStr(final String str) {
		return "'" + str + "'";
	}
	public String getBasicQuery(final String table, final String idCol, final UUID idValue) {
		return " SELECT * FROM " + table + " WHERE " + idCol + " = " + quoteStr(idValue.toString());
	}
}
