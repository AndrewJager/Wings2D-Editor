package com.wings2d.editor.objects.skeleton;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.Drawable;

public class SkeletonFrame implements SkeletonNode, Drawable{
	public static final String FRAME_TOKEN = "FRAME";
	
	protected List<SkeletonBone> bones;
	protected String name;
	protected List<SkeletonFrame> syncedFrames;
	
	private SkeletonAnimation animation;
	private SkeletonFrame parentSyncedFrame;
	private UUID syncFrameID;
	private UUID frameID; // Unique id to differentiate between frames with the same name
	
	public static final String SYNC_FRAME_TOKEN = "SYNCFRAME";
	
	public SkeletonFrame(final String frameName, final SkeletonAnimation frameParent)
	{
		// frameParent will be null for Master Frame
		if (frameParent != null && frameParent.containsFrameWithName(frameName))
		{
			throw new IllegalArgumentException("A Frame with this name already exists in this Animation!");
		}
		name = frameName;
		setup(frameParent);
		frameID = UUID.randomUUID();
	}
	
	public SkeletonFrame(final Scanner in, final SkeletonAnimation frameParent)
	{
		setup(frameParent);
		
		boolean keepReading = true;
		while(in.hasNext() && keepReading)
		{
			String[] tokens = in.next().split(":");
			if (tokens[0].equals(NAME_TOKEN))
			{
				name = tokens[1];
			}
			else if (tokens[0].equals(SkeletonBone.BONE_TOKEN))
			{
				bones.add(new SkeletonBone(in, this));
			}
			else if (tokens[0].equals(ID_TOKEN))
			{
				frameID = UUID.fromString(tokens[1]);
			}
			else if (tokens[0].equals(SYNC_FRAME_TOKEN))
			{
				syncFrameID = UUID.fromString(tokens[1]);
			}
			else if(tokens[0].equals(END_TOKEN))
			{
				keepReading = false;
			}
		}
	}

	private void setup(final SkeletonAnimation frameParent)
	{
		animation = frameParent;
		bones = new ArrayList<SkeletonBone>();
		syncedFrames = new ArrayList<SkeletonFrame>();
	}

	public String toString()
	{
		return name;
	}
	public boolean containsBoneWithName(final String boneName)
	{
		boolean hasName = false;
		for(int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i).toString().equals(boneName))
			{
				hasName = true;
				break;
			}
		}
		return hasName;
	}

	public SkeletonFrame getParentSyncedFrame() {
		return parentSyncedFrame;
	}
	public void setParentSyncedFrame(SkeletonFrame syncedFrame) {
		if (syncedFrame != null)
		{
			this.parentSyncedFrame = syncedFrame;
			this.syncFrameID = syncedFrame.getGUID();
			this.parentSyncedFrame.getSyncedFrames().add(this);
		}
		else
		{
			this.parentSyncedFrame.getSyncedFrames().remove(this);
			this.syncFrameID = null;
			this.parentSyncedFrame = null;
		}
	}
	public List<SkeletonFrame> getSyncedFrames() {
		return syncedFrames;
	}
	public String[] getSyncedFrameNames()
	{
		String[] names = new String[syncedFrames.size()];
		for (int i = 0; i < syncedFrames.size(); i++)
		{
			names[i] = syncedFrames.get(i).toString();
		}
		return names;
	}
	public List<SkeletonBone> getBones()
	{
		return bones;
	}
	/**
	 * Returns null if no bone is found.
	 */
	public SkeletonBone getBoneWithName(final String boneName)
	{
		SkeletonBone bone = null;
		for(int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i).toString().equals(boneName))
			{
				bone = bones.get(i);
			}
		}
		return bone;
	}
	/**
	 * Returns an ArrayList of all bones in the frame except the argument Bone
	 */
	public SkeletonBone[] getArrayOfBonesExcept(final SkeletonBone notThis)
	{
		SkeletonBone[] allButOne = new SkeletonBone[bones.size()];
		int lastAdded = 1;
		for (int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i) != notThis)
			{
				allButOne[lastAdded] = bones.get(i);
				lastAdded++;
			}
		}
		return allButOne;
	}
	/** Set bone parent bones to the bone stored in there parentBoneName variable **/
	public void syncBonesToParents()
	{
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).setParentBone(this.getBoneWithName(bones.get(i).getParentBoneName()));
		}
	}
	
	/**
	 * Gets the Bone at the location (with a margin of error). Returns null if no Bone is close enough
	 */
	public SkeletonBone getBoneAtPosition(final Point loc, final double scale)
	{
		final double minDistance = 10;
		SkeletonBone selectedBone = null;
		double distance = 0;
		for (int i = 0; i < bones.size(); i++)
		{
			SkeletonBone bone = bones.get(i);
			double dist = Math.sqrt(Math.pow((loc.getX()-(bone.getX() * scale)), 2) + Math.pow((loc.getY()-(bone.getY() * scale)), 2));
			if ((dist < minDistance) && (dist > distance))
			{
				distance = dist;
				selectedBone = bone;
			}
		}
		
		return selectedBone;
	}
	public SkeletonBone getBoneByRotHandle(final Point loc, final double scale)
	{
		final double minDistance = 10;
		SkeletonBone selectedBone = null;
		for (int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i).getIsSelected())
			{
				Point2D rotPoint = bones.get(i).getRotHandle(scale);
				double dist = Math.sqrt(Math.pow((loc.getX()-(rotPoint.getX() * scale)), 2) 
						+ Math.pow((loc.getY()-(rotPoint.getY() * scale)), 2));
				if (dist < minDistance)
				{
					selectedBone = bones.get(i);
				}
			}
		}
		return selectedBone;
	}
	public Sprite selectSprite(final Point loc, final double scale)
	{
		Sprite selectedSprite = null;
		SkeletonBone bone = getSelectedBone();
		if (bone != null)
		{
			for (int i = 0; i < bone.getSprites().size(); i++)
			{
				Sprite sprite = bone.getSprites().get(i);
				if (sprite.getScaledAndTranslatedPath(scale).contains(loc))
				{
					selectedSprite = sprite;
					break;
				}
			}
		}
		if (bone == null || selectedSprite == null)
		{
			for (int i = 0; i < bones.size(); i++)
			{
				for (int j = 0; j < bones.get(i).getSprites().size(); j++)
				{
					Sprite sprite = bones.get(i).getSprites().get(j);
					if (sprite.getScaledAndTranslatedPath(scale).contains(loc))
					{
						selectedSprite = sprite;
						break;
					}
				}
			}
		}
		
		return selectedSprite;
	}
	public Sprite selectSpriteVertex(final Point loc, final double scale)
	{
		Sprite selectedSprite = null;
		SkeletonBone bone = getSelectedBone();
		if (bone != null)
		{
			selectedSprite = bone.selectSpriteVertex(loc, scale);
		}
		if (selectedSprite == null)
		{
			for (int i = 0; i < bones.size(); i++)
			{
				selectedSprite = bones.get(i).selectSpriteVertex(loc, scale);
				if (selectedSprite != null)
				{
					break;
				}
			}
		}
		
		return selectedSprite;
	}
	
	public void setSelectedBone(final SkeletonBone bone)
	{
		for (int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i) == bone)
			{
				bones.get(i).setIsSelected(true);
			}
			else
			{
				bones.get(i).setIsSelected(false);
			}
		}
	}
	public SkeletonBone getSelectedBone()
	{
		SkeletonBone selected = null;
		for (int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i).getIsSelected())
			{
				selected = bones.get(i);
				break;
			}
		}
		return selected;
	}
	
	public void syncBonePositions()
	{
		for (int i = 0;  i < bones.size(); i++)
		{
			SkeletonBone bone = bones.get(i);
			if (bone.getParentSyncedBone() != null)
			{
				bone.setLocation(bone.getParentSyncedBone().getX(), bone.getParentSyncedBone().getY(), false);
			}
		}
	}
	public void cautiousSyncBonePositions()
	{
		for (int i = 0;  i < bones.size(); i++)
		{
			SkeletonBone bone = bones.get(i);
			if (bone.getParentSyncedBone() != null)
			{
				if((bone.getX() == SkeletonBone.START_POS.getX()) && (bone.getY() == SkeletonBone.START_POS.getY()))
				{
					bone.setLocation(bone.getParentSyncedBone().getX(), bone.getParentSyncedBone().getY(), false);
				}
			}
		}
	}
	public UUID getGUID()
	{
		return frameID;
	}
	public SkeletonAnimation getAnimation()
	{
		return animation;
	}
	public void deselectAllBones()
	{
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).setIsSelected(false);
		}
	}
	public void unsyncAll()
	{
		setParentSyncedFrame(null);
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).unsyncAll();
		}
	}
	
	// MutableTreeNode methods
	@Override
	public void insert(final MutableTreeNode child, final int index) {
		SkeletonBone newBone = (SkeletonBone)child;
		bones.add(index, newBone);
		for(int i = 0; i < syncedFrames.size(); i++)
		{
			syncedFrames.get(i).insert(new SkeletonBone(newBone, syncedFrames.get(i)), index);
		}
	}
	@Override
	public void remove(final int index) {
		bones.remove(index);
	}
	@Override
	public void remove(final MutableTreeNode node) {
		bones.remove(node);
	}
	@Override
	public void setUserObject(final Object object) {}
	@Override
	public void removeFromParent() {
		animation.remove(this);
	}
	@Override
	public void setParent(final MutableTreeNode newParent) {
		animation = (SkeletonAnimation)newParent;
	}
	@Override
	public TreeNode getChildAt(final int childIndex) {
		return bones.get(childIndex);
	}
	@Override
	public int getChildCount() {
		return bones.size();
	}
	@Override
	public TreeNode getParent() {
		return animation;
	}
	@Override
	public int getIndex(final TreeNode node) {
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
	
	// SkeletonNode methods
	public void setName(final String newName)
	{
		name = newName;
	}
	public void saveToFile(final PrintWriter out)
	{
		out.write(FRAME_TOKEN + "\n");
		writeFrameInfo(out);
		out.write(END_TOKEN + ":" + FRAME_TOKEN + "\n");
	}
	public void resyncAll()
	{
		if (syncFrameID != null)
		{
			setParentSyncedFrame(animation.getSkeleton().getFrameByID(syncFrameID));
		}
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).resyncAll();
		}
	}
	
	protected void writeFrameInfo(final PrintWriter out)
	{
		out.print(NAME_TOKEN  + ":" + name + "\n");
		out.print(ID_TOKEN + ":" + frameID.toString() + "\n");
		if (parentSyncedFrame != null)
		{
			out.print(SYNC_FRAME_TOKEN + ":" + parentSyncedFrame.getGUID() + "\n");
		}
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).saveToFile(out);
		}
	}
	
	// Drawable methods
	@Override
	public void draw(final Graphics2D g2d, final double scale, final DrawMode mode) {
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).draw(g2d, scale, mode);
		}
	}
	@Override
	public Dimension getDrawSize(final double scale) {
		Rectangle2D bounds = new Rectangle2D.Double(0, 0, 0, 0);
		for(int i = 0; i < bones.size(); i++)
		{
			SkeletonBone bone = bones.get(i);
			if (!bounds.contains(bone.getLocation()))
			{
				if(bone.getX() > bounds.getMaxX())
				{
					bounds.setRect(0, 0, bone.getX(), bounds.getHeight());
				}
				if(bone.getY() > bounds.getMaxY())
				{
					bounds.setRect(0, 0, bounds.getWidth(), bone.getY());
				}
			}
		}
		bounds.setRect(0, 0, (bounds.getWidth() + (Drawable.DRAW_PADDING * scale)) * scale, 
				(bounds.getHeight() + (Drawable.DRAW_PADDING * scale)) * scale);
		
		return new Dimension((int)bounds.getWidth(), (int)bounds.getHeight());
	}
	@Override
	public void generateRender(final double scale)
	{
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).generateRender(scale);
		}
	}
	@Override
	public void drawRender(final Graphics2D g2d, final double scale)
	{
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).drawRender(g2d, scale);
		}
	}
	@Override
	public void moveUp()
	{
		List<SkeletonFrame> frames = animation.getFrames();
		int index = frames.indexOf(this);
		if (index > 0) 
		{
			SkeletonFrame swap = frames.get(index - 1);
			frames.remove(swap);
			frames.add(index, swap);
		}
	}
}
