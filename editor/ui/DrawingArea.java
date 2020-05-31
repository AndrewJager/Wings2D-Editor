package editor.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.List;

import framework.animation.Frame;
import framework.animation.Joint;

public class DrawingArea extends UIElement{
	private boolean shouldRedraw;
	
	public DrawingArea(Editor edit, Rectangle bounds)
	{
		super(edit, bounds);
		panel.setBackground(Color.WHITE);
		
		shouldRedraw = false;
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getAnimLists();
		panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            	if (ani.getIsObjectSelected())
            	{
            		Frame curFrame = ani.getSelectedFrame();
                	curFrame.processMousePress(ani.getObjectList().getSelectedIndex(), evt.getPoint());
            	}
                
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	if (ani.getIsObjectSelected())
            	{
            		Frame curFrame = ani.getSelectedFrame();
                	curFrame.processMouseRelease(curFrame.getJoint(ani.getObjectList().getSelectedIndex()).getName(), evt.getPoint());
    				shouldRedraw = true;
    				editor.getRender().setShouldReRender(true);
            	}
            }
        });
	}
	
	/**
	 * Should not be called directly, as this will be called by the timer periodicly.
	 * Use DrawingArea.SetShouldRedraw to indicate that the drawing should be updated
	 */
	public void updateDrawing()
	{
		if (shouldRedraw)
		{
			shouldRedraw = false;
			AnimationLists ani = editor.getAnimLists();
			Graphics2D g2d = (Graphics2D)panel.getGraphics();
			double scale = Double.valueOf(panel.getWidth()) / ogBounds.getWidth();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());
			g2d.scale(scale, scale);
			Frame curFrame = ani.getSelectedFrame();
			int objIndex = ani.getObjectList().getSelectedIndex();
			List<Joint> objects = curFrame.getJoints();
			for (int i = 0; i < objects.size(); i++)
			{
				Joint joint = objects.get(i);
				if (joint.getPath() != null)
				{
					Path2D path = joint.getPath();
					
					if (i == objIndex)
					{
						g2d.setColor(joint.getColor());
					}
					else
					{
						g2d.setColor(joint.getFadedColor());
					}
					g2d.translate(joint.getX(), joint.getY());
					g2d.fill(path);
					g2d.translate(-joint.getX(), -joint.getY());
					g2d.setColor(Color.RED);
			    	if (i == objIndex)
			    	{
						if (curFrame.getEditOptions().getEditing())
						{    
						    for (int j = 0; j < objects.get(i).getPoints().size(); j++)
						    {
						    	drawHandle(objects.get(i).getPoints().get(j).getX() + joint.getX(), objects.get(i).getPoints().get(j).getY() + joint.getY(), g2d);
						    }
						}
						else
						{
							drawHandle(path.getBounds2D().getCenterX() + joint.getX(), path.getBounds2D().getCenterY() + joint.getY(), g2d);
						}
						
						if (curFrame.getEditOptions().getRotating())
						{
							drawHandle(path.getBounds2D().getCenterX(), path.getBounds2D().getY(), g2d);
						}
			    	}
				}
			}
		}
	}
	private static void drawHandle(double x, double y, Graphics2D g2d)
	{
		Ellipse2D circle = new Ellipse2D.Double(x - 6, y - 6, 12, 12);
		g2d.draw(circle);
	}

	public boolean getShouldRedraw() {
		return shouldRedraw;
	}

	public void setShouldRedraw(boolean shouldRedraw) {
		this.shouldRedraw = shouldRedraw;
	}
}
