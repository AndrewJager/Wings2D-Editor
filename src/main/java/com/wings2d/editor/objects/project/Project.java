package com.wings2d.editor.objects.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.save.DBFile;
import com.wings2d.editor.objects.save.DBString;

public class Project {
	private List<ProjectEntity> entities;
	private DBString name;
	private DBFile directory;
	
	public Project()
	{
		entities = new ArrayList<ProjectEntity>();
	}
	public Project(final Connection con, final String id, final EditorSettings settings) {
		this();
		try {
			initData(con, id);
		} catch (SQLException e) {e.printStackTrace();}
		
		createAllInDirectory(directory.getValue(), settings);
	}
	public Project(final File directory, final String name, final EditorSettings settings, final Connection con) throws FileNotFoundException
	{
		this(directory, false, name, settings, con);
	}
	public Project(final File directoryFile, final boolean autoAcceptDir, final String projectName,
			final EditorSettings settings, final Connection con) throws FileNotFoundException
	{
		this();
		String id = UUID.randomUUID().toString();
		String query = "INSERT INTO PROJECT (ID, Name, Directory)"
				+ " VALUES(" + "'" + id + "'" + "," + "'" + projectName + "'" + "," + "'" + directoryFile + "'" + ")";
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

		validateDirectory(directory.getValue());
		
		boolean loadProject = true;
//		File projectFile = new File(directory + "/PROJECTINFO.txt");
//		if (projectFile.exists())
//		{
//			Scanner projFile = new Scanner(projectFile);
//			if (projFile.hasNext())
//			{
//				String[] tokens = projFile.next().split(":");
//				name = tokens[1];
//			}
//			projFile.close();
//		}
//		else
//		{
//			int result;
//			if (!autoAcceptDir)
//			{
//				result = JOptionPane.showConfirmDialog(null, "Folder does not have a PROJECTINFO.txt file. Would you like to create one?");
//			}
//			else
//			{
//				result = JOptionPane.OK_OPTION;
//			}
//			if (result == JOptionPane.OK_OPTION)
//			{
//				if (projectName != null)
//				{
//					name = projectName;
//				}
//				else
//				{
//					name = JOptionPane.showInputDialog("Enter the project name:", "Project");
//				}
//				File newProjFile = new File(directory + "/PROJECTINFO.txt");
//				try {
//					newProjFile.createNewFile();
//					PrintWriter writer = new PrintWriter(newProjFile);
//					writer.print("NAME:" + name + "\n"); 
//					writer.close();
//				} catch (IOException e) {
//					throw new FileNotFoundException("Project file creation failed \n" + e.getMessage());
//				}
//			}
//			else
//			{
//				loadProject = false;
//			}
//		}
		
		if (loadProject)
		{
			createAllInDirectory(directory.getValue(), settings);
		}
	}
	
	private void initData(final Connection con, final String id) throws SQLException {
		directory = new DBFile(con, "PROJECT", "Directory", id);
		name = new DBString (con, "PROJECT", "Name", id);
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
	
	private void createAllInDirectory(final File directory, final EditorSettings settings)
	{
		for (int i = 0; i < directory.listFiles().length; i++)
		{
			if (directory.listFiles()[i].isDirectory())
			{
				createAllInDirectory(directory.listFiles()[i], settings);
			}
			else
			{
				try {
					ProjectEntity newEntity = ProjectEntityFactory.createFromFile(directory.listFiles()[i], this, settings);
					if (newEntity != null)
					{
						entities.add(newEntity);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void validateDirectory(File directory) throws FileNotFoundException
	{
		if(!directory.isDirectory())
		{
			throw new FileNotFoundException(directory.toString() + " is not a directory!");
		}
	}
	
	public void saveProject()
	{
		for (int i = 0; i < entities.size(); i++)
		{
			entities.get(i).saveToFile();
		}
	}
	
	public List<ProjectEntity> getEntities()
	{
		return entities;
	}
	public String getName()
	{
		return name.getValue();
	}
	public File getDirectory()
	{
		return directory.getValue();
	}
	
	@Override
	public String toString() {
		return name.getValue();
	}
}
