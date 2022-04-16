package com.wings2d.editor.objects.skeleton.path;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.DrawMode.MoveType;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.DrawingArea;
import com.wings2d.editor.ui.edits.SetBoneLocation;
import com.wings2d.editor.ui.edits.SetBoneRotation;
import com.wings2d.editor.ui.skeleton.SkeletonEdit;

public class DrawingLogic {
	private SkeletonFrame frame;
	private SkeletonEdit edit;
	private DrawingArea drawArea;
	private JTree tree;
	
	private DrawMode.MoveType moveType;
	private SkeletonNode item;
	private double curX, curY, curRot, offsetX, offsetY;
	private boolean snap = false;
	
	public DrawingLogic(final SkeletonEdit edit, final DrawingArea drawArea) {
		this.edit = edit;
		this.drawArea = drawArea;
		this.tree = edit.getSkeletonTree().getTree();
	}
	
	public void setFrame(final SkeletonFrame frame) {
		this.frame = frame;
	}
	public void setMode(final DrawMode mode) {
		
	}
	public void setSnap(final boolean snap) {
		this.snap = snap;
	}
	public boolean getSnap() {
		return snap;
	}
	
	public void processPressed(final MouseEvent e) {
		double scale = edit.getEditor().getUIScale() * drawArea.getZoomScale();
		
		if (frame != null) {
			switch (edit.getDrawMode()) {
				case BONE_MOVE -> {
					item = frame.getBoneAtPosition(e.getPoint(), scale);
					if (item != null) {
						moveType = MoveType.MOVE_BOTH;
					}
					else {
						item = frame.getBoneByXHandle(e.getPoint(), scale);
						if (item != null) {
							moveType = MoveType.MOVE_X;
						}
						else {
							item = frame.getBoneByYHandle(e.getPoint(), scale);
							if (item != null) {
								moveType = MoveType.MOVE_Y;
							}
						}
					}
					
					if (item != null) {
						SkeletonBone bone = (SkeletonBone)item;
						frame.setSelectedBone(bone);
						curX = bone.getX();
						curY = bone.getY();
					}
				}
				case BONE_ROTATE -> {
					item = frame.getBoneByRotHandle(e.getPoint(), scale);
					if (item != null) {
						SkeletonBone bone = (SkeletonBone)item;
						curRot = bone.getRotation();
					}
				}
				case SPRITE_MOVE -> {
					item = frame.getSpriteAtPosition(e.getPoint(), scale);
					if (item != null) {
						Sprite sprite = (Sprite)item;
						sprite.getBone().deselectAllVertices();
						sprite.setIsSelected(true);
						Point2D center = sprite.getLocation();
						curX = center.getX();
						curY = center.getY();
						offsetX = e.getPoint().getX() - (curX * scale);
						offsetY = e.getPoint().getY() - (curY * scale);
					}
				}
				case SPRITE_EDIT -> {
					item = frame.getSelectedBone().getSelectedSprite();
					if (item != null) {
						Sprite sprite = (Sprite)item;
						sprite.getBone().deselectAllVertices();
						sprite.setIsSelected(true);
						sprite.processPressed(e.getPoint(), scale);
					}
				}
			}
			
			if (item != null)
			{
				TreePath path = new TreePath(frame);
				path = path.pathByAddingChild(item);
				tree.setSelectionPath(path);
			}
			drawArea.setUserLoc(e.getPoint());
			drawArea.repaint();
		}
	}
	
	public void processDragged(final MouseEvent e) {
		if (item != null) {
			double scale = edit.getEditor().getUIScale() * drawArea.getZoomScale();
			switch (edit.getDrawMode()) {
				case BONE_MOVE -> {
					SkeletonBone bone = (SkeletonBone)item;
					switch(moveType) {
						case MOVE_BOTH -> {
							bone.setLocation(e.getPoint(), scale, true, false);
						}
						case MOVE_X -> {
							bone.setLocation(e.getPoint().getX() - edit.getEditor().getSettings().getPosHandleOffset(), 
									bone.getY() * scale, scale, true, false);
						}
						case MOVE_Y -> {
							bone.setLocation(bone.getX() * scale, e.getPoint().getY() + edit.getEditor().getSettings().getPosHandleOffset(),
									scale, true, false);
						}
					}
				}
				case BONE_ROTATE -> {
					SkeletonBone bone = (SkeletonBone)item;
					bone.rotateByHandle(e.getPoint(), scale, false);
				}
				case SPRITE_MOVE -> {
					Sprite sprite = (Sprite)item;
					Point loc = new Point((int)(e.getPoint().getX() - offsetX), (int)(e.getPoint().getY() - offsetY));
					sprite.setLocation(loc, scale);
				}
				case SPRITE_EDIT -> {
					Sprite sprite = (Sprite)item;
					sprite.processDragged(e.getPoint(), scale);
				}
			}
		}
	}
	
	public void processRelease(final MouseEvent e) {
		if (item != null) {
			switch (edit.getDrawMode()) {
				case BONE_MOVE -> {
					SkeletonBone bone = (SkeletonBone)item;
					edit.getEditor().getEditsManager().edit(new SetBoneLocation(bone, bone.getX(), bone.getY(), curX, curY, true, true));
				}
				case BONE_ROTATE -> {
					SkeletonBone bone = (SkeletonBone)item;
					edit.getEditor().getEditsManager().edit(new SetBoneRotation(bone, bone.getRotation(), curRot, true));
				}
				case SPRITE_MOVE -> {

				}
				case SPRITE_EDIT -> {
					Sprite sprite = (Sprite)item;
					sprite.processRelease();
				}
			}
		}


	}
}
