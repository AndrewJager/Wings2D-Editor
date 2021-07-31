package com.wings2d.editor.objects.save;

public class WritableInt extends Writable{
	private Integer value;
	
	public WritableInt(final int value, final String token) {
		super(token);
		this.value = value;
	}
	
	public void setFromToken(final String token) {
		value = Integer.parseInt(token);
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(final int value) {
		this.value = value;
	}
	public String getSaveData() {
		return value.toString();
	}
}
