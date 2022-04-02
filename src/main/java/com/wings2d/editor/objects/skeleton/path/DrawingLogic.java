package com.wings2d.editor.objects.skeleton.path;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.DrawMode.MoveType;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.DrawingArea;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.edits.SetBoneLocation;
import com.wings2d.editor.ui.edits.SetBoneRotation;
import com.wings2d.editor.ui.skeleton.SkeletonEdit;

public class DrawingLogic {
	private SkeletonFrame frame;
	private SkeletonEdit edit;
	private DrawingArea drawArea;
	
	private DrawMode.MoveType moveType;
	private SkeletonNode item;
	private double curX, curY, curRot;
	
	public DrawingLogic(final SkeletonEdit edit, final DrawingArea drawArea) {
		this.edit = edit;
		this.drawArea = drawArea;
	}
	
	public void setFrame(final SkeletonFrame frame) {
		this.frame = frame;
	}
	public void setMode(final DrawMode mode) {
		
	}
	
	public void processPressed(final MouseEvent e) {
		double scale = edit.getEditor().getUIScale() * drawArea.getZoomScale();
		
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
				item = frame.getSelectedBone().getSelectedSprite();
				if (item != null) {
					Sprite sprite = (Sprite)item;
					Point2D center = sprite.getLocation();
					curX = center.getX();
					curY = center.getY();
				}
			}
			case SPRITE_EDIT -> {
				item = frame.getSelectedBone().getSelectedSprite();
				if (item != null) {
					Sprite sprite = (Sprite)item;
					sprite.processPressed(e.getPoint());
				}
			}
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
							bone.setLocation(e.getPoint(), scale, true);
						}
						case MOVE_X -> {
							bone.setLocation(e.getPoint().getX() - edit.getEditor().getSettings().getPosHandleOffset(), 
									bone.getY() * scale, scale, true);
						}
						case MOVE_Y -> {
							bone.setLocation(bone.getX() * scale, e.getPoint().getY() + edit.getEditor().getSettings().getPosHandleOffset(),
									scale, true);
						}
					}
				}
				case BONE_ROTATE -> {
					SkeletonBone bone = (SkeletonBone)item;
					bone.rotateByHandle(e.getPoint(), scale);
				}
				case SPRITE_MOVE -> {
					Sprite sprite = (Sprite)item;
					sprite.setLocation(e.getPoint(), scale);
				}
				case SPRITE_EDIT -> {
					Sprite sprite = (Sprite)item;
					sprite.processDragged(e.getPoint());
				}
			}
		}
	}
	
	public void processRelease(final MouseEvent e) {
		if (item != null) {
			switch (edit.getDrawMode()) {
				case BONE_MOVE -> {
					SkeletonBone bone = (SkeletonBone)item;
					edit.getEditor().getEditsManager().edit(new SetBoneLocation(bone, bone.getX(), bone.getY(), curX, curY, true));
				}
				case BONE_ROTATE -> {
					SkeletonBone bone = (SkeletonBone)item;
					edit.getEditor().getEditsManager().edit(new SetBoneRotation(bone, bone.getRotation(), curRot));
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
