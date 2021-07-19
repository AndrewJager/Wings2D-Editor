package com.wings2d.editor.objects.skeleton;

import java.io.PrintWriter;
import java.util.List;

import javax.swing.tree.MutableTreeNode;

import com.wings2d.editor.ui.edits.ActionNotDoneException;

public abstract class SkeletonNode implements MutableTreeNode{
	public abstract void setName(final String newName);
	public abstract String getName();
	public abstract void saveToFile(final PrintWriter out);
	public abstract void resyncAll();
	public abstract void generateRender(final double scale);
	public abstract void moveUp() throws ActionNotDoneException;
	public abstract void moveDown() throws ActionNotDoneException;
	public abstract List<SkeletonNode> getNodes();
	public abstract boolean isMaster();
	
	public static final String NAME_TOKEN = "NAME";
	public static final String END_TOKEN = "END";
	public static final String ID_TOKEN = "ID";
	public static final String COLOR_TOKEN = "COLOR";
	
	public static final String MOVE_UP_ERROR = "Cannot move up when item is first in list!";
	public static final String MOVE_DOWN_ERROR = "Cannot move down when item is last in list!";
}
