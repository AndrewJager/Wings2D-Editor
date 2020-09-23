package editor.objects.skeleton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import editor.objects.Drawable;
import framework.Utils;

public class SkeletonBone implements SkeletonNode, Drawable{
	public static final String BONE_TOKEN = "BONE";
	
	private SkeletonFrame frame;
	private String name;
	private UUID boneID;
	private SkeletonBone parentSyncedBone;
	private List<SkeletonBone> syncedBones;
	private UUID syncBoneID;
	private SkeletonBone parentBone;
	/** Used to determine the parent bone when copying bones between frames **/
	private String parentBoneName;
	private List<SkeletonBone> childBones;
	private Point2D location;
	private Color handleColor;
	private double rotation;
	private boolean rotating = false;
	private boolean showRotHandle = false;
	
	private static final Color HANDLE_COLOR_UNSELECTED = Color.GREEN;
	private static final Color HANDLE_COLOR_SELECTED = Color.RED;
	private static final int ROT_HANDLE_OFFSET = 15;
	
	public static final Point2D START_POS = new Point2D.Double(10, 15);
	public static final String PARENT_BONE_TOKEN = "PARENTBONE";
	public static final String SYNC_BONE_ID_TOKEN = "SYNCBONEID";
	public static final String POSITION_TOKEN = "POSITION";
	
	public SkeletonBone(String boneName, SkeletonFrame boneParent)
	{
		if (boneParent.containsBoneWithName(boneName))
		{
			throw new IllegalArgumentException("A Bone with this name already exists in the Frame!");
		}
		setup(boneParent);
		name = boneName;
		boneID = UUID.randomUUID();
		location = new Point2D.Double(START_POS.getX(), START_POS.getY());		
	}
	/** Create a copy of the bone passed in to this constructor **/
	public SkeletonBone(SkeletonBone syncBone, SkeletonFrame boneParent)
	{
		if (boneParent.containsBoneWithName(syncBone.toString()))
		{
			throw new IllegalArgumentException("A Bone with this name already exists in the Frame!");
		}
		setup(boneParent);
		name = syncBone.toString();
		boneID = UUID.randomUUID();
		setParentSyncedBone(syncBone);
		parentBoneName = syncBone.getParentBoneName();
		location = new Point2D.Double(syncBone.getX(), syncBone.getY());
		
	}
	
	public SkeletonBone(final Scanner in, final SkeletonFrame boneParent)
	{
		setup(boneParent);
		location = new Point2D.Double();
		
		boolean keepReading = true;
		while(in.hasNext() && keepReading)
		{
			String[] tokens = in.next().split(":");
			if (tokens[0].equals(NAME_TOKEN))
			{
				name = tokens[1];
			}
			else if (tokens[0].equals(ID_TOKEN))
			{
				boneID = UUID.fromString(tokens[1]);
			}
			else if (tokens[0].equals(PARENT_BONE_TOKEN))
			{
				parentBoneName = tokens[1];
			}
			else if (tokens[0].equals(SYNC_BONE_ID_TOKEN))
			{
				syncBoneID = UUID.fromString(tokens[1]);
			}
			else if (tokens[0].equals(POSITION_TOKEN))
			{
				location.setLocation(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
			}
			else if (tokens[0].equals(END_TOKEN))
			{
				keepReading = false;
			}
		}
	}
	
	private void setup(final SkeletonFrame boneParent)
	{
		frame = boneParent;
		syncedBones = new ArrayList<SkeletonBone>();
		childBones = new ArrayList<SkeletonBone>();
		handleColor = HANDLE_COLOR_UNSELECTED;
		rotation = 0;
	}
	
	public String toString()
	{
		return name;
	}
	public SkeletonFrame getFrame()
	{
		return frame;
	}

	public SkeletonBone getParentSyncedBone() {
		return parentSyncedBone;
	}
	public void setParentSyncedBone(final SkeletonBone syncedBone) {
		this.parentSyncedBone = syncedBone;
		this.syncBoneID = syncedBone.getID();
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
	public void setParentBone(final SkeletonBone bone)
	{
		if (bone != null)
		{
			parentBone = bone;
			parentBoneName = bone.toString();
			parentBone.getChildBones().add(this);
		}
		else
		{
			parentBoneName = null;
			if (parentBone != null)
			{
				parentBone.getChildBones().remove(this);
			}
			parentBone = null;
		}
		for (int i = 0; i < syncedBones.size(); i++)
		{
			if (bone != null)
			{
				syncedBones.get(i).setParentBone(bone.toString());
			}
			else
			{
				SkeletonBone nullBone = null;
				System.out.println("cat");
				syncedBones.get(i).setParentBone(nullBone);
			}
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
	public void setParentBoneName(String newName) 
	{
		parentBoneName = newName;
	}
	public List<SkeletonBone> getChildBones()
	{
		return childBones;
	}
	public void setLocation(double x, double y, double scale, boolean translateChildren)
	{
		double unscale = 1.0 / scale;
		double unscaledX = x * unscale;
		double unscaledY = y * unscale;
		
		double deltaX = unscaledX - location.getX();
		double deltaY = unscaledY - location.getY();
		location.setLocation(Utils.makeInRange(unscaledX, 0, (int)Double.MAX_VALUE), Utils.makeInRange(unscaledY, 0, (int)Double.MAX_VALUE));
		if (translateChildren)
		{
			for (int i = 0; i < childBones.size(); i++)
			{
				childBones.get(i).translateBy(deltaX, deltaY);
			}
		}
	}
	/** Calls setLocation with scale = 1 **/
	public void setLocation(double x, double y, boolean translateChildren)
	{
		setLocation(x, y, 1, translateChildren);
	}
	public void setLocation(Point loc, double scale, boolean translateChildren)
	{
		this.setLocation(loc.getX(), loc.getY(), scale, translateChildren);
	}
	/** Calls setLocation with scale = 1 **/
	public void setLocation(Point loc, boolean translateChildren)
	{
		setLocation(loc, 1, translateChildren);
	}
	public Point2D getLocation()
	{
		return location;
	}
	public double getX()
	{
		return location.getX();
	}
	public double getY()
	{
		return location.getY();
	}
	public void translateBy(double x, double y)
	{
		double newX = getX() + x;
		double newY = getY() + y;
		newX = Utils.makeInRange(newX, 0, (int)Double.MAX_VALUE);
		newY = Utils.makeInRange(newY, 0, (int)Double.MAX_VALUE);
		location.setLocation(newX, newY);
		for (int i = 0; i < childBones.size(); i++)
		{
			childBones.get(i).translateBy(x, y);
		}
	}
	public void setIsSelected(boolean selected)
	{
		if (selected)
		{
			handleColor = HANDLE_COLOR_SELECTED;
		}
		else
		{
			handleColor = HANDLE_COLOR_UNSELECTED;
		}
	}
	public void setRotating(boolean draw)
	{
		rotating = draw;
	}
	public boolean getRotating()
	{
		return rotating;
	}
	public void setShowRotHandle(boolean show)
	{
		showRotHandle = show;
	}
	public boolean getShowRotHandle()
	{
		return showRotHandle;
	}
	public double getRotation()
	{
		return rotation;
	}
	public Point2D getRotHandle(double scale)
	{
		double scaleBack = 1.0 / scale;
		Point2D rotHandleLoc = new Point2D.Double(location.getX(), location.getY() - (ROT_HANDLE_OFFSET * scaleBack));
		AffineTransform transform = new AffineTransform();
		transform.setToRotation(Math.toRadians(this.rotation - 90), location.getX(), location.getY());
		transform.transform(rotHandleLoc, rotHandleLoc);
		return rotHandleLoc;
	}
	public void rotateByHandle(Point loc, double scale)
	{
		double oldRotation = rotation;
		rotation = Math.toDegrees(Math.atan2((location.getY() * scale) - loc.getY(), (location.getX() * scale) - loc.getX()));
		double delta = rotation - oldRotation;
		for(int i = 0; i < childBones.size(); i++)
		{
			childBones.get(i).rotateAround(location, delta);
		}
	}
	public void rotateAround(Point2D point, double amt)
	{
		AffineTransform transform = new AffineTransform();
		transform.setToRotation(Math.toRadians(amt), point.getX(), point.getY());
		transform.transform(location, location);
		for (int i = 0; i < childBones.size(); i++)
		{
			childBones.get(i).rotateAround(point, amt);
		}
	}
	public UUID getID()
	{
		return boneID;
	}
	public UUID getSyncBoneID()
	{
		return syncBoneID;
	}

	
	// MutableTreeNode methods
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
	
	// SkeletonNode methods
	@Override
	public int getTreeLevel() {
		return 3;
	}
	@Override
	public void setName(final String newName) {
		name = newName;
		for(int i = 0; i < syncedBones.size(); i++)
		{
			syncedBones.get(i).setName(newName);
		}
		for (int i = 0; i < childBones.size(); i++)
		{
			childBones.get(i).setParentBoneName(newName);
		}
	}
	public void saveToFile(final PrintWriter out)
	{
		out.write(BONE_TOKEN + "\n");
		out.print(NAME_TOKEN + ":" + name + "\n");
		out.print(ID_TOKEN + ":" + boneID.toString() + "\n");
		if (parentBoneName != null)
		{
			out.print(PARENT_BONE_TOKEN + ":" + parentBoneName + "\n");
		}
		if (parentSyncedBone != null)
		{
			out.print(SYNC_BONE_ID_TOKEN + ":" + syncBoneID.toString() + "\n");
		}
		out.print(POSITION_TOKEN + ":" + location.getX() +":" + location.getY() + "\n");
		
		out.write(END_TOKEN + ":" + BONE_TOKEN + "\n");
	}
	public void resyncAll()
	{
		if (syncBoneID != null && (parentSyncedBone == null || (!parentSyncedBone.getID().equals(syncBoneID))))
		{
			parentSyncedBone = frame.getAnimation().getSkeleton().getBoneBYID(syncBoneID);
		}
		if (parentBoneName != null)
		{
			setParentBone(parentBoneName);
		}
	}
	
	// Drawable methods
	@Override
	public void draw(final Graphics2D g2d, final double scale) {
		final int handleSize = 10;
		g2d.setColor(handleColor);
		g2d.drawArc((int)((location.getX() * scale) - (handleSize / 2)), (int)((location.getY() * scale) - (handleSize / 2)),
				handleSize, handleSize, 0, 360);
		if (rotating || showRotHandle)
		{	
			Point2D rotHandleLoc = getRotHandle(scale);
			g2d.setColor(Color.BLACK);
			g2d.drawLine((int)(location.getX() * scale), (int)(location.getY() * scale),
					(int)(rotHandleLoc.getX() * scale), (int)(rotHandleLoc.getY() * scale));
			g2d.setColor(Color.YELLOW);
			g2d.drawArc((int)((rotHandleLoc.getX() * scale) - (handleSize / 2)), (int)((rotHandleLoc.getY() * scale) - (handleSize / 2)),
					handleSize, handleSize, 0, 360);
		}
	}
	@Override
	public Dimension getDrawSize(double scale) {
		return new Dimension((int)(Drawable.DRAW_PADDING * scale), (int)(Drawable.DRAW_PADDING * scale));
	}
}
