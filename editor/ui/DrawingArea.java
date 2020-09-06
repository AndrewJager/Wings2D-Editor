package editor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import editor.objects.Drawable;

public class DrawingArea extends JPanel{
	private static final long serialVersionUID = 1L;
	private Drawable drawItem;
	private Editor edit;
	
	public DrawingArea(Editor editor)
	{
		edit = editor;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (drawItem != null)
		{
			drawItem.draw((Graphics2D)g, edit.getUIScale());
		}
	}
	
	public void setDrawItem(Drawable draw)
	{
		drawItem = draw;
		repaint();
	}
	
	public void resizeToDrawItem()
	{
		if(drawItem != null)
		{
			Dimension drawDim = drawItem.getDrawSize();
			Dimension newSize = new Dimension(this.getPreferredSize());
			if (drawDim.getWidth() > newSize.getWidth())
			{
				newSize.setSize(drawDim.getWidth(), newSize.getHeight());
			}
			if (drawDim.getHeight() > newSize.getHeight())
			{
				newSize.setSize(newSize.getWidth(), drawDim.getHeight());
			}
			this.setPreferredSize(newSize);
			this.revalidate();
		}
	}
}
