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
import com.wings2d.editor.objects.skeleton.DBObject;
import com.wings2d.editor.objects.skeleton.Skeleton;

public class Project extends DBObject{
	
	public Project(final Connection con, final String id, final EditorSettings settings) {
		super("PROJECT");
		this.query(con, id);
	}
	public Project(final String name, final EditorSettings settings, final Connection con) throws FileNotFoundException
	{
		this(false, name, settings, con);
	}
	public Project(final boolean autoAcceptDir, final String projectName,
			final EditorSettings settings, final Connection con) throws FileNotFoundException
	{
		super("PROJECT");
		this.insert(con);
		this.query(con, id.getStoredValue());
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
				entities.add(Skeleton.read(rs.getString("ID"), settings, con));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entities;
	}
	public String getName()
	{
		return name.getStoredValue();
	}
	public String getID() {
		return id.getStoredValue();
	}
	
	@Override
	public String toString() {
		return name.getStoredValue();
	}
	
	public void delete(final Connection con, final EditorSettings settings) {
		// Delete Skeletons assocated with this project
		List<Skeleton> sks = getSkeletons(settings, con);
		for (int i = 0; i < sks.size(); i++) {
			sks.get(i).delete(con);
		}

		 
		// Delete this project
		String sql = "DELETE FROM PROJECT WHERE ID = " + "'" + this.getID() +"'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	@Override
	protected void deleteChildren(final String id, final Connection con) {
		
	}
	@Override
	protected void queryChildren(final String id, final Connection con) {
		
	}
}
