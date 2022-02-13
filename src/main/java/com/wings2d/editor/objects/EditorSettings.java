package com.wings2d.editor.objects;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import com.wings2d.editor.objects.project.EditorKeyBind;
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
	private DBInt defaultTime;
	
	private HashMap<String, EditorKeyBind> keyBinds;
	
	public EditorSettings(final Connection con)
	{
		super(TABLE_NAME, false);
		fields.add(handleSize = new DBInt("HandleSize"));
		fields.add(posHandleOffset = new DBInt("PosHandleOffset"));
		fields.add(rotHandleOffset = new DBInt("RotHandleOffset"));
		fields.add(selectedHandleColor = new DBColor("SelectedHandleColor"));
		fields.add(unselectedHandleColor = new DBColor("UnselectedHandleColor"));
		fields.add(projectID = new DBString("SelectedProject"));
		fields.add(defaultTime = new DBInt("DefaultTime"));
		keyBinds = new HashMap<String, EditorKeyBind>();
		
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
	public int getDefaultTime() {
		return defaultTime.getStoredValue();
	}
	public void setDefaultTime(final int defaultTime) {
		this.defaultTime.setStoredValue(defaultTime);
	}
	public String getProjectID() {
		return this.projectID.getStoredValue();
	}
	public HashMap<String, EditorKeyBind> getKeyBinds() {
		return keyBinds;
	}

	@Override
	protected void deleteChildren(final Connection con) {	
	}

	@Override
	protected void queryChildren(final UUID id, final Connection con) {
		String sql = " SELECT * FROM " + EditorKeyBind.TABLE_NAME;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				keyBinds.put(rs.getString("Name"), new EditorKeyBind(con, UUID.fromString(rs.getString("ID"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void updateChildren(final Connection con) {
		for (EditorKeyBind keyBind : keyBinds.values()) {
		    keyBind.update(con);
		}
	}
}
