package editor.objects.skeleton;

import javax.swing.tree.MutableTreeNode;

public interface SkeletonItem extends MutableTreeNode{
	public int getTreeLevel();
	public void setName(String newName);
}
