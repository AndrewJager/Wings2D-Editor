package com.wings2d.editor.ui.edits;

import java.util.ArrayList;
import java.util.List;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.Sprite;

public class SyncBone extends Edit{
	private SkeletonBone bone;
	private SetBoneLocation loc;
	private List<SetBoneSprite> sprites;
	private List<DeleteSprite> spriteDeletes;
	
	public SyncBone(final SkeletonBone bone) {
		this.bone = bone;
		SkeletonBone sync = bone.getParentSyncedBone();
		loc = new SetBoneLocation(bone, sync.getX(), sync.getY(), false);
		
		sprites = new ArrayList<SetBoneSprite>();
		for (int i = 0; i < sync.getSprites().size(); i++) {
			sprites.add(new SetBoneSprite(sync.getSprites().get(i), bone));
		}
		
		// Delete sprites synced to a non-existent sprite 
		spriteDeletes = new ArrayList<DeleteSprite>();
		for (int i = 0; i < bone.getSprites().size(); i++) {
			Sprite sprite = bone.getSprites().get(i);
			if ((sprite.getSyncID() != null) && (sync.getSpriteBySyncID(sprite.getSyncID()) == null)) {
				spriteDeletes.add(new DeleteSprite(sprite));
			}
		}
	}

	@Override
	public void edit() throws ActionNotDoneException {
		loc.doEdit();
		for (int i = 0; i < sprites.size(); i++) {
			sprites.get(i).doEdit();
		}
		for (int i = 0; i < spriteDeletes.size(); i++) {
			spriteDeletes.get(i).doEdit();
		}
	}

	@Override
	public void undo() throws ActionNotDoneException {
		loc.undoEdit();
		for (int i = 0; i < sprites.size(); i++) {
			sprites.get(i).undoEdit();
		}
		for (int i = 0; i < spriteDeletes.size(); i++) {
			spriteDeletes.get(i).undoEdit();
		}
	}

	@Override
	public String getDescription() {
		return "Sync " + bone.getName();
	}
	
	@Override
	public List<Edit> getChildEdits() {
		List<Edit> edits = new ArrayList<Edit>();
		edits.add(loc);
		for (int i = 0; i < sprites.size(); i++) {
			edits.add(sprites.get(i));
		}
		for (int i = 0; i < spriteDeletes.size(); i++) {
			edits.add(spriteDeletes.get(i));
		}
		return edits;
	}
}
