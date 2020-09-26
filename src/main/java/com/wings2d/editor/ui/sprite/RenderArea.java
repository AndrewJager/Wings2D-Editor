package com.wings2d.editor.ui.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;

import com.wings2d.editor.objects.EditorFrame;
import com.wings2d.editor.objects.EditorJoint;
import com.wings2d.editor.ui.Editor;

public class RenderArea extends SpriteUIElement{
	private boolean shouldReRender;
	
	public RenderArea(Editor edit, Rectangle bounds)
	{
		super(edit, bounds);
		panel.setBackground(Color.WHITE);
		
		setShouldReRender(false);
	}
	
	public void updateRender(boolean advanceFrame)
	{
		EditorFrame curFrame = editor.getSpriteEdit().getAnimLists().getSelectedFrame();
		AnimationLists ani = editor.getSpriteEdit().getAnimLists();
		if (getShouldReRender())
		{
			Graphics2D g2d = (Graphics2D)panel.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 150, 150);
			List<EditorJoint> objects = curFrame.getJoints();
			for (int i = 0; i < objects.size(); i++)
			{
				double scale = editor.getDemoLevel().getManager().getScale() * editor.RENDER_SCALE_TO_ACTUAL;
				objects.get(i).makeImage();
				g2d.translate(objects.get(i).getX() * scale * 0.25, objects.get(i).getY() * scale * 0.25);
				objects.get(i).getImage().render(g2d, false);
				g2d.translate(-objects.get(i).getX() * scale * 0.25, -objects.get(i).getY() * scale * 0.25);
			}
			setShouldReRender(false);
		}
		
		if (advanceFrame)
		{
		curFrame.setTimePassed(curFrame.getTimePassed() + editor.getTimeStep());
			if (curFrame.getTimePassed() >= curFrame.getFrameTime())
			{
				curFrame.setTimePassed(0);
				// Go to next frame
				if (ani.getFrameList().getSelectedIndex() >= ani.getFrameList().getModel().getSize() - 1)
				{
					ani.getFrameList().setSelectedIndex(0);
				}
				else
				{
					ani.getFrameList().setSelectedIndex(ani.getFrameList().getSelectedIndex() + 1);
				}
				setShouldReRender(true);
			}
		}
	}
	
	public void createEvents()
	{
		
	}

	/**
	 * @return If the render will be updated the next time the timer calls RenderArea.updateRender
	 */
	public boolean getShouldReRender() {
		return shouldReRender;
	}

	/**
	 * @param Set if the render will be updated the next time the timer calls RenderArea.updateRender
	 */
	public void setShouldReRender(boolean shouldReRender) {
		this.shouldReRender = shouldReRender;
	}
}
