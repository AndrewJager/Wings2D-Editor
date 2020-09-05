package editor.objects.skeleton;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SkeletonBone implements SkeletonNode{
	private SkeletonFrame frame;
	private String name;
	private SkeletonBone parentSyncedBone;
	private List<SkeletonBone> syncedBones;
	private SkeletonBone parentBone;
	/** Used to determine the parent bone when copying bones between frames **/
	private String parentBoneName;
	private Point2D location;
	
	public SkeletonBone(String boneName, SkeletonFrame boneParent)
	{
		if (boneParent.containsBoneWithName(boneName))
		{
			throw new IllegalArgumentException("A Bone with this name already exists in the Frame!");
		}
		name = boneName;
		frame = boneParent;
		syncedBones = new ArrayList<SkeletonBone>();
		location = new Point2D.Double(10, 10);
	}
	public SkeletonBone(SkeletonBone syncBone, SkeletonFrame boneParent)
	{
		if (boneParent.containsBoneWithName(syncBone.toString()))
		{
			throw new IllegalArgumentException("A Bone with this name already exists in the Frame!");
		}
		name = syncBone.toString();
		frame = boneParent;
		setParentSyncedBone(syncBone);
		parentBoneName = syncBone.getParentBoneName();
		syncedBones = new ArrayList<SkeletonBone>();
	}
	
	public String toString()
	{
		return name;
	}
	public SkeletonFrame getFrame()
	{
		return frame;
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
		frame.remove(this);
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		frame = (SkeletonFrame)newParent;
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
		return frame;
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
		for(int i = 0; i < syncedBones.size(); i++)
		{
			syncedBones.get(i).setName(newName);
		}
	}

	public SkeletonBone getParentSyncedBone() {
		return parentSyncedBone;
	}
	public void setParentSyncedBone(SkeletonBone parentSyncedBone) {
		this.parentSyncedBone = parentSyncedBone;
		this.parentSyncedBone.getSyncedBones().add(this);
	}
	public List<SkeletonBone> getSyncedBones()
	{
		return syncedBones;
	}
	public String[] getSyncedBoneNames()
	{
		String[] names = new String[syncedBones.size()];
		for (int i = 0; i < syncedBones.size(); i++)
		{
			names[i] = syncedBones.get(i).toString();
		}
		return names;
	}
	public void setParentBone(SkeletonBone bone)
	{
		parentBone = bone;
		if (bone != null)
		{
			parentBoneName = bone.toString();
		}
		else
		{
			parentBoneName = null;
		}
		for (int i = 0; i < syncedBones.size(); i++)
		{
			syncedBones.get(i).setParentBone(bone.toString());
		}
	}
	public void setParentBone(String boneName)
	{
		setParentBone(frame.getBoneWithName(boneName));
	}
	public SkeletonBone getParentBone()
	{
		return parentBone;
	}
	public String getParentBoneName()
	{
		return parentBoneName;
	}
	public Point2D getLocation()
	{
		return location;
	}
}
