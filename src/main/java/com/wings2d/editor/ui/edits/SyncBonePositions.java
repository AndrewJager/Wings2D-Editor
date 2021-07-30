package com.wings2d.editor.ui.edits;

import java.util.ArrayList;
import java.util.List;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;

public class SyncBonePositions extends Edit{
	private SkeletonFrame frame;
	private boolean cautious;
	private List<SetBoneLocation> moves;
	
	/**
	 * Set the positions of all bones to the position of their synced bone  
	 * @param frame Frame to sync Bones for
	 * @param cautious If true, Bones will only be synced if their position is equal to the default starting position
	 */
	public SyncBonePositions(final SkeletonFrame frame, final boolean cautious) {
		super();
		this.frame = frame;
		this.cautious = cautious;
		moves = new ArrayList<SetBoneLocation>();

		for (int i = 0;  i < frame.getBones().size(); i++)
		{
			SkeletonBone bone = frame.getBones().get(i);
			if (bone.getParentSyncedBone() != null)
			{
				if ((!cautious) || 
					((bone.getX() == SkeletonBone.START_POS.getX()) && (bone.getY() == SkeletonBone.START_POS.getY()))) 
				{
					moves.add(new SetBoneLocation(bone, bone.getParentSyncedBone().getX(), bone.getParentSyncedBone().getY(), false));
				}
			}
		}
	}
	
	@Override
	public void edit() throws ActionNotDoneException {
		for (int i = 0; i < moves.size(); i++) {
			moves.get(i).doEdit();
		}
	}

	@Override
	public void undo() throws ActionNotDoneException {
		for (int i = 0; i < moves.size(); i++) {
			moves.get(i).undoEdit();
		}
	}

	@Override
	public String getDescription() {
		String result = "";
		result = result + "Sync bones of frame " + frame.getName();
		if (cautious) {
			result = result + " (cautious)";
		}
		return result;
	}
}
