package com.wings2d.editor.objects;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class EditorSettings {
	private File editorDir;
	private File projectDir;
	
	private int handleSize;
	private int posHandleOffset;
	private int rotHandleOffset;
	private Color selectedHandleColor;
	private Color unselectedHandleColor;
	
	private static final String FILE_NAME = "PROJECTSETTINGS.txt";
	private static final String SPLIT = ">"; // Don't use ":" due to it being in the path
	private static final String CUR_PROJECT_TOKEN = "DIR";
	private static final String HANDLE_SIZE_TOKEN = "HANDLE_SIZE";
	private static final String POS_HANDLE_OFFSET_TOKEN = "POS_HANDLE_OFFSET";
	private static final String ROT_HANDLE_OFFSET_TOKEN = "ROT_HANDLE_OFFSET";
	
	public EditorSettings()
	{
		// Defaults
		handleSize = 10;
		selectedHandleColor = Color.RED;
		unselectedHandleColor = Color.GREEN;
		editorDir = new File(System.getProperty("user.dir") + "/src/main/resources");
		File editorFile = new File(editorDir + "/" + FILE_NAME);
		if (!editorFile.exists())
		{
			try {
				editorFile.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		createFromFile(editorFile);
	}

	private void createFromFile(final File file)
	{
		try {
			Scanner in = new Scanner(file);
			while(in.hasNext())
			{
				String[] tokens = in.next().split(SPLIT);
				switch(tokens[0])
				{
				case CUR_PROJECT_TOKEN -> {
					projectDir = new File(tokens[1]);
				}
				case HANDLE_SIZE_TOKEN -> {
					handleSize = Integer.parseInt(tokens[1]);
				}
				case POS_HANDLE_OFFSET_TOKEN -> {
					posHandleOffset = Integer.parseInt(tokens[1]);
				}
				case ROT_HANDLE_OFFSET_TOKEN -> {
					rotHandleOffset = Integer.parseInt(tokens[1]);
				}
				default -> {
					break;
				}
				}
				
			}
			in.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
	}
	
	public void saveToFile()
	{
		try {
			PrintWriter out = new PrintWriter(editorDir + "/" + FILE_NAME);
			out.print(""); // Clear the file
			if (projectDir != null)
			{
				out.print(CUR_PROJECT_TOKEN + SPLIT + projectDir.toString() + "\n");
			}
			out.print(HANDLE_SIZE_TOKEN + SPLIT + handleSize + "\n");
			out.print(POS_HANDLE_OFFSET_TOKEN + SPLIT + posHandleOffset + "\n");
			out.print(ROT_HANDLE_OFFSET_TOKEN + SPLIT + rotHandleOffset + "\n");
			out.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}
	
	public File getProjectDirectory()
	{
		return projectDir;
	}
	public void setProjectDirectory(final File dir)
	{
		projectDir = dir;
	}
	public int getHandleSize() {
		return handleSize;
	}
	public void setHandleSize(final int size) {
		handleSize = size;
	}
	public int getRotHandleOffset() {
		return rotHandleOffset;
	}
	public void setRotHandleOffset(final int rotHandleOffset) {
		this.rotHandleOffset = rotHandleOffset;
	}
	public int getPosHandleOffset() {
		return posHandleOffset;
	}
	public void setPosHandleOffset(final int posHandleOffset) {
		this.posHandleOffset = posHandleOffset;
	}
	public Color getSelectedHandleColor() {
		return selectedHandleColor;
	}
	public void setSelectedHandleColor(final Color selectedHandleColor) {
		this.selectedHandleColor = selectedHandleColor;
	}
	public Color getUnselectedHandleColor() {
		return unselectedHandleColor;
	}
	public void setUnselectedHandleColor(final Color unselectedHandleColor) {
		this.unselectedHandleColor = unselectedHandleColor;
	}
}
