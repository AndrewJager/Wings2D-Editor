package com.wings2d.editor.objects.project;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.save.DBString;
import com.wings2d.editor.objects.skeleton.Skeleton;

public class Project {
	private DBString id;
	private DBString name;
	
	public Project(final Connection con, final String id, final EditorSettings settings) {
		try {
			initData(con, id);
		} catch (SQLException e) {e.printStackTrace();}
	}
	public Project(final String name, final EditorSettings settings, final Connection con) throws FileNotFoundException
	{
		this(false, name, settings, con);
	}
	public Project(final boolean autoAcceptDir, final String projectName,
			final EditorSettings settings, final Connection con) throws FileNotFoundException
	{
		String id = UUID.randomUUID().toString();
		String query = "INSERT INTO PROJECT (ID, Name)"
				+ " VALUES(" + "'" + id + "'" + "," + "'" + projectName + "'" + ")";
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		query = " SELECT * FROM PROJECT WHERE ID = " + "'" + id + "'";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			initData(con,rs.getString("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initData(final Connection con, final String thisID) throws SQLException {
		id = new DBString(con, "PROJECT", "ID", thisID);
		name = new DBString (con, "PROJECT", "Name", thisID);
	}
	
	public static List<Project> getAll(final Connection con, final EditorSettings settings) {
		List<Project> projects = new ArrayList<Project>();
		
		String query = "SELECT * FROM PROJECT";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				projects.add(new Project(con, rs.getString("ID"), settings));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return projects;
	}
	
	public List<Skeleton> getSkeletons(final EditorSettings settings, final Connection con)
	{
		String id = this.getID();
		List<Skeleton> entities = new ArrayList<Skeleton>();
		String query = "SELECT * FROM SKELETON WHERE Project = " + "'" + id + "'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				entities.add(new Skeleton(rs.getString("ID"), settings, con));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entities;
	}
	public String getName()
	{
		return name.getValue();
	}
	public String getID() {
		return id.getValue();
	}
	
	@Override
	public String toString() {
		return name.getValue();
	}
	
	public static void delete(final String id, final Connection con) {
		// Delete Skeletons assocated with this project
		String sql = "SELECT * FROM SKELETON WHERE Project = " + "'" + id +"'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				Skeleton.delete(rs.getString("ID"), con);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		 
		// Delete this project
		sql = "DELETE FROM PROJECT WHERE ID = " + "'" + id +"'";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
