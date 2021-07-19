package com.wings2d.editor.objects.skeleton;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.edits.SetName;

public class SkeletonTreeModelListener implements TreeModelListener{
	private JTree tree;
	private Editor edit;
	
	public SkeletonTreeModelListener(final JTree tree, final Editor edit)
	{
		this.tree = tree;
		this.edit = edit;
	}
	
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
		if (!(selectedNode instanceof SkeletonMasterFrame))
		{
			edit.getEditsManager().edit(new SetName(selectedNode, tree.getCellEditor().getCellEditorValue().toString(), selectedNode.getName()));
		}
	}
	@Override
	public void treeNodesInserted(TreeModelEvent e) {}
	@Override
	public void treeNodesRemoved(TreeModelEvent e) {}
	@Override
	public void treeStructureChanged(TreeModelEvent e) {}
}
