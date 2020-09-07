package editor.objects;

import java.awt.Dimension;
import java.awt.Graphics2D;

public interface Drawable {
	public void draw(Graphics2D g2d, double scale);
	public Dimension getDrawSize(double scale);
	
	public static final int DRAW_PADDING = 10;
}
