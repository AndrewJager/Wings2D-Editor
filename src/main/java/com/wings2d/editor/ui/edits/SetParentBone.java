package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonBone;

public class SetParentBone extends Edit{
	private SkeletonBone bone, newParent, oldParent;
	
	public SetParentBone(final SkeletonBone bone, final SkeletonBone newParent) {
		super();
		this.bone = bone;
		this.oldParent = bone.getParentBone();
		this.newParent = newParent;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		bone.setParentBone(newParent);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		bone.setParentBone(oldParent);
	}

	@Override
	public String getDescription() {
		return "Set parent bone of " + bone.getName();
	}
}
