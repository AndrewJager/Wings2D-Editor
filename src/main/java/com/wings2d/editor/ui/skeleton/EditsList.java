package com.wings2d.editor.ui.skeleton;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.wings2d.editor.ui.edits.EditCellRenderer;
import com.wings2d.editor.ui.edits.EditsManager;

public class EditsList extends JDialog{
	private static final long serialVersionUID = 1L;
	private JTree tree;
	
	public EditsList(final EditsManager edits) {
		super();
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		edits.addToTree(root);
		tree = new JTree(root);
		tree.setShowsRootHandles(true);
		tree.expandRow(0);
		tree.setRootVisible(false);
		tree.setCellRenderer(new EditCellRenderer());
		tree.setSelectionRow(edits.getEditsCount() - edits.getCurEdit() - 1);
		
		JScrollPane pane = new JScrollPane(tree);
		this.add(pane);
		
		this.setTitle("Edits list");
		this.setSize(650, 600);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setVisible(true);
	}

	
}
