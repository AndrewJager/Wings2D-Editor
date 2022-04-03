package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.path.DrawingLogic;
import com.wings2d.editor.ui.DrawingArea;
import com.wings2d.editor.ui.UIElement;

public class SkeletonDrawingPanel extends UIElement<SkeletonEdit>{

	
	private DrawingArea drawArea;
	private JScrollPane pane;
	
	private DrawingLogic logic;

	public SkeletonDrawingPanel(final SkeletonEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setPreferredSize(new Dimension(400, 400));

		drawArea = new DrawingArea(edit.getEditor(), DrawingArea.DrawType.DRAW);
		pane = new JScrollPane(drawArea);
		panel.setLayout(new GridLayout(0,1));
		panel.add(pane);
		
		logic = new DrawingLogic(getEditPanel(), drawArea);
	}
	public void setSelectedFrame(final SkeletonFrame f)
	{
		logic.setFrame(f);
	}
	public DrawingArea getDrawArea()
	{
		return drawArea;
	}

	@Override
	public void createEvents() {
		drawArea.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
				{
					if (getEditPanel().getDrawMode().getSuperMode() == DrawMode.SuperDrawMode.BONE)
					{
						if (getEditPanel().getDrawMode() == DrawMode.BONE_ROTATE)
						{
							getEditPanel().setDrawMode(DrawMode.BONE_MOVE);
						}
						else
						{
							getEditPanel().setDrawMode(DrawMode.BONE_ROTATE);
						}
					}
					else if (getEditPanel().getDrawMode().getSuperMode() == DrawMode.SuperDrawMode.SPRITE)
					{
						if (getEditPanel().getDrawMode() == DrawMode.SPRITE_MOVE)
						{
							getEditPanel().setDrawMode(DrawMode.SPRITE_EDIT);
						}
						else if (getEditPanel().getDrawMode() == DrawMode.SPRITE_EDIT)
						{
							getEditPanel().setDrawMode(DrawMode.SPRITE_MOVE);
						}
					}
				}
				else if (SwingUtilities.isMiddleMouseButton(e))
				{	
					if (getEditPanel().getDrawMode().getSuperMode() == DrawMode.SuperDrawMode.BONE)
					{
						getEditPanel().setDrawMode(getEditPanel().getLastSpriteDrawMode());
					}
					else if (getEditPanel().getDrawMode().getSuperMode() == DrawMode.SuperDrawMode.SPRITE)
					{
						getEditPanel().setDrawMode(getEditPanel().getLastBoneDrawMode());
					}
				}
				else if (SwingUtilities.isLeftMouseButton(e)) {
					logic.processPressed(e);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					logic.processRelease(e);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}	
		});
		drawArea.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					logic.processDragged(e);
					
					drawArea.resizeToDrawItem(getEditPanel().getEditor().getUIScale());
					panel.repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {}
		});
		drawArea.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				drawArea.zoom(e.getWheelRotation());
				drawArea.resizeToDrawItem(getEditPanel().getEditor().getUIScale());
			}
		});
	}
}
