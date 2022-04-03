package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonBone;

public class SetBoneLocation extends Edit{
	private SkeletonBone bone;
	private double x, y, oldX, oldY;
	private boolean translateChildren, recalcParts;
	
	public SetBoneLocation(final SkeletonBone bone, final double x, final double y, final boolean translateChildren, final boolean recalcParts) {
		this(bone, x, y, bone.getX(), bone.getY(), translateChildren, recalcParts);
	}
	public SetBoneLocation(final SkeletonBone bone, final double x, final double y, final double curX, final double curY, 
			final boolean translateChildren, final boolean recalcParts) {
		super();
		this.bone = bone;
		this.x = x;
		this.y = y;
		this.translateChildren = translateChildren;
		this.oldX = curX;
		this.oldY = curY;
		this.recalcParts = recalcParts;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		bone.setLocation(x, y, translateChildren, recalcParts);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		bone.setLocation(oldX, oldY, translateChildren, recalcParts);
	}

	@Override
	public String getDescription() {
		return "Set location of bone " + bone.getName() + " from X: " + oldX + " Y: " + oldY + " to X: " + x + " Y: " + y;
	}
}
