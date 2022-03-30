package com.wings2d.editor.objects.save;

public class DBDouble extends DBValue<Double>{

	public DBDouble(final String column) {
		super(column);
	}

	@Override
	public String asString() {
		return nullSafeFromString();
	}

	@Override
	public void fromString(String str) {
		if (str != null && !str.equals("")) {
			value = Double.parseDouble(str);	
		}
		else {
			value = null;
		}
	}
}
