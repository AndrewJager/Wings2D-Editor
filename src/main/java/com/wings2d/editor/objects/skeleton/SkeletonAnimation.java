package com.wings2d.editor.objects.skeleton;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.save.DBString;
import com.wings2d.editor.ui.edits.ActionNotDoneException;

public class SkeletonAnimation extends SkeletonNode{
	public static final String ANIM_TOKEN = "ANIMATION";
	
	private EditorSettings settings;
	private Skeleton skeleton;
	private List<SkeletonFrame> frames;
	
	/** Insert constructor */
	public SkeletonAnimation(final String animName, final String skeletonID, final Skeleton parent, final Connection con)
	{
		if (parent.containsAnimWithName(animName))
		{
			throw new IllegalArgumentException("An Animation with this name already exists!");
		}
		setup(parent);
		
		String id = UUID.randomUUID().toString();
		String query = "INSERT INTO ANIMATION (ID, Name, Skeleton)"
				+ " VALUES(" + "'" + id + "'" + "," + "'" + animName + "'" + "," + "'" + skeletonID + "'" + ")";
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		query = " SELECT * FROM ANIMATION WHERE ID = " + "'" + id + "'";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			initData(con,rs.getString("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public SkeletonAnimation(final String animID, final Skeleton parent, final Connection con) {
		setup(parent);
		
		String query = " SELECT * FROM ANIMATION WHERE ID = " + "'" + animID + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			initData(con,rs.getString("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setup(final Skeleton parent) {
		this.skeleton = parent;
		this.settings = parent.getSettings();
		frames = new ArrayList<SkeletonFrame>();
	}
	private void initData(final Connection con, final String thisID) throws SQLException {
		id = new DBString(con, "ANIMATION", "ID", thisID);
		name = new DBString (con, "ANIMATION", "Name", thisID);
	}

	public String toString()
	{
		return name.getValue();
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
					newFrame.getBones().add(new SkeletonBone(bone, newFrame));
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
	
	// SkeletonNode methods
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
