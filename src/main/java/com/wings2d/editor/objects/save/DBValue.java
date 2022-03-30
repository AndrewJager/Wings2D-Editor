package com.wings2d.editor.objects.save;

/** Represents a field in the database */
public abstract class DBValue<T> {
	/** Name of the column in the database */
	protected String column;
	/** Cached value */
	protected T value;
	
	/** 
	 * Base constructor 
	 * @param column Name of the database column
	 */
	public DBValue(final String column) {
		this.column = column;
	}
	
	/** @return The cached value. */
	public T getStoredValue() {
		return value;
	}
	/**
	 * Only updates the cached value. Does not push to the database.
	 * @param newValue Value to set the cache to
	 */
	public void setStoredValue(final T newValue) {
		value = newValue;
	}
	/** @return The name of the column */
	public String getColumn() {
		return column;
	}
	
	/** @return The value as a string */
	public abstract String asString();
	/** 
	 * Sets the cached value from a string
	 * @param str String to use to set the value 
	 */
	public abstract void fromString(final String str);
	
	protected String nullSafeFromString() {
		if (value == null) {
			return "";
		}
		else {
			return value.toString();
		}
	}
}
