package editor.objects;

import javax.swing.tree.MutableTreeNode;

public interface ISkeleton extends MutableTreeNode{
	public int getTreeLevel();
	public void setName(String newName);
}
