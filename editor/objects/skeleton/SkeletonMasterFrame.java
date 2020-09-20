package editor.objects.skeleton;

import java.io.PrintWriter;

public class SkeletonMasterFrame extends SkeletonFrame{
	public static final String FILE_MARKER = "MASTERFRAME";

	public SkeletonMasterFrame(String frameName) {
		super(frameName, null);
		
	}
	
	@Override
	public void saveToFile(final PrintWriter out)
	{
		
	}
	
	@Override
	public String toString()
	{
		return name + " (Master)";
	}
	@Override
	public int getTreeLevel()
	{
		return 1;
	}
}
