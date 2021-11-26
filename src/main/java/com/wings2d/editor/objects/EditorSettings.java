package com.wings2d.editor.objects;

import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.save.DBColor;
import com.wings2d.editor.objects.save.DBInt;
import com.wings2d.editor.objects.save.DBString;
import com.wings2d.editor.objects.skeleton.DBObject;
public class EditorSettings extends DBObject{
	public static final String TABLE_NAME = "EDITORSETTINGS";
	
	private DBString projectID;
	private Project selectedProj;
	
	private DBInt handleSize;
	private DBInt posHandleOffset;
	private DBInt rotHandleOffset;
	private DBColor selectedHandleColor;
	private DBColor unselectedHandleColor;
	
	
	public EditorSettings(final Connection con)
	{
		super(TABLE_NAME, false);
		fields.add(handleSize = new DBInt("HandleSize"));
		fields.add(posHandleOffset = new DBInt("PosHandleOffset"));
		fields.add(rotHandleOffset = new DBInt("RotHandleOffset"));
		fields.add(selectedHandleColor = new DBColor("SelectedHandleColor"));
		fields.add(unselectedHandleColor = new DBColor("UnselectedHandleColor"));
		fields.add(projectID = new DBString("SelectedProject"));
		
		this.querySingleRecord(con);
//		selectedProj = new Project(projectLink.getValue(), con);
	}
	
	public int getHandleSize() {
		return handleSize.getStoredValue();
	}
	public void setHandleSize(final int size) {
		handleSize.setStoredValue(size);
	}
	public int getRotHandleOffset() {
		return rotHandleOffset.getStoredValue();
	}
	public void setRotHandleOffset(final int rotHandleOffset) {
		this.rotHandleOffset.setStoredValue(rotHandleOffset);
	}
	public int getPosHandleOffset() {
		return posHandleOffset.getStoredValue();
	}
	public void setPosHandleOffset(final int posHandleOffset) {
		this.posHandleOffset.setStoredValue(posHandleOffset);
	}
	public Color getSelectedHandleColor() {
		return selectedHandleColor.getStoredValue();
	}
	public void setSelectedHandleColor(final Color selectedHandleColor) {
		this.selectedHandleColor.setStoredValue(selectedHandleColor);
	}
	public Color getUnselectedHandleColor() {
		return unselectedHandleColor.getStoredValue();
	}
	public void setUnselectedHandleColor(final Color unselectedHandleColor) {
		this.unselectedHandleColor.setStoredValue(unselectedHandleColor);;
	}
	public String getProjectID() {
		return this.projectID.getStoredValue();
	}

	@Override
	protected void deleteChildren(final String id, final Connection con) {
		
		
	}

	@Override
	protected void queryChildren(final String id, final Connection con) {
		
	}
}
