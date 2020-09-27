package com.wings2d.editor.objects.skeleton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.Drawable;

public class Sprite implements SkeletonNode, Drawable{
	private String name;
	private SkeletonBone parent;
	private Path2D path;
	private Color color;
	
	private static final Rectangle2D DEFAULT_SHAPE = new Rectangle2D.Double(0, 0, 50, 40);
	
	public Sprite(final String spriteName, final SkeletonBone parent)
	{
		this.name = spriteName;
		this.parent = parent;
		color = Color.LIGHT_GRAY;
		path = new Path2D.Double(DEFAULT_SHAPE);
		path = new Path2D.Double();
		path.moveTo(0, 0);
		path.lineTo(50, 0);
		path.lineTo(50, 40);
		path.lineTo(0, 100);
	}
	
	public String toString()
	{
		return name;
	}

	// MutableTreeNode methods
	@Override
	public void insert(final MutableTreeNode child, final int index) {}
	@Override
	public void remove(final int index) {}
	@Override
	public void remove(final MutableTreeNode node) {}
	@Override
	public void setUserObject(final Object object) {}
	@Override
	public void removeFromParent() {}
	@Override
	public void setParent(final MutableTreeNode newParent) {}
	@Override
	public TreeNode getChildAt(final int childIndex) {return null;}
	@Override
	public int getChildCount() {return 0;}
	@Override
	public TreeNode getParent() {return null;}
	@Override
	public int getIndex(final TreeNode node) {return 0;}
	@Override
	public boolean getAllowsChildren() {return false;}
	@Override
	public boolean isLeaf() {return true;}
	@Override
	public Enumeration<? extends TreeNode> children() {return null;}


	// SkeletonNode methods
	@Override
	public void setName(final String newName) {
		this.name = newName;
	}
	@Override
	public void saveToFile(final PrintWriter out) {
		
	}
	@Override
	public void resyncAll() {
		
	}

	// Drawable methods
	@Override
	public void draw(final Graphics2D g2d, final double scale) {
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		transform.translate(parent.getX() - (path.getBounds2D().getWidth() / 2), 
				parent.getY() - (path.getBounds2D().getHeight() / 2));
		Shape draw = transform.createTransformedShape(path);
		g2d.setColor(color);
		g2d.fill(draw);
	}
	@Override
	public Dimension getDrawSize(final double scale) {
		Shape bounds = path.getBounds2D();
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		bounds = transform.createTransformedShape(bounds);
		return new Dimension((int)bounds.getBounds().getWidth(), (int)bounds.getBounds().getHeight());
	}
}
