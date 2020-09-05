package editor.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import editor.objects.skeleton.SkeletonBone;
import editor.objects.skeleton.SkeletonFrame;

public class DrawingArea extends JPanel{
	private static final long serialVersionUID = 1L;
	private SkeletonFrame drawFrame;
	private Graphics2D g2d;
	
	public DrawingArea()
	{
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g2d = (Graphics2D)g;
		if (drawFrame != null)
		{
			for(int i = 0; i < drawFrame.getBones().size(); i++)
			{
				drawBone(drawFrame.getBones().get(i));
			}
		}
	}
	
	private void drawBone(SkeletonBone bone)
	{
		g2d.setColor(Color.GREEN);
		g2d.drawRect((int)bone.getLocation().getX(), (int)bone.getLocation().getY(), 10, 10);
		System.out.println(bone.toString());
	}
	
	public void setDrawFrame(SkeletonFrame frame)
	{
		drawFrame = frame;
	}
}
