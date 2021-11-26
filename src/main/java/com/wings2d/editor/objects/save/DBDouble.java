package com.wings2d.editor.objects.save;

public class DBDouble extends DBValue<Double>{

	public DBDouble(final String column) {
		super(column);
	}

	@Override
	public String asString() {
		return value.toString();
	}

	@Override
	public void fromString(String str) {
		value = Double.parseDouble(str);	
	}
}
