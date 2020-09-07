package editor.objects.skeleton;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import editor.objects.Drawable;

public class SkeletonFrame implements SkeletonNode, Drawable{
	private SkeletonAnimation animation;
	protected List<SkeletonBone> bones;
	protected String name;
	private SkeletonFrame parentSyncedFrame;
	protected List<SkeletonFrame> syncedFrames;
	
	public SkeletonFrame(String frameName, SkeletonAnimation frameParent)
	{
		// frameParent will be null for Master Frame
		if (frameParent != null && frameParent.containsFrameWithName(frameName))
		{
			throw new IllegalArgumentException("A Frame with this name already exists in this Animation!");
		}
		name = frameName;
		animation = frameParent;
		bones = new ArrayList<SkeletonBone>();
		syncedFrames = new ArrayList<SkeletonFrame>();
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	public String toString()
	{
		return name;
	}
	public boolean containsBoneWithName(String boneName)
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
		return animation;
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
		SkeletonBone newBone = (SkeletonBone)child;
		bones.add(index, newBone);
		for(int i = 0; i < syncedFrames.size(); i++)
		{
			syncedFrames.get(i).insert(new SkeletonBone(newBone, syncedFrames.get(i)), index);
		}
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
		animation.remove(this);
	}
	@Override
	public void setParent(MutableTreeNode newParent) {
		animation = (SkeletonAnimation)newParent;
	}

	public SkeletonFrame getParentSyncedFrame() {
		return parentSyncedFrame;
	}
	public void setParentSyncedFrame(SkeletonFrame syncedFrame) {
		this.parentSyncedFrame = syncedFrame;
		this.parentSyncedFrame.getSyncedFrames().add(this);
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
	public SkeletonBone getBoneWithName(String boneName)
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
	public SkeletonBone[] getArrayOfBonesExcept(SkeletonBone notThis)
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

	@Override
	public void draw(Graphics2D g2d, double scale) {
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).draw(g2d, scale);
		}
	}
	
	/**
	 * Gets the Bone at the location (with a margin of error). Returns null if no Bone is close enough
	 */
	public SkeletonBone getBoneAtPosition(Point loc, double scale)
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
	public SkeletonBone getHandleBone(Point loc, double scale)
	{
		final double minDistance = 10;
		SkeletonBone selectedBone = null;
		for (int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i).getShowRotHandle() || bones.get(i).getRotating())
			{
				Point2D rotPoint = bones.get(i).getRotHandle();
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
	
	public void setSelectedBone(SkeletonBone bone)
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
				bones.get(i).setShowRotHandle(false);
			}
		}
	}

	@Override
	public Dimension getDrawSize(double scale) {
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
	
	public void syncBonePositions()
	{
		for (int i = 0;  i < bones.size(); i++)
		{
			SkeletonBone bone = bones.get(i);
			if (bone.getParentSyncedBone() != null)
			{
				bone.setLocation(bone.getParentSyncedBone().getX(), bone.getParentSyncedBone().getY());
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
					bone.setLocation(bone.getParentSyncedBone().getX(), bone.getParentSyncedBone().getY());
				}
			}
		}
	}
}
