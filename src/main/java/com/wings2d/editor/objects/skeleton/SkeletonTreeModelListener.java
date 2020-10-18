package com.wings2d.editor.objects.skeleton;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

public class SkeletonTreeModelListener implements TreeModelListener{
	private JTree tree;
	
	public SkeletonTreeModelListener(final JTree tree)
	{
		this.tree = tree;
	}
	
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
		if (!(selectedNode instanceof SkeletonMasterFrame))
		{
			selectedNode.setName(tree.getCellEditor().getCellEditorValue().toString());
		}
	}
	@Override
	public void treeNodesInserted(TreeModelEvent e) {}
	@Override
	public void treeNodesRemoved(TreeModelEvent e) {}
	@Override
	public void treeStructureChanged(TreeModelEvent e) {}
}
