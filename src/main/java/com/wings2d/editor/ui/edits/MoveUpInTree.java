package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class MoveUpInTree extends Edit{
	private SkeletonNode node;
	
	
	public MoveUpInTree(final SkeletonNode node) {
		super();
		this.node = node;
	}

	@Override
	public void edit() throws ActionNotDoneException{
		node.moveUp();
	}

	@Override
	public void undo() throws ActionNotDoneException{
		node.moveDown();
	}
	@Override
	public String getDescription() {
		return "Move " + node.toString() + " up in tree";
	}
}
