package com.wings2d.editor.objects.save;

import java.util.UUID;

public class DBUUID extends DBValue<UUID>{
	public DBUUID(final String column) {
		super(column);
	}
	
	@Override
	public String asString() {
		if (value == null) {
			return "";
		}
		else {
			return value.toString();
		}
	}
	
	@Override
	public void fromString(final String str) {
		if (str.isEmpty() || str.equals("null")) {
			value = null;
		}
		else {
			value = UUID.fromString(str);
		}
	}
}