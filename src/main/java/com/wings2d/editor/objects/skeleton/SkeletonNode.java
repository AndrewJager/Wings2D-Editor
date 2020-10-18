package com.wings2d.editor.objects.skeleton;

import java.io.PrintWriter;
import java.util.List;

import javax.swing.tree.MutableTreeNode;

public abstract class SkeletonNode implements MutableTreeNode{
	public abstract void setName(final String newName);
	public abstract void saveToFile(final PrintWriter out);
	public abstract void resyncAll();
	public abstract void generateRender(final double scale);
	public abstract void moveUp();
	public abstract void moveDown();
	public abstract List<SkeletonNode> getNodes();
	
	public static final String NAME_TOKEN = "NAME";
	public static final String END_TOKEN = "END";
	public static final String ID_TOKEN = "ID";
	public static final String COLOR_TOKEN = "COLOR";
}
