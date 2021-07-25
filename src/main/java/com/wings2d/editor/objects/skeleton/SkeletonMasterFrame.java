package com.wings2d.editor.objects.skeleton;

import java.io.PrintWriter;
import java.util.Scanner;

import com.wings2d.editor.objects.EditorSettings;

public class SkeletonMasterFrame extends SkeletonFrame{
	public static final String FILE_MARKER = "MASTERFRAME";

	public SkeletonMasterFrame(final String frameName, final EditorSettings settings) {
		super(frameName, null, settings);		
	}
	
	public SkeletonMasterFrame(final Scanner in, final EditorSettings settings)
	{
		super(in, null, settings);
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
}
