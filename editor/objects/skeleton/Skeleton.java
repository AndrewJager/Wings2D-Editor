package editor.objects.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import editor.objects.project.ProjectEntity;

public class Skeleton implements SkeletonNode, ProjectEntity {
	public static final String FILE_MARKER = "SKELETON";
	
	private List<SkeletonNode> animations;
	private SkeletonMasterFrame masterFrame;
	private String name;
	
	public Skeleton(String skeletonName)
	{
		animations = new ArrayList<SkeletonNode>();
		masterFrame = new SkeletonMasterFrame("Master");
		animations.add(masterFrame);
		this.name = skeletonName;
	}
	
	/**
	 * Creates a new skeleton from the file. The file should have been checked first to ensure that it is valid.
	 * @param file File to create from
	 */
	public Skeleton(Scanner in)
	{
		while(in.hasNext())
		{
			String[] tokens = in.next().split(":");
			if (tokens[0].equals("NAME"))
			{
				name = tokens[1];
			}
		}
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
	
	public boolean containsAnimWithName(String animName)
	{
		boolean hasName = false;
		for(int i = 1; i < animations.size(); i++) // Start at one to avoid checking the Master Frame
		{
			if (animations.get(i).toString().equals(animName))
			{
				hasName = true;
				break;
			}
		}
		return hasName;
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
