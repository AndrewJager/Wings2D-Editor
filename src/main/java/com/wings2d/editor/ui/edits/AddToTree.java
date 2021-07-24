package com.wings2d.editor.ui.edits;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class AddToTree extends Edit{
	private DefaultTreeModel model;
	private SkeletonNode node;
	
	public AddToTree(final DefaultTreeModel model, final SkeletonNode node) {
		this.model = model;
		this.node = node;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		Skeleton root = (Skeleton) model.getRoot();
		model.insertNodeInto((MutableTreeNode)node, root, root.getChildCount());
	}

	@Override
	public void undo() throws ActionNotDoneException {
		model.removeNodeFromParent((MutableTreeNode)node);
	}

	@Override
	public String getDescription() {
		Skeleton root = (Skeleton) model.getRoot();
		return "Add " + node.getName() + " to " + root.getName();
	}

}
