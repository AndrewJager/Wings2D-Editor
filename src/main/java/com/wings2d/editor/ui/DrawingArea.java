package com.wings2d.editor.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

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
	private Point2D userLoc;
	private int CIRCLE_SIZE = 10;
	private int LINE_LENGTH = 20;
	
	public DrawingArea(final Editor editor, final DrawType type)
	{
		edit = editor;
		zoomScale = 1;
		drawType = type;
		userLoc = new Point2D.Double(10, 10);
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
				drawItem.draw((Graphics2D)g, edit.getUIScale() * zoomScale, edit.getSkeletonEdit().getDrawMode(), edit.getSettings());
				break;
			case RENDER:
				drawItem.drawRender((Graphics2D)g, edit.getUIScale() * zoomScale);
				break;
			}
		}
		
		if (drawType == DrawType.DRAW)
		{
			double scale = edit.getUIScale() * zoomScale; 
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(1));
			g2d.drawArc((int)(userLoc.getX() * scale - (CIRCLE_SIZE / 2)), (int)(userLoc.getY() * scale - (CIRCLE_SIZE / 2)),
					CIRCLE_SIZE, CIRCLE_SIZE, 0, 360);
			g2d.drawLine((int)(userLoc.getX()  * scale), (int)(userLoc.getY() * scale - (LINE_LENGTH / 2)), (int)(userLoc.getX() * scale),
					(int)(userLoc.getY() * scale + (LINE_LENGTH / 2)));
			g2d.drawLine((int)(userLoc.getX() * scale - (LINE_LENGTH / 2)), (int)(userLoc.getY() * scale),
					(int)(userLoc.getX() * scale + (LINE_LENGTH / 2)), (int)(userLoc.getY() * scale));
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
	public double getScale()
	{
		return edit.getUIScale() * zoomScale;
	}
	
	public void setUserLoc(final Point2D point)
	{
		double scale = edit.getUIScale() * zoomScale;
		double unscale = 1.0 / scale;
		userLoc = new Point2D.Double(point.getX() * unscale, point.getY() * unscale);
	}
	
	public Point2D getUserLoc()
	{
		return userLoc;
	}
}
