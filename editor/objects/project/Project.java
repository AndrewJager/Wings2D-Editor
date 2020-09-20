package editor.objects.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Project {
	private File projectMap;
	private List<ProjectEntity> entities;
	private String name;
	private String directory;
	
	public Project()
	{
		entities = new ArrayList<ProjectEntity>();
	}
	public Project(final File directory) throws FileNotFoundException
	{
		this();
		validateDirectory(directory);
		
		boolean loadProject = true;
		File projectFile = new File(directory + "/PROJECTINFO.txt");
		if (projectFile.exists())
		{
			Scanner projFile = new Scanner(projectFile);
			if (projFile.hasNext())
			{
				String[] tokens = projFile.next().split(":");
				name = tokens[1];
			}
			projFile.close();
		}
		else
		{
			int result = JOptionPane.showConfirmDialog(null, "Folder does not have a PROJECTINFO.txt file. Would you like to create one?");
			if (result == JOptionPane.OK_OPTION)
			{
				name = JOptionPane.showInputDialog("Enter the project name:", "Project");
				File newProjFile = new File(directory + "/PROJECTINFO.txt");
				try {
					newProjFile.createNewFile();
					PrintWriter writer = new PrintWriter(newProjFile);
					writer.print("NAME:" + name + "\n"); 
					writer.close();
				} catch (IOException e) {
					throw new FileNotFoundException("Project file creation failed \n" + e.getMessage());
				}
			}
			else
			{
				loadProject = false;
			}
		}
		
		if (loadProject)
		{

		}
	}
	
	private void validateDirectory(File directory) throws FileNotFoundException
	{
		if(!directory.isDirectory())
		{
			throw new FileNotFoundException(directory.toString() + " is not a directory!");
		}
	}
	
	public List<ProjectEntity> getEntities()
	{
		return entities;
	}
	public String getName()
	{
		return name;
	}
}
