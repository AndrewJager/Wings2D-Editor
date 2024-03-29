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
import com.wings2d.editor.ui.edits.ActionNotDoneException;

public class SkeletonAnimation extends SkeletonNode{
	public static final String TABLE_NAME = "ANIMATION";
	
	private EditorSettings settings;
	private Skeleton skeleton;
	private List<SkeletonFrame> frames;
	
	private Connection con;
	private DBUUID skelID;
	
	public static SkeletonAnimation insert(final String animName, final UUID skeletonID, final Skeleton parent, final Connection con) {
		return new SkeletonAnimation(animName, skeletonID, parent, con);
	}
	public static SkeletonAnimation read(final UUID animID, final Skeleton parent, final Connection con) {
		return new SkeletonAnimation(animID, parent, con);
	}
	
	/** Insert constructor */
	private SkeletonAnimation(final String animName, final UUID skeletonID, final Skeleton parent, final Connection con)
	{
		this(parent, con);
		
		if (parent.containsAnimWithName(animName))
		{
			throw new IllegalArgumentException("An Animation with this name already exists!");
		}
		name.setStoredValue(animName);
		
		this.insert(con);
		this.query(con, id.getStoredValue());
	}
	
	private SkeletonAnimation(final UUID animID, final Skeleton parent, final Connection con) {
		this(parent, con);
		this.query(con, animID);
	}
	
	
	private SkeletonAnimation(final Skeleton parent, final Connection con) {
		super(TABLE_NAME);
		this.skeleton = parent;
		this.settings = parent.getSettings();
		this.con = con;
		frames = new ArrayList<SkeletonFrame>();
		
		fields.add(skelID = new DBUUID("Skeleton"));
		skelID.setStoredValue(parent.getID());
	}
	
	@Override
	protected void deleteChildren(final Connection con) {
		// Delete Frames 
		for (int i = 0; i < frames.size(); i++) {
			frames.get(i).delete(con);
		}
	}
	
	@Override
	protected void queryChildren(final UUID id, final Connection con) {
		frames.clear();
		String sql = getBasicQuery(SkeletonFrame.TABLE_NAME, "Animation", id);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				frames.add(SkeletonFrame.read(UUID.fromString(rs.getString("ID")), this, con, settings));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void updateChildren(final Connection con) {
		for(int i = 0; i < frames.size(); i++) {
			frames.get(i).update(con);
		}
	}

	@Override
	public String toString()
	{
		return name.getStoredValue();
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
	public List<SkeletonFrame> getFrames() {
		return frames;
	}
	public Skeleton getSkeleton() {
		return skeleton;
	}
	public EditorSettings getSettings() {
		return settings;
	}

	// MutableTreeNode methods
	@Override
	public void insert(final MutableTreeNode child, final int index) {
		SkeletonFrame newFrame = (SkeletonFrame)child;
		frames.add(index, newFrame);
		SkeletonFrame syncFrame = newFrame.getParentSyncedFrame();
		if (syncFrame != null) {
			for(int i = 0; i < syncFrame.getChildCount(); i++)
			{
				SkeletonBone bone = syncFrame.getBones().get(i);
				if (!newFrame.containsBoneWithName(bone.getName())) {
					newFrame.getBones().add(new SkeletonBone(bone, newFrame, con));
				}
			}
			newFrame.syncBonesToParents();
		}
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
	public void moveUp() throws ActionNotDoneException
	{
		List<SkeletonNode> anims = skeleton.getAnimations();
		int index = anims.indexOf(this);
		if (index > 1) // Cannot swap with Master Frame
		{
			Collections.swap(anims, index, index - 1);
		}
		else {
			throw new ActionNotDoneException(MOVE_UP_ERROR, false);
		}
	}
	@Override
	public void moveDown() throws ActionNotDoneException
	{
		List<SkeletonNode> anims = skeleton.getAnimations();
		int index = anims.indexOf(this);
		if (index < anims.size() - 1) 
		{
			Collections.swap(anims, index, index - 1);
		}
		else {
			throw new ActionNotDoneException(MOVE_DOWN_ERROR, false);
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
	public boolean isMaster() {
		return true;
	}
}
