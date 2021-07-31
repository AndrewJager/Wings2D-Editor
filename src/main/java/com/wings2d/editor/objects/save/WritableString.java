package com.wings2d.editor.objects.save;

public class WritableString extends Writable{
	private String value;

	public WritableString(final String value, final String token) {
		super(token);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public void setFromToken(String token) {
		this.value = token;
	}

	@Override
	protected String getSaveData() {
		return value;
	}
}
