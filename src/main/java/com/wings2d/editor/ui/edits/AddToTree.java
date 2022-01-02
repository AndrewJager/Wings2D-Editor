package com.wings2d.editor.ui.edits;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class AddToTree extends Edit{
	private DefaultTreeModel model;
	private SkeletonNode node, parentNode;
	
	public AddToTree(final DefaultTreeModel model, final SkeletonNode node, final SkeletonNode parentNode) {
		super();
		this.model = model;
		this.node = node;
		this.parentNode = parentNode;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		model.insertNodeInto((MutableTreeNode)node, parentNode, parentNode.getChildCount());
		
		// Resync children after adding node
		Skeleton skel = (Skeleton)model.getRoot();
		skel.setAllSyncNodes();
	}

	@Override
	public void undo() throws ActionNotDoneException {
		model.removeNodeFromParent((MutableTreeNode)node);
		
		// Resync children after removing node
		Skeleton skel = (Skeleton)model.getRoot();
		skel.setAllSyncNodes();
	}

	@Override
	public String getDescription() {
		return "Add " + node.getName() + " to " + parentNode.getName();
	}
}
