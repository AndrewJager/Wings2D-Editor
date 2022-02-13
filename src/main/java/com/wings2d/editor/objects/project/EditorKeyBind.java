package com.wings2d.editor.objects.project;

import java.sql.Connection;
import java.util.UUID;

import com.wings2d.editor.objects.save.DBString;
import com.wings2d.editor.objects.skeleton.DBObject;
import com.wings2d.framework.misc.ActionBinding;

public class EditorKeyBind extends DBObject{
	public static final String TABLE_NAME = "KEYBIND";
	
	private DBString keys;
	private ActionBinding binding;

	public EditorKeyBind(final Connection con, final UUID thisID) {
		super(TABLE_NAME);

		fields.add(keys = new DBString("Keys"));
		
		this.query(con, thisID);
		
		binding = new ActionBinding(keys.getStoredValue());
	}
	
	public String getKeys() {
		return keys.getStoredValue();
	}
	
	public void setKeys(final String newKeys) {
		keys.setStoredValue(newKeys);
	}
	
	public ActionBinding getBinding() {
		return binding;
	}

	@Override
	protected void deleteChildren(Connection con) {}
	@Override
	protected void queryChildren(UUID id, Connection con) {}
	@Override
	protected void updateChildren(Connection con) {}
}
