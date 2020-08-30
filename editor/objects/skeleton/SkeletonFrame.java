package editor.objects.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SkeletonFrame implements SkeletonItem{
	private SkeletonAnimation parent;
	private List<SkeletonBone> bones;
	private String name;
	
	public SkeletonFrame(String frameName, SkeletonAnimation frameParent)
	{
		name = frameName;
		parent = frameParent;
		bones = new ArrayList<SkeletonBone>();
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	public String toString()
	{
		return name;
	}
	public int getTreeLevel()
	{
		return 2;
	}
	@Override
	public TreeNode getChildAt(int childIndex) {
		return bones.get(childIndex);
	}
	@Override
	public int getChildCount() {
		return bones.size();
	}
	@Override
	public TreeNode getParent() {
		return parent;
	}
	@Override
	public int getIndex(TreeNode node) {
		return bones.indexOf(node);
	}
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	@Override
	public boolean isLeaf() {
		return (bones.size() == 0);
	}
	@Override
	public Enumeration<? extends TreeNode> children() {
		return Collections.enumeration(bones);
	}
	@Override
	public void insert(MutableTreeNode child, int index) {
		bones.add(index, (SkeletonBone)child);
	}
	@Override
	public void remove(int index) {
		bones.remove(index);
	}
	@Override
	public void remove(MutableTreeNode node) {
		bones.remove(node);
	}
	@Override
	public void setUserObject(Object object) {}
	@Override
	public void removeFromParent() {
		parent.remove(this);
	}
	@Override
	public void setParent(MutableTreeNode newParent) {
		parent = (SkeletonAnimation)newParent;
	}
	

}
