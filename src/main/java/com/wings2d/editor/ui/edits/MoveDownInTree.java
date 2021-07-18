package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class MoveDownInTree extends Edit{
	private SkeletonNode node;
	
	
	public MoveDownInTree(final SkeletonNode node) {
		super();
		this.node = node;
	}

	@Override
	public void edit() throws ActionNotDoneException{
		node.moveDown();
	}

	@Override
	public void undo() throws ActionNotDoneException{
		node.moveUp();
	}
}
