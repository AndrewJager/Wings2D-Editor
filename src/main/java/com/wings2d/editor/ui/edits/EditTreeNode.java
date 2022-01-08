package com.wings2d.editor.ui.edits;

import javax.swing.tree.DefaultMutableTreeNode;

public class EditTreeNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	private EditsManager manager;
	private Edit edit;
	
	public EditTreeNode(final EditsManager manager, final Edit edit) {
		super(edit.getDescription());
		this.manager = manager;
		this.edit = edit;
	}

	public boolean isActive() {
		return manager.isEditActive(edit);
	}
}
