package com.wings2d.editor.objects.save;

import java.awt.Color;

public class WritableColor extends Writable{
	private Color value;
	private static final String TOKEN = ":";
	
	public WritableColor(final Color value, final String token) {
		super(token);
		this.value = value;
	}
	
	public void setFromToken(final String token) {
		String[] tokens = token.split(TOKEN);
		value = new Color(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
	}
	
	public Color getValue() {
		return value;
	}
	public void setValue(final Color value) {
		this.value = value;
	}
	public String getSaveData() {
		return value.getRed() + TOKEN + value.getGreen() + TOKEN + value.getBlue();
	}
}
