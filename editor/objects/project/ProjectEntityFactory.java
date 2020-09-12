package editor.objects.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import editor.objects.skeleton.SkeletonAnimation;

/** Factory class to create Project Entities from files **/
public class ProjectEntityFactory {
	
	public static ProjectEntity createFromFile(File directory) throws FileNotFoundException
	{
		ProjectEntity newItem = null;
		Scanner in = new Scanner(directory);
		try {
			if (in.hasNext())
			{
				String line = in.next();
				if (line.equals(SkeletonAnimation.FILE_MARKER))
				{
					newItem = new SkeletonAnimation(in);
				}
				else
				{
					throw new FileNotFoundException(directory.getName() + " - File not recognised!");
				}
			}
			else
			{
				throw new FileNotFoundException(directory.getName() + " - Empty file!");
			}
		}
		finally
		{
			in.close();
		}
		
		return newItem;
	}
}
