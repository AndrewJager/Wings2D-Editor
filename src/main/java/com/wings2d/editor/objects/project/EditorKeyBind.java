package com.wings2d.editor.objects.project;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.util.UUID;

import com.wings2d.editor.objects.save.DBBoolean;
import com.wings2d.editor.objects.save.DBInt;
import com.wings2d.editor.objects.skeleton.DBEditObject;
import com.wings2d.editor.objects.skeleton.DBObject;

public class EditorKeyBind extends DBEditObject{
	public static final String TABLE_NAME = "KEYBIND";
	
	private DBInt key;
	private DBBoolean ctrl;
	private DBBoolean shift;
	private DBBoolean alt;

	public EditorKeyBind(final Connection con, final UUID thisID) {
		super(TABLE_NAME);

		fields.add(key = new DBInt("Key"));
		fields.add(ctrl = new DBBoolean("Ctrl"));
		fields.add(shift = new DBBoolean("Shift"));
		fields.add(alt = new DBBoolean("Alt"));
		
		this.query(con, thisID);
	}
	
	public String getKey() {
		return KeyEvent.getKeyText(key.getStoredValue());
	}
	public int getKeyCode() {
		return key.getStoredValue();
	}
	public void setKey(final int newKey) {
		key.setStoredValue(newKey);
	}
	public Boolean getCtrl() {
		return ctrl.getStoredValue();
	}
	public void setCtrl(final boolean ctrl) {
		this.ctrl.setStoredValue(ctrl);
	}
	public Boolean getShift() {
		return shift.getStoredValue();
	}
	public void setShift(final boolean shift) {
		this.shift.setStoredValue(shift);
	}
	public Boolean getAlt() {
		return alt.getStoredValue();
	}
	public void setAlt(final boolean alt) {
		this.alt.setStoredValue(alt);
	}


	@Override
	protected void deleteChildren(Connection con) {}
	@Override
	protected void queryChildren(UUID id, Connection con) {}
	@Override
	protected void updateChildren(Connection con) {}
}
