package editor.objects.skeleton;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SkeletonBone implements SkeletonNode{
	private SkeletonFrame parent;
	private String name;
	private SkeletonBone parentSyncedBone;
	private List<SkeletonBone> syncedBones;
	
	public SkeletonBone(String boneName, SkeletonFrame boneParent)
	{
		if (boneParent.containsBoneWithName(boneName))
		{
			throw new IllegalArgumentException("A Bone with this name already exists in the Frame!");
		}
		name = boneName;
		parent = boneParent;
		syncedBones = new ArrayList<SkeletonBone>();
	}
	
	public SkeletonBone copy(SkeletonFrame parentFrame)
	{
		SkeletonBone copy = new SkeletonBone(this.name, parentFrame);
		
		return copy;
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
}
