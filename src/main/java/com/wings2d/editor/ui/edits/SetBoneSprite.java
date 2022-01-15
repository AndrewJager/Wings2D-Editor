package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.Sprite;

public class SetBoneSprite extends Edit{
	private Sprite sprite, oldSprite, newSprite;
	private SkeletonBone bone;
	
	public SetBoneSprite(final Sprite sprite, final SkeletonBone bone) {
		super();
		this.sprite = sprite; // Sprite to copy
		this.bone = bone;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		oldSprite = bone.getSpriteBySyncID(sprite.getID());
		if (oldSprite != null) { // Synced sprite already exists
			bone.getSprites().remove(oldSprite);
			oldSprite.delete(bone.getStoredConnection());
		}
		try {
			newSprite = sprite.copy(bone, bone.getStoredConnection());
			bone.getSprites().add(newSprite);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void undo() throws ActionNotDoneException {
		bone.getSprites().remove(newSprite);
		newSprite.delete(bone.getStoredConnection());
		if (oldSprite != null) {
			bone.getSprites().add(oldSprite);
			oldSprite.insert(bone.getStoredConnection());
		}
	}

	@Override
	public String getDescription() {
		return "Sync " + sprite.getName();
	}

}
