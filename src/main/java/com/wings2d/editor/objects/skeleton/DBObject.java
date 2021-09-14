package com.wings2d.editor.objects.skeleton;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.wings2d.editor.objects.save.DBString;

public abstract class DBObject {
	protected String tableName;
	protected DBString name;
	protected DBString id;
	
	public DBObject(final String tableName) {
		this.tableName = tableName;
	}
	
	public void delete(final Connection con) {
		String id = this.getID();
		deleteChildren(id, con);
		
		String sql = "DELETE FROM " + this.getTableName() + " WHERE ID = " + "'" + id +"'";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void initData(final Connection con, final String thisID) throws SQLException;
	protected abstract void deleteChildren(final String id, final Connection con);
	
	public void setName(final String newName) {
		this.name.setValue(newName);
	}
	public String getName() {
		return name.getValue();
	}
	public String getID() {
		return id.getValue();
	}
	public String getTableName() {
		return tableName;
	}
	
	public static String quoteStr(final String str) {
		return "'" + str + "'";
	}
}
