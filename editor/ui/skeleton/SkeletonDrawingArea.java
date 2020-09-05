package editor.ui.skeleton;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import editor.objects.skeleton.SkeletonBone;
import editor.objects.skeleton.SkeletonFrame;
import editor.ui.DrawingArea;

public class SkeletonDrawingArea extends SkeletonUIElement{

	public SkeletonDrawingArea(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);

		// Replace panel with drawing area
		panel = new DrawingArea();
	}
	
	public DrawingArea getDrawArea()
	{
		return (DrawingArea)panel;
	}

	@Override
	public void createEvents() {
		
	}
}
