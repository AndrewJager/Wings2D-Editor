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
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.DrawingArea;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonDrawingArea extends SkeletonUIElement{
	private DrawingArea drawArea;
	private JScrollPane pane;
	private JTree tree;
	private SkeletonFrame frame;
	private SkeletonNode selectedItem;
	private SkeletonTreeControls controls;

	public SkeletonDrawingArea(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		drawArea = new DrawingArea(edit.getEditor(), DrawingArea.DrawType.DRAW);
		pane = new JScrollPane(drawArea);
		panel.setLayout(new GridLayout(0,1));
		panel.add(pane);
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
				if (frame != null)
				{
					if (SwingUtilities.isRightMouseButton(e))
					{
						if (skeleton.getDrawMode().getSuperMode() == DrawMode.SuperDrawMode.BONE)
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
						else if (skeleton.getDrawMode().getSuperMode() == DrawMode.SuperDrawMode.SPRITE)
						{
							if (skeleton.getDrawMode() == DrawMode.SPRITE_MOVE)
							{
								skeleton.setDrawMode(DrawMode.SPRITE_EDIT);
							}
							else if (skeleton.getDrawMode() == DrawMode.SPRITE_EDIT)
							{
								skeleton.setDrawMode(DrawMode.SPRITE_MOVE);
							}
						}
					}
					else if (SwingUtilities.isMiddleMouseButton(e))
					{	
						if (skeleton.getDrawMode().getSuperMode() == DrawMode.SuperDrawMode.BONE)
						{
							skeleton.setDrawMode(skeleton.getLastSpriteDrawMode());
						}
						else if (skeleton.getDrawMode().getSuperMode() == DrawMode.SuperDrawMode.SPRITE)
						{
							skeleton.setDrawMode(skeleton.getLastBoneDrawMode());
						}
					}

					double scale = skeleton.getEditor().getUIScale() * drawArea.getZoomScale();
					if (skeleton.getDrawMode() == DrawMode.BONE_MOVE)
					{
						selectedItem = frame.getBoneAtPosition(e.getPoint(), scale);
					}
					if (selectedItem == null && skeleton.getDrawMode() == DrawMode.BONE_ROTATE)
					{
						selectedItem = frame.getBoneByRotHandle(e.getPoint(), scale);
					}
					else if (selectedItem ==  null && skeleton.getDrawMode() == DrawMode.SPRITE_MOVE)
					{
						selectedItem = frame.selectSprite(e.getPoint(), scale);
					}
					else if (selectedItem ==  null && skeleton.getDrawMode() == DrawMode.SPRITE_EDIT)
					{
						selectedItem = frame.selectSpriteVertex(e.getPoint(), scale);
					}
					
					if (selectedItem != null)
					{
						TreePath path = new TreePath(frame);
						path = path.pathByAddingChild(selectedItem);
						tree.setSelectionPath(path);
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (selectedItem != null)
				{
					selectedItem = null;
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
				if (selectedItem != null && SwingUtilities.isLeftMouseButton(e))
				{
					double scale = skeleton.getEditor().getUIScale() * drawArea.getZoomScale();
					switch(skeleton.getDrawMode())
					{
					case BONE_MOVE:
						SkeletonBone bone = (SkeletonBone)selectedItem;
						bone.setLocation(e.getPoint(), scale, true);
						break;
					case BONE_ROTATE:
						bone = (SkeletonBone)selectedItem;
						bone.rotateByHandle(e.getPoint(), scale);
						break;
					case SPRITE_MOVE:
						Sprite sprite = (Sprite)selectedItem;
						sprite.setLocation(e.getPoint(), scale);
						break;
					case SPRITE_EDIT:
						sprite = (Sprite)selectedItem;
						if (sprite.getSelectedVertex() != -1)
						{
							sprite.setVertexLocation(e.getPoint(), scale);
						}
						break;
					}
				}
				
				controls.updateNodeInfo(selectedItem);
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
