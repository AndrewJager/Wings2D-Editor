package com.wings2d.editor.objects.save;

import java.util.UUID;

/** Represents a field in the database */
public abstract class DBValue<T> {
	protected String column;
	protected T value;
	
	public DBValue(final String column) {
		this.column = column;
	}
	
	public T getStoredValue() {
		return value;
	}
	public void setStoredValue(final T newValue) {
		value = newValue;
	}
	public String getColumn() {
		return column;
	}
	
	public abstract String asString();
	public abstract void fromString(final String str);
	
	public static UUID getUUID(final DBValue<String> value) {
		if ((value.getStoredValue() != null) && (!value.getStoredValue().equals("null"))) {
			return UUID.fromString(value.getStoredValue());
		}
		else {
			return null;
		}
	}
}
