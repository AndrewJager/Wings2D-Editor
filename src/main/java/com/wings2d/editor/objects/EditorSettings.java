package com.wings2d.editor.objects;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.wings2d.editor.objects.save.SaveData;
import com.wings2d.editor.objects.save.WritableColor;
import com.wings2d.editor.objects.save.WritableFile;
import com.wings2d.editor.objects.save.WritableInt;

public class EditorSettings {
	private File editorDir;
	
	private WritableFile projectDir;
	
	private SaveData data;
	
	private WritableInt handleSize;
	private WritableInt posHandleOffset;
	private WritableInt rotHandleOffset;
	private WritableColor selectedHandleColor;
	private WritableColor unselectedHandleColor;
	
	private static final String FILE_NAME = "EDITORSETTINGS.txt";
	private static final String SPLIT = ">"; // Don't use ":" due to it being in the path
	
	public EditorSettings()
	{
		data = new SaveData(SPLIT);
		
		// Defaults
		handleSize = data.add(new WritableInt(10, "HANDLE_SIZE"));
		posHandleOffset = data.add(new WritableInt(20, "POS_HANDLE_OFFSET"));
		rotHandleOffset = data.add(new WritableInt(20, "ROT_HANDLE_OFFSET"));
		projectDir = data.add(new WritableFile(null, "DIR"));

		selectedHandleColor = data.add(new WritableColor(Color.RED, "SELECTED_HANDLE_COLOR"));
		unselectedHandleColor = data.add(new WritableColor(Color.GREEN, "UNSELECTED_HANDLE_COLOR"));
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
			data.parseFile(in);
			in.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
	}
	
	public void saveToFile()
	{
		try {
			PrintWriter out = new PrintWriter(editorDir + "/" + FILE_NAME);
			data.saveData(out);
			out.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}
	
	public File getProjectDirectory()
	{
		return projectDir.getValue();
	}
	public void setProjectDirectory(final File dir)
	{
		projectDir.setValue(dir);
	}
	public int getHandleSize() {
		return handleSize.getValue();
	}
	public void setHandleSize(final int size) {
		handleSize.setValue(size);
	}
	public int getRotHandleOffset() {
		return rotHandleOffset.getValue();
	}
	public void setRotHandleOffset(final int rotHandleOffset) {
		this.rotHandleOffset.setValue(rotHandleOffset);
	}
	public int getPosHandleOffset() {
		return posHandleOffset.getValue();
	}
	public void setPosHandleOffset(final int posHandleOffset) {
		this.posHandleOffset.setValue(posHandleOffset);
	}
	public Color getSelectedHandleColor() {
		return selectedHandleColor.getValue();
	}
	public void setSelectedHandleColor(final Color selectedHandleColor) {
		this.selectedHandleColor.setValue(selectedHandleColor);
	}
	public Color getUnselectedHandleColor() {
		return unselectedHandleColor.getValue();
	}
	public void setUnselectedHandleColor(final Color unselectedHandleColor) {
		this.unselectedHandleColor.setValue(unselectedHandleColor);;
	}
}
