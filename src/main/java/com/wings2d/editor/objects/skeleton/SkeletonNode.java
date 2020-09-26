package com.wings2d.editor.objects.skeleton;

import java.io.PrintWriter;

import javax.swing.tree.MutableTreeNode;

public interface SkeletonNode extends MutableTreeNode{
	public void setName(final String newName);
	public void saveToFile(final PrintWriter out);
	public void resyncAll();
	
	public static final String NAME_TOKEN = "NAME";
	public static final String END_TOKEN = "END";
	public static final String ID_TOKEN = "ID";
}
