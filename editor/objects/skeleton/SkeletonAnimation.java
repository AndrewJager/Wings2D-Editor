package editor.objects.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SkeletonAnimation implements SkeletonNode{
	private Skeleton parent;
	private List<SkeletonFrame> frames;
	private String name;
	
	public SkeletonAnimation(String animName, Skeleton animParent)
	{
		if (animParent.containsAnimWithName(animName))
		{
			throw new IllegalArgumentException("An Animation with this name already exists!");
		}
		
		this.parent = animParent;
		frames = new ArrayList<SkeletonFrame>();
		this.name = animName;
	}
	public void setName(String newName)
	{
		name = newName;
	}
	public String toString()
	{
		return name;
	}
	public boolean containsFrameWithName(String frameName)
	{
		boolean hasName = false;
		for(int i = 0; i < frames.size(); i++)
		{
			if (frames.get(i).toString().equals(frameName))
			{
				hasName = true;
				break;
			}
		}
		return hasName;
	}
	
	public int getTreeLevel()
	{
		return 1;
	}
	@Override
	public TreeNode getChildAt(int childIndex) {
		return frames.get(childIndex);
	}
	@Override
	public int getChildCount() {
		return frames.size();
	}
	@Override
	public TreeNode getParent() {
		return parent;
	}
	@Override
	public int getIndex(TreeNode node) {
		return frames.indexOf(node);
	}
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	@Override
	public boolean isLeaf() {
		return (frames.size() == 0);
	}
	@Override
	public Enumeration<? extends TreeNode> children() {
		return Collections.enumeration(frames);
	}
	@Override
	public void insert(MutableTreeNode child, int index) {
		SkeletonFrame newFrame = (SkeletonFrame)child;
		SkeletonFrame syncFrame = newFrame.getParentSyncedFrame();
		frames.add(index, newFrame);
		for(int i = 0; i < syncFrame.getChildCount(); i++)
		{
			SkeletonBone bone = syncFrame.getBones().get(i);
			newFrame.getBones().add(bone.copy(newFrame));
		}
	}
	@Override
	public void remove(int index) {
		frames.remove(index);
	}
	@Override
	public void remove(MutableTreeNode node) {
		frames.remove((SkeletonFrame)node);
	}
	@Override
	public void setUserObject(Object object) {}
	@Override
	public void removeFromParent() {
		parent.remove(this);
	}
	@Override
	public void setParent(MutableTreeNode newParent) {
		parent = (Skeleton)newParent;
	}
}
