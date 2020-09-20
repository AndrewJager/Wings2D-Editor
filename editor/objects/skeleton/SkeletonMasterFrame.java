package editor.objects.skeleton;

import java.io.PrintWriter;
import java.util.Scanner;

public class SkeletonMasterFrame extends SkeletonFrame{
	public static final String FILE_MARKER = "MASTERFRAME";

	public SkeletonMasterFrame(String frameName) {
		super(frameName, null);		
	}
	
	public SkeletonMasterFrame(final Scanner in)
	{
		super(in, null);
	}
	
	@Override
	public void saveToFile(final PrintWriter out)
	{
		out.write(FILE_MARKER);
		out.write("\n");
		writeFrameInfo(out);
		out.write("END:" + FILE_MARKER + "\n");
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
