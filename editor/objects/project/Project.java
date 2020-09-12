package editor.objects.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Project {
	private File projectMap;
	private List<ProjectEntity> entities;
	
	public Project()
	{
		
	}
	public Project(final File directory) throws FileNotFoundException
	{
		validateDirectory(directory);
		entities = new ArrayList<ProjectEntity>();
		
		File directoryFile = new File(directory + "/DIRECTORY.txt");
		Scanner in = new Scanner(directoryFile);
		try {
			while (in.hasNext())
			{
				entities.add(ProjectEntityFactory.createFromFile(new File(in.next())));
			}
		}
		finally
		{
			in.close();
		}
	}
	
	private void validateDirectory(File directory) throws FileNotFoundException
	{
		if(!directory.isDirectory())
		{
			throw new FileNotFoundException(directory.toString() + " is not a directory!");
		}
		
		if (directory.list().length == 0) // Setup new project in this directory
		{
			projectMap = new File("DIRECTORY.txt");
			PrintWriter out = new PrintWriter(new FileOutputStream(directory + "/" + projectMap.getName()));
			out.write("Test");
			out.close();
		}
		else
		{
			File directoryFile = new File(directory + "/DIRECTORY.txt");
			if(!directoryFile.exists())
			{
				throw new FileNotFoundException(directory.toString() + " contains files, but does not appear to be a project! (no DIRECTORY.txt found)");
			}
		}
	}
}
