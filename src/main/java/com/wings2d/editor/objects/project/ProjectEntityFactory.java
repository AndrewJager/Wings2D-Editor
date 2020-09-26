package com.wings2d.editor.objects.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.wings2d.editor.objects.skeleton.Skeleton;

/** Factory class to create Project Entities from files **/
public class ProjectEntityFactory {
	
	public static ProjectEntity createFromFile(final File directory, final Project project) throws FileNotFoundException
	{
		ProjectEntity newItem = null;
		Scanner in = new Scanner(directory);
		try {
			newItem = null; // Return a null object if file is not valid
			if (in.hasNext())
			{
				String line = in.next();
				if (line.equals(Skeleton.FILE_MARKER))
				{
					newItem = new Skeleton(in, project);
				}
			}
		}
		finally
		{
			in.close();
		}
		
		return newItem;
	}
}
