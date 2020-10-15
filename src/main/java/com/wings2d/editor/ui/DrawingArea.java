package com.wings2d.editor.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.wings2d.editor.objects.Drawable;

public class DrawingArea extends JPanel{
	public enum DrawType{
		DRAW,
		RENDER,
	}
	
	private static final long serialVersionUID = 1L;
	private Drawable drawItem;
	private Editor edit;
	private double zoomScale;
	private DrawType drawType;
	
	public DrawingArea(final Editor editor, final DrawType type)
	{
		edit = editor;
		zoomScale = 1;
		drawType = type;
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);
		if (drawItem != null)
		{
			switch(drawType)
			{
			case DRAW:
				drawItem.draw((Graphics2D)g, edit.getUIScale() * zoomScale, edit.getSkeletonEdit().getDrawMode());
				break;
			case RENDER:
				drawItem.drawRender((Graphics2D)g, edit.getUIScale() * zoomScale);
				break;
			}
		}
	}
	
	public void setDrawItem(final Drawable draw)
	{
		drawItem = draw;
		repaint();
	}
	
	public void resizeToDrawItem(final double uiScale)
	{
		if(drawItem != null)
		{
			Dimension drawDim = drawItem.getDrawSize(uiScale * zoomScale);
			Dimension newSize = new Dimension((int)drawDim.getWidth(), (int)drawDim.getHeight());
			this.setPreferredSize(newSize);
			this.revalidate();
		}
	}
	
	public void zoom(final int amt)
	{
		double zoomAmt = -amt * 0.1;
		zoomScale = zoomScale + zoomAmt;
		
		if (zoomScale < 0.1)
		{
			zoomScale = 0.1;
		}
		this.repaint();
	}
	
	public double getZoomScale()
	{
		return zoomScale;
	}
}
