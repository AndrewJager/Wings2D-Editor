package com.wings2d.editor.objects.skeleton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.Drawable;

public class SkeletonBone implements SkeletonNode, Drawable{
	public static final String BONE_TOKEN = "BONE";
	public static final String PARENT_BONE_TOKEN = "PARENTBONE";
	public static final String SYNC_BONE_ID_TOKEN = "SYNCBONEID";
	public static final String POSITION_TOKEN = "POSITION";
	public static final String ROTATION_TOKEN = "ROTATION";
	public static final Point2D START_POS = new Point2D.Double(10, 15);
	
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
	private boolean selected = false;
	private Sprite selectedSprite;
	private List<Sprite> sprites;
	
	private static final Color HANDLE_COLOR_UNSELECTED = Color.GREEN;
	private static final Color HANDLE_COLOR_SELECTED = Color.RED;
	private static final int ROT_HANDLE_OFFSET = 15;
	
	public SkeletonBone(final String boneName, final SkeletonFrame boneParent)
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
	public SkeletonBone(final SkeletonBone syncBone, final SkeletonFrame boneParent)
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
			switch(tokens[0])
			{
			case NAME_TOKEN:
				name = tokens[1];
				break;
			case ID_TOKEN:
				boneID = UUID.fromString(tokens[1]);
				break;
			case PARENT_BONE_TOKEN:
				parentBoneName = tokens[1];
				break;
			case SYNC_BONE_ID_TOKEN:
				syncBoneID = UUID.fromString(tokens[1]);
				break;
			case POSITION_TOKEN:
				location.setLocation(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
				break;
			case Sprite.SPRITE_TOKEN:
				sprites.add(new Sprite(in, this));
				break;
			case ROTATION_TOKEN:
				rotation = Double.parseDouble(tokens[1]);
				break;
			case END_TOKEN:
				keepReading = false;
				break;
			}
		}
	}
	
	private void setup(final SkeletonFrame boneParent)
	{
		frame = boneParent;
		syncedBones = new ArrayList<SkeletonBone>();
		childBones = new ArrayList<SkeletonBone>();
		sprites = new ArrayList<Sprite>();
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
	public void setParentBone(final String boneName)
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
	public void setParentBoneName(final String newName) 
	{
		parentBoneName = newName;
	}
	public List<SkeletonBone> getChildBones()
	{
		return childBones;
	}
	public void setLocation(final double x, final double y, final double scale, final boolean translateChildren)
	{
		double unscale = 1.0 / scale;
		double unscaledX = x * unscale;
		double unscaledY = y * unscale;
		
		double deltaX = unscaledX - location.getX();
		double deltaY = unscaledY - location.getY();

		if ((unscaledX > 0 && unscaledY > 0) && checkTranslate(deltaX, deltaY))
		{
			location.setLocation(unscaledX, unscaledY);
			if (translateChildren)
			{
				for (int i = 0; i < childBones.size(); i++)
				{
					childBones.get(i).translateBy(deltaX, deltaY);
				}
			}
		}
	}
	/** Calls setLocation with scale = 1 **/
	public void setLocation(final double x, final double y, final boolean translateChildren)
	{
		setLocation(x, y, 1, translateChildren);
	}
	public void setLocation(final Point loc, final double scale, final boolean translateChildren)
	{
		this.setLocation(loc.getX(), loc.getY(), scale, translateChildren);
	}
	/** Calls setLocation with scale = 1 **/
	public void setLocation(final Point loc, final boolean translateChildren)
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

	public void translateBy(final double x, final double y)
	{
		double newX = getX() + x;
		double newY = getY() + y;
		location.setLocation(newX, newY);
		for (int i = 0; i < childBones.size(); i++)
		{
			childBones.get(i).translateBy(x, y);
		}
	}
	/** Returns true if this bone and all child bones are not being moved outside the edit zone **/
	private boolean checkTranslate(final double x, final double y)
	{
		boolean valid = true;
		double newX = getX() + x;
		double newY = getY() + y;
		if (newX < 0 || newY < 0)
		{
			valid = false;
		}
		else
		{
			for (int i = 0; i < childBones.size(); i++)
			{
				if (!childBones.get(i).checkTranslate(x, y)) // Check fails
				{
					valid = false;
					break;
				}
			}
		}
		return valid;
	}
	public void setIsSelected(final boolean selected)
	{
		this.selected = selected;
		if (selected)
		{
			handleColor = HANDLE_COLOR_SELECTED;
		}
		else
		{
			handleColor = HANDLE_COLOR_UNSELECTED;
		}
	}
	public boolean getIsSelected()
	{
		return selected;
	}
	public double getRotation()
	{
		return rotation;
	}
	public Point2D getRotHandle(final double scale)
	{
		double scaleBack = 1.0 / scale;
		Point2D rotHandleLoc = new Point2D.Double(location.getX(), location.getY() - (ROT_HANDLE_OFFSET * scaleBack));
		AffineTransform transform = new AffineTransform();
		transform.setToRotation(Math.toRadians(this.rotation - 90), location.getX(), location.getY());
		transform.transform(rotHandleLoc, rotHandleLoc);
		return rotHandleLoc;
	}
	public void rotateByHandle(final Point loc, final double scale)
	{
		double oldRotation = rotation;
		rotation = Math.toDegrees(Math.atan2((location.getY() * scale) - loc.getY(), (location.getX() * scale) - loc.getX()));
		double delta = rotation - oldRotation;
		for (int i = 0; i < sprites.size(); i++)
		{
			sprites.get(i).rotateAroundBone(delta);
		}
		for (int i = 0; i < childBones.size(); i++)
		{
			childBones.get(i).rotateAround(location, delta);
		}
	}
	public void rotateAround(final Point2D point, final double amt)
	{
		AffineTransform transform = new AffineTransform();
		transform.setToRotation(Math.toRadians(amt), point.getX(), point.getY());
		transform.transform(location, location);
		for (int i = 0; i < sprites.size(); i++)
		{
			sprites.get(i).rotateAroundBone(amt);
		}
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
	public void addSprite(final Sprite newSprite)
	{
		sprites.add(newSprite);
	}
	public List<Sprite> getSprites()
	{
		return sprites;
	}
	public void setSelectedSprite(final Sprite sprite)
	{
		selectedSprite = sprite;
	}
	public Sprite getSelectedSprite()
	{
		return selectedSprite;
	}
	public int getSelectedSpriteIndex()
	{
		return sprites.indexOf(selectedSprite);
	}
	public Sprite getSpriteBySyncID(final UUID id)
	{
		Sprite selectedSprite = null;
		for (int i = 0; i < sprites.size(); i++)
		{
			if (sprites.get(i).getSyncID().equals(id))
			{
				selectedSprite = sprites.get(i);
				break;
			}
		}
		return selectedSprite;
	}
	public void deselectAllVertices()
	{
		for (int i = 0; i < sprites.size(); i++)
		{
			sprites.get(i).setSelectedVertex(-1);
		}
	}
	public Sprite selectSpriteVertex(final Point loc, final double scale)
	{
		deselectAllVertices();
		
		Sprite selectedSprite = null;
		boolean spriteSelected = false;
		// Attempt to select a vertex on the selected Sprite
		if (getSelectedSprite() != null)
		{
			spriteSelected = checkIfVertexIsSelected(getSelectedSprite(), loc, scale);
		}
		if (spriteSelected)
		{
			selectedSprite = getSelectedSprite();
		}
		else
		{
			for (int i = 0; i < sprites.size(); i++)
			{
				spriteSelected = checkIfVertexIsSelected(sprites.get(i), loc, scale);
				if (spriteSelected)
				{
					selectedSprite = sprites.get(i);
					break;
				}
			}
		}
		
		return selectedSprite;
	}
	
	/** Returns true if a vertex is close to loc **/
	private boolean checkIfVertexIsSelected(final Sprite sprite, final Point loc, final double scale)
	{
		boolean isSpriteSelected = false;
		final double MIN_DISTANCE = 10;
		Path2D path = sprite.getScaledAndTranslatedPath(scale);
		PathIterator iter = path.getPathIterator(null);
		double[] coords = new double[6];
		int vertex = 0;
		while(!iter.isDone())
		{
			iter.currentSegment(coords);
			double dist = Math.sqrt(Math.pow((loc.getX() - coords[0]), 2) 
					+ Math.pow((loc.getY() - coords[1]), 2));
			
			if (dist <= MIN_DISTANCE)
			{
				isSpriteSelected = true;
				sprite.setSelectedVertex(vertex);
				break;
			}
			
			vertex++;
			iter.next();
		}
		return isSpriteSelected;
	}

	
	// MutableTreeNode methods
	@Override
	public void insert(final MutableTreeNode child, final int index) {
		Sprite sprite = (Sprite)child;
		sprites.add(index, sprite);
		this.setSelectedSprite(sprite);
		for (int i = 0; i < syncedBones.size(); i++)
		{
			syncedBones.get(i).insert(sprite.copy(syncedBones.get(i)), syncedBones.get(i).getSprites().size());		
		}
	}
	@Override
	public void remove(final int index) {
		sprites.remove(index);
	}
	@Override
	public void remove(final MutableTreeNode node) {
		sprites.remove(node);
	}
	@Override
	public void setUserObject(final Object object) {}
	@Override
	public void removeFromParent() {
		frame.remove(this);
	}
	@Override
	public void setParent(final MutableTreeNode newParent) {
		frame = (SkeletonFrame)newParent;
	}
	@Override
	public TreeNode getChildAt(final int childIndex) {
		return sprites.get(childIndex);
	}
	@Override
	public int getChildCount() {
		return sprites.size();
	}
	@Override
	public TreeNode getParent() {
		return frame;
	}
	@Override
	public int getIndex(final TreeNode node) {
		return sprites.indexOf(node);
	}
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	@Override
	public boolean isLeaf() {
		return (sprites.size() == 0);
	}
	@Override
	public Enumeration<? extends TreeNode> children() {
		return Collections.enumeration(sprites);
	}
	
	// SkeletonNode methods
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
		out.print(ROTATION_TOKEN + ":" + rotation + "\n");
		for (int i = 0; i < sprites.size(); i++)
		{
			sprites.get(i).saveToFile(out);
		}
		
		out.write(END_TOKEN + ":" + BONE_TOKEN + "\n");
	}
	public void resyncAll()
	{
		if (syncBoneID != null && (parentSyncedBone == null || (!parentSyncedBone.getID().equals(syncBoneID))))
		{
			parentSyncedBone = frame.getAnimation().getSkeleton().getBoneBYID(syncBoneID);
			parentSyncedBone.getSyncedBones().add(this);
		}
		if (parentBoneName != null)
		{
			setParentBone(parentBoneName);
		}
	}
	
	// Drawable methods
	@Override
	public void draw(final Graphics2D g2d, final double scale, final DrawMode mode) {
		for (int i = 0; i < sprites.size(); i++)
		{
			sprites.get(i).draw(g2d, scale, mode);
		}
		
		final int handleSize = 10;
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(handleColor);
		g2d.drawArc((int)((location.getX() * scale) - (handleSize / 2)), (int)((location.getY() * scale) - (handleSize / 2)),
				handleSize, handleSize, 0, 360);
		if (mode == DrawMode.BONE_ROTATE && selected)
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
