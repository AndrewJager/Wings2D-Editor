package com.wings2d.editor.objects;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.wings2d.editor.objects.skeleton.DrawMode;

public interface Drawable {
	public void draw(final Graphics2D g2d, final double scale, final DrawMode mode, final EditorSettings settings);
	public Dimension getDrawSize(final double scale);
	public void drawRender(final Graphics2D g2d, final double scale);
	
	public static final int DRAW_PADDING = 10;
}
