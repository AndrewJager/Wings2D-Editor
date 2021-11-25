package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonBone;

public class SetBoneLocation extends Edit{
	private SkeletonBone bone;
	private double x, y, oldX, oldY;
	private boolean translateChildren;
	private boolean commit;
	
	public SetBoneLocation(final SkeletonBone bone, final double x, final double y, final boolean translateChildren, final boolean commit) {
		super();
		this.bone = bone;
		this.x = x;
		this.y = y;
		this.translateChildren = translateChildren;
		this.commit = commit;
		this.oldX = bone.getX();
		this.oldY = bone.getY();
	}

	@Override
	public void edit() throws ActionNotDoneException {
		bone.setLocation(x, y, translateChildren, commit);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		bone.setLocation(oldX, oldY, translateChildren, commit);
	}

	@Override
	public String getDescription() {
		return "Set location of bone " + bone.getName() + " from X: " + oldX + " Y: " + oldY + " to X: " + x + " Y: " + y;
	}
}
