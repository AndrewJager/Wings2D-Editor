package editor.objects.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class Skeleton implements SkeletonItem{
	private List<SkeletonItem> animations;
	private SkeletonMasterFrame masterFrame;
	private String name;
	
	public Skeleton(String skeletonName)
	{
		animations = new ArrayList<SkeletonItem>();
		masterFrame = new SkeletonMasterFrame("Master");
		animations.add(masterFrame);
		this.name = skeletonName;
	}
	
	public SkeletonFrame getMasterFrame() {
		return masterFrame;
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
		return 0;
	}
	@Override
	public TreeNode getChildAt(int childIndex) {
		return animations.get(childIndex);
	}
	@Override
	public int getChildCount() {
		return animations.size();
	}
	@Override
	public TreeNode getParent() {
		return null;
	}
	@Override
	public int getIndex(TreeNode node) {
		return animations.indexOf(node);
	}
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	@Override
	public boolean isLeaf() {
		return (animations.size() == 0);
	}
	@Override
	public Enumeration<? extends TreeNode> children() {
		return Collections.enumeration(animations);
	}
	@Override
	public void insert(MutableTreeNode child, int index) {
		animations.add(index, (SkeletonAnimation)child);
	}
	@Override
	public void remove(int index) {
		animations.remove(index);
	}
	@Override
	public void remove(MutableTreeNode node) {
		animations.remove((SkeletonAnimation)node);
	}
	@Override
	public void setUserObject(Object object) {}
	@Override
	public void removeFromParent() {}
	@Override
	public void setParent(MutableTreeNode newParent) {}
}
