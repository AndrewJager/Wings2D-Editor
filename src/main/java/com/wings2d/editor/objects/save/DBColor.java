package com.wings2d.editor.objects.save;

import java.awt.Color;

public class DBColor extends DBValue<Color>{
	public static class ColorUtils {
		public static String toString(final Color col) {
			return col.getRed() + ","
					+ col.getGreen() + ","
					+ col.getBlue() + ","
					+ col.getAlpha();
		}
		public static Color fromString(final String str) {
			String[] tokens = str.split(",");
			return new Color(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
		}
	}

	public DBColor(final String column) {
		super(column);
	}

	@Override
	public String asString() {
		return ColorUtils.toString(value);
	}

	@Override
	public void fromString(String str) {
		value = ColorUtils.fromString(str);
	}
}
