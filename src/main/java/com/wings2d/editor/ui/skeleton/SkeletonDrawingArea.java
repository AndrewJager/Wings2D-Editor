package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.ui.DrawingArea;

public class SkeletonDrawingArea extends SkeletonUIElement{
	private DrawingArea drawArea;
	private JScrollPane pane;
	private JTree tree;
	private SkeletonFrame frame;
	private SkeletonBone selectedBone;
	private SkeletonTreeControls controls;

	public SkeletonDrawingArea(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Replace panel with drawing area
		drawArea = new DrawingArea(edit.getEditor());
		pane = new JScrollPane(drawArea);
		panel.setLayout(new GridLayout(0,1));
		panel.add(pane);
		panel.getAccessibleContext();
		tree = edit.getSkeletonTree().getTree();
	}
	public void setSelectedFrame(final SkeletonFrame f)
	{
		frame = f;
	}
	public DrawingArea getDrawArea()
	{
		return drawArea;
	}
	public void setControls(final SkeletonTreeControls c)
	{
		controls = c;
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
					if (skeleton.getDrawMode() == DrawMode.BONE_ROTATE)
					{
						skeleton.setDrawMode(DrawMode.BONE_MOVE);
					}
					else
					{
						skeleton.setDrawMode(DrawMode.BONE_ROTATE);
					}
				}
				selectedBone = frame.getBoneAtPosition(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale());
				if (selectedBone == null && skeleton.getDrawMode() == DrawMode.BONE_ROTATE)
				{
					selectedBone = frame.getBoneByRotHandle(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale());
				}
				
				if (selectedBone != null)
				{
					TreePath path = new TreePath(frame);
					path = path.pathByAddingChild(selectedBone);
					tree.setSelectionPath(path);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (selectedBone != null)
				{
					selectedBone = null;
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
				if (selectedBone != null && SwingUtilities.isLeftMouseButton(e))
				{
					switch(skeleton.getDrawMode())
					{
					case BONE_MOVE:
						selectedBone.setLocation(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale(), true);
						break;
					case BONE_ROTATE:
						selectedBone.rotateByHandle(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale());
						break;
					case SPRITE:
						break;
					}
				}
				
				controls.updateBoneInfo();
				drawArea.resizeToDrawItem(skeleton.getEditor().getUIScale());
				panel.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {}
		});
		drawArea.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				drawArea.zoom(e.getWheelRotation());
				drawArea.resizeToDrawItem(skeleton.getEditor().getUIScale());
			}
		});
	}
}
