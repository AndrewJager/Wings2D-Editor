package com.wings2d.editor.objects;

import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.save.DBColor;
import com.wings2d.editor.objects.save.DBInt;
import com.wings2d.editor.objects.save.DBString;
public class EditorSettings {
	
	private DBString projectID;
	private Project selectedProj;
	
	private DBInt handleSize;
	private DBInt posHandleOffset;
	private DBInt rotHandleOffset;
	private DBColor selectedHandleColor;
	private DBColor unselectedHandleColor;
	
	
	public EditorSettings(final Connection con)
	{
		
		handleSize = new DBInt(con, "EDITORSETTINGS", "HandleSize");
		posHandleOffset = new DBInt(con, "EDITORSETTINGS", "PosHandleOffset");
		rotHandleOffset = new DBInt(con, "EDITORSETTINGS", "RotHandleOffset");
		selectedHandleColor = new DBColor(con, "EDITORSETTINGS", "SelectedHandleColor");
		unselectedHandleColor = new DBColor(con, "EDITORSETTINGS", "UnselectedHandleColor");
		projectID = new DBString(con, "EDITORSETTINGS", "SelectedProject");
//		selectedProj = new Project(projectLink.getValue(), con);
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
	public String getProjectID() {
		return this.projectID.getValue();
	}
}
