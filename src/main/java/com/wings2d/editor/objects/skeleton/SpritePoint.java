package com.wings2d.editor.objects.skeleton;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SpritePoint extends SkeletonNode{
	private Sprite parent;
	private double x;
	private double y;
	
	public SpritePoint(final double x, final double y, final Sprite parent) {
		super("POINT");
		this.parent = parent;
		this.x = x;
		this.y = y;
	}
	
	@Override 
	protected void initData(final Connection con, final String thisID) throws SQLException {
		
	}
	
	@Override
	public void deleteChildren(final String ID, final Connection con) {
		
	}
	
	public String toString() {
		return "X: " + Math.round(x) + " - Y: " + Math.round(y); 
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
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
