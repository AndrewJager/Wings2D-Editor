package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonBone;

public class SetBoneRotation extends Edit{
	private SkeletonBone bone;
	private double rot, oldRot;
	private boolean recalcParts;
	
	public SetBoneRotation(final SkeletonBone bone, final double rot, final boolean recalcParts) {
		this(bone, rot, bone.getRotation(), recalcParts);
	}
	public SetBoneRotation(final SkeletonBone bone, final double rot, final double oldRot, final boolean recalcParts) {
		super();
		this.bone = bone;
		this.rot = rot;
		this.oldRot = oldRot;
		this.recalcParts = recalcParts;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		bone.setRotation(rot, recalcParts);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		bone.setRotation(oldRot, recalcParts);
	}

	@Override
	public String getDescription() {
		return "Set rotation of bone " + bone.getName() + " from " + oldRot + " to " + rot; 
	}
}
