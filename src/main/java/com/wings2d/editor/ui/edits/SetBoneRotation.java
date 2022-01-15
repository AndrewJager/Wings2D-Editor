package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonBone;

public class SetBoneRotation extends Edit{
	private SkeletonBone bone;
	private double rot, oldRot;
	
	public SetBoneRotation(final SkeletonBone bone, final double rot) {
		this(bone, rot, bone.getRotation());
	}
	public SetBoneRotation(final SkeletonBone bone, final double rot, final double oldRot) {
		super();
		this.bone = bone;
		this.rot = rot;
		this.oldRot = oldRot;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		bone.setRotation(rot);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		bone.setRotation(oldRot);
	}

	@Override
	public String getDescription() {
		return "Set rotation of bone " + bone.getName() + " from " + oldRot + " to " + rot; 
	}
}
