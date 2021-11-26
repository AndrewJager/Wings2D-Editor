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

public class Skeleton extends SkeletonNode {
	private List<SkeletonNode> animations;
	private SkeletonFrame masterFrame;
	private EditorSettings settings;
	
	public static Skeleton insert(final String skeletonName, final String projID, final EditorSettings settings, final Connection con) {
		return new Skeleton(skeletonName, projID, settings, con);
	}
	public static Skeleton read(final String skelID, final EditorSettings settings, final Connection con) {
		return new Skeleton(skelID, settings, con);
	}
	
	/** Insert constructor */
	private Skeleton(final String skeletonName, final String projID, final EditorSettings settings, final Connection con)
	{	
		this(settings);
		
		this.insert(con);
		
		
		// Create Master Frame

		
		this.query(con, id.getStoredValue());
	}
	
	/** Read constructor */
	private Skeleton(final String skelID, final EditorSettings settings, final Connection con) {
		this(settings);
		this.query(con, skelID);
	}
	
	private Skeleton(final EditorSettings settings) {
		super("SKELETON");
		animations = new ArrayList<SkeletonNode>();
		this.settings = settings;
	}
	
	@Override
	protected void deleteChildren(final String id, final Connection con) {
		// Delete Animations (and Master Frame) associated with the skeleton
		for(int i = 0; i < animations.size(); i++) {
			animations.get(i).delete(con);
		}
	}
	
	@Override
	protected void queryChildren(final String id, final Connection con)
	{
		String sql = " SELECT * FROM " + SkeletonFrame.TABLE_NAME + " WHERE Skeleton = " + quoteStr(id);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				animations.add(SkeletonFrame.read(rs.getString("ID"), null, con, settings, true, this));
				masterFrame = (SkeletonFrame)animations.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sql = " SELECT * FROM " + SkeletonAnimation.TABLE_NAME + " WHERE Skeleton = " + quoteStr(id);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				animations.add(SkeletonAnimation.read(rs.getString("ID"), this, con));
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
	public List<SkeletonNode> getAnimations(){
		return animations;
	}
	public EditorSettings getSettings() {
		return settings;
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
