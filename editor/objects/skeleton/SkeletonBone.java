package editor.objects.skeleton;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import editor.objects.Drawable;

public class SkeletonBone implements SkeletonNode, Drawable{
	private SkeletonFrame frame;
	private String name;
	private SkeletonBone parentSyncedBone;
	private List<SkeletonBone> syncedBones;
	private SkeletonBone parentBone;
	/** Used to determine the parent bone when copying bones between frames **/
	private String parentBoneName;
	private List<SkeletonBone> childBones;
	private Point2D location;
	private Color handleColor;
	private double rotation;
	private boolean rotating = false;
	private boolean showRotHandle = false;
	
	private final Color HANDLE_COLOR_UNSELECTED = Color.GREEN;
	private final Color HANDLE_COLOR_SELECTED = Color.RED;
	private final int ROT_HANDLE_OFFSET = 15;
	
	public SkeletonBone(String boneName, SkeletonFrame boneParent)
	{
		if (boneParent.containsBoneWithName(boneName))
		{
			throw new IllegalArgumentException("A Bone with this name already exists in the Frame!");
		}
		name = boneName;
		frame = boneParent;
		syncedBones = new ArrayList<SkeletonBone>();
		childBones = new ArrayList<SkeletonBone>();
		location = new Point2D.Double(10, 25);
		handleColor = HANDLE_COLOR_UNSELECTED;
		rotation = 0;
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
		childBones = new ArrayList<SkeletonBone>();
		location = new Point2D.Double(syncBone.getX(), syncBone.getY());
		handleColor = HANDLE_COLOR_UNSELECTED;
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
			parentBone.getChildBones().add(this);
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
	public List<SkeletonBone> getChildBones()
	{
		return childBones;
	}
	public void setLocation(double x, double y)
	{
		double deltaX = x - location.getX();
		double deltaY = y - location.getY();
		location.setLocation(x, y);
		for (int i = 0; i < childBones.size(); i++)
		{
			childBones.get(i).translateBy(deltaX, deltaY);
		}
	}
	public void setLocation(Point loc)
	{
		this.setLocation(loc.getX(), loc.getY());
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
		location.setLocation(getX() + x, getY() +y);
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
	public Point2D getRotHandle()
	{
		Point2D rotHandleLoc = new Point2D.Double(location.getX(), location.getY() - ROT_HANDLE_OFFSET);
		AffineTransform transform = new AffineTransform();
		transform.setToRotation(Math.toRadians(this.rotation - 90), location.getX(), location.getY());
		transform.transform(rotHandleLoc, rotHandleLoc);
		return rotHandleLoc;
	}
	public void rotateByHandle(Point loc)
	{
		double oldRotation = rotation;
		rotation = Math.toDegrees(Math.atan2(location.getY() - loc.getY(), location.getX() - loc.getX()));
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
	@Override
	public void draw(Graphics2D g2d) {
		final int handleSize = 10;
		g2d.setColor(handleColor);
		g2d.drawArc((int)(location.getX() - (handleSize / 2)), (int)(location.getY() - (handleSize / 2)),
				handleSize, handleSize, 0, 360);
		if (rotating || showRotHandle)
		{	
			Point2D rotHandleLoc = getRotHandle();
			g2d.setColor(Color.BLACK);
			g2d.drawLine((int)location.getX(), (int)location.getY(), (int)rotHandleLoc.getX(), (int)rotHandleLoc.getY());
			g2d.setColor(Color.YELLOW);
			g2d.drawArc((int)(rotHandleLoc.getX() - (handleSize / 2)), (int)(rotHandleLoc.getY() - (handleSize / 2)),
					handleSize, handleSize, 0, 360);
		}
	}
}
