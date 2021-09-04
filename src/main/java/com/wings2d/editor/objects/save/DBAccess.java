package com.wings2d.editor.objects.save;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBAccess {
	private class DBEdit {
		private int id;
		private String sql;
		
		public DBEdit(final String sql) {
			this.sql = sql;
		}
		
		public void setID(final int id) {
			this.id = id;
		}
		public Integer getID() {
			return id;
		}
		
		public void runEdit(final Connection con) {
			try {
		    	Statement stmt = con.createStatement();
		        stmt.executeUpdate(sql);
		        String log = "INSERT INTO DBEDITS(ID)"
		        		+ " VALUES(" + id +")";
		        stmt.executeUpdate(log);
		        stmt.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class DBEdits {
		private List<DBEdit> edits;
		
		public DBEdits() {
			edits = new ArrayList<DBEdit>();
		}
		
		public void add(final DBEdit edit) {
			edit.setID(edits.size());
			edits.add(edit);
		}
		
		public List<DBEdit> getList() {
			return edits;
		}
	}
	public DBAccess() {
		boolean dbCreated = new File(System.getProperty("user.dir") + "/src/main/resources/projects.db").exists();
		
		Connection c = null;
		
		try {
			c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/projects.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (!dbCreated) {
			createDatabase(c);
		}
		
		runEdits(c);
		
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createDatabase(final Connection c) {
	    try {
	    	Statement stmt = c.createStatement();
		    String sql = "CREATE TABLE DBEDITS"
	                      + " (ID INT PRIMARY KEY     NOT NULL)"; 
	        stmt.executeUpdate(sql);
	        stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void runEdits(final Connection con) {
		List<DBEdit> edits = getEdits().getList();
		List<Integer> ids = new ArrayList<Integer>();
		
		String query = "SELECT ID FROM DBEDITS";
        try { 
        	Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
	        while (rs.next()) {
	        	System.out.println(rs.getInt("id"));
	        	ids.add(rs.getInt("id"));
	        }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
        
        for (int i = 0; i < edits.size(); i++) {
        	if (!ids.contains(edits.get(i).getID())) {
        		edits.get(i).runEdit(con);
        	}
        }
	}
	
	private DBEdits getEdits() {
		DBEdits edits = new DBEdits();
		edits.add(new DBEdit(
				"CREATE TABLE EDITORSETTINGS"
				+ " (ID INT PRIMARY KEY NOT NULL)"
				));
		edits.add(new DBEdit(
				"ALTER TABLE EDITORSETTINGS"
				+ " ADD COLUMN HandleSize INT"
				));
		
		return edits;
	}
}
