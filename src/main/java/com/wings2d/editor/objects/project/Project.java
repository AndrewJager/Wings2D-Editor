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
import com.wings2d.editor.objects.skeleton.DBEditObject;
import com.wings2d.editor.objects.skeleton.DBObject;
import com.wings2d.editor.objects.skeleton.Skeleton;

public class Project extends DBEditObject{
	public static final String TABLE_NAME = "PROJECT";
	
	private List<Skeleton> skeletons;
	private EditorSettings settings;
	
	public Project(final Connection con, final UUID id, final EditorSettings settings) {
		super(TABLE_NAME);
		skeletons = new ArrayList<Skeleton>();
		this.settings = settings;
		this.query(con, id);
	}
	public Project(final String name, final EditorSettings settings, final Connection con) throws FileNotFoundException
	{
		this(false, name, settings, con);
	}
	public Project(final boolean autoAcceptDir, final String projectName,
			final EditorSettings settings, final Connection con) throws FileNotFoundException
	{
		super(TABLE_NAME);
		name.setStoredValue(projectName);
		skeletons = new ArrayList<Skeleton>();
		this.settings = settings;
		
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
				projects.add(new Project(con, UUID.fromString(rs.getString("ID")), settings));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return projects;
	}

	@Override
	public String getName()
	{
		return name.getStoredValue();
	}
	@Override
	public UUID getID() {
		return id.getStoredValue();
	}
	
	@Override
	public String toString() {
		return name.getStoredValue();
	}
	
	@Override
	protected void deleteChildren(final Connection con) {
		for (int i = 0; i < skeletons.size(); i++) {
			skeletons.get(i).delete(con);
		}
	}
	@Override
	protected void queryChildren(final UUID id, final Connection con) {
		skeletons.clear();
		String sql = " SELECT * FROM " + Skeleton.TABLE_NAME + " WHERE Project = " + quoteStr(id.toString());
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				skeletons.add(Skeleton.read(UUID.fromString(rs.getString("ID")), settings, con));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void updateChildren(final Connection con) {
		for (int i = 0; i < skeletons.size(); i++) {
			skeletons.get(i).update(con);
		}
	}
	
	public List<Skeleton> getSkeletons() {
		return skeletons;
	}
}
