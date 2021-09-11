package com.wings2d.editor.objects.skeleton;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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

import com.wings2d.editor.objects.Drawable;
import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.save.DBBoolean;
import com.wings2d.editor.objects.save.DBString;
import com.wings2d.editor.ui.edits.ActionNotDoneException;

public class SkeletonFrame extends SkeletonNode implements Drawable{
	public static final String FRAME_TOKEN = "FRAME";
	
	protected List<SkeletonBone> bones;
	protected List<SkeletonFrame> syncedFrames;
	
	private EditorSettings settings;
	private SkeletonNode parent;
	private SkeletonFrame parentSyncedFrame;
	
	private DBString syncFrameID;
	private DBBoolean isMaster;
	
	public static final String SYNC_FRAME_TOKEN = "SYNCFRAME";
	
	public SkeletonFrame(final String frameName, final SkeletonAnimation frameParent, final EditorSettings settings, 
			final Connection con)
	{
		this(frameName, frameParent, settings, con, false, null);
	}
	public SkeletonFrame(final String frameName, final SkeletonAnimation frameParent, final EditorSettings settings, 
			final Connection con, final boolean isMaster, final Skeleton skeleton)
	{
		super("FRAME");
		if ((frameParent == null) && (skeleton == null)) {
			throw new IllegalArgumentException("Frame must be provided a parent!");
		}
		
		// frameParent will be null for Master Frame
		if (frameParent != null && frameParent.containsFrameWithName(frameName))
		{
			throw new IllegalArgumentException("A Frame with this name already exists in this Animation!");
		}

		if (isMaster) {
			setup(skeleton, settings);
		}
		else {
			setup(frameParent, settings);
		}
		
		String animID = "";
		if (frameParent != null) {
			animID = frameParent.getID();
		}
		String skelID = "";
		if (skeleton != null) {
			skelID = skeleton.getID();
		}
		String newID = UUID.randomUUID().toString();
		String query = "INSERT INTO FRAME (ID, Name, Animation, Skeleton, IsMaster, SyncFrame)"
				+ " VALUES(" + "'" + newID + "'" + "," + "'" + frameName + "'" + "," + "'" + animID + "'" 
				+ "," + "'" + skelID + "'" + "," + isMaster + "," + "'" + " " + "'" + ")";
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		query = " SELECT * FROM FRAME WHERE ID = " + "'" + newID + "'";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			initData(con,rs.getString("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public SkeletonFrame(final String thisID, final SkeletonAnimation frameParent, final Connection con, final EditorSettings settings) {
		this(thisID, frameParent, con, settings, false, null);
	}
	public SkeletonFrame(final String thisID, final SkeletonAnimation frameParent, final Connection con, final EditorSettings settings, 
			final boolean isMaster, final Skeleton skeleton)
	{
		super("FRAME");
		if ((frameParent == null) && (skeleton == null)) {
			throw new IllegalArgumentException("Frame must be provided a parent!");
		}

		if (isMaster) {
			setup(skeleton, settings);
		}
		else {
			setup(frameParent, settings);
		}
		
		String query = " SELECT * FROM FRAME WHERE ID = " + "'" + thisID + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			initData(con,rs.getString("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setup(final SkeletonNode frameParent, final EditorSettings settings)
	{
		parent = frameParent;
		bones = new ArrayList<SkeletonBone>();
		syncedFrames = new ArrayList<SkeletonFrame>();
		this.settings = settings;
	}
	private void initData(final Connection con, final String thisID) throws SQLException {
		id = new DBString(con, "FRAME", "ID", thisID);
		name = new DBString (con, "FRAME", "Name", thisID);
		isMaster = new DBBoolean(con, "FRAME", "IsMaster", thisID);
		syncFrameID = new DBString(con, "FRAME", "SyncFrame", thisID);
	}
	
	@Override
	public void deleteChildren(final String ID, final Connection con) {
		
	}

	public String toString()
	{
		if (!isMaster.getValue()) {
			return name.getValue();
		}
		else {
			return name.getValue() + " (Master)";
		}
	}
	public boolean getIsMaster() {
		return isMaster.getValue();
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
			this.syncFrameID.setValue(syncedFrame.getGUID().toString());
			this.parentSyncedFrame.getSyncedFrames().add(this);
		}
		else
		{
			if (this.parentSyncedFrame != null) {
				this.parentSyncedFrame.getSyncedFrames().remove(this);
			}
			this.syncFrameID.setValue(null);
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
	/** Set bone parent bones to the bone stored in their parentBoneName variable **/
	public void syncBonesToParents()
	{
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).setParentBone(this.getBoneWithName(bones.get(i).getParentBoneName()));
		}
	}
	
	/**
	 * Gets the Bone at the location (with a margin of error).
	 * @param loc Location to check for Bone
	 * @param scale Scale of the canvas
	 * @return {@code null} if no Bone is close enough
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
	/** 
	 * Returns the a bone by comparing the locations of the X position handle with the {@code loc} Parameter
	 * @param loc Location to check for X position handle
	 * @param scale Scale of the canvas
	 * @return {@code null} if no bone is found
	 */
	public SkeletonBone getBoneByXHandle(final Point loc, final double scale)
	{
		final double minDistance = 10;
		SkeletonBone selectedBone = null;
		for (int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i).getIsSelected())
			{
				Point2D xPoint = bones.get(i).getXPosHandle(scale);
				double dist = Math.sqrt(Math.pow((loc.getX()-(xPoint.getX() * scale)), 2) 
						+ Math.pow((loc.getY()-(xPoint.getY() * scale)), 2));
				if (dist < minDistance)
				{
					selectedBone = bones.get(i);
					break;
				}
			}
		}
		return selectedBone;
	}
	/** 
	 * Returns the a bone by comparing the locations of the Y position handle with the {@code loc} Parameter
	 * @param loc Location to check for Y position handle
	 * @param scale Scale of the canvas
	 * @return {@code null} if no bone is found
	 */
	public SkeletonBone getBoneByYHandle(final Point loc, final double scale)
	{
		final double minDistance = 10;
		SkeletonBone selectedBone = null;
		for (int i = 0; i < bones.size(); i++)
		{
			if (bones.get(i).getIsSelected())
			{
				Point2D yPoint = bones.get(i).getYPosHandle(scale);
				double dist = Math.sqrt(Math.pow((loc.getX()-(yPoint.getX() * scale)), 2) 
						+ Math.pow((loc.getY()-(yPoint.getY() * scale)), 2));
				if (dist < minDistance)
				{
					selectedBone = bones.get(i);
					break;
				}
			}
		}
		return selectedBone;
	}
	/** 
	 * Returns the a bone by comparing the locations of the rotation handles with the {@code loc} Parameter
	 * @param loc Location to check for rotation handle
	 * @param scale Scale of the canvas
	 * @return {@code null} if no bone is found
	 */
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
					break;
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
	
	
	public UUID getGUID() {
		return UUID.fromString(id.getValue());
	}
	public EditorSettings getSettings() {
		return settings;
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
		parent.remove(this);
	}
	@Override
	public void setParent(final MutableTreeNode newParent) {
		parent = (SkeletonNode)newParent;
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
		return parent;
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
	public void resyncAll()
	{
		if (syncFrameID.getValue() != null)
		{
			// Won't need to do this for Master Frame
			if (!this.isMaster()) {
				SkeletonAnimation animation = (SkeletonAnimation)parent;
				setParentSyncedFrame(animation.getSkeleton().getFrameByID(UUID.fromString(syncFrameID.getValue())));
			}
		}
		for (int i = 0; i < bones.size(); i++)
		{
			bones.get(i).resyncAll();
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
	public void moveUp() throws ActionNotDoneException
	{
		// Won't need to do this if Master Frame
		if (parent instanceof SkeletonAnimation) {
			SkeletonAnimation animation = (SkeletonAnimation)parent;
			List<SkeletonFrame> frames = animation.getFrames();
			int index = frames.indexOf(this);
			if (index > 0) 
			{
				Collections.swap(frames, index, index - 1);
			}
			else {
				throw new ActionNotDoneException(MOVE_UP_ERROR, false);
			}
		}
	}
	@Override
	public void moveDown() throws ActionNotDoneException
	{
		// Won't need to do this if Master Frame
		if (parent instanceof SkeletonAnimation) {
			SkeletonAnimation animation = (SkeletonAnimation)parent;
			List<SkeletonFrame> frames = animation.getFrames();
			int index = frames.indexOf(this);
			if (index < frames.size() - 1) 
			{
				Collections.swap(frames, index, index + 1);
			}
			else {
				throw new ActionNotDoneException(MOVE_DOWN_ERROR, false);
			}
		}
	}
	@Override
	public List<SkeletonNode> getNodes()
	{
		List<SkeletonNode> nodes = new ArrayList<SkeletonNode>();
		for (int i = 0; i < bones.size(); i++)
		{
			nodes.add((SkeletonNode)bones.get(i));
		}
		return nodes;
	}
	@Override
	public boolean isMaster() {
		return this.getParentSyncedFrame() == null;
	}
}
