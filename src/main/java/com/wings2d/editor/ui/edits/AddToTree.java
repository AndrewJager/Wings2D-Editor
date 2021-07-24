package com.wings2d.editor.ui.edits;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class AddToTree extends Edit{
	private DefaultTreeModel model;
	private SkeletonNode node, parentNode;
	
	public AddToTree(final DefaultTreeModel model, final SkeletonNode node, final SkeletonNode parentNode) {
		this.model = model;
		this.node = node;
		this.parentNode = parentNode;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		model.insertNodeInto((MutableTreeNode)node, parentNode, parentNode.getChildCount());
	}

	@Override
	public void undo() throws ActionNotDoneException {
		model.removeNodeFromParent((MutableTreeNode)node);
	}

	@Override
	public String getDescription() {
		return "Add " + node.getName() + " to " + parentNode.getName();
	}
}
