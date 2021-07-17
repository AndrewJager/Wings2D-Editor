package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.ui.DrawingArea;

public class RenderArea extends SkeletonUIElement{
	private DrawingArea drawArea;
	private JScrollPane pane;

	public RenderArea(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		drawArea = new DrawingArea(edit.getEditor(), DrawingArea.DrawType.RENDER);
		pane = new JScrollPane(drawArea);
		panel.setLayout(new GridLayout(0,1));
		panel.add(pane);
	}
	
	public void drawRender(final SkeletonFrame frame, final double scale)
	{
		if (drawArea != null)
		{
			drawArea.repaint();
		}
	}
	
	public DrawingArea getDrawArea()
	{
		return drawArea;
	}

	@Override
	public void createEvents() {
		
	}
}
