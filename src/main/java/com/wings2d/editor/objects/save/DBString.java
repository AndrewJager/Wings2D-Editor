package com.wings2d.editor.objects.save;

public class DBString extends DBValue<String>{
	public DBString(final String column) {
		super(column);
	}
	
	@Override
	public String asString() {
		return value;
	}
	
	@Override
	public void fromString(final String str) {
		value = str;
	}
}
