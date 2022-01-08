package com.wings2d.editor.ui.edits;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class EditCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(
            tree, value, sel, exp, leaf, row, hasFocus);
        if (!sel) {
	        EditTreeNode node = (EditTreeNode) value;
	        this.setOpaque(true);
	        if (!node.isActive()) {
	        	this.setBackground(Color.LIGHT_GRAY);
	        }
	        else {
	        	this.setBackground(Color.WHITE);
	        }
        }
        return this;
    }
}