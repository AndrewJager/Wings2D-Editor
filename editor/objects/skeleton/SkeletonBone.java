package editor.objects.skeleton;

import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SkeletonBone implements SkeletonItem{
	private SkeletonFrame parent;
	private String name;
	
	public SkeletonBone(String boneName, SkeletonFrame parentFrame)
	{
		name = boneName;
		parent = parentFrame;
	}
	
	public String toString()
	{
		return name;
	}

	@Override
	public void insert(MutableTreeNode child, int index) {}

	@Override
	public void remove(int index) {}

	@Override
	public void remove(MutableTreeNode node) {}

	@Override
	public void setUserObject(Object object) {}

	@Override
	public void removeFromParent() {
		parent.remove(this);
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		parent = (SkeletonFrame)newParent;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		return null;
	}

	@Override
	public int getTreeLevel() {
		return 3;
	}

	@Override
	public void setName(String newName) {
		name = newName;	
	}
}
