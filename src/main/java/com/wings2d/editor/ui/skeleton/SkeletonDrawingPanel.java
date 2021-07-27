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
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.DrawingArea;
import com.wings2d.editor.ui.UIElement;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonDrawingPanel extends UIElement<SkeletonEdit>{
	private enum MoveType {
		MOVE_BOTH,
		MOVE_X,
		MOVE_Y
	}
	
	private DrawingArea drawArea;
	private JScrollPane pane;
	private JTree tree;
	private SkeletonFrame frame;
	private SkeletonNode selectedItem;
	private SkeletonTreeControls treeControls;
	private MoveType moveType;

	public SkeletonDrawingPanel(final SkeletonEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setPreferredSize(new Dimension(400, 400));

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
	public void setTreeControls(final SkeletonTreeControls c)
	{
		treeControls = c;
	}

	@Override
	public void createEvents() {
		drawArea.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				if (frame != null)
				{
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

					double scale = getEditPanel().getEditor().getUIScale() * drawArea.getZoomScale();
					if (getEditPanel().getDrawMode() == DrawMode.BONE_MOVE)
					{
						selectedItem = frame.getBoneAtPosition(e.getPoint(), scale);
						if (selectedItem == null) {
							selectedItem = frame.getBoneByXHandle(e.getPoint(), scale);
							if (selectedItem == null) {
								selectedItem = frame.getBoneByYHandle(e.getPoint(), scale);
								if (selectedItem != null) {
									moveType = MoveType.MOVE_Y;
								}
							}
							else {
								moveType = MoveType.MOVE_X;
							}
						}
						else {
							moveType = MoveType.MOVE_BOTH;
						}
					}
					if (selectedItem == null && getEditPanel().getDrawMode() == DrawMode.BONE_ROTATE)
					{
						selectedItem = frame.getBoneByRotHandle(e.getPoint(), scale);
					}
					else if (selectedItem ==  null && getEditPanel().getDrawMode() == DrawMode.SPRITE_MOVE)
					{
						selectedItem = frame.selectSprite(e.getPoint(), scale);
					}
					else if (selectedItem ==  null && getEditPanel().getDrawMode() == DrawMode.SPRITE_EDIT)
					{
						selectedItem = frame.selectSpriteVertex(e.getPoint(), scale);
					}
					
					if (selectedItem != null)
					{
						TreePath path = new TreePath(frame);
						path = path.pathByAddingChild(selectedItem);
						tree.setSelectionPath(path);
					}
					else
					{
						drawArea.setUserLoc(e.getPoint());
						drawArea.repaint();
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
					double scale = getEditPanel().getEditor().getUIScale() * drawArea.getZoomScale();
					switch(getEditPanel().getDrawMode())
					{
					case BONE_MOVE:
						SkeletonBone bone = (SkeletonBone)selectedItem;
						switch(moveType) {
							case MOVE_BOTH -> {
								bone.setLocation(e.getPoint(), scale, true);
							}
							case MOVE_X -> {
								bone.setLocation(e.getPoint().getX(), bone.getY() * scale, scale, true);
							}
							case MOVE_Y -> {
								bone.setLocation(bone.getX() * scale, e.getPoint().getY(), scale, true);
							}
						}
						
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
				
				treeControls.updateNodeInfo(selectedItem);
				drawArea.resizeToDrawItem(getEditPanel().getEditor().getUIScale());
				panel.repaint();
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
