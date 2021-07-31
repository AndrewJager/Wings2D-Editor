package com.wings2d.editor.objects.save;

import java.io.File;

/**
 * Writes/reads a path to a file
 */
public class WritableFile extends Writable{
	private File value;

	public WritableFile(final File value, final String token) {
		super(token);
		this.value = value;
	}
	
	public File getValue() {
		return value;
	}
	public void setValue(final File value) {
		this.value = value;
	}

	@Override
	public void setFromToken(String token) {
		this.value = new File(token);
	}

	@Override
	protected String getSaveData() {
		return value.toString();
	}
}
