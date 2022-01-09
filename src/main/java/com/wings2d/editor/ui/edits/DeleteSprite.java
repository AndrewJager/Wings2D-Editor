package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.Sprite;

public class DeleteSprite extends Edit{
	private Sprite sprite;
	private SkeletonBone bone;
	
	public DeleteSprite(final Sprite sprite) {
		super();
		this.sprite = sprite;
		bone = sprite.getBone();
	}

	@Override
	public void edit() throws ActionNotDoneException {
		bone.getSprites().remove(sprite);
		sprite.delete(bone.getStoredConnection());
	}

	@Override
	public void undo() throws ActionNotDoneException {
		sprite.insert(bone.getStoredConnection());
		bone.getSprites().add(sprite);
	}

	@Override
	public String getDescription() {
		return "Delete " + sprite.getName();
	}
}
