package com.wings2d.editor.objects.skeleton;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SkeletonAnimation extends SkeletonNode{
	public static final String ANIM_TOKEN = "ANIMATION";
	
	private Skeleton skeleton;
	private List<SkeletonFrame> frames;
	private String name;
	
	public SkeletonAnimation(final String animName, final Skeleton animParent)
	{
		if (animParent.containsAnimWithName(animName))
		{
			throw new IllegalArgumentException("An Animation with this name already exists!");
		}
		
		this.skeleton = animParent;
		frames = new ArrayList<SkeletonFrame>();
		this.name = animName;
	}
	
	public SkeletonAnimation(final Scanner in, final Skeleton animParent)
	{
		this.skeleton = animParent;
		frames = new ArrayList<SkeletonFrame>();
		
		boolean keepReading = true;
		while(in.hasNext() && keepReading)
		{
			String[] tokens = in.next().split(":");
			if (tokens[0].equals(NAME_TOKEN))
			{
				name = tokens[1];
			}
			else if (tokens[0].equals(SkeletonFrame.FRAME_TOKEN))
			{
				frames.add(new SkeletonFrame(in, this));
			}
			else if (tokens[0].equals(END_TOKEN))
			{
				keepReading = false;
			}
		}
	}

	public String toString()
	{
		return name;
	}
	public boolean containsFrameWithName(final String frameName)
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
	public List<SkeletonFrame> getFrames()
	{
		return frames;
	}
	public Skeleton getSkeleton()
	{
		return skeleton;
	}

	// MutableTreeNode methods
	@Override
	public void insert(final MutableTreeNode child, final int index) {
		SkeletonFrame newFrame = (SkeletonFrame)child;
		SkeletonFrame syncFrame = newFrame.getParentSyncedFrame();
		frames.add(index, newFrame);
		for(int i = 0; i < syncFrame.getChildCount(); i++)
		{
			SkeletonBone bone = syncFrame.getBones().get(i);
			newFrame.getBones().add(new SkeletonBone(bone, newFrame));
		}
		newFrame.syncBonesToParents();
	}
	@Override
	public void remove(final int index) {
		if (frames.get(index).syncedFrames.size() > 0)
		{
			throw new IllegalStateException("Cannot remove Frame that has one or more synced Frames!"); 
		}
		else
		{
			frames.get(index).unsyncAll();
			frames.remove(index);
		}
	}
	@Override
	public void remove(final MutableTreeNode node) {
		frames.remove((SkeletonFrame)node);
	}
	@Override
	public void setUserObject(final Object object) {}
	@Override
	public void removeFromParent() {
		skeleton.remove(this);
	}
	@Override
	public void setParent(final MutableTreeNode newParent) {
		skeleton = (Skeleton)newParent;
	}
	@Override
	public TreeNode getChildAt(final int childIndex) {
		return frames.get(childIndex);
	}
	@Override
	public int getChildCount() {
		return frames.size();
	}
	@Override
	public TreeNode getParent() {
		return skeleton;
	}
	@Override
	public int getIndex(final TreeNode node) {
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
	
	// SkeletonNode methods
	@Override
	public void setName(final String newName)
	{
		name = newName;
	}
	@Override
	public void saveToFile(final PrintWriter out)
	{
		out.write(ANIM_TOKEN + "\n");
		out.write(NAME_TOKEN + ":" + name + "\n");
		for (int i = 0; i < frames.size(); i++)
		{
			frames.get(i).saveToFile(out);
		}
		out.write(END_TOKEN + ":" + ANIM_TOKEN + "\n");
	}
	@Override
	public void resyncAll()
	{
		for (int i = 0; i < frames.size(); i++)
		{
			frames.get(i).resyncAll();
		}
	}
	@Override
	public void generateRender(final double scale)
	{
		for (int i = 0; i < frames.size(); i++)
		{
			frames.get(i).generateRender(scale);
		}
	}
	@Override
	public void moveUp()
	{
		List<SkeletonNode> anims = skeleton.getAnimations();
		int index = anims.indexOf(this);
		if (index > 1) // Cannot swap with Master Frame
		{
			SkeletonNode swap = anims.get(index - 1);
			anims.remove(swap);
			anims.add(index, swap);
		}
	}
	@Override
	public List<SkeletonNode> getNodes()
	{
		List<SkeletonNode> nodes = new ArrayList<SkeletonNode>();
		for (int i = 0; i < frames.size(); i++)
		{
			nodes.add((SkeletonNode)frames.get(i));
		}
		return nodes;
	}
	@Override
	public void moveDown()
	{
		List<SkeletonNode> anims = skeleton.getAnimations();
		int index = anims.indexOf(this);
		if (index < anims.size() - 1) 
		{
			anims.remove(this);
			anims.add(index + 1, this);
		}
	}
}
