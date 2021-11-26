package com.wings2d.editor.objects.save;

public class DBBoolean extends DBValue<Boolean>{

	public DBBoolean(final String column) {
		super(column);
	}

	@Override
	public String asString() {
		return value.toString();
	}

	@Override
	public void fromString(String str) {
		value = Boolean.parseBoolean(str);
	}
}
