package com.wings2d.editor.objects.skeleton;

import java.util.List;

import javax.swing.tree.MutableTreeNode;

import com.wings2d.editor.ui.edits.ActionNotDoneException;

public abstract class SkeletonNode extends DBEditObject implements MutableTreeNode{
	public SkeletonNode(final String tableName) {
		this(tableName, true);
	}
	public SkeletonNode(final String tableName, final Boolean hasName) {
		super(tableName, hasName);
	}

	public abstract void resyncAll();
	public abstract void generateRender(final double scale);
	public abstract void moveUp() throws ActionNotDoneException;
	public abstract void moveDown() throws ActionNotDoneException;
	public abstract List<SkeletonNode> getNodes();
	public abstract boolean isMaster();
	
	public static final String MOVE_UP_ERROR = "Cannot move up when item is first in list!";
	public static final String MOVE_DOWN_ERROR = "Cannot move down when item is last in list!";
}
