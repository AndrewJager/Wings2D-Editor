package editor.objects.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Project {
	private File projectMap;
	
	public Project()
	{
		
	}
	public Project(final File directory) throws Exception
	{
		if(!directory.isDirectory())
		{
			throw new Exception(directory.toString() + " is not a directory!");
		}
		
		if (directory.list().length == 0) // Setup new project in this directory
		{
			projectMap = new File("DIRECTORY.txt");
			try {
				PrintWriter out = new PrintWriter(new FileOutputStream(directory + "/" + projectMap.getName()));
				out.write("Test");
				out.close();
			}
			catch(FileNotFoundException e)
			{
				
			}
		}
		else
		{
			File directoryFile = new File(directory + "/DIRECTORY.txt");
			System.out.println(directoryFile);
			if(!directoryFile.exists())
			{
				throw new Exception(directory.toString() + " contains files, but does not appear to be a project! (no DIRECTORY.txt found)");
			}
		}
	}
}
