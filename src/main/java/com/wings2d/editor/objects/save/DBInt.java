package com.wings2d.editor.objects.save;

public class DBInt extends DBValue<Integer>{
	public DBInt(final String column) {
		super(column);
	}

	@Override
	public String asString() {
		return value.toString();
	}

	@Override
	public void fromString(String str) {
		value = Integer.parseInt(str);
	}
}
