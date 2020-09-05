package editor.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import editor.objects.Drawable;
import editor.objects.skeleton.SkeletonBone;
import editor.objects.skeleton.SkeletonFrame;

public class DrawingArea extends JPanel{
	private static final long serialVersionUID = 1L;
	private Drawable drawItem;
	private Graphics2D g2d;
	
	public DrawingArea()
	{
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (drawItem != null)
		{
			drawItem.draw((Graphics2D)g);
		}
	}
	
	public void setDrawItem(Drawable draw)
	{
		drawItem = draw;
		repaint();
	}
}
