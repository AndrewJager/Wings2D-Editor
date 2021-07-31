package com.wings2d.editor.objects.save;

public abstract class Writable {
	protected String token;
	
	public Writable(final String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	
	public abstract void setFromToken(final String token);
	protected abstract String getSaveData();
}
