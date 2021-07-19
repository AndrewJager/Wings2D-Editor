package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class SetName extends Edit{
	private SkeletonNode node;
	private String oldName, newName;

	public SetName(final SkeletonNode node, final String newName, final String oldName) {
		super();
		this.node = node;
		this.newName = newName;
		this.oldName = oldName;
	}
	
	@Override
	public void edit() throws ActionNotDoneException {
		node.setName(newName);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		node.setName(oldName);
	}

	@Override
	public String getDescription() {
		return "Change name of " + oldName + " to " + newName;
	}

}
