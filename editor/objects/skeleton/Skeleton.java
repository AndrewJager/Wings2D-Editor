package editor.objects.skeleton;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import editor.objects.project.Project;
import editor.objects.project.ProjectEntity;

public class Skeleton implements SkeletonNode, ProjectEntity {
	public static final String FILE_MARKER = "SKELETON";
	
	private List<SkeletonNode> animations;
	private SkeletonMasterFrame masterFrame;
	private String name;
	private Project project;
	
	public Skeleton(final String skeletonName, final Project p)
	{
		animations = new ArrayList<SkeletonNode>();
		masterFrame = new SkeletonMasterFrame("Master");
		animations.add(masterFrame);
		this.name = skeletonName;
		this.project = p;
	}
	
	/**
	 * Creates a new skeleton from the file. The file should have been checked first to ensure that it is valid.
	 * @param file File to create from
	 */
	public Skeleton(final Scanner in, final Project p)
	{
		this.project = p;
		animations = new ArrayList<SkeletonNode>();
		
		while(in.hasNext())
		{
			String[] tokens = in.next().split(":");
			if (tokens[0].equals("NAME"))
			{
				name = tokens[1];
			}
			else if (tokens[0].equals(SkeletonMasterFrame.FILE_MARKER))
			{
				masterFrame = new SkeletonMasterFrame(in);
				animations.add(0, masterFrame);
			}
			else if (tokens[0].equals(SkeletonAnimation.FILE_MARKER))
			{
				animations.add(new SkeletonAnimation(in, this));
			}
		}
		
		resyncAll();
	}
	
	public void saveToFile()
	{	
		try {
			File saveFile = new File(project.getDirectory() + "/" + name + ".txt");
			PrintWriter writer = new PrintWriter(saveFile);
			saveToFile(writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void saveToFile(final PrintWriter out)
	{	
		out.print(""); // Clear the file
		out.print(FILE_MARKER + "\n");
		out.print("NAME:" + name + "\n");
		if (masterFrame != null)
		{
			for (int i = 0; i < animations.size(); i++)
			{
				animations.get(i).saveToFile(out);
			}
		}
		out.write("END:" + FILE_MARKER + "\n");
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
	public SkeletonFrame getFrameByID(final UUID id)
	{
		if (masterFrame.getGUID().equals(id))
		{
			return masterFrame;
		}
		else
		{
			for (int i = 1; i < animations.size(); i++) // Start at one to skip master frame
			{
				SkeletonAnimation anim = (SkeletonAnimation)animations.get(i);
				for (int j = 0; j < anim.getFrames().size(); j++)
				{
					if (anim.getFrames().get(j).getGUID().equals(id))
					{
						return anim.getFrames().get(j);
					}
				}
			}
		}
		return null; // If no result found
	}
	
	public SkeletonBone getBoneBYID(final UUID id)
	{
		for (int i = 0; i < masterFrame.getBones().size(); i++)
		{
			if (masterFrame.getBones().get(i).getID().equals(id))
			{
				return masterFrame.getBones().get(i);
			}
		}
		for (int i = 1; i < animations.size(); i++) // Start at one to skip master frame
		{
			SkeletonAnimation anim = (SkeletonAnimation)animations.get(i);
			for (int j = 0; j < anim.getFrames().size(); j++)
			{
				SkeletonFrame frame = anim.getFrames().get(j);
				for (int k = 0; k < frame.getBones().size(); k++)
				{
					if (frame.getBones().get(k).getID().equals(id))
					{
						return frame.getBones().get(k);
					}
				}
			}
		}
		return null; // If no result found
	}
	
	public int getTreeLevel()
	{
		return 0;
	}
	public void resyncAll()
	{
		for (int i = 0; i < animations.size(); i++)
		{
			animations.get(i).resyncAll();
		}
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
