package editor.objects.skeleton;

import javax.swing.tree.MutableTreeNode;

public interface SkeletonNode extends MutableTreeNode{
	public int getTreeLevel();
	public void setName(String newName);
}
