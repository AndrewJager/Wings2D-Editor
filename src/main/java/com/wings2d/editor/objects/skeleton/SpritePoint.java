package com.wings2d.editor.objects.skeleton;

import java.awt.geom.Point2D;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.save.DBDouble;
import com.wings2d.editor.objects.save.DBInt;
import com.wings2d.editor.objects.save.DBUUID;

public class SpritePoint extends SkeletonNode{
	public static final String TABLE_NAME = "SPRITEPOINT";
	
	private Sprite parent;
	
	private DBUUID spriteID;
	private DBDouble x;
	private DBDouble y;
	private DBInt index;
	
	public static SpritePoint insert(final double x, final double y, final int idx, final Sprite parent, final Connection con) {
		return new SpritePoint(x, y, idx, parent, con);
	}
	public static SpritePoint read(final UUID pointID, final Sprite parent, final Connection con) {
		return new SpritePoint(pointID, con, parent);
	}
	
	/** Insert constructor */
	private SpritePoint(final double x, final double y, final int idx, final Sprite parent, final Connection con) {
		this(parent);
//		System.out.println(parent);
		spriteID.setStoredValue(parent.getID());
		this.x.setStoredValue(x);
		this.y.setStoredValue(y);
		this.index.setStoredValue(idx);
		
		this.insert(con);
		this.query(con, id.getStoredValue());
	}
	
	/** Read constructor */
	private SpritePoint(final UUID pointID, final Connection con, final Sprite parent) {
		this(parent);

		this.query(con, pointID);
	}
	
	private SpritePoint(final Sprite parent) {
		super(TABLE_NAME, false);
		this.parent = parent;
		
		fields.add(spriteID = new DBUUID("Sprite"));
		fields.add(x = new DBDouble("X"));
		fields.add(y = new DBDouble("Y"));
		fields.add(index = new DBInt("Position"));
	}
	
	@Override
	public void deleteChildren(final UUID ID, final Connection con) {}
	@Override
	public void queryChildren(final UUID ID, final Connection con) {}
	@Override
	protected void updateChildren(final Connection con) {}
	
	public String toString() {
		return "X: " + Math.round(x.getStoredValue()) + " - Y: " + Math.round(y.getStoredValue()); 
	}
	
	public double getX() {
		return x.getStoredValue();
	}
	public void setX(final double x) {
		this.x.setStoredValue(x);;
	}
	public double getY() {
		return y.getStoredValue();
	}
	public void setY(final double y) {
		this.y.setStoredValue(y);
	}
	
	public void setPoint(final Point2D point) {
		this.x.setStoredValue(point.getX());
		this.y.setStoredValue(point.getY());
	}
	
	public Sprite getSprite() {
		return parent;
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
		parent.getPoints().remove(this);
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		parent = (Sprite)newParent;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {return null;}

	@Override
	public int getChildCount() {return 0;}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {return 0;}

	@Override
	public boolean getAllowsChildren() {return false;}

	@Override
	public boolean isLeaf() {return true;}

	@Override
	public Enumeration<? extends TreeNode> children() {return null;}

	// SkeletonNode methods
	@Override
	public void resyncAll() {}

	@Override
	public void generateRender(double scale) {}

	@Override
	public void moveUp() {}

	@Override
	public void moveDown() {}

	@Override
	public List<SkeletonNode> getNodes() {return null;}
	@Override
	public boolean isMaster() {
		return this.getSprite().isMaster();
	}
}
