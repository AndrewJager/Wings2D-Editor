package com.wings2d.editor.objects.skeleton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.save.DBUUID;

public class Skeleton extends SkeletonNode {
	public static final String TABLE_NAME = "SKELETON";
	private List<SkeletonNode> animations;
	private SkeletonFrame masterFrame;
	private EditorSettings settings;
	
	private DBUUID projID;
	
	public static Skeleton insert(final String skeletonName, final UUID projID, final EditorSettings settings, final Connection con) {
		return new Skeleton(skeletonName, projID, settings, con);
	}
	public static Skeleton read(final UUID skelID, final EditorSettings settings, final Connection con) {
		return new Skeleton(skelID, settings, con);
	}
	
	/** Insert constructor */
	private Skeleton(final String skeletonName, final UUID projID, final EditorSettings settings, final Connection con)
	{	
		this(settings);
		this.projID.setStoredValue(projID);
		name.setStoredValue(skeletonName);
		
		this.insert(con);
		
		SkeletonFrame.insert("Master", null, settings, con, true, this);
		
		this.query(con, id.getStoredValue());
	}
	
	/** Read constructor */
	private Skeleton(final UUID skelID, final EditorSettings settings, final Connection con) {
		this(settings);
		this.query(con, skelID);
		
		setAllSyncNodes();
	}
	
	private Skeleton(final EditorSettings settings) {
		super(TABLE_NAME);
		animations = new ArrayList<SkeletonNode>();
		this.settings = settings;
		
		fields.add(projID = new DBUUID("Project"));
	}
	
	@Override
	protected void deleteChildren(final Connection con) {
		// Delete Animations (and Master Frame) associated with the skeleton
		for(int i = 0; i < animations.size(); i++) {
			animations.get(i).delete(con);
		}
	}
	
	@Override
	protected void queryChildren(final UUID id, final Connection con)
	{
		animations.clear();
		String sql = getBasicQuery(SkeletonFrame.TABLE_NAME, "Skeleton", id);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				animations.add(SkeletonFrame.read(UUID.fromString(rs.getString("ID")), null, con, settings, this));
				masterFrame = (SkeletonFrame)animations.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sql = getBasicQuery(SkeletonAnimation.TABLE_NAME, "Skeleton", id);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				animations.add(SkeletonAnimation.read(UUID.fromString(rs.getString("ID")), this, con));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void updateChildren(final Connection con) {
		masterFrame.update(con);
		for(int i = 0; i < animations.size(); i++) {
			animations.get(i).update(con);
		}
	}
	
	public SkeletonFrame getMasterFrame() {
		return masterFrame;
	}

	public String toString()
	{
		return name.getStoredValue();
	}
	
	public boolean containsAnimWithName(final String animName)
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
		if (id != null) {
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
		}
		return null; // If no result found
	}
	public List<SkeletonNode> getAnimations(){
		return animations;
	}
	public EditorSettings getSettings() {
		return settings;
	}
	
	/** Call this after all children are created to sync object references that may not have been created
	 * when the child was created
	 */
	public void setAllSyncNodes() {
		for (int i = 0; i < animations.size(); i++) {
			if (animations.get(i) instanceof SkeletonFrame) { // Master Frame
				SkeletonFrame frame = (SkeletonFrame)animations.get(i);
				setSyncFrame(frame);
			}
			else if (animations.get(i) instanceof SkeletonAnimation) {
				SkeletonAnimation anim = (SkeletonAnimation)animations.get(i);
				for (int j = 0; j < anim.getFrames().size(); j++) {
					SkeletonFrame frame = (SkeletonFrame)anim.getFrames().get(j);
					setSyncFrame(frame);
				}
			}
		}
	}
	private void setSyncFrame(final SkeletonFrame frame) {
		frame.setParentSyncedFrame(this.getFrameByID(frame.getSyncFrameID()));
		for (int i = 0; i < frame.getBones().size(); i++) {
			SkeletonBone bone = (SkeletonBone)frame.getBones().get(i);
			setSyncBone(bone);
		}
	}
	private void setSyncBone(final SkeletonBone bone) {
		bone.setParentSyncedBone(this.getBoneBYID(bone.getSyncBoneID()));
		bone.setParentBone(this.getBoneBYID(bone.getParentBoneID()), true);
	}
	
	// MutableTreeNode methods
	@Override
	public void insert(final MutableTreeNode child, final int index) {
		animations.add(index, (SkeletonAnimation)child);
	}
	@Override
	public void remove(final int index) {
		animations.remove(index);
	}
	@Override
	public void remove(final MutableTreeNode node) {
		animations.remove((SkeletonAnimation)node);
	}
	@Override
	public void setUserObject(final Object object) {}
	@Override
	public void removeFromParent() {}
	@Override
	public void setParent(final MutableTreeNode newParent) {}
	@Override
	public TreeNode getChildAt(final int childIndex) {
		return animations.get(childIndex);
	}
	@Override
	public int getChildCount() {
		return animations.size();
	}
	@Override
	public TreeNode getParent() {return null;}
	@Override
	public int getIndex(final TreeNode node) {
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

	// SkeletonNode methods
	@Override
	public void resyncAll()
	{
		for (int i = 0; i < animations.size(); i++)
		{
			animations.get(i).resyncAll();
		}
	}
	@Override
	public void generateRender(final double scale)
	{
		for (int i = 0; i < animations.size(); i++)
		{
			animations.get(i).generateRender(scale);
		}
	}
	@Override
	public void moveUp(){/* Do nothing, as this node has no parent.*/}
	@Override
	public void moveDown(){/* Do nothing, as this node has no parent.*/}
	@Override
	public List<SkeletonNode> getNodes()
	{
		return getAnimations();
	}
	@Override
	public boolean isMaster() {
		return true;
	}
}
