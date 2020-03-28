package editor.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import framework.animation.Frame;
import framework.animation.Joint;

public class RenderArea {
	private Editor editor;
	private JPanel panel;
	
	public RenderArea(Editor edit)
	{
		editor = edit;
		panel = new JPanel();
		panel.setBounds(700, 10, 150, 150);
		panel.setBackground(Color.WHITE);
	}
	
	public void updateRender(boolean advanceFrame)
	{
		Frame curFrame = editor.getAnimLists().getSelectedFrame();
		AnimationLists ani = editor.getAnimLists();
		if (editor.getShouldReRender())
		{
			Graphics2D g2d = (Graphics2D)editor.getRender().getPanel().getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 150, 150);
			List<Joint> objects = curFrame.getJoints();
			for (int i = 0; i < objects.size(); i++)
			{
				objects.get(i).makeImage();
				g2d.translate(objects.get(i).getX() * 0.25, objects.get(i).getY() * 0.25);
				objects.get(i).getImage().render(g2d, false);
				g2d.translate(-objects.get(i).getX() * 0.25, -objects.get(i).getY() * 0.25);
			}
			editor.setShouldReRender(false);
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
				editor.setShouldReRender(true);
			}
		}
	}

	public JPanel getPanel()
	{
		return panel;
	}
}
