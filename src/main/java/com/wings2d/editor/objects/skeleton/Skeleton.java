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
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.save.DBString;

public class Skeleton extends SkeletonNode {
	public static final String FILE_MARKER = "SKELETON";
	
	private List<SkeletonNode> animations;
	private SkeletonMasterFrame masterFrame;
	private EditorSettings settings;
	
	/** Insert constructor */
	public Skeleton(final String skeletonName, final String projID, final EditorSettings settings, final Connection con)
	{
		setup(settings);
		String id = UUID.randomUUID().toString();
		String query = "INSERT INTO SKELETON (ID, Name, Project)"
				+ " VALUES(" + "'" + id + "'" + "," + "'" + skeletonName + "'" + "," + "'" + projID + "'" + ")";
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		query = " SELECT * FROM SKELETON WHERE ID = " + "'" + id + "'";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			initData(con,rs.getString("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Read constructor */
	public Skeleton(final String skelID, final EditorSettings settings, final Connection con) {
		setup(settings);
		String query = " SELECT * FROM SKELETON WHERE ID = " + "'" + skelID + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			initData(con,rs.getString("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void setup(final EditorSettings settings) {
		animations = new ArrayList<SkeletonNode>();
		masterFrame = new SkeletonMasterFrame("Master", settings);
		animations.add(masterFrame);
		this.settings = settings;
	}
	private void initData(final Connection con, final String thisID) throws SQLException {
		id = new DBString(con, "SKELETON", "ID", thisID);
		name = new DBString (con, "SKELETON", "Name", thisID);
		
		// Add Animations
		String query = " SELECT * FROM ANIMATION WHERE Skeleton = " + "'" + id.getValue() + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				animations.add(new SkeletonAnimation(rs.getString("ID"), this, con));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void delete(final String id, final Connection con) {
		// Delete Animations assocated with the skeleton
		String sql = "SELECT * FROM ANIMATION WHERE Skeleton = " + "'" + id +"'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				SkeletonAnimation.delete(rs.getString("ID"), con);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		// Delete this Skeleton
		sql = "DELETE FROM SKELETON WHERE ID = " + "'" + id +"'";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public SkeletonFrame getMasterFrame() {
		return masterFrame;
	}

	public String toString()
	{
		return name.getValue();
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
	public void saveToFile(final PrintWriter out)
	{	
		out.print(""); // Clear the file
		out.print(FILE_MARKER + "\n");
		out.print(NAME_TOKEN + ":" + name + "\n");
		if (masterFrame != null)
		{
			for (int i = 0; i < animations.size(); i++)
			{
				animations.get(i).saveToFile(out);
			}
		}
		out.write(END_TOKEN + ":" + FILE_MARKER + "\n");
	}
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
